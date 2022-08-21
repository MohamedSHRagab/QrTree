package com.mohamedragab.cashpos.modules.info;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.modules.buyinvoice.views.buyinvoice;
import com.mohamedragab.cashpos.modules.invoice.views.invoiceView;
import com.mohamedragab.cashpos.modules.masroufreport.views.masroufreport;
import com.mohamedragab.cashpos.modules.mored.views.moredmoney;
import com.mohamedragab.cashpos.modules.omla.views.omlamoney;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.shopmove.views.shopmove;
import com.mohamedragab.cashpos.modules.store.models.product;
import com.mohamedragab.cashpos.modules.store.views.viewproducts;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.graph.graph;
import com.mohamedragab.cashpos.modules.latekist.views.latekists;
import com.mohamedragab.cashpos.modules.moneyboxreport.views.moneyboxreport;
import com.mohamedragab.cashpos.modules.repair.views.laterepairs;
import com.mohamedragab.cashpos.utils.Round;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class info extends AppCompatActivity {

    TextView currentDate;
    private Calendar mcalendar;
    private int day, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        currentDate = (TextView) findViewById(R.id.currentdate);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String formattedDate = df.format(c);
        currentDate.setText(formattedDate + "");
    }

    public void go_home(View view) {
        onBackPressed();
    }

    public void go_showglobal(View view) {
    }

    public void go_shopmove(View view) {
        Intent intent = new Intent(this, shopmove.class);
        intent.putExtra("date", "0");
        startActivity(intent);
    }

    public void go_sales(View view) {
        startActivity(new Intent(this, invoiceView.class));
    }

    public void go_buy(View view) {
        startActivity(new Intent(this, buyinvoice.class));
    }

    public void go_money_box(View view) {
        startActivity(new Intent(this, moneyboxreport.class));
    }

    public void go_graph(View view) {
        startActivity(new Intent(this, graph.class));
    }

    public void go_customer_money(View view) {
        startActivity(new Intent(this, omlamoney.class));
    }

    public void go_mored_money(View view) {
        startActivity(new Intent(this, moredmoney.class));
    }

    public void go_shopmove2(View view) {
        Intent intent = new Intent(this, shopmove.class);
        intent.putExtra("date", "1");
        startActivity(intent);
    }

    public void go_masrouf0(View view) {
        Intent intent = new Intent(this, masroufreport.class);
        intent.putExtra("date", "0");
        startActivity(intent);
    }

    public void go_masrouf2(View view) {
        Intent intent = new Intent(this, masroufreport.class);
        intent.putExtra("date", "1");
        startActivity(intent);
    }

    public void go_total_buy(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.total_money_dialog);
        dialog.setCancelable(false);

        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(view1 -> dialog.dismiss());
        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setText("اجمالي رأس المال بسعر البيع");
        TextView total_money = (TextView) dialog.findViewById(R.id.total_money);

        DataBaseHelper db = new DataBaseHelper(getBaseContext());
        Cursor res = db.getallproducts();
        double total = 0;
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
                pro.setImage(imgByte);

                total += res.getDouble(4) * res.getDouble(6);

            }
            total_money.setText(Round.round(total, 3) + " " + SheredPrefranseHelper.getmoney_type(this));
        }
        dialog.show();
        db.close();


    }

    public void go_total_sell(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.total_money_dialog);
        dialog.setCancelable(false);

        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(view1 -> dialog.dismiss());
        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setText("اجمالي رأس المال بسعر الشراء");
        TextView total_money = (TextView) dialog.findViewById(R.id.total_money);

        DataBaseHelper db = new DataBaseHelper(getBaseContext());
        Cursor res = db.getallproducts();
        double total = 0;
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
                pro.setImage(imgByte);

                total += res.getDouble(5) * res.getInt(6);

            }
            total_money.setText(Round.round(total, 3) + " " + SheredPrefranseHelper.getmoney_type(this));
        }
        dialog.show();
        db.close();
    }

    public void go_shop_products(View view) {
        startActivity(new Intent(this, viewproducts.class));

    }

    public void go_late_kists(View view) {
        startActivity(new Intent(this, latekists.class));

    }

    public void go_late_repairs(View view) {
        startActivity(new Intent(this, laterepairs.class));

    }

    public void go_total_buy2(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.total_money_dialog);
        dialog.setCancelable(false);

        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(view1 -> dialog.dismiss());
        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setText("اجمالي رأس المال بسعر البيع 2");
        TextView total_money = (TextView) dialog.findViewById(R.id.total_money);

        DataBaseHelper db = new DataBaseHelper(getBaseContext());
        Cursor res = db.getallproducts();
        double total = 0;
        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                total += res.getDouble(13) * res.getDouble(6);
            }
            total_money.setText(Round.round(total, 3) + " " + SheredPrefranseHelper.getmoney_type(this));
        }
        dialog.show();
        db.close();

    }

    public void go_total_buy3(View view) {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.total_money_dialog);
        dialog.setCancelable(false);

        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(view1 -> dialog.dismiss());
        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setText("اجمالي رأس المال بسعر البيع 3");
        TextView total_money = (TextView) dialog.findViewById(R.id.total_money);

        DataBaseHelper db = new DataBaseHelper(getBaseContext());
        Cursor res = db.getallproducts();
        double total = 0;
        if (res != null && res.getCount() > 0) {

            while (res.moveToNext()) {
                total += res.getDouble(14) * res.getDouble(6);
            }
            total_money.setText(Round.round(total, 3) + " " + SheredPrefranseHelper.getmoney_type(this));
        }
        dialog.show();
        db.close();


    }
}
