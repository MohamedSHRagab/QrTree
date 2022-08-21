package com.mohamedragab.cashpos.modules.masroufreport.views;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.base.DateUtil;
import com.mohamedragab.cashpos.modules.masroufreport.wedgit.masroufAdapter;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.moneybox.models.money;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class masroufreport extends AppCompatActivity {

    ListView transaction;
    TextView search;
    List<money> moneyList;
    DataBaseHelper db;
    masroufAdapter masroufAd;
    private Calendar mcalendar;
    private int day, month, year;
    public static TextView total_masrouf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masroufreport);

        transaction = (ListView) findViewById(R.id.list_masrouf);
        search = (TextView) findViewById(R.id.search_date);
        total_masrouf=(TextView)findViewById(R.id.total_masrouf);

        String date_type = getIntent().getStringExtra("date");
        if (date_type.equals("0")) {
            search.setOnClickListener(v -> DateDialog());
        } else if (date_type.equals("1")) {
            search.setOnClickListener(v -> MonthDialog());

        }
        moneyList = new ArrayList<>();
        db = new DataBaseHelper(this);

        masroufAd = new masroufAdapter(this, moneyList);

        Cursor res = db.getallTransactions();

        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                money money = new money();

                money.setValue(res.getDouble(1));
                money.setTotalbefore(Double.parseDouble(res.getString(2)));
                money.setTotalAfter(res.getDouble(3));
                money.setDate(res.getString(4));
                money.setNotes(res.getString(5));

                if (res.getString(5).startsWith("مصروف")) {
                    moneyList.add(money);
                }

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

            masroufAd.setmasroufAdapter(moneyList);
            transaction.setAdapter(masroufAd);

        } else {
            Toast.makeText(getBaseContext(), "لا يوجد مصروفات !", Toast.LENGTH_SHORT).show();
            moneyList.clear();
            masroufAd.setmasroufAdapter(moneyList);
            transaction.setAdapter(masroufAd);

        }
    }

    private void MonthDialog() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy", Locale.US);
        Calendar cal1 = Calendar.getInstance();
        Date dt = null;
        try {
            dt = df.parse(c + "");
            cal1.setTime(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int currentDaycmp = cal1.get(Calendar.DAY_OF_MONTH);
        int currentMonthcmp = cal1.get(Calendar.MONTH);
        int currentYearcmp = cal1.get(Calendar.YEAR);

        DatePickerDialog monthDatePickerDialog = new DatePickerDialog(masroufreport.this,
                AlertDialog.THEME_HOLO_LIGHT, (view, year, month, dayOfMonth) -> search.setText((month+1) + "-" + year), currentYearcmp, currentMonthcmp, currentDaycmp) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
            }
        };
        monthDatePickerDialog.setTitle("اختيار شهر محدد");
        monthDatePickerDialog.show();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
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

                        if (res.getString(4).contains(search.getText().toString())){
                            if (res.getString(5).startsWith("مصروف")) {
                                moneyList.add(money);
                            }
                        }
                    }
                    masroufAd.setmasroufAdapter(moneyList);
                    transaction.setAdapter(masroufAd);

                } else {
                    Toast.makeText(getBaseContext(), "لا يوجد مصروفات !", Toast.LENGTH_SHORT).show();
                    moneyList.clear();
                    masroufAd.setmasroufAdapter(moneyList);
                    transaction.setAdapter(masroufAd);
                }


            }
        });
    }

    public void go_masrouf(View view) {
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
            MediaPlayer mp = MediaPlayer.create(masroufreport.this, R.raw.finalsound);
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

                    if (res.getString(5).startsWith("مصروف")) {
                        moneyList.add(money);
                    }
                }
                masroufAd.setmasroufAdapter(moneyList);
                transaction.setAdapter(masroufAd);

            } else {
                Toast.makeText(getBaseContext(), "لا يوجد مصروفات !", Toast.LENGTH_SHORT).show();
                moneyList.clear();
                masroufAd.setmasroufAdapter(moneyList);
                transaction.setAdapter(masroufAd);
            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }

}
