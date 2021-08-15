package com.mohamedragab.cashpos.modules.invoicesalesback.views;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.base.DateUtil;
import com.mohamedragab.cashpos.modules.invoicesalesback.wedgits.invoicesaleAdapter;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.home.MainActivity;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.sales.models.invoice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class invoicesaleView extends AppCompatActivity {
    ListView invoice_lv;
    TextView search;
    List<invoice> invoiceList;
    DataBaseHelper invoicedb;
    invoicesaleAdapter invoiceAdapter;
    private Calendar mcalendar;
    private int day, month, year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoicesale);
        search = (TextView) findViewById(R.id.search_date);
        search.setOnClickListener(v -> DateDialog());

        invoiceList = new ArrayList<>();
        invoicedb = new DataBaseHelper(getBaseContext());


        invoiceAdapter = new invoicesaleAdapter(invoicesaleView.this, invoiceList);

        invoice_lv = (ListView) findViewById(R.id.list_invoices);

        Cursor res = invoicedb.getallsellinvoices();


        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                invoice invoice = new invoice();

                invoice.setId(res.getInt(0));
                invoice.setCustomer_name(res.getString(3));
                invoice.setDate(res.getString(2));
                invoice.setInvoice_id(res.getString(1));
                invoice.setNotpaid(res.getDouble(5));
                invoice.setTotal(res.getDouble(4));
                invoice.setDiscount(res.getDouble(7));
                invoice.setKind(res.getString(8));
                invoice.setDiscount_kind(res.getString(9));

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
            Toast.makeText(getBaseContext(), "لا يوجد فواتير بيع حاليا !", Toast.LENGTH_SHORT).show();
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
            MediaPlayer mp = MediaPlayer.create(getBaseContext(), R.raw.finalsound);
            mp.start();
            Cursor res = invoicedb.getsellinvoice(search.getText().toString());
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
                    invoice.setDiscount(res.getDouble(7));
                    invoice.setKind(res.getString(8));
                    invoice.setDiscount_kind(res.getString(9));

                    invoiceList.add(invoice);

                }
                invoiceAdapter.setinvoiceAdapter(invoiceList);
                invoice_lv.setAdapter(invoiceAdapter);


            } else {
                Toast.makeText(getBaseContext(), "لا يوجد فواتير بيع حاليا !", Toast.LENGTH_SHORT).show();
                invoiceList.clear();
                invoiceAdapter.setinvoiceAdapter(invoiceList);
                invoice_lv.setAdapter(invoiceAdapter);

            }
        };
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }

    public void go_home(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
