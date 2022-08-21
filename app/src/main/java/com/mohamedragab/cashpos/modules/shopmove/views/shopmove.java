package com.mohamedragab.cashpos.modules.shopmove.views;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.DateUtil;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.graph.graph;
import com.mohamedragab.cashpos.modules.moneybox.models.money;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.sales.models.sellproduct;
import com.mohamedragab.cashpos.utils.Round;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class shopmove extends AppCompatActivity {
    TextView search, sales, buy, profit, sellproduct;
    private Calendar mcalendar;
    private int day, month, year;
    DataBaseHelper db;
    List<sellproduct> productsList;

    @Override
    protected void onStart() {
        super.onStart();
        db.close();
        db = new DataBaseHelper(getBaseContext());

    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopmove);

        productsList = new ArrayList<>();

        search = (TextView) findViewById(R.id.search_date);
        String date_type = getIntent().getStringExtra("date");
        if(date_type!=null){
            if (date_type.equals("0")) {
                search.setOnClickListener(v -> DateDialog());
            } else if (date_type.equals("1")) {
                search.setOnClickListener(v -> MonthDialog());
            }
        }

        db = new DataBaseHelper(getBaseContext());

        sales = (TextView) findViewById(R.id.sales);
        buy = (TextView) findViewById(R.id.buy);
        profit = (TextView) findViewById(R.id.profit);
        sellproduct = (TextView) findViewById(R.id.sellproduct);

    }

    public void DateDialog() {
        Locale.setDefault(Locale.US);

        mcalendar = Calendar.getInstance();
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);
        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener listener = (view, year, monthOfYear, dayOfMonth) -> {
            search.setText(DateUtil.getDateOnly(year, monthOfYear, dayOfMonth));
            MediaPlayer mp = MediaPlayer.create(shopmove.this, R.raw.finalsound);
            mp.start();
            int total = 0;

            Cursor res = db.getsellinvoice(search.getText().toString());
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {

                    total += res.getInt(4);

                }
                sales.setText(Round.round(total, 3) + SheredPrefranseHelper.getmoney_type(shopmove.this));

            } else {
                sales.setText("لا يوجد فواتير بيع !");
            }

            int salestotal = 0;
            Cursor res2 = db.getbuyinvoice(search.getText().toString());
            if (res2 != null && res2.getCount() > 0) {
                while (res2.moveToNext()) {

                    salestotal += res2.getInt(4);

                }
                buy.setText(Round.round(salestotal, 3) + SheredPrefranseHelper.getmoney_type(shopmove.this));

            } else {
                buy.setText("لا يوجد فواتير شراء !");
            }

            int profittotal = 0;
            Cursor res3 = db.getsellproductbydate(search.getText().toString());
            Cursor mon_res = db.getTransactions(search.getText().toString());


            if (res3 != null && res3.getCount() > 0) {
                while (res3.moveToNext()) {

                    Cursor invoice_res = db.getsellinvoicebyInvoiceId(res3.getString(1));
                    double discount = 0;
                    String discount_type = "";
                    double total1 = 0;
                    if (invoice_res != null && invoice_res.getCount() > 0) {
                        while (invoice_res.moveToNext()) {
                            discount_type = invoice_res.getString(9);
                            discount = invoice_res.getDouble(7);
                            total1 = invoice_res.getDouble(4);
                        }
                    }
                    double sellprice = res3.getDouble(6);
                    if (discount_type.equals("percentage")) {
                        sellprice = sellprice - sellprice * (discount / 100);
                    } else {
                        sellprice = sellprice - sellprice * (discount / (total1 + discount));
                    }
                    double buyprice = res3.getDouble(7);
                    double pro = (sellprice - buyprice) * res3.getDouble(2);

                    profittotal += pro;

                }
                double masrouf_total = 0;
                if (mon_res != null && mon_res.getCount() > 0) {
                    while (mon_res.moveToNext()) {
                        money money = new money();

                        money.setValue(mon_res.getDouble(1));
                        money.setTotalbefore(Double.parseDouble(mon_res.getString(2)));
                        money.setTotalAfter(mon_res.getDouble(3));
                        money.setDate(mon_res.getString(4));
                        money.setNotes(mon_res.getString(5));

                        if (mon_res.getString(5).contains("مصروف")) {
                            masrouf_total += mon_res.getDouble(1);
                        }

                    }
                }
                double profitfinal = profittotal - masrouf_total;
                profit.setText(Round.round(profitfinal, 3) + SheredPrefranseHelper.getmoney_type(shopmove.this));

            } else {
                profit.setText("لا يوجد ارباح !");
            }


        };
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }

    public void DateDialog(TextView v) {
        Locale.setDefault(Locale.US);

        mcalendar = Calendar.getInstance();
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);
        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener listener = (view, year, monthOfYear, dayOfMonth) -> {
            v.setText(DateUtil.getDateOnly(year, monthOfYear, dayOfMonth));
        };
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }

    public void go_home(View view) {
        onBackPressed();
        db.close();
    }

    public void go_statistics(View view) {
        startActivity(new Intent(this, graph.class));
        finish();
    }

    private void MonthDialog() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy", Locale.US);
        Calendar cal1 = Calendar.getInstance();
        Date dt = null;
        try {
            dt = df.parse(c + "");
            cal1.setTime(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int currentDaycmp = cal1.get(Calendar.DAY_OF_MONTH);
        int currentMonthcmp = cal1.get(Calendar.MONTH);
        int currentYearcmp = cal1.get(Calendar.YEAR);

        DatePickerDialog monthDatePickerDialog = new DatePickerDialog(shopmove.this,
                AlertDialog.THEME_HOLO_LIGHT, (view, year, month, dayOfMonth) -> search.setText((month + 1) + "-" + year), currentYearcmp, currentMonthcmp, currentDaycmp) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
            }
        };
        monthDatePickerDialog.setTitle("اختيار شهر محدد");
        monthDatePickerDialog.show();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int total = 0;

                Cursor res = db.getsellinvoice(search.getText().toString());
                if (res != null && res.getCount() > 0) {
                    while (res.moveToNext()) {

                        total += res.getDouble(4);

                    }
                    sales.setText(Round.round(total, 3) + SheredPrefranseHelper.getmoney_type(shopmove.this));

                } else {
                    sales.setText("لا يوجد فواتير بيع !");
                }

                int salestotal = 0;
                Cursor res2 = db.getbuyinvoice(search.getText().toString());
                if (res2 != null && res2.getCount() > 0) {
                    while (res2.moveToNext()) {

                        salestotal += res2.getDouble(4);

                    }
                    buy.setText(Round.round(salestotal, 3) + SheredPrefranseHelper.getmoney_type(shopmove.this));

                } else {
                    buy.setText("لا يوجد فواتير شراء !");
                }

                double profittotal = 0;
                Cursor res3 = db.getsellproductbydate(search.getText().toString());
                Cursor mon_res = db.getTransactions(search.getText().toString());
                if (res3 != null && res3.getCount() > 0) {
                    while (res3.moveToNext()) {
                        Cursor invoice_res = db.getsellinvoicebyInvoiceId(res3.getString(1));
                        double discount = 0;
                        String discount_type = "";
                        double total1 = 0;
                        if (invoice_res != null && invoice_res.getCount() > 0) {
                            while (invoice_res.moveToNext()) {
                                discount_type = invoice_res.getString(9);
                                discount = invoice_res.getDouble(7);
                                total1 = invoice_res.getDouble(4);
                            }
                        }
                        double sellprice = res3.getDouble(6);
                        if (discount_type.equals("percentage")) {
                            sellprice = sellprice - sellprice * (discount / 100);
                        } else {
                            sellprice = sellprice - sellprice * (discount / (total1 + discount));
                        }
                        double buyprice = res3.getDouble(7);
                        double pro = (sellprice - buyprice) * res3.getDouble(2);

                        profittotal += pro;

                    }
                    double masrouf_total = 0;
                    if (mon_res != null && mon_res.getCount() > 0) {
                        while (mon_res.moveToNext()) {
                            money money = new money();

                            money.setValue(mon_res.getDouble(1));
                            money.setTotalbefore(Double.parseDouble(mon_res.getString(2)));
                            money.setTotalAfter(mon_res.getDouble(3));
                            money.setDate(mon_res.getString(4));
                            money.setNotes(mon_res.getString(5));

                            if (mon_res.getString(5).contains("مصروف")) {
                                masrouf_total += mon_res.getDouble(1);
                            }

                        }
                    }
                    double profitfinal = profittotal - masrouf_total;
                    profit.setText(Round.round(profitfinal, 3) + SheredPrefranseHelper.getmoney_type(shopmove.this));

                } else {
                    profit.setText("لا يوجد ارباح !");
                }


            }
        });
    }

    public void range(View view) {

        final Dialog after_dialog = new Dialog(shopmove.this);
        after_dialog.setContentView(R.layout.fromtodialog);
        after_dialog.setCancelable(false);

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String formattedDate = df.format(c);


        TextView date1 = (TextView) after_dialog.findViewById(R.id.date1);
        date1.setText(formattedDate);
        date1.setOnClickListener(view1 -> {
            DateDialog(date1);
        });
        TextView date2 = (TextView) after_dialog.findViewById(R.id.date2);
        date2.setText(formattedDate);
        date2.setOnClickListener(view1 -> {
            DateDialog(date2);

        });
        Button ok = (Button) after_dialog.findViewById(R.id.save);
        ok.setOnClickListener(view12 -> {

            String d1 = date1.getText().toString();
            String d2 = date2.getText().toString();

            int total = 0;

            Cursor res = db.getallsellinvoices();
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    String d = res.getString(2);
                    try {
                        Date date_1 = df.parse(d1);
                        Date date_2 = df.parse(d2);
                        Date date = df.parse(d);
                        if ((date.after(date_1) && date.before(date_2))|| d.equals(d1) || d.equals(d2)) {
                            total += res.getDouble(4);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                sales.setText(Round.round(total, 3) + SheredPrefranseHelper.getmoney_type(shopmove.this));

            } else {
                sales.setText("لا يوجد فواتير بيع !");
            }

            int salestotal = 0;
            Cursor res2 = db.getbuyallinvoices();
            if (res2 != null && res2.getCount() > 0) {
                while (res2.moveToNext()) {
                    String d = res2.getString(2);

                    try {
                        Date date_1 = df.parse(d1);
                        Date date_2 = df.parse(d2);
                        Date date = df.parse(d);
                        if ((date.after(date_1) && date.before(date_2))|| d.equals(d1) || d.equals(d2)) {
                            salestotal += res2.getInt(4);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
                buy.setText(Round.round(salestotal, 3) + SheredPrefranseHelper.getmoney_type(shopmove.this));

            } else {
                buy.setText("لا يوجد فواتير شراء !");
            }

            int profittotal = 0;
            Cursor res3 = db.getallsellproducts();
            Cursor mon_res = db.getallTransactions();


            if (res3 != null && res3.getCount() > 0) {
                while (res3.moveToNext()) {

                    String d = res3.getString(8);
                    try {
                        Date date_1 = df.parse(d1);
                        Date date_2 = df.parse(d2);
                        Date date = df.parse(d);
                        if ((date.after(date_1) && date.before(date_2))|| d.equals(d1) || d.equals(d2)) {
                            Cursor invoice_res = db.getsellinvoicebyInvoiceId(res3.getString(1));
                            double discount = 0;
                            String discount_type = "";
                            double total1 = 0;
                            if (invoice_res != null && invoice_res.getCount() > 0) {
                                while (invoice_res.moveToNext()) {
                                    discount_type = invoice_res.getString(9);
                                    discount = invoice_res.getDouble(7);
                                    total1 = invoice_res.getDouble(4);
                                }
                            }
                            double sellprice = res3.getDouble(6);
                            if (discount_type.equals("percentage")) {
                                sellprice = sellprice - sellprice * (discount / 100);
                            } else {
                                sellprice = sellprice - sellprice * (discount / (total1 + discount));
                            }
                            double buyprice = res3.getDouble(7);
                            double pro = (sellprice - buyprice) * res3.getDouble(2);

                            profittotal += pro;

                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }


                double masrouf_total = 0;
                if (mon_res != null && mon_res.getCount() > 0) {
                    while (mon_res.moveToNext()) {
                        String d = mon_res.getString(4);
                        try {
                            Date date_1 = df.parse(d1);
                            Date date_2 = df.parse(d2);
                            Date date = df.parse(d);
                            if ((date.after(date_1) && date.before(date_2))|| d.equals(d1) || d.equals(d2)) {

                                money money = new money();

                                money.setValue(mon_res.getDouble(1));
                                money.setTotalbefore(Double.parseDouble(mon_res.getString(2)));
                                money.setTotalAfter(mon_res.getDouble(3));
                                money.setDate(mon_res.getString(4));
                                money.setNotes(mon_res.getString(5));

                                if (mon_res.getString(5).contains("مصروف")) {
                                    masrouf_total += mon_res.getDouble(1);
                                }

                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                    double profitfinal = profittotal - masrouf_total;
                    profit.setText(Round.round(profitfinal, 3) + SheredPrefranseHelper.getmoney_type(shopmove.this));

                }
            } else {
                profit.setText("لا يوجد ارباح !");
            }
            after_dialog.dismiss();


        });
        TextView cancel = (TextView) after_dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(view13 -> after_dialog.dismiss());
after_dialog.show();

    }


}

