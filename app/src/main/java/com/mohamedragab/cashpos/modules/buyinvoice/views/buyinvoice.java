package com.mohamedragab.cashpos.modules.buyinvoice.views;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.base.DateUtil;
import com.mohamedragab.cashpos.modules.buyinvoice.wedgits.invoiceAdapter;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.buy.models.invoice;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class buyinvoice extends AppCompatActivity {
    ListView invoice_lv;
    TextView search;
    List<invoice> invoiceList;
    DataBaseHelper db;
    com.mohamedragab.cashpos.modules.buyinvoice.wedgits.invoiceAdapter invoiceAdapter;
    private Calendar mcalendar;
    private int day, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyinvoice);

        search = (TextView) findViewById(R.id.search_date);
        search.setOnClickListener(v -> DateDialog());

        invoiceList = new ArrayList<>();
        db = new DataBaseHelper(this);


        invoiceAdapter = new invoiceAdapter(buyinvoice.this, invoiceList);

        invoice_lv = (ListView) findViewById(R.id.list_invoices);

        Cursor res = db.getbuyallinvoices();


        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                invoice invoice = new invoice();

                invoice.setId(res.getInt(0));
                invoice.setCustomer_name(res.getString(3));
                invoice.setDate(res.getString(2));
                invoice.setInvoice_id(res.getString(1));
                invoice.setNotpaid(res.getDouble(5));
                invoice.setTotal(res.getDouble(4));

                invoiceList.add(invoice);

            }
            Collections.sort(invoiceList, (arg0, arg1) -> {
                SimpleDateFormat format = new SimpleDateFormat(
                        "dd-MM-yyyy", Locale.US);
                int compareResult = 0;
                try {
                    Date arg0Date = format.parse(arg0.getDate());
                    Date arg1Date = format.parse(arg1.getDate());
                    compareResult = arg0Date.compareTo(arg1Date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    compareResult = arg0.getDate().compareTo(arg1.getDate());
                }
                return compareResult;
            });
            Collections.reverse(invoiceList);
            invoiceAdapter.setinvoiceAdapter(invoiceList);
            invoice_lv.setAdapter(invoiceAdapter);

        } else {
            Toast.makeText(getBaseContext(), "لا يوجد فواتير شراء حاليا !", Toast.LENGTH_SHORT).show();
            invoiceList.clear();
            invoiceAdapter.setinvoiceAdapter(invoiceList);
            invoice_lv.setAdapter(invoiceAdapter);

        }


    }

    public void DateDialog() {
        Locale.setDefault(Locale.US);

        mcalendar = Calendar.getInstance();
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);
        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener listener = (view, year, monthOfYear, dayOfMonth) -> {
            search.setText(DateUtil.getDateOnly(year, monthOfYear, dayOfMonth));
            MediaPlayer mp = MediaPlayer.create(buyinvoice.this, R.raw.finalsound);
            mp.start();
            Cursor res = db.getbuyinvoice(search.getText().toString());
            invoiceList.clear();
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    invoice invoice = new invoice();

                    invoice.setId(res.getInt(0));
                    invoice.setCustomer_name(res.getString(3));
                    invoice.setDate(res.getString(2));
                    invoice.setInvoice_id(res.getString(1));
                    invoice.setNotpaid(res.getDouble(5));
                    invoice.setTotal(res.getDouble(4));

                    invoiceList.add(invoice);

                }
                invoiceAdapter.setinvoiceAdapter(invoiceList);
                invoice_lv.setAdapter(invoiceAdapter);


            } else {
                Toast.makeText(getBaseContext(), "لا يوجد فواتير شراء حاليا !", Toast.LENGTH_SHORT).show();
                invoiceList.clear();
                invoiceAdapter.setinvoiceAdapter(invoiceList);
                invoice_lv.setAdapter(invoiceAdapter);

            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }

    public void go_home(View view) {
        onBackPressed();
    }


}
