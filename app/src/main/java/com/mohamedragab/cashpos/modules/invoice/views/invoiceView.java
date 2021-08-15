package com.mohamedragab.cashpos.modules.invoice.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.base.DateUtil;
import com.mohamedragab.cashpos.modules.invoice.wedgits.invoiceAdapter;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.sales.models.invoice;
import com.mohamedragab.cashpos.utils.Round;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class invoiceView extends AppCompatActivity {

    ListView invoice_lv;
    TextView search, search2;
    List<invoice> invoiceList;
    DataBaseHelper db;
    com.mohamedragab.cashpos.modules.invoice.wedgits.invoiceAdapter invoiceAdapter;
    private Calendar mcalendar;
    private int day, month, year;
    TextView cashier_name;
    EditText invoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);


        invoiceList = new ArrayList<>();
        db = new DataBaseHelper(getBaseContext());
        cashier_name = (TextView) findViewById(R.id.cashier_name);
        invoice=(EditText)findViewById(R.id.invoice);
        invoice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (invoice.getText().toString().trim().equals("")) {
                    invoiceList.clear();
                    invoiceAdapter.setinvoiceAdapter(invoiceList);
                    invoice_lv.setAdapter(invoiceAdapter);
                }else{
                    String invoice_id=invoice.getText().toString().trim();
                    Cursor res = db.getsellinvoicebyInvoiceId(invoice_id);
                    if (res != null && res.getCount() > 0) {
                        while (res.moveToNext()) {
                            invoice invoice = new invoice();

                            invoice.setId(res.getInt(0));
                            invoice.setCustomer_name(res.getString(3));
                            invoice.setDate(res.getString(2));
                            invoice.setInvoice_id(res.getString(1));
                            invoice.setNotpaid(res.getDouble(5));
                            invoice.setTotal(res.getDouble(4));
                            invoice.setCashier(res.getString(6));
                            invoice.setDiscount(res.getDouble(7));
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
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        invoiceAdapter = new invoiceAdapter(invoiceView.this, invoiceList);

        invoice_lv = (ListView) findViewById(R.id.list_invoices);

        Cursor res = db.getallsellinvoices();


        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                invoice invoice = new invoice();

                invoice.setId(res.getInt(0));
                invoice.setCustomer_name(res.getString(3));
                invoice.setDate(res.getString(2));
                invoice.setInvoice_id(res.getString(1));
                invoice.setNotpaid(res.getDouble(5));
                invoice.setTotal(res.getDouble(4));
                invoice.setCashier(res.getString(6));
                invoice.setDiscount(res.getDouble(7));
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

    TextView e;
    int num;

    public void DateDialog(TextView e, int num) {
        Locale.setDefault(Locale.US);
        this.e = e;
        this.num = num;
        mcalendar = Calendar.getInstance();
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);
        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener listener = (view, year, monthOfYear, dayOfMonth) -> {
            search.setText(DateUtil.getDateOnly(year, monthOfYear, dayOfMonth));
            MediaPlayer mp = MediaPlayer.create(invoiceView.this, R.raw.finalsound);
            mp.start();
        };
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }


    public void go_home(View view) {
        onBackPressed();
        db.close();
    }

    String search_1 = "", search_2 = "";

    public void go_filter(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.filter_invoices);
        dialog.setCancelable(false);
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        Spinner cashier = (Spinner) dialog.findViewById(R.id.cashierspinner);
        final Cursor res2 = db.getallcashier();
        String cash_names[] = new String[0];
        if (res2 != null && res2.getCount() > 0) {
            cash_names = new String[res2.getCount() + 1];

            int index = 1;
            cash_names[0] = "اضغط لتحديد كاشير";
            while (res2.moveToNext()) {
                cash_names[index] = res2.getString(1);
                index++;
            }

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, cash_names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cashier.setAdapter(adapter);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String currentdate = df.format(c);


        search = (TextView) dialog.findViewById(R.id.search_date);
        search.setOnClickListener(v -> DateDialog(search, 1));
        search2 = (TextView) dialog.findViewById(R.id.search_date2);
        search2.setOnClickListener(v -> DateDialog(search2, 2));
        search.setText(currentdate);
        search2.setText(currentdate);
        search_1 = currentdate;
        search_2 = currentdate;

        Button ok = (Button) dialog.findViewById(R.id.ok);
        ok.setOnClickListener(v -> {
            Cursor res = db.getallsellinvoices();
            invoiceList.clear();
            invoice.setText("");
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    invoice invoice = new invoice();

                    invoice.setId(res.getInt(0));
                    invoice.setCustomer_name(res.getString(3));
                    invoice.setDate(res.getString(2));
                    invoice.setInvoice_id(res.getString(1));
                    invoice.setNotpaid(res.getDouble(5));
                    invoice.setTotal(res.getDouble(4));
                    invoice.setCashier(res.getString(6));
                    invoice.setDiscount(res.getDouble(7));
                    invoice.setDiscount_kind(res.getString(9));

                    String d1 = search.getText().toString();
                    String d2 = search2.getText().toString();
                    String d = res.getString(2);

                    if (res.getString(6) != null && cashier.getSelectedItem() != null) {
                       if ((res.getString(6).equals(cashier.getSelectedItem().toString()) && cashier.getSelectedItemPosition() != 0)) {
                            try {
                                Date date1 = df.parse(d1);
                                Date date2 = df.parse(d2);
                                Date date = df.parse(d);
                                if (date.after(date1) && date.before(date2)) {
                                    invoiceList.add(invoice);
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                       if (cashier.getSelectedItemPosition() == 0){
                           try {
                               Date date1 = df.parse(d1);
                               Date date2 = df.parse(d2);
                               Date date = df.parse(d);
                               if (date.after(date1) && date.before(date2)) {
                                   invoiceList.add(invoice);
                               }

                           } catch (ParseException e) {
                               e.printStackTrace();
                           }
                       }
                    } else {
                        try {
                            Date date1 = df.parse(d1);
                            Date date2 = df.parse(d2);
                            Date date = df.parse(d);
                            if (date.after(date1) && date.before(date2)) {
                                invoiceList.add(invoice);
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }


                }
                invoiceAdapter.setinvoiceAdapter(invoiceList);
                invoice_lv.setAdapter(invoiceAdapter);


            } else {
                Toast.makeText(getBaseContext(), "لا يوجد فواتير بيع حاليا !", Toast.LENGTH_SHORT).show();
                invoiceList.clear();
                invoiceAdapter.setinvoiceAdapter(invoiceList);
                invoice_lv.setAdapter(invoiceAdapter);

            }
            dialog.dismiss();
        });

        dialog.show();
    }

    public void gettotal(View view) {
        double total = 0.0;
        for (int i = 0; i < invoiceList.size(); i++) {
            total += invoiceList.get(i).getTotal();
        }
        Toast.makeText(getBaseContext(), Round.round(total, 3) + "" + SheredPrefranseHelper.getmoney_type(invoiceView.this), Toast.LENGTH_SHORT).show();
    }
}
