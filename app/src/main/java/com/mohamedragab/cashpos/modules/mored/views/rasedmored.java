package com.mohamedragab.cashpos.modules.mored.views;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.modules.mored.models.mored;
import com.mohamedragab.cashpos.modules.mored.wedgit.moredrasedAdapter;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.R;

import java.util.ArrayList;
import java.util.List;

public class rasedmored extends AppCompatActivity {
    EditText search;
    DataBaseHelper db;
    List<mored> moredList;
    ListView moredListView;
    com.mohamedragab.cashpos.modules.mored.wedgit.moredrasedAdapter moredrasedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rasedmored);
        search = (EditText) findViewById(R.id.search);

        moredList = new ArrayList<>();
        db = new DataBaseHelper(getBaseContext());


        moredrasedAdapter = new moredrasedAdapter(rasedmored.this, moredList);

        moredListView = (ListView) findViewById(R.id.list_omla);

        Cursor res = db.getallmored();


        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                mored mored = new mored();

                mored.setName(res.getString(1));
                mored.setAddress(res.getString(2));
                mored.setPaymoney(res.getDouble(6));
                mored.setHasmoney(res.getDouble(5));

                moredList.add(mored);

            }
            moredrasedAdapter.setmoredAdapter(moredList);
            moredListView.setAdapter(moredrasedAdapter);

        } else {
            Toast.makeText(getBaseContext(), "لا يوجد موردين برجاء اضافه موردين !", Toast.LENGTH_SHORT).show();
            moredList.clear();
            moredrasedAdapter.setmoredAdapter(moredList);
            moredListView.setAdapter(moredrasedAdapter);

        }

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (search.getText().equals("")) {
                    moredList.clear();
                    moredrasedAdapter.setmoredAdapter(moredList);
                    moredListView.setAdapter(moredrasedAdapter);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String Search_text = search.getText().toString();
                Cursor res = db.getmored2(Search_text);
                moredList.clear();
                moredrasedAdapter.setmoredAdapter(moredList);
                moredListView.setAdapter(moredrasedAdapter);
                if (search.getText().equals("")) {
                    moredList.clear();
                    moredrasedAdapter.setmoredAdapter(moredList);
                    moredListView.setAdapter(moredrasedAdapter);
                }
                if (res != null && res.getCount() > 0) {
                    while (res.moveToNext()) {
                        mored mored = new mored();
                        // Log.d("test","result "+res.getColumnName(4)+" "+res.getDouble(4));

                        mored.setName(res.getString(1));
                        mored.setPaymoney(res.getDouble(6));
                        mored.setHasmoney(res.getDouble(5));
                        mored.setAddress(res.getString(2));
                        moredList.add(mored);

                    }

                    moredrasedAdapter.setmoredAdapter(moredList);
                    moredListView.setAdapter(moredrasedAdapter);

                } else {
                    Toast.makeText(getBaseContext(), "لا يوجد موردين تطابق عملية البحث !", Toast.LENGTH_SHORT).show();
                    moredList.clear();
                    moredrasedAdapter.setmoredAdapter(moredList);
                    moredListView.setAdapter(moredrasedAdapter);

                }
            }
        });
    }


    public void go_customers(View view) {
       onBackPressed();
    }
}
