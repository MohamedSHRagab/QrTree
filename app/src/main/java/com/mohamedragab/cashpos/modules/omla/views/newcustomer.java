package com.mohamedragab.cashpos.modules.omla.views;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.modules.omlatransactions.models.omlatransaction;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.omla.models.omla;
import com.mohamedragab.cashpos.utils.Round;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class newcustomer extends AppCompatActivity {
    EditText name, address, phone, notes, pay, maxnotpaid;
   DataBaseHelper db;
    omla oldomla;
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
        setContentView(R.layout.activity_newcustomer);

        name = (EditText) findViewById(R.id.name);
        address = (EditText) findViewById(R.id.address);
        phone = (EditText) findViewById(R.id.phone);
        notes = (EditText) findViewById(R.id.notes);
        pay = (EditText) findViewById(R.id.paymoney);
        maxnotpaid = (EditText) findViewById(R.id.maxnotpaid);

        db = new DataBaseHelper(getBaseContext());

        oldomla = (omla) getIntent().getSerializableExtra("omla");
        if (oldomla != null) {
            name.setText(oldomla.getName() + "");
            name.setEnabled(false);
            address.setText(oldomla.getAddress() + "");
            phone.setText(oldomla.getPhone() + "");
            if (oldomla.getNotes() != null) {
                notes.setText(oldomla.getNotes() + "");
            }
            pay.setText(oldomla.getPaymoney() + "");
            pay.setEnabled(false);
            maxnotpaid.setText(oldomla.getMaxnotpaid() + "");
        }

    }

    public void save(View view) {
        if (name.getText().toString().equals("") || address.getText().toString().equals("") || phone.getText().toString().equals("") || pay.getText().toString().equals("") || maxnotpaid.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), "رجاء اكمال كافة البيانات المطلوبه !", Toast.LENGTH_SHORT).show();
        } else {
            if (oldomla != null) {
                oldomla.setAddress(address.getText().toString()+"");
                oldomla.setMaxnotpaid(Double.parseDouble(maxnotpaid.getText().toString()+""));
                oldomla.setNotes(notes.getText().toString()+"");
                oldomla.setPhone(phone.getText().toString()+"");

                db.updateData(oldomla);
                Toast.makeText(getBaseContext(), "تم تعديل بيانات العميل بنجاح .", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,viewallomla.class));
                finish();

            }else{
                Cursor res = db.getallomla();
                if (res != null && res.getCount() > 0) {
                    while (res.moveToNext()) {
                        if (name.getText().toString().trim().equals(res.getString(1))) {
                            Toast.makeText(getBaseContext(), "هذاالاسم تم استخدامه من قبل !", Toast.LENGTH_SHORT).show();
                            break;
                        } else if (phone.getText().toString().trim().equals(res.getString(3))) {
                            Toast.makeText(getBaseContext(), "هذا المحمول مستخدم بالفعل !", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if (res.isLast()) {

                            String name_val = name.getText().toString().trim();
                            String address_val = address.getText().toString();
                            String phone_val = phone.getText().toString();
                            String notes_val = notes.getText().toString() + "";
                            String paymoney_val = pay.getText().toString();
                            String maxnotpaid_val = maxnotpaid.getText().toString();

                            omla omla = new omla();

                            omla.setAddress(address_val);
                            omla.setHasmoney(0);
                            omla.setName(name_val);
                            omla.setNotes(notes_val);
                            omla.setPaymoney(Round.round(Double.parseDouble(paymoney_val), 3));
                            omla.setPhone(phone_val);
                            omla.setMaxnotpaid(Round.round(Double.parseDouble(maxnotpaid_val), 3));


                            if (db.insert_date(omla)) {
                                omlatransaction omlatransaction = new omlatransaction();
                                Date c = Calendar.getInstance().getTime();
                                System.out.println("Current time => " + c);

                                SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                                String formattedDate2 = df2.format(c);
                                omlatransaction.setDate(formattedDate2);
                                omlatransaction.setName(name_val);
                                omlatransaction.setInvoiceId("-");
                                omlatransaction.setNotpaid(Round.round(Double.parseDouble(paymoney_val), 3));
                                omlatransaction.setProcess("رصيد افتتاحي");

                                if (db.insert_date(omlatransaction)) {
                                    MediaPlayer mp = MediaPlayer.create(newcustomer.this, R.raw.finalsound);
                                    mp.start();
                                    db.close();
                                    Toast.makeText(getBaseContext(), "تم تسجيل العميل بنجاح", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                            } else {
                                // Toast.makeText(getBaseContext(), "فشل تسجيل العميل تحقق من ادخال بيانات صحيحه", Toast.LENGTH_SHORT).show();

                            }
                        }

                    }
                } else {
                    String name_val = name.getText().toString().trim();
                    String address_val = address.getText().toString();
                    String phone_val = phone.getText().toString().trim();
                    String notes_val = notes.getText().toString();
                    String paymoney_val = pay.getText().toString();
                    String maxnotpaid_val = maxnotpaid.getText().toString();

                    omla omla = new omla();

                    omla.setAddress(address_val);
                    omla.setHasmoney(0);
                    omla.setName(name_val);
                    omla.setNotes(notes_val);
                    omla.setPaymoney(Round.round(Double.parseDouble(paymoney_val), 3));
                    omla.setPhone(phone_val);
                    omla.setMaxnotpaid(Round.round(Double.parseDouble(maxnotpaid_val), 3));

                    if (db.insert_date(omla)) {
                        omlatransaction omlatransaction = new omlatransaction();
                        Date c = Calendar.getInstance().getTime();
                        System.out.println("Current time => " + c);

                        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                        String formattedDate2 = df2.format(c);
                        omlatransaction.setDate(formattedDate2);
                        omlatransaction.setName(name_val);
                        omlatransaction.setInvoiceId("-");
                        omlatransaction.setNotpaid(Round.round(Double.parseDouble(paymoney_val), 3));
                        omlatransaction.setProcess("رصيد افتتاحي");

                        if (db.insert_date(omlatransaction)) {
                            MediaPlayer mp = MediaPlayer.create(newcustomer.this, R.raw.finalsound);
                            mp.start();
                            db.close();
                            Toast.makeText(getBaseContext(), "تم تسجيل العميل بنجاح", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }

                    } else {
                        // Toast.makeText(getBaseContext(), "فشل تسجيل العميل تحقق من ادخال بيانات صحيحه", Toast.LENGTH_SHORT).show();

                    }
                }
            }


        }
    }

    public void go_customers(View view) {
        onBackPressed();
    }
}
