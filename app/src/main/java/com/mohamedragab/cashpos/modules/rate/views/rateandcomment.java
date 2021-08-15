package com.mohamedragab.cashpos.modules.rate.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class rateandcomment extends AppCompatActivity {

    ProgressBar progressBar;
    EditText comment;
    RatingBar ratebar;
    DatabaseReference reference;
    String shopid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rateandcomment);

        comment = (EditText) findViewById(R.id.comment);
        ratebar = (RatingBar) findViewById(R.id.rating);


    }

    public void rate(View view) {
        if (comment.getText().toString().equals("") || ratebar.getRating() == 0) {
            Toast.makeText(getBaseContext(), "برجاء تكمله بيانات التقييم الفارغه !", Toast.LENGTH_SHORT).show();
        } else {

            final ProgressDialog dialog2 = ProgressDialog.show(rateandcomment.this, "انتظر ...",
                    "برجاء الانتظار جاري الارسال ! ", true);
            dialog2.show();
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);


            SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss", Locale.US);
            String formattedDate2 = df2.format(c);
            HashMap<String, String> hashMap = new HashMap<>();

            if (SheredPrefranseHelper.getAdminData(rateandcomment.this) != null) {
                hashMap.put("id", formattedDate2);
                shopid = SheredPrefranseHelper.getAdminData(rateandcomment.this).getId();
                hashMap.put("shopphone", shopid);
                hashMap.put("adminid", shopid);
                hashMap.put("createdat", formattedDate2);
                hashMap.put("seen", "false");
                hashMap.put("comment", comment.getText().toString());
                hashMap.put("rate_value", ratebar.getRating() + "");
            } else if (SheredPrefranseHelper.getUserData(rateandcomment.this) != null) {
                hashMap.put("id", formattedDate2);
                shopid = SheredPrefranseHelper.getUserData(rateandcomment.this).getPhone();
                hashMap.put("shopphone", shopid);
                hashMap.put("adminid", SheredPrefranseHelper.getUserData(rateandcomment.this).getAdminid());
                hashMap.put("createdat", formattedDate2);
                hashMap.put("seen", "false");
                hashMap.put("comment", comment.getText().toString());
                hashMap.put("rate_value", ratebar.getRating() + "");
            }
            reference = FirebaseDatabase.getInstance().getReference("rate").child(shopid).child(formattedDate2);

            reference.setValue(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    dialog2.dismiss();
                    Toast.makeText(getBaseContext(), "تم ارسال تعليقك بنجاح !", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {

                    comment.setText("");
                    dialog2.dismiss();

                    Toast.makeText(getBaseContext(), "لم يتم الارسال تحقق من ادخال بيانات صحيحه !", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }

    public void go_home(View view) {
        onBackPressed();
    }
}
