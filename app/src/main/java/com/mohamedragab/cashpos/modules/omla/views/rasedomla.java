package com.mohamedragab.cashpos.modules.omla.views;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.modules.omla.models.omla;
import com.mohamedragab.cashpos.modules.omla.wedgits.omlarasedAdapter;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.R;

import java.util.ArrayList;
import java.util.List;

public class rasedomla extends AppCompatActivity {
    EditText search;
    DataBaseHelper db;
    List<omla> omlaList;
    ListView omlaListView;
    com.mohamedragab.cashpos.modules.omla.wedgits.omlarasedAdapter omlarasedAdapter;
    @Override
    protected void onStart() {
        super.onStart();
        db.close();
        db = new DataBaseHelper(getBaseContext());

    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rasedomla);
        search = (EditText) findViewById(R.id.search);

        omlaList = new ArrayList<>();
        db = new DataBaseHelper(getBaseContext());


        omlarasedAdapter = new omlarasedAdapter(rasedomla.this, omlaList);

        omlaListView = (ListView) findViewById(R.id.list_omla);

        Cursor res = db.getallomla();


        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                omla omla = new omla();

                omla.setName(res.getString(1));
                omla.setAddress(res.getString(2));
                omla.setPaymoney(res.getDouble(6));
                omla.setHasmoney(res.getDouble(5));

                omlaList.add(omla);

            }
            omlarasedAdapter.setomlaAdapter(omlaList);
            omlaListView.setAdapter(omlarasedAdapter);

        } else {
            Toast.makeText(getBaseContext(), "لا يوجد عملاء برجاء اضافه عملاء !", Toast.LENGTH_SHORT).show();
            omlaList.clear();
            omlarasedAdapter.setomlaAdapter(omlaList);
            omlaListView.setAdapter(omlarasedAdapter);

        }

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (search.getText().equals("")) {
                    omlaList.clear();
                    omlarasedAdapter.setomlaAdapter(omlaList);
                    omlaListView.setAdapter(omlarasedAdapter);
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
                omlarasedAdapter.setomlaAdapter(omlaList);
                omlaListView.setAdapter(omlarasedAdapter);
                if (search.getText().equals("")) {
                    omlaList.clear();
                    omlarasedAdapter.setomlaAdapter(omlaList);
                    omlaListView.setAdapter(omlarasedAdapter);
                }
                if (res != null && res.getCount() > 0) {
                    while (res.moveToNext()) {
                        omla omla = new omla();
                        // Log.d("test","result "+res.getColumnName(4)+" "+res.getDouble(4));

                        omla.setName(res.getString(1));
                        omla.setPaymoney(res.getDouble(6));
                        omla.setHasmoney(res.getDouble(5));
                        omla.setAddress(res.getString(2));
                        omlaList.add(omla);

                    }

                    omlarasedAdapter.setomlaAdapter(omlaList);
                    omlaListView.setAdapter(omlarasedAdapter);

                } else {
                    Toast.makeText(getBaseContext(), "لا يوجد عملاء تطابق عملية البحث !", Toast.LENGTH_SHORT).show();
                    omlaList.clear();
                    omlarasedAdapter.setomlaAdapter(omlaList);
                    omlaListView.setAdapter(omlarasedAdapter);

                }
            }
        });
    }


    public void go_customers(View view) {
       onBackPressed();
    }
}