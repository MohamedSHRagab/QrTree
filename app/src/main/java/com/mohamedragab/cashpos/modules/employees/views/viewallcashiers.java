package com.mohamedragab.cashpos.modules.employees.views;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.modules.employees.models.Cashier;
import com.mohamedragab.cashpos.modules.employees.wedgits.cashierAdapter;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class viewallcashiers extends AppCompatActivity {

    EditText search;
    DataBaseHelper db;
    List<Cashier> cashierList;
    ListView cashierListView;
    com.mohamedragab.cashpos.modules.employees.wedgits.cashierAdapter cashierAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewallcashiers);
        search = (EditText) findViewById(R.id.search);
        cashierList = new ArrayList<>();
        db = new DataBaseHelper(getBaseContext());


        cashierAdapter = new cashierAdapter(viewallcashiers.this, cashierList,"CASH");

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
                cashier.setNotes(res.getString(4));
                cashier.setUserName(res.getString(7));
                cashier.setPassword(res.getString(8));
                cashier.setP1(res.getString(9));
                cashier.setP2(res.getString(10));
                cashier.setP3(res.getString(11));
                cashier.setP4(res.getString(12));
                cashier.setP5(res.getString(13));
                cashier.setP6(res.getString(14));
                cashier.setP7(res.getString(15));
                cashier.setP8(res.getString(16));
                cashier.setP9(res.getString(17));
                cashier.setP10(res.getString(18));
                cashier.setP11(res.getString(19));
                cashier.setP12(res.getString(20));
                cashier.setP13(res.getString(21));
                cashier.setP14(res.getString(22));
                cashier.setP15(res.getString(23));
                cashier.setP16(res.getString(24));
                if (res.getString(6).equals("CASH")) {
                    cashierList.add(cashier);
                }
            }

            cashierAdapter.setcashierAdapter(cashierList);
            cashierListView.setAdapter(cashierAdapter);

        } else {
            Toast.makeText(getBaseContext(), "لا يوجد كاشير برجاء اضافه الكاشير !", Toast.LENGTH_SHORT).show();
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
                        cashier.setNotes(res.getString(4));
                        cashier.setUserName(res.getString(7));
                        cashier.setPassword(res.getString(8));
                        cashier.setP1(res.getString(9));
                        cashier.setP2(res.getString(10));
                        cashier.setP3(res.getString(11));
                        cashier.setP4(res.getString(12));
                        cashier.setP5(res.getString(13));
                        cashier.setP6(res.getString(14));
                        cashier.setP7(res.getString(15));
                        cashier.setP8(res.getString(16));
                        cashier.setP9(res.getString(17));
                        cashier.setP10(res.getString(18));
                        cashier.setP11(res.getString(19));
                        cashier.setP12(res.getString(20));
                        cashier.setP13(res.getString(21));
                        cashier.setP14(res.getString(22));
                        cashier.setP15(res.getString(23));
                        cashier.setP16(res.getString(24));
                        if (res.getString(6).equals("CASH")) {
                            cashierList.add(cashier);
                        }
                    }

                    cashierAdapter.setcashierAdapter(cashierList);
                    cashierListView.setAdapter(cashierAdapter);

                } else {
                    Toast.makeText(getBaseContext(), "لا يوجد كاشير تطابق عملية البحث !", Toast.LENGTH_SHORT).show();
                    cashierList.clear();
                    cashierAdapter.setcashierAdapter(cashierList);
                    cashierListView.setAdapter(cashierAdapter);

                }
            }
        });
    }

    public void go_cashiers(View view) {
        onBackPressed();
    }
}
