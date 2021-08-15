package com.mohamedragab.cashpos.modules.repair.views;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.modules.repair.adapters.omlaAdapter3;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.omla.models.omla;
import com.mohamedragab.cashpos.modules.repair.models.Repair;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class omlarepair extends AppCompatActivity {
    EditText search;
    DataBaseHelper db;
    List<omla> omlaList;
    ListView omlaListView;
    omlaAdapter3 omlaAdapter;
    List repairList;
    Date datecollectDate, current;
    TextView notificationCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_omlarepair);
        search = (EditText) findViewById(R.id.search);
        omlaList = new ArrayList<>();
        db = new DataBaseHelper(getBaseContext());
        notificationCount = (TextView) findViewById(R.id.notificationcount);

        omlaAdapter = new omlaAdapter3(omlarepair.this, omlaList);

        omlaListView = (ListView) findViewById(R.id.list_omla);

        Cursor res = db.getallomla();


        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                omla omla = new omla();

                omla.setName(res.getString(1));
                omla.setPhone(res.getString(3));
                omla.setAddress(res.getString(2));

                omlaList.add(omla);

            }
            omlaAdapter.setomlaAdapter(omlaList);
            omlaListView.setAdapter(omlaAdapter);

        } else {
            Toast.makeText(getBaseContext(), "لا يوجد عملاء برجاء اضافه عملاء !", Toast.LENGTH_SHORT).show();
            omlaList.clear();
            omlaAdapter.setomlaAdapter(omlaList);
            omlaListView.setAdapter(omlaAdapter);

        }

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (search.getText().equals("")) {
                    omlaList.clear();
                    omlaAdapter.setomlaAdapter(omlaList);
                    omlaListView.setAdapter(omlaAdapter);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String Search_text = search.getText().toString();
                Cursor res = db.getomla2(Search_text);
                omlaList.clear();
                omlaAdapter.setomlaAdapter(omlaList);
                omlaListView.setAdapter(omlaAdapter);
                if (search.getText().equals("")) {
                    omlaList.clear();
                    omlaAdapter.setomlaAdapter(omlaList);
                    omlaListView.setAdapter(omlaAdapter);
                }
                if (res != null && res.getCount() > 0) {
                    while (res.moveToNext()) {
                        omla omla = new omla();
                        // Log.d("test","result "+res.getColumnName(4)+" "+res.getDouble(4));

                        omla.setName(res.getString(1));
                        omla.setPhone(res.getString(3));
                        omla.setAddress(res.getString(2));
                        omlaList.add(omla);

                    }

                    omlaAdapter.setomlaAdapter(omlaList);
                    omlaListView.setAdapter(omlaAdapter);

                } else {
                    Toast.makeText(getBaseContext(), "لا يوجد عملاء تطابق عملية البحث !", Toast.LENGTH_SHORT).show();
                    omlaList.clear();
                    omlaAdapter.setomlaAdapter(omlaList);
                    omlaListView.setAdapter(omlaAdapter);

                }
            }
        });

        getlaterepairs();
    }

    void getlaterepairs() {
        repairList = new ArrayList<>();
        DataBaseHelper db = new DataBaseHelper(getBaseContext());

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String currentdate = df.format(c);
        try {
            current = new SimpleDateFormat("dd-MM-yyyy", Locale.US).parse(currentdate);
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


                String collectDate = res.getString(3);

                try {
                    datecollectDate = new SimpleDateFormat("dd-MM-yyyy", Locale.US).parse(collectDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (res.getString(5).equals("not_completed")) {
                    repairList.add(repair);
                }
            }
            notificationCount.setText(repairList.size() + "");
            db.close();
            this.db.close();

        }
    }

    public void go_customers(View view) {
        onBackPressed();
        db.close();
    }
    public void go_kistlate(View view) {
        startActivity(new Intent(this, laterepairs.class));
    }
}