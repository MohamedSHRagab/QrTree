package com.mohamedragab.cashpos.modules.buyinvoicedetails.views;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.buy.models.buyproduct;
import com.mohamedragab.cashpos.modules.buyinvoice.views.buyinvoice;
import com.mohamedragab.cashpos.modules.buyinvoicedetails.wedgits.invoicedetailsAdapter;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;

public class buyinvoicedetails extends AppCompatActivity {
    TextView invoiceid ;
    DataBaseHelper db;
    List<buyproduct> productsList;
    ListView productsListView;
    String value;
    com.mohamedragab.cashpos.modules.buyinvoicedetails.wedgits.invoicedetailsAdapter invoicedetailsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyinvoicedetails);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("key");
        }

        invoiceid=(TextView)findViewById(R.id.invoice_id);
        invoiceid.setText(value+"");
        productsListView=(ListView)findViewById(R.id.list_products);

        productsList = new ArrayList<>();
        db = new DataBaseHelper(getBaseContext());

        invoicedetailsAdapter = new invoicedetailsAdapter(buyinvoicedetails.this, productsList);

        Cursor res = db.getbuyproduct(value);

        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                buyproduct buyproduct = new buyproduct();

                buyproduct.setName(res.getString(4));
                buyproduct.setQuantity(res.getDouble(2));
                buyproduct.setBuyprice(res.getDouble(7));
                buyproduct.setId(res.getInt(0));
                buyproduct.setInvoice_id(res.getString(1));

                productsList.add(buyproduct);

            }
            invoicedetailsAdapter.setinvoicedetailsAdapter(productsList);
            productsListView.setAdapter(invoicedetailsAdapter);

        } else {
            Toast.makeText(getBaseContext(), "لا يوجد منتجات حاليا !", Toast.LENGTH_SHORT).show();
            productsList.clear();
            invoicedetailsAdapter.setinvoicedetailsAdapter(productsList);
            productsListView.setAdapter(invoicedetailsAdapter);

        }


    }

    public void go_invoices(View view) {
       onBackPressed();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(buyinvoicedetails.this, buyinvoice.class));

    }
}
