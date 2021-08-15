package com.mohamedragab.cashpos.modules.repair.views;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.modules.repair.adapters.repairlateAdapter;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.repair.models.Repair;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class laterepairs extends AppCompatActivity {

    DataBaseHelper db;
    List<Repair> repairList;
    ListView repairsListView;
    repairlateAdapter repairAdapter;
    Date datecollectDate,current;
    ArrayList<Long> Days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laterepairs);

        repairsListView = (ListView) findViewById(R.id.list);

        repairList = new ArrayList<>();
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


        Cursor res = db.getallrepair();

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

                String collectDate=res.getString(3);

                try {
                     datecollectDate=new SimpleDateFormat("dd-MM-yyyy",Locale.US).parse(collectDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (res.getString(5).equals("not_completed")) {
                    repairList.add(repair);
                    long days = getDifferenceDays(datecollectDate,current);
                    Days.add(days);
                }
            }
            repairAdapter = new repairlateAdapter(laterepairs.this, repairList,Days);
            repairAdapter.setrepairlateAdapter(repairList,Days);
            repairsListView.setAdapter(repairAdapter);

        } else {
            Toast.makeText(getBaseContext(), "لا يوجد صيانه حاليا !", Toast.LENGTH_SHORT).show();
            repairList.clear();
            repairAdapter = new repairlateAdapter(laterepairs.this, repairList,Days);
            repairAdapter.setrepairlateAdapter(repairList,Days);
            repairsListView.setAdapter(repairAdapter);
        }


    }

    public void go_money_box(View view) {
        onBackPressed();
    }
    public static long getDifferenceDays(Date d1, Date d2) {
        long diff =   d1.getTime() - d2.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        db.close();
    }
}
