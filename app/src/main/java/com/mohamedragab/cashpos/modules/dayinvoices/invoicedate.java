package com.mohamedragab.cashpos.modules.dayinvoices;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.DateUtil;
import com.mohamedragab.cashpos.modules.invoicedetails.wedgits.invoicedetailsAdapter;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.sales.models.sellproduct;
import com.mohamedragab.cashpos.utils.Round;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class invoicedate extends AppCompatActivity {

    public static   TextView total_txt;
    DataBaseHelper db;
    List<sellproduct> productsList;
    ListView productsListView;
    String value,client_name;
    invoicedetailsAdapter invoicedetailsAdapter;
    public static List<Bitmap> imagesList;
    TextView  search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoicedate);

          search = (TextView) findViewById(R.id.dateee);
        total_txt = (TextView) findViewById(R.id.total);
        search.setOnClickListener(v -> DateDialog());

        productsListView = (ListView) findViewById(R.id.list_products);

        productsList = new ArrayList<>();
        db = new DataBaseHelper(getBaseContext());

        invoicedetailsAdapter = new invoicedetailsAdapter(invoicedate.this, productsList);

        Cursor res = db.getsellproductbydate(search.getText().toString());

        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                sellproduct sellproduct = new sellproduct();

                sellproduct.setName(res.getString(4));
                sellproduct.setInvoice_id(res.getString(1));
                sellproduct.setQuantity(res.getDouble(2));
                sellproduct.setSellprice(res.getDouble(6));
                sellproduct.setId(res.getInt(0));

                productsList.add(sellproduct);

            }
            double total = 0;
            for (int i = 0; i < productsList.size(); i++) {
                total += productsList.get(i).getQuantity();
            }
            total_txt.setText("اجمالي الكمية : " + Round.round(total, 3) );

            invoicedetailsAdapter.setinvoicedetailsAdapter(productsList,client_name);
            productsListView.setAdapter(invoicedetailsAdapter);

        } else {
            Toast.makeText(getBaseContext(), "لا يوجد منتجات حاليا !", Toast.LENGTH_SHORT).show();
            productsList.clear();
            double total = 0;
            for (int i = 0; i < productsList.size(); i++) {
                total += productsList.get(i).getQuantity();
            }
            total_txt.setText("اجمالي الكمية : " + Round.round(total, 3) );

            invoicedetailsAdapter.setinvoicedetailsAdapter(productsList,client_name);
            productsListView.setAdapter(invoicedetailsAdapter);

        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
    private Calendar mcalendar;
    private int day, month, year;

    public void DateDialog() {
        Locale.setDefault(Locale.US);

        mcalendar = Calendar.getInstance();
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);
        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener listener = (view, year, monthOfYear, dayOfMonth) -> {
            search.setText(DateUtil.getDateOnly(year, monthOfYear, dayOfMonth));
            MediaPlayer mp = MediaPlayer.create(invoicedate.this, R.raw.finalsound);
            mp.start();
            productsList = new ArrayList<>();
            db = new DataBaseHelper(getBaseContext());

            invoicedetailsAdapter = new invoicedetailsAdapter(invoicedate.this, productsList);

            Cursor res = db.getsellproductbydate(search.getText().toString());

            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    sellproduct sellproduct = new sellproduct();

                    sellproduct.setName(res.getString(4));
                    sellproduct.setInvoice_id(res.getString(1));
                    sellproduct.setQuantity(res.getDouble(2));
                    sellproduct.setSellprice(res.getDouble(6));
                    sellproduct.setId(res.getInt(0));

                    productsList.add(sellproduct);

                }
                double total = 0;
                for (int i = 0; i < productsList.size(); i++) {
                    total += productsList.get(i).getQuantity();
                }
                total_txt.setText("اجمالي الكمية : " + Round.round(total, 3) );

                invoicedetailsAdapter.setinvoicedetailsAdapter(productsList,client_name);
                productsListView.setAdapter(invoicedetailsAdapter);

            } else {
                Toast.makeText(getBaseContext(), "لا يوجد منتجات حاليا !", Toast.LENGTH_SHORT).show();
                productsList.clear();
                double total = 0;
                for (int i = 0; i < productsList.size(); i++) {
                    total += productsList.get(i).getQuantity();
                }
                total_txt.setText("اجمالي الكمية : " + Round.round(total, 3) );

                invoicedetailsAdapter.setinvoicedetailsAdapter(productsList,client_name);
                productsListView.setAdapter(invoicedetailsAdapter);

            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }


    public void go_invoices(View view) {
        onBackPressed();
    }
}
