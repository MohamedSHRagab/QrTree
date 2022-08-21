package com.mohamedragab.cashpos.modules.repair.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.base.DateUtil;
import com.mohamedragab.cashpos.modules.repair.adapters.repairAdapter;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.repair.models.Repair;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.utils.Round;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class customerRepair extends AppCompatActivity {
    String client_name;
    DataBaseHelper db;
    List<Repair> repairList;
    ListView repairListView;
    repairAdapter repair_Adapter;
    LinearLayout container;
    TextView customername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_repair);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            client_name = extras.getString("key");
        }
        customername = (TextView) findViewById(R.id.customername);
        customername.setText(client_name + "");

        repairListView = (ListView) findViewById(R.id.list_repair);
        repairList = new ArrayList<>();
        db = new DataBaseHelper(getBaseContext());

        repair_Adapter = new repairAdapter(customerRepair.this, repairList);

        Cursor res = db.getrepairbyclient(client_name);

        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                Repair repair = new Repair();

                repair.setId(res.getInt(0));
                repair.setClient_name(res.getString(1));
                repair.setCreated_at(res.getString(2));
                repair.setVisit_at(res.getString(3));
                repair.setMoney_collect(res.getDouble(4));
                repair.setStatue(res.getString(5));
                repair.setNotes(res.getString(6));


                repairList.add(repair);


            }
            repair_Adapter.setrepairAdapter(repairList);
            repairListView.setAdapter(repair_Adapter);

        } else {
            Toast.makeText(getBaseContext(), "لا يوجد صيانه حاليا !", Toast.LENGTH_SHORT).show();
            repairList.clear();
            repair_Adapter.setrepairAdapter(repairList);
            repairListView.setAdapter(repair_Adapter);
        }

    }

    public void go_customers(View view) {
        onBackPressed();
    }

    TextView visitedate;

    public void new_repair(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.newrepairdialog);
        dialog.setCancelable(false);

        visitedate = (TextView) dialog.findViewById(R.id.date);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String formattedDate = df.format(c);
        visitedate.setText(formattedDate);
        visitedate.setOnClickListener(v -> {
            DateDialog();
        });
        RadioButton completed = (RadioButton) dialog.findViewById(R.id.completed);
        EditText money = (EditText) dialog.findViewById(R.id.money);
        EditText notes = (EditText) dialog.findViewById(R.id.notes);

        Button ok = (Button) dialog.findViewById(R.id.save);
        ok.setOnClickListener(v -> {


            Repair repair = new Repair();
            repair.setClient_name(client_name + "");
            repair.setCreated_at(formattedDate + "");
            if (money.getText().toString().trim().equals("")) {
                repair.setMoney_collect(0);
            } else {
                repair.setMoney_collect(Math.ceil(Double.parseDouble(money.getText().toString())));
            }
            repair.setNotes(notes.getText().toString() + "");
            if (completed.isChecked()) {
                repair.setStatue("completed");
            } else {
                repair.setStatue("not_completed");
            }
            repair.setVisit_at(visitedate.getText() + "");

            if (db.insert_date(repair)) {
                DataBaseHelper moneydb = new DataBaseHelper(getBaseContext());

                if (repair.getStatue().equals("completed")){
                    com.mohamedragab.cashpos.modules.moneybox.models.money moneyobj = new com.mohamedragab.cashpos.modules.moneybox.models.money();
                    moneyobj.setDate(formattedDate);
                    moneyobj.setNotes("صيانه للعميل : " +repair.getClient_name());
                    moneyobj.setValue( Round.round(repair.getMoney_collect(),3));

                    final Cursor res3 = moneydb.getallTransactions();
                    double total1 = 0;
                    if (res3 != null && res3.getCount() > 0) {
                        while (res3.moveToNext()) {
                            total1 = res3.getLong(3);

                        }
                    }
                    moneyobj.setTotalbefore(Round.round(total1,3));
                    double totalAfter = Round.round((total1 +  repair.getMoney_collect()),3);

                    moneyobj.setTotalAfter(Round.round(totalAfter,3));
                    if( moneydb.insert_date(moneyobj)){
                        // Toast.makeText(con, "تم تعديل الصيانه بنجاح", Toast.LENGTH_SHORT).show();
                    }
                }
                Toast.makeText(getBaseContext(), "تم تسجيل الصيانه بنجاح", Toast.LENGTH_SHORT).show();
                this.recreate();
                moneydb.close();
                dialog.dismiss();
            }

        });
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();


    }

    private Calendar mcalendar;
    private TextView current_date;
    private int day, month, year;

    public void DateDialog() {
        Locale.setDefault(Locale.US);

        mcalendar = Calendar.getInstance();
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);
        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener listener = (view, year, monthOfYear, dayOfMonth) -> visitedate.setText(DateUtil.getDateOnly(year, monthOfYear, dayOfMonth));
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        db.close();
    }
}
