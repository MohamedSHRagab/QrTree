package com.mohamedragab.cashpos.modules.masrouf;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.base.DateUtil;
import com.mohamedragab.cashpos.modules.masroufreport.views.masroufreport;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.utils.Round;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class masrof extends AppCompatActivity {

    private Calendar mcalendar;
    private int day, month, year;
    DataBaseHelper db;
    EditText money_et, notes;
    TextView currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masrof);
        currentDate = (TextView) findViewById(R.id.currentdate);
        money_et = (EditText) findViewById(R.id.value);
        notes = (EditText) findViewById(R.id.notes);
        db = new DataBaseHelper(this);

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String formattedDate = df.format(c);
        currentDate.setText(formattedDate + "");

        currentDate = (TextView) findViewById(R.id.currentdate);
        currentDate.setOnClickListener(view -> DateDialog());

    }

    public void save(View view) {
        if (money_et.getText().toString().equals("") || notes.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), "من فضلك اكمل البيانات المطلوبه !", Toast.LENGTH_SHORT).show();
        } else {
            double money_value = Double.parseDouble(money_et.getText().toString());
            String notes_value ="مصروف :"+ notes.getText().toString();
            String current_date = currentDate.getText().toString();

            com.mohamedragab.cashpos.modules.moneybox.models.money money = new com.mohamedragab.cashpos.modules.moneybox.models.money();
            money.setDate(current_date);
            money.setNotes(notes_value);
            money.setValue(Round.round(money_value,3));

            final Cursor res = db.getallTransactions();
            double total = 0;
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    total = res.getDouble(3);
                }
            }
            money.setTotalbefore(Round.round(total,3));
            double totalAfter = total - money_value;

            money.setTotalAfter(Round.round(totalAfter,3));

            if (db.insert_date(money)) {
                money_et.setText("");
                notes.setText("");
                MediaPlayer mp = MediaPlayer.create(masrof.this, R.raw.finalsound);
                mp.start();
                Toast.makeText(getBaseContext(), "تم خصم المبلغ من الصندوق ", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getBaseContext(), "فشل خصم المبلغ من الصندوق تحقق من ادخال بيانات صحيحه ", Toast.LENGTH_SHORT).show();

            }
        }
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
        db.close();
        onBackPressed();
    }

    public void go_report(View view) {
        Intent intent = new Intent(this, masroufreport.class);
        intent.putExtra("date", "0");
        startActivity(intent);
    }

    public void go_report_month(View view) {
        Intent intent = new Intent(this, masroufreport.class);
        intent.putExtra("date", "1");
        startActivity(intent);
    }
}
