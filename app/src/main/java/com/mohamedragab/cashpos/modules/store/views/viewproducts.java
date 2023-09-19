package com.mohamedragab.cashpos.modules.store.views;


import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.scanner.scanner;
import com.mohamedragab.cashpos.modules.store.models.product;
import com.mohamedragab.cashpos.modules.store.wedgit.productAdapter;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class viewproducts extends AppCompatActivity {
    public static EditText search;
    DataBaseHelper db;
    List<product> productList;
    ListView productListView;
    productAdapter productAdapter;
    private ZXingScannerView scannerView;
    public static TextView total_money;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewproducts);
        search = (EditText) findViewById(R.id.search);
        total_money = (TextView) findViewById(R.id.total_money);
        productList = new ArrayList<>();
        db = new DataBaseHelper(getBaseContext());

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
            }

            @Override
            public void onAdClosed() {
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdImpression() {
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdOpened() {
                // covers the screen.
            }
        });
        productAdapter = new productAdapter(viewproducts.this, productList);

        productListView = (ListView) findViewById(R.id.list_products);

        Cursor res = db.getallproducts();

        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                product pro = new product();

                pro.setId(res.getInt(0));
                pro.setCode_id(res.getString(1));
                pro.setName(res.getString(2));
                pro.setSellprice(res.getDouble(4));
                pro.setQuantity(res.getDouble(6));
                pro.setDescription(res.getString(3));
                pro.setBuyprice(res.getDouble(5));
                pro.setExpiredate(res.getString(7));
                byte[] imgByte = res.getBlob(9);

                pro.setMeasure1(res.getString(10));
                pro.setMeasure2(res.getString(11));
                pro.setMeasure3(res.getString(12));
                pro.setSellprice2(res.getDouble(13));
                pro.setSellprice3(res.getDouble(14));
                pro.setCategory(res.getString(15));
                pro.setFactor2(res.getInt(16));
                pro.setFactor3(res.getInt(17));

                pro.setImage(imgByte);

                productList.add(pro);

            }
            productAdapter.setproductAdapter(productList);
            productListView.setAdapter(productAdapter);

        } else {
            Toast.makeText(getBaseContext(), "لا يوجد منتجات حاليا في المخزن برجاء اضافه منتجات !", Toast.LENGTH_SHORT).show();
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
                String Search_text = search.getText().toString();
                Cursor res = db.getproducts2(Search_text);
                productList.clear();
                productAdapter.setproductAdapter(productList);
                productListView.setAdapter(productAdapter);
                if (search.getText().equals("")) {
                    productList.clear();
                    productAdapter.setproductAdapter(productList);
                    productListView.setAdapter(productAdapter);
                }
                if (res != null && res.getCount() > 0) {
                    while (res.moveToNext()) {
                        product pro = new product();
                        //Log.d("test", "result " + res.getColumnName(4) + " " + res.getDouble(4));

                        pro.setId(res.getInt(0));
                        pro.setCode_id(res.getString(1));
                        pro.setName(res.getString(2));
                        pro.setSellprice(res.getDouble(4));
                        pro.setQuantity(res.getDouble(6));
                        pro.setDescription(res.getString(3));
                        pro.setBuyprice(res.getDouble(5));
                        pro.setExpiredate(res.getString(7));
                        byte[] imgByte = res.getBlob(9);
                        pro.setMeasure1(res.getString(10));
                        pro.setMeasure2(res.getString(11));
                        pro.setMeasure3(res.getString(12));
                        pro.setSellprice2(res.getDouble(13));
                        pro.setSellprice3(res.getDouble(14));
                        pro.setCategory(res.getString(15));
                        pro.setFactor2(res.getInt(16));
                        pro.setFactor3(res.getInt(17));
                        // BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
                        pro.setImage(imgByte);
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
        });
    }


    public void go_home(View view) {
        onBackPressed();
    }

    public void go_scan(View view) {
        String value = "view";
        Intent i = new Intent(viewproducts.this, scanner.class);
        i.putExtra("activity", value);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        db.close();
    }

    SearchableSpinner category_spinner;

    public void go_category_filter(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.deletecustomedialog);
        dialog.setCancelable(false);
        Button delete = (Button) dialog.findViewById(R.id.delete);
        delete.setText("حفظ");
        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setText("اختيار تصنيف");
        delete.setOnClickListener(v -> {
            if (category_spinner.getSelectedItemPosition()==0){
                Cursor res = db.getallproducts();
                productList.clear();
                if (res != null && res.getCount() > 0) {
                    while (res.moveToNext()) {
                        product pro = new product();

                        pro.setId(res.getInt(0));
                        pro.setCode_id(res.getString(1));
                        pro.setName(res.getString(2));
                        pro.setSellprice(res.getDouble(4));
                        pro.setQuantity(res.getDouble(6));
                        pro.setDescription(res.getString(3));
                        pro.setBuyprice(res.getDouble(5));
                        pro.setExpiredate(res.getString(7));
                        byte[] imgByte = res.getBlob(9);

                        pro.setMeasure1(res.getString(10));
                        pro.setMeasure2(res.getString(11));
                        pro.setMeasure3(res.getString(12));
                        pro.setSellprice2(res.getDouble(13));
                        pro.setSellprice3(res.getDouble(14));
                        pro.setCategory(res.getString(15));
                        pro.setFactor2(res.getInt(16));
                        pro.setFactor3(res.getInt(17));

                        pro.setImage(imgByte);

                        productList.add(pro);

                    }
                    productAdapter.setproductAdapter(productList);
                    productListView.setAdapter(productAdapter);
                    dialog.dismiss();

                } else {
                    Toast.makeText(getBaseContext(), "لا يوجد منتجات حاليا في المخزن برجاء اضافه منتجات !", Toast.LENGTH_SHORT).show();
                    productList.clear();
                    productAdapter.setproductAdapter(productList);
                    productListView.setAdapter(productAdapter);
                    dialog.dismiss();

                }
            }else{
                String CAT_name = category_spinner.getSelectedItem().toString();
                Cursor res = db.getproductsbycategory(CAT_name);
                productList.clear();
                if (res != null && res.getCount() > 0) {
                    while (res.moveToNext()) {
                        product pro = new product();
                        //Log.d("test", "result " + res.getColumnName(4) + " " + res.getDouble(4));

                        pro.setId(res.getInt(0));
                        pro.setCode_id(res.getString(1));
                        pro.setName(res.getString(2));
                        pro.setSellprice(res.getDouble(4));
                        pro.setQuantity(res.getDouble(6));
                        pro.setDescription(res.getString(3));
                        pro.setBuyprice(res.getDouble(5));
                        pro.setExpiredate(res.getString(7));
                        byte[] imgByte = res.getBlob(9);
                        pro.setMeasure1(res.getString(10));
                        pro.setMeasure2(res.getString(11));
                        pro.setMeasure3(res.getString(12));
                        pro.setSellprice2(res.getDouble(13));
                        pro.setSellprice3(res.getDouble(14));
                        pro.setCategory(res.getString(15));
                        pro.setFactor2(res.getInt(16));
                        pro.setFactor3(res.getInt(17));
                        // BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
                        pro.setImage(imgByte);
                        productList.add(pro);

                    }

                    productAdapter.setproductAdapter(productList);
                    productListView.setAdapter(productAdapter);
                    dialog.dismiss();

                }
                else {
                    Toast.makeText(getBaseContext(), "لا يوجد منتجات تطابق عملية البحث !", Toast.LENGTH_SHORT).show();
                    productList.clear();
                    productAdapter.setproductAdapter(productList);
                    productListView.setAdapter(productAdapter);
                    dialog.dismiss();
                }

            }

        });
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> dialog.dismiss());

        category_spinner = (SearchableSpinner) dialog.findViewById(R.id.omla2);
        final Cursor res2 = db.getallcategories();
        String omla_names[] = new String[0];
        if (res2 != null && res2.getCount() > 0) {
            omla_names = new String[res2.getCount() + 1];

            int index = 1;
            omla_names[0] = "جميع الاصناف";
            while (res2.moveToNext()) {
                omla_names[index] = res2.getString(0);
                index++;
            }

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, omla_names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(adapter);

        dialog.show();
    }
}
