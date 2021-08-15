package com.mohamedragab.cashpos.modules.settings.views;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.settings.models.screens;


public class settings extends AppCompatActivity {

    Context context;
    Spinner spinner;
    String[] money_types = {" جنيه ", " ريال ", " درهم ", " دينار ", " ليره "};
    CheckBox delivery;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = this;
        spinner = (Spinner) findViewById(R.id.spinner);
        delivery = (CheckBox) findViewById(R.id.delivery);


        if (SheredPrefranseHelper.getdelivery(this) != null) {
            if (SheredPrefranseHelper.getdelivery(this).equals("true")) {
                delivery.setChecked(true);
            } else {
                delivery.setChecked(false);
            }
        }
        delivery.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (delivery.isChecked()) {
                SheredPrefranseHelper.adddelivery(this, "true");
            } else {
                SheredPrefranseHelper.adddelivery(this, "false");
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, money_types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SheredPrefranseHelper.addmoney_type(settings.this, spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (SheredPrefranseHelper.getmoney_type(settings.this) != null) {
            if (SheredPrefranseHelper.getmoney_type(settings.this).equals(" جنيه ")) {
                spinner.setSelection(0);
            } else if (SheredPrefranseHelper.getmoney_type(settings.this).equals(" ريال ")) {
                spinner.setSelection(1);
            } else if (SheredPrefranseHelper.getmoney_type(settings.this).equals(" درهم ")) {
                spinner.setSelection(2);
            } else if (SheredPrefranseHelper.getmoney_type(settings.this).equals(" دينار ")) {
                spinner.setSelection(3);
            } else if (SheredPrefranseHelper.getmoney_type(settings.this).equals(" ليره ")) {
                spinner.setSelection(4);
            } else {
                spinner.setSelection(0);
            }
        }


    }

    public void go_home(View view) {
        onBackPressed();
    }

    public void go_shop_info(View view) {
        startActivity(new Intent(this, shopinfo.class));
    }


    public void createpassword(View view) {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.newpassworddialog);
        dialog.setCancelable(false);

        TextInputEditText password = (TextInputEditText) dialog.findViewById(R.id.password);
        TextInputEditText password2 = (TextInputEditText) dialog.findViewById(R.id.password2);
        Button ok = (Button) dialog.findViewById(R.id.ok);
        ok.setOnClickListener(v -> {
            if (password.getText().toString().trim().equals("") || password2.getText().toString().trim().equals("")) {
                Toast.makeText(getBaseContext(), "برجاء اكمال كافه البيانات الفارغه !", Toast.LENGTH_SHORT).show();
            } else {
                if (!password.getText().toString().trim().equals(password2.getText().toString().trim())) {
                    Toast.makeText(getBaseContext(), "تأكيد الباسورد غير صحيح ..", Toast.LENGTH_SHORT).show();
                } else {
                    SheredPrefranseHelper.addpassword(settings.this, password.getText().toString().trim());
                    Toast.makeText(getBaseContext(), "تم انشاء الباسورد بنجاح .", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    public void delete_password(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(settings.this);
        dialog.setCancelable(false);
        dialog.setTitle("حذف كلمة المرور !");
        dialog.setMessage("هل تريد حذف كلمة المرور !");
        dialog.setPositiveButton("حذف", (dialog12, id) -> {
            SheredPrefranseHelper.addpassword(settings.this, null);
            Toast.makeText(getBaseContext(), "تم حذف كلمة المرور بنجاح .", Toast.LENGTH_SHORT).show();
        })
                .setNegativeButton("الغاء ", (dialog1, which) -> {

                });

        final AlertDialog alert = dialog.create();
        alert.show();

    }

    public void change_screens(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.password_screens_dialog);
        dialog.setCancelable(false);

        CheckBox pass1 = (CheckBox) dialog.findViewById(R.id.pass1);
        CheckBox pass2 = (CheckBox) dialog.findViewById(R.id.pass2);
        CheckBox pass3 = (CheckBox) dialog.findViewById(R.id.pass3);
        CheckBox pass4 = (CheckBox) dialog.findViewById(R.id.pass4);
        CheckBox pass5 = (CheckBox) dialog.findViewById(R.id.pass5);
        CheckBox pass6 = (CheckBox) dialog.findViewById(R.id.pass6);
        CheckBox pass7 = (CheckBox) dialog.findViewById(R.id.pass7);
        CheckBox pass8 = (CheckBox) dialog.findViewById(R.id.pass8);
        CheckBox pass9 = (CheckBox) dialog.findViewById(R.id.pass9);
        CheckBox pass10 = (CheckBox) dialog.findViewById(R.id.pass10);
        CheckBox pass11 = (CheckBox) dialog.findViewById(R.id.pass11);
        CheckBox pass12 = (CheckBox) dialog.findViewById(R.id.pass12);
        CheckBox pass13 = (CheckBox) dialog.findViewById(R.id.pass13);
        CheckBox pass14 = (CheckBox) dialog.findViewById(R.id.pass14);
        CheckBox pass15 = (CheckBox) dialog.findViewById(R.id.pass15);
        CheckBox pass16 = (CheckBox) dialog.findViewById(R.id.pass16);
        if (SheredPrefranseHelper.getpasswordscreens(this) != null) {
            pass1.setChecked(SheredPrefranseHelper.getpasswordscreens(this).getPass1());
            pass2.setChecked(SheredPrefranseHelper.getpasswordscreens(this).getPass2());
            pass3.setChecked(SheredPrefranseHelper.getpasswordscreens(this).getPass3());
            pass4.setChecked(SheredPrefranseHelper.getpasswordscreens(this).getPass4());
            pass5.setChecked(SheredPrefranseHelper.getpasswordscreens(this).getPass5());
            pass6.setChecked(SheredPrefranseHelper.getpasswordscreens(this).getPass6());
            pass7.setChecked(SheredPrefranseHelper.getpasswordscreens(this).getPass7());
            pass8.setChecked(SheredPrefranseHelper.getpasswordscreens(this).getPass8());
            pass9.setChecked(SheredPrefranseHelper.getpasswordscreens(this).getPass9());
            pass10.setChecked(SheredPrefranseHelper.getpasswordscreens(this).getPass10());
            pass11.setChecked(SheredPrefranseHelper.getpasswordscreens(this).getPass11());
            pass12.setChecked(SheredPrefranseHelper.getpasswordscreens(this).getPass12());
            pass13.setChecked(SheredPrefranseHelper.getpasswordscreens(this).getPass13());
            pass14.setChecked(SheredPrefranseHelper.getpasswordscreens(this).getPass14());
            pass15.setChecked(SheredPrefranseHelper.getpasswordscreens(this).getPass15());
            pass16.setChecked(SheredPrefranseHelper.getpasswordscreens(this).getPass16());
        }

        Button ok = (Button) dialog.findViewById(R.id.ok);
        ok.setOnClickListener(v -> {
            screens screen = new screens();
            screen.setPass1(pass1.isChecked());
            screen.setPass2(pass2.isChecked());
            screen.setPass3(pass3.isChecked());
            screen.setPass4(pass4.isChecked());
            screen.setPass5(pass5.isChecked());
            screen.setPass6(pass6.isChecked());
            screen.setPass7(pass7.isChecked());
            screen.setPass8(pass8.isChecked());
            screen.setPass9(pass9.isChecked());
            screen.setPass10(pass10.isChecked());
            screen.setPass11(pass11.isChecked());
            screen.setPass12(pass12.isChecked());
            screen.setPass13(pass13.isChecked());
            screen.setPass14(pass14.isChecked());
            screen.setPass15(pass15.isChecked());
            screen.setPass16(pass16.isChecked());
            SheredPrefranseHelper.addpasswordscreens(this, screen);
            Toast.makeText(getBaseContext(), "تم تعديل الشاشات المراد قفلها .", Toast.LENGTH_SHORT).show();
            dialog.dismiss();

        });
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    public void go_shop_delete(View view) {
        startActivity(new Intent(this, cleardata.class));
    }

    public void change_color(View view) {

    }
}
