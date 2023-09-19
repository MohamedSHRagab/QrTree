package com.mohamedragab.cashpos.modules.login.views;

import static android.os.Build.VERSION.SDK_INT;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mohamedragab.cashpos.BuildConfig;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.AppConfig;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.adminpanel.models.adminmodel;
import com.mohamedragab.cashpos.modules.blockscreen.blockscreen;
import com.mohamedragab.cashpos.modules.employees.models.Cashier;
import com.mohamedragab.cashpos.modules.home.MainActivity;
import com.mohamedragab.cashpos.modules.login.models.User;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;

import java.io.File;

public class login extends AppCompatActivity {
    DatabaseReference reference;
    EditText username;
    TextInputEditText password;
    String shopname_val;
    ProgressBar progressBar;
    RadioButton shop_radio, admin_radio;
    Button activate, trial;
    CardView cardView;
    DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         db=new DataBaseHelper(login.this);

        setContentView(R.layout.activity_login);

        if(SDK_INT >= 30) {

                                try {
                                    Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
                                    startActivity(intent);
                                } catch (Exception ex) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                    startActivity(intent);
                                }
                            }




        username = (EditText) findViewById(R.id.username);
        password = (TextInputEditText) findViewById(R.id.password_et);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        admin_radio = (RadioButton) findViewById(R.id.admin);
        shop_radio = (RadioButton) findViewById(R.id.shop);
        trial = (Button) findViewById(R.id.trial);
        activate = (Button) findViewById(R.id.activated_button);

        cardView = (CardView) findViewById(R.id.card_activated);

        if (getIntent().getExtras()!=null){
            String from = getIntent().getExtras().getString("from");
            if (from != null) {
                trial.setVisibility(View.GONE);
                activate.setVisibility(View.GONE);
                cardView.setVisibility(View.VISIBLE);
            }
        }


        AppConfig.request_permission(this, this);
    }

    public void login(View view) {
        File file = new File(Environment.getExternalStorageDirectory() + "/cashpos/database");
        File file2 = new File(Environment.getExternalStorageDirectory() + "/cashpos/invoices");
        File file3 = new File(Environment.getExternalStorageDirectory() + "/cashpos/agelpaid");
        File file4 = new File(Environment.getExternalStorageDirectory() + "/cashpos/kestpaid");
        File file5 = new File(Environment.getExternalStorageDirectory() + "/cashpos/buyinvoices");

        file.mkdirs(); // Will create parent directories if not exists
        file2.mkdirs(); // Will create parent directories if not exists
        file3.mkdirs(); // Will create parent directories if not exists
        file4.mkdirs(); // Will create parent directories if not exists
        file5.mkdirs(); // Will create parent directories if not exists


        if (username.getText().toString().trim().equals("") || password.getText().toString().trim().equals("")) {
            Toast.makeText(getBaseContext(), "برجاء ادخال بيانات الدخول !", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);

            if (shop_radio.isChecked()) {

                reference = FirebaseDatabase.getInstance().getReference("Users").child(username.getText().toString().trim());

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final User user = dataSnapshot.getValue(User.class);

                        if (user == null) {
                            Toast.makeText(getBaseContext(), "ليس لديك صلاحيات دخول", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        } else {
                            Log.d("loginerror", username.getText().toString().trim() + " : " + user.getPhone() + " : " + password.getText().toString() + " : " + user.getPassword());

                            if (username.getText().toString().trim().equals(user.getPhone()) && password.getText().toString().trim().equals(user.getPassword())) {
                                if (user.getBlocked().equals("true")) {
                                    startActivity(new Intent(getBaseContext(), blockscreen.class));
                                    progressBar.setVisibility(View.GONE);
                                } else if (user.getUsed().equals("true")) {
                                    Toast.makeText(getBaseContext(), "هذا الحساب تم استخدامه بالفعل !", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    shopname_val = user.getPhone();

                                    AppConfig.request_permission(login.this, login.this);
                                    reference.child("used").setValue("true").addOnSuccessListener(task -> {
                                        Intent intent = new Intent(login.this, shopdata.class);

                                        Cashier new_cash=new Cashier();
                                        new_cash.setAddress("");
                                        new_cash.setName("المدير");
                                        new_cash.setNotes("جديد");
                                        new_cash.setPhone(username.getText().toString());
                                        new_cash.setJob("CASH");
                                        new_cash.setUserName( username.getText().toString());
                                        new_cash.setPassword(password.getText().toString() .trim());
                                        new_cash.setP1("0");
                                        new_cash.setP2("0");
                                        new_cash.setP3("0");
                                        new_cash.setP4("0");
                                        new_cash.setP5("0");
                                        new_cash.setP6("0");
                                        new_cash.setP7("0");
                                        new_cash.setP8("0");
                                        new_cash.setP9("0");
                                        new_cash.setP10("0");
                                        new_cash.setP11("0");
                                        new_cash.setP12("0");
                                        new_cash.setP13("0");
                                        new_cash.setP14("0");
                                        new_cash.setP15("0");
                                        new_cash.setP16("0");

                                        SheredPrefranseHelper.addcurrentcashier(login.this, new_cash);
                                        SheredPrefranseHelper.addmoney_type(login.this, " جنيه ");
                                        intent.putExtra("EXTRA_SESSION_ID", user.getPhone());
                                        startActivity(intent);
                                        finish();
                                    }).addOnFailureListener(e -> {
                                        Toast.makeText(getBaseContext(), "فشل الدخول تأكد من اتصالك بالانترنت !", Toast.LENGTH_SHORT).show();

                                    });

                                }

                            } else {

                                Toast.makeText(getBaseContext(), "ليس لديك صلاحيات دخول !", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getBaseContext(), "لا يوجد صلاحيات دخول لهذه البيانات لطلب مستخدم جديد وكلمة مرور تواصل مع الادمن ", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            } else {
                reference = FirebaseDatabase.getInstance().getReference("admins").child(username.getText().toString().trim());

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final adminmodel admin = dataSnapshot.getValue(adminmodel.class);

                        if (admin == null) {
                            Toast.makeText(getBaseContext(), "ليس لديك صلاحيات دخول !", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        } else {
                            Log.d("loginerror", username.getText().toString().trim() + " : " + admin.getPhone() + " : " + password.getText().toString() + " : " + admin.getPassword());
                            if (username.getText().toString().trim().equals(admin.getPhone()) && password.getText().toString().trim().equals(admin.getPassword())) {
                                if (admin.getBlocked().equals("true")) {
                                    startActivity(new Intent(getBaseContext(), blockscreen.class));
                                    progressBar.setVisibility(View.GONE);
                                } else if (admin.getUsed().equals("true")) {
                                    Toast.makeText(getBaseContext(), "هذا الحساب تم استخدامه بالفعل !", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                } else {

                                    AppConfig.request_permission(login.this, login.this);

                                    progressBar.setVisibility(View.GONE);

                                    SheredPrefranseHelper.addAdminData(login.this, admin);
                                    reference.child("used").setValue("true").addOnSuccessListener(task -> {
                                        SheredPrefranseHelper.addmoney_type(login.this, " جنيه ");
                                        Intent intent = new Intent(login.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }).addOnFailureListener(e -> {
                                        Toast.makeText(getBaseContext(), "فشل الدخول تأكد من اتصالك بالانترنت !", Toast.LENGTH_SHORT).show();

                                    });

                                }

                            } else {

                                Toast.makeText(getBaseContext(), "ليس لديك صلاحيات دخول", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getBaseContext(), "لا يوجد صلاحيات دخول لهذه البيانات لطلب مستخدم جديد وكلمة مرور تواصل مع الادمن ", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }


        }


    }


    public void limit(View view) {
        File file = new File(Environment.getExternalStorageDirectory() + "/cashpos/database");
        File file2 = new File(Environment.getExternalStorageDirectory() + "/cashpos/invoices");
        File file3 = new File(Environment.getExternalStorageDirectory() + "/cashpos/agelpaid");
        File file4 = new File(Environment.getExternalStorageDirectory() + "/cashpos/kestpaid");
        File file5 = new File(Environment.getExternalStorageDirectory() + "/cashpos/buyinvoices");

        file.mkdirs(); // Will create parent directories if not exists
        file2.mkdirs(); // Will create parent directories if not exists
        file3.mkdirs(); // Will create parent directories if not exists
        file4.mkdirs(); // Will create parent directories if not exists
        file5.mkdirs(); // Will create parent directories if not exists




        SheredPrefranseHelper.addmoney_type(login.this, " جنيه ");
        Intent intent = new Intent(login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void show_card(View view) {
        activate.setVisibility(View.GONE);
        cardView.setVisibility(View.VISIBLE);
    }
}
