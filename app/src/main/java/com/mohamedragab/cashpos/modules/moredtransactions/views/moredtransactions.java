package com.mohamedragab.cashpos.modules.moredtransactions.views;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.modules.moredtransactions.models.moredtransaction;
import com.mohamedragab.cashpos.modules.moredtransactions.wedgits.moredtransdetailsAdapter;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.mored.models.mored;
import com.mohamedragab.cashpos.modules.mored.views.moward;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.utils.Round;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class moredtransactions extends AppCompatActivity {
    TextView name, totalnotpaid;
    DataBaseHelper db;
    List<moredtransaction> moredtransactionList;
    ListView omlatransactionListView;
    String value;
    com.mohamedragab.cashpos.modules.moredtransactions.wedgits.moredtransdetailsAdapter moredtransdetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moredtransactions);

        db = new DataBaseHelper(getBaseContext());

        totalnotpaid = (TextView) findViewById(R.id.totalnotpaid);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("key");
        }
        Cursor result = db.getmored(value);
        while (result.moveToNext()) {
            totalnotpaid.setText("اجمالي المتبقي :"+ Round.round(result.getDouble(6),3) + SheredPrefranseHelper.getmoney_type(this));
        }

        name = (TextView) findViewById(R.id.name);
        name.setText(value + "");
        omlatransactionListView = (ListView) findViewById(R.id.list_transactions);

        moredtransactionList = new ArrayList<>();
        db = new DataBaseHelper(getBaseContext());

        moredtransdetailsAdapter = new moredtransdetailsAdapter(moredtransactions.this, moredtransactionList);

        Cursor res = db.getmoredtransaction(value);

        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                moredtransaction moredtransaction = new moredtransaction();

                moredtransaction.setName(res.getString(1));
                moredtransaction.setValue(res.getDouble(3));
                moredtransaction.setProcess(res.getString(2));
                moredtransaction.setNotpaid(res.getDouble(6));
                moredtransaction.setDate(res.getString(4));
                moredtransaction.setInvoiceId(res.getString(5));

                moredtransactionList.add(moredtransaction);

            }
            moredtransdetailsAdapter.setmoredtransdetailsAdapter(moredtransactionList);
            omlatransactionListView.setAdapter(moredtransdetailsAdapter);

        } else {
            Toast.makeText(getBaseContext(), "لا يوجد تعاملات حاليا !", Toast.LENGTH_SHORT).show();
            moredtransactionList.clear();
            moredtransdetailsAdapter.setmoredtransdetailsAdapter(moredtransactionList);
            omlatransactionListView.setAdapter(moredtransdetailsAdapter);
        }
    }

    public void go_mored(View view) {
       onBackPressed();
    }

    public void go_pay(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.addmoneydialog);

        final EditText money_et = (EditText) dialog.findViewById(R.id.value);

        TextView pay = (TextView) dialog.findViewById(R.id.pay);
        pay.setOnClickListener(v -> {

            moredtransaction moredtransaction = new moredtransaction();
            Date c = Calendar.getInstance().getTime();

            SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            String formattedDate2 = df2.format(c);
            moredtransaction.setInvoiceId("-");
            moredtransaction.setDate(formattedDate2);
            moredtransaction.setName(name.getText().toString());
            moredtransaction.setNotpaid(0);
            moredtransaction.setProcess("خصم");
            moredtransaction.setValue(Round.round(Double.parseDouble(money_et.getText().toString()),3));

            if (db.insert_date(moredtransaction)) {
                Cursor res = db.getmored(name.getText().toString());

                if (res != null && res.getCount() > 0) {
                    while (res.moveToNext()) {
                        mored mored = new mored();

                        mored.setId(res.getInt(0));
                        mored.setName(res.getString(1));
                        double newsouldpayvalue = res.getDouble(6) - Double.parseDouble(money_et.getText().toString());
                        mored.setPaymoney(Round.round(newsouldpayvalue,3));
                        mored.setHasmoney(res.getDouble(5));
                        mored.setAddress(res.getString(2));
                        mored.setPhone(res.getString(3));
                        mored.setNotes(res.getString(4));

                        if (db.updateData(mored)) {

                            com.mohamedragab.cashpos.modules.moneybox.models.money money = new com.mohamedragab.cashpos.modules.moneybox.models.money();
                            money.setDate(formattedDate2);
                            money.setNotes("خصم مبلغ المورد :" + name.getText().toString());
                            money.setValue(Double.parseDouble(money_et.getText().toString()));

                            final Cursor res3 = db.getallTransactions();
                            double total = 0;
                            if (res3 != null && res3.getCount() > 0) {
                                while (res3.moveToNext()) {
                                    total = res3.getDouble(3);
                                }
                            }
                            money.setTotalbefore(Round.round(total,3));
                            double totalAfter =Round.round(( total - Double.parseDouble(money_et.getText().toString())),3);

                            money.setTotalAfter(totalAfter);

                            if (db.insert_date(money)) {
                                Toast.makeText(getBaseContext(), "تم خصم المبلغ من حساب المورد !", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(getBaseContext(), "فشل اضافه المبلغ في الصندوق ! ", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(getBaseContext(), "لم يحدث تغيير في حساب المورد !", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getBaseContext(), "لا يوجد موردين بهذا الاسم !", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
                startActivity(new Intent(this, moward.class));
                finish();
            }

        });

        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();


    }
}
