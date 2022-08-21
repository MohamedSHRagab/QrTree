package com.mohamedragab.cashpos.modules.moneybox.views;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.base.DateUtil;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.moneybox.models.money;
import com.mohamedragab.cashpos.modules.moneyboxreport.views.moneyboxreport;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.utils.Round;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class moneybox extends AppCompatActivity {
    RadioButton add, min;
    Button action;
    EditText money_et, notes;
    TextView currentDate, total_ET;

    private Calendar mcalendar;
    private int day, month, year;
    DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moneybox);

        add = (RadioButton) findViewById(R.id.radioadd);
        min = (RadioButton) findViewById(R.id.radiomin);
        action = (Button) findViewById(R.id.button);
        money_et = (EditText) findViewById(R.id.money);
        notes = (EditText) findViewById(R.id.notes);
        db = new DataBaseHelper(getBaseContext());
        currentDate = (TextView) findViewById(R.id.currentdate);
        total_ET = (TextView) findViewById(R.id.total);


        currentDate.setOnClickListener(view -> DateDialog());
        final Cursor res = db.getallTransactions();
        if (res != null && res.getCount() > 0) {
            double total = 0;
            while (res.moveToNext()) {
                total = res.getDouble(3);
                total_ET.setText(Round.round(total,3) + SheredPrefranseHelper.getmoney_type(moneybox.this));

            }

        }
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String formattedDate = df.format(c);
        currentDate.setText(formattedDate + "");


        min.setOnClickListener(v -> action.setText("خصم المبلغ من الخزينة"));
        add.setOnClickListener(v -> action.setText("اضافه المبلغ الي الخزينة"));
        action.setOnClickListener(view -> {
            if (add.isChecked()) {
                if (money_et.getText().toString().equals("") || notes.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), "من فضلك اكمل البيانات المطلوبه !", Toast.LENGTH_SHORT).show();
                } else {
                    double money_value = Double.parseDouble(money_et.getText().toString());
                    String notes_value = notes.getText().toString();
                    String current_date = currentDate.getText().toString();
                    money money = new money();
                    money.setDate(current_date);
                    money.setNotes(notes_value);
                    money.setValue(Round.round(money_value,3));


                    final Cursor res1 = db.getallTransactions();
                    double total = 0;
                    if (res1 != null && res1.getCount() > 0) {
                        while (res1.moveToNext()) {
                            total = res1.getDouble(3);

                        }

                    }
                    money.setTotalbefore(Round.round(total,3));
                    double totalAfter = total + money_value;

                    money.setTotalAfter(Round.round(totalAfter,3));
                    Boolean result = db.insert_date(money);

                    if (result == true) {
                        notes.setText("");
                        money_et.setText("");
                        MediaPlayer mp = MediaPlayer.create(getBaseContext(), R.raw.finalsound);
                        mp.start();

                       Toast.makeText(getBaseContext(), "تم اضافه المبلغ الي الصندوق ", Toast.LENGTH_SHORT).show();
                        total_ET.setText(Round.round(totalAfter,3) + SheredPrefranseHelper.getmoney_type(moneybox.this));

                    } else {
                       // Toast.makeText(getBaseContext(), "فشل اضافه المبلغ الي الصندوق تحقق من ادخال بيانات صحيحه ", Toast.LENGTH_SHORT).show();

                    }
                }
            } else {
                if (money_et.getText().toString().equals("") || notes.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), "من فضلك اكمل البيانات المطلوبه !", Toast.LENGTH_SHORT).show();
                } else {
                    double money_value = Double.parseDouble(money_et.getText().toString());
                    String notes_value = notes.getText().toString();
                    String current_date = currentDate.getText().toString();

                    money money = new money();
                    money.setDate(current_date);
                    money.setNotes(notes_value);
                    money.setValue(Round.round(money_value,3));


                    final Cursor res1 = db.getallTransactions();
                    double total = 0;
                    if (res1 != null && res1.getCount() > 0) {
                        while (res1.moveToNext()) {
                            total = res1.getDouble(3);

                        }

                    }
                    money.setTotalbefore(total);
                    double totalAfter = total - money_value;

                    money.setTotalAfter(Round.round(totalAfter,3));
                    Boolean result = db.insert_date(money);

                    if (result == true) {
                        notes.setText("");
                        money_et.setText("");
                        MediaPlayer mp = MediaPlayer.create(moneybox.this, R.raw.finalsound);
                        mp.start();
                        Toast.makeText(getBaseContext(), "تم خصم المبلغ من الخزينة ", Toast.LENGTH_SHORT).show();
                        total_ET.setText(totalAfter + SheredPrefranseHelper.getmoney_type(moneybox.this));

                    } else {
                        //Toast.makeText(getBaseContext(), "فشل خصم المبلغ من الصندوق تحقق من ادخال بيانات صحيحه ", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }


    public void DateDialog() {
        Locale.setDefault(Locale.US);
        mcalendar = Calendar.getInstance();
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);
        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener listener = (view, year, monthOfYear, dayOfMonth) -> currentDate.setText(DateUtil.getDateOnly(year, monthOfYear, dayOfMonth));
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }

    public void go_home(View view) {
        onBackPressed();
        db.close();
    }

    public void go_report(View view) {
        startActivity(new Intent(this, moneyboxreport.class));
    }
}
