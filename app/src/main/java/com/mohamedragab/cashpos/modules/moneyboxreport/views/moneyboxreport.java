package com.mohamedragab.cashpos.modules.moneyboxreport.views;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.base.DateUtil;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.moneybox.models.money;
import com.mohamedragab.cashpos.modules.moneyboxreport.wedgit.moneyboxAdapter;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class moneyboxreport extends AppCompatActivity {
    ListView transaction;
    TextView search;
    List<money> moneyList;
    DataBaseHelper db;
    moneyboxAdapter moneyboxAdapter;
    private Calendar mcalendar;
    private int day, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moneyboxreport);

        transaction = (ListView) findViewById(R.id.list_transaction);
        search = (TextView) findViewById(R.id.search_date);
        search.setOnClickListener(v -> DateDialog());

        moneyList = new ArrayList<>();
        db = new DataBaseHelper(getBaseContext());

        moneyboxAdapter = new moneyboxAdapter(moneyboxreport.this, moneyList);

        Cursor res = db.getallTransactions();

        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                money money = new money();

                money.setValue(res.getDouble(1));
                money.setTotalbefore(Double.parseDouble(res.getString(2)));
                money.setTotalAfter(res.getDouble(3));
                money.setDate(res.getString(4));
                money.setNotes(res.getString(5));

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

            moneyboxAdapter.setmoneyboxAdapter(moneyList);
            transaction.setAdapter(moneyboxAdapter);

        } else {
            Toast.makeText(getBaseContext(), "لا يوجد عمليات سحب او اضف في الخزينه !", Toast.LENGTH_SHORT).show();
            moneyList.clear();
            moneyboxAdapter.setmoneyboxAdapter(moneyList);
            transaction.setAdapter(moneyboxAdapter);

        }


    }

    public void go_money_box(View view) {
        onBackPressed();
        db.close();
    }

    public void DateDialog() {
        Locale.setDefault(Locale.US);

        mcalendar = Calendar.getInstance();
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);
        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener listener = (view, year, monthOfYear, dayOfMonth) -> {
            search.setText(DateUtil.getDateOnly(year, monthOfYear, dayOfMonth));
            MediaPlayer mp = MediaPlayer.create(moneyboxreport.this, R.raw.finalsound);
            mp.start();
            Cursor res = db.getTransactions(search.getText().toString());
            moneyList.clear();
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    money money = new money();

                    money.setValue(res.getDouble(1));
                    money.setTotalbefore(Double.parseDouble(res.getString(2)));
                    money.setTotalAfter(res.getInt(3));
                    money.setDate(res.getString(4));
                    money.setNotes(res.getString(5));

                    moneyList.add(money);

                }
                moneyboxAdapter.setmoneyboxAdapter(moneyList);
                transaction.setAdapter(moneyboxAdapter);

            } else {
                Toast.makeText(getBaseContext(), "لا يوجد عمليات سحب او اضافه في الخزينه !", Toast.LENGTH_SHORT).show();
                moneyList.clear();
                moneyboxAdapter.setmoneyboxAdapter(moneyList);
                transaction.setAdapter(moneyboxAdapter);

            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }

}
