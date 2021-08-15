package com.mohamedragab.cashpos.modules.login.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.home.MainActivity;

import java.util.HashMap;

public class addnewshop extends AppCompatActivity {
    EditText shopname, phone, address, password;
    DatabaseReference reference;
    Spinner shops;
    String shops_names[] = {"airconditioning", "caroil", "doors", "machines", "mobile", "videogame", "wires","hairstyle"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewshop);

        shopname = (EditText) findViewById(R.id.shopname);
        phone = (EditText) findViewById(R.id.phone);
        address = (EditText) findViewById(R.id.shopaddress);
        password = (EditText) findViewById(R.id.password);

        shops = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(addnewshop.this, android.R.layout.simple_spinner_dropdown_item, shops_names);
        shops.setAdapter(adapter);

    }

    public void go_home(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void register(View view) {
        if (shopname.getText().toString().equals("") || phone.getText().toString().equals("") || address.getText().toString().equals("") || password.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), "برجاء تكمله البيانات الفارغه !", Toast.LENGTH_SHORT).show();
        } else {

            final ProgressDialog dialog2 = ProgressDialog.show(addnewshop.this, "انتظر ...",
                    "برجاء الانتظار جاري اضافه بيانات المحل ", true);
            dialog2.show();

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("name", shopname.getText().toString().trim());
            hashMap.put("phone", phone.getText().toString().trim());
            hashMap.put("address", address.getText().toString().trim());
            hashMap.put("password", password.getText().toString().trim());
            hashMap.put("backupdate", "false");
            hashMap.put("datalink", "null");
            hashMap.put("databaseurl", "null");
            hashMap.put("databaseupdate", "null");
            hashMap.put("blocked", "false");
            hashMap.put("lastseen", "false");
            hashMap.put("used", "false");
            hashMap.put("adminid", SheredPrefranseHelper.getAdminData(this).getId()+"");
            hashMap.put("shoptype", shops.getSelectedItem().toString()+"");

            reference = FirebaseDatabase.getInstance().getReference("Users").child(phone.getText().toString().trim());

            reference.setValue(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    shopname.setText("");
                    phone.setText("");
                    address.setText("");
                    password.setText("");
                    dialog2.dismiss();
                    Toast.makeText(getBaseContext(), "تم تسجيل بيانات المحل بنجاح !", Toast.LENGTH_SHORT).show();

                } else {
                    shopname.setText("");
                    phone.setText("");
                    address.setText("");
                    password.setText("");
                    dialog2.dismiss();

                    Toast.makeText(getBaseContext(), "لم يتم تسجيل المحل رجاء تحقق من ادخال بيانات صحيحه !", Toast.LENGTH_SHORT).show();

                }
            });

        }


    }

    public void go_shops(View view) {
       onBackPressed();
    }
}
