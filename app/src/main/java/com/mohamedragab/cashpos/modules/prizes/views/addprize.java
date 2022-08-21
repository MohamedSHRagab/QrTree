package com.mohamedragab.cashpos.modules.prizes.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class addprize extends AppCompatActivity {
    EditText text;
    RadioButton sms, prize;
    DatabaseReference reference;
    String shopid,shopname;
    TextView shopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addprize);
        text = (EditText) findViewById(R.id.text);
        sms = (RadioButton) findViewById(R.id.sms);
        prize = (RadioButton) findViewById(R.id.prize);
        shopName=(TextView)findViewById(R.id.shopname);

        Intent intent = getIntent();
        shopid = intent.getStringExtra("shopphone");
        shopname = intent.getStringExtra("shopname");
        shopName.setText("ارسال رساله الي : "+shopname);

    }

    public void go_home(View view) {
       onBackPressed();
    }

    public void send(View view) {
        if (text.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), "برجاء تكمله البيانات الفارغه !", Toast.LENGTH_SHORT).show();
        } else {

            final ProgressDialog dialog2 = ProgressDialog.show(addprize.this, "انتظر ...",
                    "برجاء الانتظار جاري الارسال ! ", true);
            dialog2.show();
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            String formattedDate = df.format(c);

            SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss", Locale.US);
            String formattedDate2 = df2.format(c);

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("id", formattedDate2);
            hashMap.put("shopphone", shopid);
            hashMap.put("adminid", SheredPrefranseHelper.getAdminData(addprize.this).getId());
            hashMap.put("createdat", formattedDate2);
            hashMap.put("used", "false");
            hashMap.put("text", text.getText().toString());
            if (prize.isChecked()) {
                hashMap.put("type", "prize");
            } else {
                hashMap.put("type", "sms");
            }


            reference = FirebaseDatabase.getInstance().getReference("prize").child(shopid).child(formattedDate2);

            reference.setValue(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    dialog2.dismiss();
                    Toast.makeText(getBaseContext(), "تم الارسال بنجاح !", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {

                    text.setText("");
                    dialog2.dismiss();

                    Toast.makeText(getBaseContext(), "لم يتم الارسال تحقق من ادخال بيانات صحيحه !", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }
}