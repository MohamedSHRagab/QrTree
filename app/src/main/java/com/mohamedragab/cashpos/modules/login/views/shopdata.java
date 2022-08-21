package com.mohamedragab.cashpos.modules.login.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mohamedragab.cashpos.modules.employees.models.Cashier;
import com.mohamedragab.cashpos.modules.login.models.User;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.home.MainActivity;


public class shopdata extends AppCompatActivity {
    DatabaseReference reference;
    TextView shopname, phone, address;
    ProgressDialog dialog2;
    DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopdata);

        dialog2 = ProgressDialog.show(shopdata.this, "ابرجاء الانتظار ...",
                " جاري التاكد من البيانات المدخله ", true);
        dialog2.show();
        shopname = (TextView) findViewById(R.id.shopname);
        phone = (TextView) findViewById(R.id.phone);
        address = (TextView) findViewById(R.id.shopaddress);
        String sessionId = getIntent().getStringExtra("EXTRA_SESSION_ID");

        db = new DataBaseHelper(getBaseContext());


        reference = FirebaseDatabase.getInstance().getReference("Users").child(sessionId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                SheredPrefranseHelper.addUserData(shopdata.this, user);
                shopname.setText(user.getName());
                phone.setText(user.getPhone());
                address.setText(user.getAddress());
                dialog2.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog2.dismiss();

            }
        });

    }

    public void save(View view) {


        startActivity(new Intent(this, MainActivity.class));
        finish();
        db.close();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        db.close();
    }
}


