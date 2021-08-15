package com.mohamedragab.cashpos.modules.employees.views;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.modules.employees.models.Cashier;
import com.mohamedragab.cashpos.modules.employees.wedgits.cashierAdapter;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class delivery extends AppCompatActivity {

    EditText search;
    DataBaseHelper db;
    List<Cashier> cashierList;
    ListView cashierListView;
    com.mohamedragab.cashpos.modules.employees.wedgits.cashierAdapter cashierAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        search = (EditText) findViewById(R.id.search);
        cashierList = new ArrayList<>();
        db = new DataBaseHelper(getBaseContext());


        cashierAdapter = new cashierAdapter(delivery.this, cashierList,"DEL");

        cashierListView = (ListView) findViewById(R.id.list_omla);

        Cursor res = db.getallcashier();

        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                Cashier cashier = new Cashier();
                cashier.setId(res.getInt(0));
                cashier.setName(res.getString(1));
                cashier.setPhone(res.getString(3));
                cashier.setAddress(res.getString(2));
                byte[] imgByte = res.getBlob(5);
                cashier.setImage(imgByte);
                if (res.getString(6).equals("DEL")) {
                    cashierList.add(cashier);
                }
            }
            cashierAdapter.setcashierAdapter(cashierList);
            cashierListView.setAdapter(cashierAdapter);

        } else {
            cashierList.clear();
            cashierAdapter.setcashierAdapter(cashierList);
            cashierListView.setAdapter(cashierAdapter);

        }

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (search.getText().equals("")) {
                    cashierList.clear();
                    cashierAdapter.setcashierAdapter(cashierList);
                    cashierListView.setAdapter(cashierAdapter);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String Search_text = search.getText().toString();
                Cursor res = db.getcashier2(Search_text);
                cashierList.clear();
                cashierAdapter.setcashierAdapter(cashierList);
                cashierListView.setAdapter(cashierAdapter);
                if (search.getText().equals("")) {
                    cashierList.clear();
                    cashierAdapter.setcashierAdapter(cashierList);
                    cashierListView.setAdapter(cashierAdapter);
                }
                if (res != null && res.getCount() > 0) {
                    while (res.moveToNext()) {
                        Cashier cashier = new Cashier();

                        cashier.setId(res.getInt(0));
                        cashier.setName(res.getString(1));
                        cashier.setPhone(res.getString(3));
                        cashier.setAddress(res.getString(2));
                        byte[] imgByte = res.getBlob(5);
                        cashier.setImage(imgByte);
                        if (res.getString(6).equals("DEL")) {
                            cashierList.add(cashier);
                        }
                    }

                    cashierAdapter.setcashierAdapter(cashierList);
                    cashierListView.setAdapter(cashierAdapter);

                } else {

                    cashierList.clear();
                    cashierAdapter.setcashierAdapter(cashierList);
                    cashierListView.setAdapter(cashierAdapter);

                }
            }
        });
    }

    public void go_back(View view) {
        onBackPressed();
    }

    public void new_DELIVERY(View view) {
        startActivity(new Intent(this,new_delivery.class));
        finish();
    }
}
