package com.mohamedragab.cashpos.modules.notification.views;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.notification.wedgits.productquantityAdapter;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.store.models.product;

import java.util.ArrayList;
import java.util.List;


public class notification extends AppCompatActivity {

    public static EditText search;
    DataBaseHelper db;
    List<product> productList;
    ListView productListView;
    productquantityAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        search = (EditText) findViewById(R.id.search);
        productList = new ArrayList<>();
        db = new DataBaseHelper(getBaseContext());


        productAdapter = new productquantityAdapter(notification.this, productList);

        productListView = (ListView) findViewById(R.id.list_products);

        Cursor res = db.getproductsbyquantity(5.0);


        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                product pro = new product();

                pro.setId(res.getInt(0));
                pro.setName(res.getString(2));
                pro.setSellprice(res.getDouble(4));
                pro.setQuantity(res.getInt(6));
                pro.setDescription(res.getString(3));
                pro.setBuyprice(res.getDouble(5));
                pro.setExpiredate(res.getString(7));

                productList.add(pro);

            }
            productAdapter.setproductAdapter(productList);
            productListView.setAdapter(productAdapter);

        } else {
            Toast.makeText(getBaseContext(), "لا يوجد منتجات تطابق البحث في المخزن !", Toast.LENGTH_SHORT).show();
            productList.clear();
            productAdapter.setproductAdapter(productList);
            productListView.setAdapter(productAdapter);

        }

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (search.getText().equals("")) {
                    productList.clear();
                    productAdapter.setproductAdapter(productList);
                    productListView.setAdapter(productAdapter);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (search.getText().toString().equals("")) {
                    productList.clear();
                    productAdapter.setproductAdapter(productList);
                    productListView.setAdapter(productAdapter);
                }else {
                    Cursor res = db.getproductsbyquantity(Double.parseDouble(search.getText().toString()));
                    productList.clear();
                    productAdapter.setproductAdapter(productList);
                    productListView.setAdapter(productAdapter);
                    if (res != null && res.getCount() > 0) {
                        while (res.moveToNext()) {
                            product pro = new product();

                            pro.setName(res.getString(2));
                            pro.setSellprice(res.getDouble(4));
                            pro.setQuantity(res.getInt(6));
                            productList.add(pro);

                        }

                        productAdapter.setproductAdapter(productList);
                        productListView.setAdapter(productAdapter);

                    } else {
                        Toast.makeText(getBaseContext(), "لا يوجد منتجات تطابق عملية البحث !", Toast.LENGTH_SHORT).show();
                        productList.clear();
                        productAdapter.setproductAdapter(productList);
                        productListView.setAdapter(productAdapter);

                    }
                }

            }
        });
    }


    public void go_home(View view) {
        onBackPressed();
    }


}
