package com.mohamedragab.cashpos.modules.store.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.exchange.views.exchange;
import com.mohamedragab.cashpos.modules.notification.views.notification;

public class store extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
    }

    public void go_home(View view) {
      onBackPressed();
    }

    public void add_product(View view) {
        startActivity(new Intent(this, addproduct.class));
        finish();
    }

    public void go_view_products(View view) {
        startActivity(new Intent(this, viewproducts.class));
        finish();
    }


    public void search_quantity(View view) {
        startActivity(new Intent(this, notification.class));
        finish();
    }

    public void add_excel(View view) {

    }
    public void go_add_new_category(View view) {
        startActivity(new Intent(this, addnewcategory.class));
        finish();
    }


    public void go_add_new_measure(View view) {
        startActivity(new Intent(this, addnewmeasure.class));
        finish();
    }

    public void report_quantity(View view) {
        startActivity(new Intent(this, exchange.class));

    }
}
