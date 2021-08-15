package com.mohamedragab.cashpos.modules.settings.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;

public class cleardata extends AppCompatActivity {
    DataBaseHelper db;
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
        setContentView(R.layout.activity_cleardata);

        db = new DataBaseHelper(getBaseContext());

    }

    public void go_home(View view) {
        onBackPressed();
        db.close();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void delete_sell_invoices(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(cleardata.this);
        dialog.setCancelable(false);
        dialog.setTitle("حذف فواتير البيع ");
        dialog.setMessage("هل تريد حذف جميع فواتير البيع من البرنامج ؟  !");
        dialog.setPositiveButton("حذف", (dialog12, id) -> {
            db.deleteallsellproducts();
            db.deleteallsellInvoices();
            Toast.makeText(getBaseContext(), "تم حذف البيانات", Toast.LENGTH_SHORT).show();

        })
                .setNegativeButton("الغاء ", (dialog1, which) -> {
                });

        final AlertDialog alert = dialog.create();
        alert.show();

    }

    public void delete_mored(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(cleardata.this);
        dialog.setCancelable(false);
        dialog.setTitle("حذف الموردين ");
        dialog.setMessage("هل تريد حذف جميع الموردين من البرنامج ؟  !");
        dialog.setPositiveButton("حذف", (dialog12, id) -> {
            db.deleteallmoreds();
            Toast.makeText(getBaseContext(), "تم حذف البيانات", Toast.LENGTH_SHORT).show();

        })
                .setNegativeButton("الغاء ", (dialog1, which) -> {
                });

        final AlertDialog alert = dialog.create();
        alert.show();

    }

    public void delete_money_box(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(cleardata.this);
        dialog.setCancelable(false);
        dialog.setTitle("حذف بيانات الخزينه ");
        dialog.setMessage("هل تريد حذف جميع بيانات الخزينه من البرنامج ؟  !");
        dialog.setPositiveButton("حذف", (dialog12, id) -> {
            db.deleteallmoney();
            Toast.makeText(getBaseContext(), "تم حذف البيانات", Toast.LENGTH_SHORT).show();

        })
                .setNegativeButton("الغاء ", (dialog1, which) -> {
                });

        final AlertDialog alert = dialog.create();
        alert.show();

    }

    public void delete_buy_invoices(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(cleardata.this);
        dialog.setCancelable(false);
        dialog.setTitle("حذف فواتير الشراء ");
        dialog.setMessage("هل تريد حذف جميع فواتير الشراء من البرنامج ؟  !");
        dialog.setPositiveButton("حذف", (dialog12, id) -> {
            db.deleteallbuyinvoices();
            db.deleteallbuyproducts();
            Toast.makeText(getBaseContext(), "تم حذف البيانات", Toast.LENGTH_SHORT).show();
        })
                .setNegativeButton("الغاء ", (dialog1, which) -> {
                });

        final AlertDialog alert = dialog.create();
        alert.show();

    }

    public void delete_products(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(cleardata.this);
        dialog.setCancelable(false);
        dialog.setTitle("حذف المنتجات ");
        dialog.setMessage("هل تريد حذف جميع منتجات المخزن من البرنامج ؟  !");
        dialog.setPositiveButton("حذف", (dialog12, id) -> {
            db.deleteallproducts();
            Toast.makeText(getBaseContext(), "تم حذف البيانات", Toast.LENGTH_SHORT).show();

        })
                .setNegativeButton("الغاء ", (dialog1, which) -> {
                });

        final AlertDialog alert = dialog.create();
        alert.show();
    }

    public void delete_kists(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(cleardata.this);
        dialog.setCancelable(false);
        dialog.setTitle("حذف الاقساط ");
        dialog.setMessage("هل تريد حذف جميع الاقساط من البرنامج ؟  !");
        dialog.setPositiveButton("حذف", (dialog12, id) -> {
            db.deleteallKest();
            Toast.makeText(getBaseContext(), "تم حذف البيانات", Toast.LENGTH_SHORT).show();


        })
                .setNegativeButton("الغاء ", (dialog1, which) -> {
                });

        final AlertDialog alert = dialog.create();
        alert.show();
    }

    public void delete_customers(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(cleardata.this);
        dialog.setCancelable(false);
        dialog.setTitle("حذف العملاء ");
        dialog.setMessage("هل تريد حذف جميع العملاء من البرنامج ؟  !");
        dialog.setPositiveButton("حذف", (dialog12, id) -> {
            db.deleteallomla();
            db.deleteallomlatrans();
            Toast.makeText(getBaseContext(), "تم حذف البيانات", Toast.LENGTH_SHORT).show();

        })
                .setNegativeButton("الغاء ", (dialog1, which) -> {
                });

        final AlertDialog alert = dialog.create();
        alert.show();

    }

    public void delete_mords(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(cleardata.this);
        dialog.setCancelable(false);
        dialog.setTitle("حذف الموردين ");
        dialog.setMessage("هل تريد حذف جميع الموردين من البرنامج ؟  !");
        dialog.setPositiveButton("حذف", (dialog12, id) -> {
            db.deleteallmoreds();
            db.deleteallmoredtrans();
            Toast.makeText(getBaseContext(), "تم حذف البيانات", Toast.LENGTH_SHORT).show();
        })
                .setNegativeButton("الغاء ", (dialog1, which) -> {
                });

        final AlertDialog alert = dialog.create();
        alert.show();

    }
}
