package com.mohamedragab.cashpos.modules.mored.views;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.modules.mored.models.mored;
import com.mohamedragab.cashpos.modules.moredtransactions.models.moredtransaction;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.utils.Round;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class newmored extends AppCompatActivity {
    EditText name, address, phone, notes, paymoney;
    DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmored);

        name = (EditText) findViewById(R.id.name);
        address = (EditText) findViewById(R.id.address);
        phone = (EditText) findViewById(R.id.phone);
        notes = (EditText) findViewById(R.id.notes);
        paymoney = (EditText) findViewById(R.id.paymoney);

        db = new DataBaseHelper(getBaseContext());
    }

    public void go_customers(View view) {
        onBackPressed();
        db.close();
    }

    public void save(View view) {
        if (name.getText().toString().equals("") || address.getText().toString().equals("") || phone.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), "رجاء اكمال كافة البيانات المطلوبه !", Toast.LENGTH_SHORT).show();
        } else {
            Cursor res = db.getallmored();
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
                        String phone_val = phone.getText().toString().trim();
                        String notes_val = notes.getText().toString();
                        String paymoney_val = paymoney.getText().toString();

                        mored mored = new mored();

                        mored.setAddress(address_val);
                        mored.setHasmoney(0);
                        mored.setName(name_val);
                        mored.setNotes(notes_val);
                        mored.setPaymoney(Round.round(Double.parseDouble(paymoney_val), 3));
                        mored.setPhone(phone_val);

                        if (db.insert_date(mored)) {
                            moredtransaction moredtrans = new moredtransaction();
                            Date c = Calendar.getInstance().getTime();
                            System.out.println("Current time => " + c);

                            SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                            String formattedDate2 = df2.format(c);
                            moredtrans.setDate(formattedDate2);
                            moredtrans.setName(name_val);
                            moredtrans.setInvoiceId("-");
                            moredtrans.setNotpaid(Round.round(Double.parseDouble(paymoney_val), 3));
                            moredtrans.setProcess("رصيد افتتاحي");

                            if (db.insert_date(moredtrans)) {
                                MediaPlayer mp = MediaPlayer.create(newmored.this, R.raw.finalsound);
                                mp.start();
                                db.close();
                                Toast.makeText(getBaseContext(), "تم تسجيل المورد بنجاح", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                        } else {
                            //Toast.makeText(getBaseContext(), "فشل تسجيل المورد تحقق من ادخال بيانات صحيحه", Toast.LENGTH_SHORT).show();

                        }


                    }
                }
            } else {
                String name_val = name.getText().toString().trim();
                String address_val = address.getText().toString();
                String phone_val = phone.getText().toString().trim();
                String notes_val = notes.getText().toString();
                String paymoney_val = paymoney.getText().toString();

                mored mored = new mored();

                mored.setAddress(address_val);
                mored.setHasmoney(0);
                mored.setName(name_val);
                mored.setNotes(notes_val);
                mored.setPaymoney(Round.round(Double.parseDouble(paymoney_val), 3));
                mored.setPhone(phone_val);

                if (db.insert_date(mored)) {
                    moredtransaction moredtrans = new moredtransaction();
                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);

                    SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                    String formattedDate2 = df2.format(c);
                    moredtrans.setDate(formattedDate2);
                    moredtrans.setName(name_val);
                    moredtrans.setInvoiceId("-");
                    moredtrans.setNotpaid(Round.round(Double.parseDouble(paymoney_val), 3));
                    moredtrans.setProcess("رصيد افتتاحي");

                    if (db.insert_date(moredtrans)) {
                        MediaPlayer mp = MediaPlayer.create(newmored.this, R.raw.finalsound);
                        mp.start();
                        db.close();
                        Toast.makeText(getBaseContext(), "تم تسجيل المورد بنجاح", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                } else {
                  //  Toast.makeText(getBaseContext(), "فشل تسجيل المورد تحقق من ادخال بيانات صحيحه", Toast.LENGTH_SHORT).show();

                }
            }


        }
    }
}
