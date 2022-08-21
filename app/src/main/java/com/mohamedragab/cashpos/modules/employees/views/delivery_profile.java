package com.mohamedragab.cashpos.modules.employees.views;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.base.DateUtil;
import com.mohamedragab.cashpos.modules.employees.models.DelivTrans;
import com.mohamedragab.cashpos.modules.employees.wedgits.deliveryTransAdapter;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.moneybox.models.money;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.utils.Round;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class delivery_profile extends AppCompatActivity {

    ListView transaction;
    TextView search;
    List<DelivTrans> moneyList;
    DataBaseHelper db;
    com.mohamedragab.cashpos.modules.employees.wedgits.deliveryTransAdapter deliveryTransAdapter;
    private Calendar mcalendar;
    private int day, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_profile);
        transaction = (ListView) findViewById(R.id.list_omla);
        search = (TextView) findViewById(R.id.search_date);
        Bundle extras = getIntent().getExtras();
        String delivery_id = null;
        if (extras != null) {
            delivery_id = extras.getString("key");
        }
        String finalDelivery_id = delivery_id;
        search.setOnClickListener(v -> DateDialog(finalDelivery_id + ""));

        moneyList = new ArrayList<>();
        db = new DataBaseHelper(getBaseContext());

        deliveryTransAdapter = new deliveryTransAdapter(delivery_profile.this, moneyList);

        Cursor res = db.getalldeliverytrans(delivery_id);

        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                DelivTrans money = new DelivTrans();

                money.setInvoice_id(res.getString(4));
                money.setDeliv_name(res.getString(1));
                money.setMoney(res.getDouble(2));
                money.setDate(res.getString(5));
                money.setAddress(res.getString(3));

                moneyList.add(money);

            }
            Collections.sort(moneyList, (arg0, arg1) -> {
                SimpleDateFormat format = new SimpleDateFormat(
                        "dd-MM-yyyy", Locale.US);
                int compareResult = 0;
                try {
                    Date arg0Date = format.parse(arg0.getDate());
                    Date arg1Date = format.parse(arg1.getDate());
                    compareResult = arg0Date.compareTo(arg1Date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    compareResult = arg0.getDate().compareTo(arg1.getDate());
                }
                return compareResult;
            });
            Collections.reverse(moneyList);

            deliveryTransAdapter.setdeliveryTransAdapter(moneyList);
            transaction.setAdapter(deliveryTransAdapter);

        } else {
            Toast.makeText(getBaseContext(), "لا يوجد عمليات للدليفري !", Toast.LENGTH_SHORT).show();
            moneyList.clear();
            deliveryTransAdapter.setdeliveryTransAdapter(moneyList);
            transaction.setAdapter(deliveryTransAdapter);


        }


    }

    public void go_back(View view) {
        onBackPressed();
    }

    public void DateDialog(String id) {
        Locale.setDefault(Locale.US);

        mcalendar = Calendar.getInstance();
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);
        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener listener = (view, year, monthOfYear, dayOfMonth) -> {
            search.setText(DateUtil.getDateOnly(year, monthOfYear, dayOfMonth));
            MediaPlayer mp = MediaPlayer.create(delivery_profile.this, R.raw.finalsound);
            mp.start();
            Cursor res = db.getdeliverytransbydate(search.getText().toString(), id);
            moneyList.clear();
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    DelivTrans money = new DelivTrans();

                    money.setInvoice_id(res.getString(4));
                    money.setDeliv_name(res.getString(1));
                    money.setMoney(res.getDouble(2));
                    money.setDate(res.getString(5));
                    money.setAddress(res.getString(3));

                    moneyList.add(money);

                }
                deliveryTransAdapter.setdeliveryTransAdapter(moneyList);
                transaction.setAdapter(deliveryTransAdapter);

            } else {
                Toast.makeText(getBaseContext(), "لا يوجد عمليات للدليفري !", Toast.LENGTH_SHORT).show();
                moneyList.clear();
                deliveryTransAdapter.setdeliveryTransAdapter(moneyList);
                transaction.setAdapter(deliveryTransAdapter);

            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }

    public void gettotal(View view) {
        double total = 0.0;
        for (int i = 0; i < moneyList.size(); i++) {
            total += moneyList.get(i).getMoney();
        }
        if (search.getText().toString().trim().equals("")) {
            Toast.makeText(getBaseContext(), "برجاء اختيار تاريخ لمحاسبة الدليفري !", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder dialog2 = new AlertDialog.Builder(delivery_profile.this);
            dialog2.setCancelable(false);
            dialog2.setTitle(" اجمالي حساب الدليفري ليوم "+search.getText().toString());
            dialog2.setMessage(" أضافه المبلغ "+  total+"الي الخزينة  ! ");
            double finalTotal = total;
            dialog2.setPositiveButton("تأكيد", (dialog12, id) -> {
               money money = new money();
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                String formattedDate = df.format(c);
                money.setDate(formattedDate);
                money.setNotes("حساب الدليفري ليوم " +search.getText().toString());
                money.setValue(Round.round(finalTotal, 3));
                final Cursor res3 = db.getallTransactions();
                double total1 = 0;
                if (res3 != null && res3.getCount() > 0) {
                    while (res3.moveToNext()) {
                        total1 = res3.getDouble(3);
                    }
                }
                money.setTotalbefore(Round.round(total1, 3));
                double totalAfter = total1 + (finalTotal);

                money.setTotalAfter(Round.round(totalAfter, 3));

                if (db.insert_date(money)) {
                    Toast.makeText(getBaseContext(), "تم أضافة المبلغ الي الخزينة .", Toast.LENGTH_SHORT).show();
                }

            })
                    .setNegativeButton("الغاء ", (dialog1, which) -> {
                    });

            final AlertDialog alert = dialog2.create();
            alert.show();

        }


    }
}
