package com.mohamedragab.cashpos.modules.employees.views;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.mohamedragab.cashpos.modules.employees.models.Cashier;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.home.MainActivity;
import com.mohamedragab.cashpos.modules.login.views.login;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;

public class employee_login extends AppCompatActivity {
    EditText username;
    TextInputEditText pass;
    DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_login);

        username = (EditText) findViewById(R.id.username);
        pass = (TextInputEditText) findViewById(R.id.password);
        db = new DataBaseHelper(getBaseContext());

    }

    public void login(View view) {

        Cursor res = db.getcashierbyusert_pass(username.getText().toString().trim(), pass.getText().toString().trim());
        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                Cashier cashier = new Cashier();
                cashier.setId(res.getInt(0));
                cashier.setName(res.getString(1));
                cashier.setPhone(res.getString(3));
                cashier.setAddress(res.getString(2));
                byte[] imgByte = res.getBlob(5);
                cashier.setImage(imgByte);
                cashier.setNotes(res.getString(4));
                cashier.setUserName(res.getString(7));
                cashier.setPassword(res.getString(8));
                cashier.setP1(res.getString(9));
                cashier.setP2(res.getString(10));
                cashier.setP3(res.getString(11));
                cashier.setP4(res.getString(12));
                cashier.setP5(res.getString(13));
                cashier.setP6(res.getString(14));
                cashier.setP7(res.getString(15));
                cashier.setP8(res.getString(16));
                cashier.setP9(res.getString(17));
                cashier.setP10(res.getString(18));
                cashier.setP11(res.getString(19));
                cashier.setP12(res.getString(20));
                cashier.setP13(res.getString(21));
                cashier.setP14(res.getString(22));
                cashier.setP15(res.getString(23));
                cashier.setP16(res.getString(24));
                if (res.getString(6).equals("CASH")) {
                    SheredPrefranseHelper.addcurrentcashier(getBaseContext(), cashier);
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "غير مسموح للدليفري بالدخول !", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(getBaseContext(), "بيانات الدخول غير صحيحة !", Toast.LENGTH_SHORT).show();
        }


    }

    public void go_login(View view) {

        Intent i = new Intent(this, login.class);
        i.putExtra("from","emp");
        startActivity(i);
        finish();

    }
}