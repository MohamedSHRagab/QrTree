package com.mohamedragab.cashpos.modules.kist.views;

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

import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.kist.adapters.omlaAdapter2;
import com.mohamedragab.cashpos.modules.latekist.views.latekists;
import com.mohamedragab.cashpos.modules.omla.models.omla;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.sales.models.Kist;
import com.mohamedragab.cashpos.utils.Round;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class omalkist extends AppCompatActivity {
    EditText search;
    DataBaseHelper db;
    List<omla> omlaList;
    ListView omlaListView;
    omlaAdapter2 omlaAdapter;
    List kistList;
    Date datecollectDate, current;
    TextView notificationCount;
    public static TextView not_paid_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_omalkist);

        search = (EditText) findViewById(R.id.search);
        omlaList = new ArrayList<>();
        db = new DataBaseHelper(getBaseContext());
        notificationCount = (TextView) findViewById(R.id.notificationcount);
        not_paid_total = (TextView) findViewById(R.id.not_paid_total);
        if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()) != null) {
            if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getP10().equals("1")) {
                not_paid_total.setVisibility(View.GONE);
            }
        }

        omlaAdapter = new omlaAdapter2(omalkist.this, omlaList);

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
            res.close();
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
                omalkist.not_paid_total.setText("");

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
res.close();
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

       // getlatekist();
    }

    void getlatekist() {
        kistList = new ArrayList<>();
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


        Cursor res = db.getallkest();

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

                String collectDate = res.getString(4);

                try {
                    datecollectDate = new SimpleDateFormat("dd-MM-yyyy", Locale.US).parse(collectDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (res.getString(6).equals("not_paid") && current.after(datecollectDate)) {
                    kistList.add(kist);
                }
            }
            res.close();
            notificationCount.setText(kistList.size() + "");
            db.close();
            this.db.close();

        }
    }

    public void go_customers(View view) {
        onBackPressed();
        db.close();
    }

    public void go_kistlate(View view) {
        startActivity(new Intent(this, latekists.class));
    }

    public void gettotal(View view) {
        double total = 0;
        DataBaseHelper kestDataBaseHelper = new DataBaseHelper(getBaseContext());

        for (int i = 0; i < omlaList.size(); i++) {
            Cursor res2 = kestDataBaseHelper.getkestbyclient(omlaList.get(i).getName());
            double totalNotPaid2 = 0;
            if (res2 != null && res2.getCount() > 0) {
                while (res2.moveToNext()) {
                    Kist kist = new Kist();

                    kist.setId(res2.getInt(0));
                    kist.setInvoice_id(res2.getString(1));
                    kist.setDescription(res2.getString(2));
                    kist.setDayslimit(res2.getInt(3));
                    kist.setCollectdate(res2.getString(4));
                    kist.setKist_value(res2.getDouble(5));
                    kist.setStatue(res2.getString(6));
                    kist.setClient_name(res2.getString(7));
                    kist.setPay_type(res2.getString(8));
                    kist.setDamen_name(res2.getString(9));
                    kist.setDamen_phone(res2.getString(10));
                    kist.setPaydate(res2.getString(11));

                    if (res2.getString(6).equals("not_paid")) {
                        totalNotPaid2 += res2.getDouble(5);
                    }
                }
                res2.close();

            }
            total += totalNotPaid2;
        }
        omalkist.not_paid_total.setText("الاجمالي : "+ Round.round(total,3) + SheredPrefranseHelper.getmoney_type(getBaseContext()));
        kestDataBaseHelper.close();
    }
}
