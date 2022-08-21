package com.mohamedragab.cashpos.modules.latekist.views;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.modules.latekist.adapters.kistlateAdapter;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.sales.models.Kist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class latekists extends AppCompatActivity {
    TextView invoiceid;
    DataBaseHelper db;
    List<Kist> kistList;
    ListView kistsListView;
    kistlateAdapter kistAdapter;
    Date datecollectDate,current;
    ArrayList<Long> Days;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latekists);

        kistsListView = (ListView) findViewById(R.id.list);

        kistList = new ArrayList<>();
        db = new DataBaseHelper(getBaseContext());

        Days=new ArrayList<>();
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String currentdate = df.format(c);
        try {
            current=new SimpleDateFormat("dd-MM-yyyy",Locale.US).parse(currentdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Cursor res = db.getlatekest();

        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                Kist kist = new Kist();

                kist.setId(res.getInt(0));
                kist.setInvoice_id(res.getString(1));
                kist.setDescription(res.getString(2));
                kist.setDayslimit(res.getInt(3));
                kist.setCollectdate(res.getString(4));
                kist.setKist_value(res.getDouble(5));
                kist.setStatue(res.getString(6));
                kist.setClient_name(res.getString(7));
                kist.setPay_type(res.getString(8));
                kist.setDamen_name(res.getString(9));
                kist.setDamen_phone(res.getString(10));
                kist.setPaydate(res.getString(11));

                String collectDate=res.getString(4);

                try {
                     datecollectDate=new SimpleDateFormat("dd-MM-yyyy",Locale.US).parse(collectDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (res.getString(6).equals("not_paid")&&current.after(datecollectDate)) {
                    kistList.add(kist);
                    long days = getDifferenceDays(datecollectDate,current);
                    Days.add(days);
                }
            }
            kistAdapter = new kistlateAdapter(latekists.this, kistList,Days);
            kistAdapter.setKistlateAdapter(kistList,Days);
            kistsListView.setAdapter(kistAdapter);

        } else {
            Toast.makeText(getBaseContext(), "لا يوجد أقساط حاليا !", Toast.LENGTH_SHORT).show();
            kistList.clear();
            kistAdapter = new kistlateAdapter(this, kistList,Days);
            kistAdapter.setKistlateAdapter(kistList,Days);
            kistsListView.setAdapter(kistAdapter);
        }


    }

    public void go_money_box(View view) {
        onBackPressed();
    }
    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
}
