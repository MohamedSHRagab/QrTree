package com.mohamedragab.cashpos.modules.employees.views;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.modules.employees.models.Cashier;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class newCashier extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 100;
    EditText name, address, phone, notes, pay, username, password;
    DataBaseHelper db;
    CircleImageView cashierImage;
    Cashier cashier;
    CheckBox P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16;
    private static final int CHOOSE_IMAGE = 101;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cashier);

        name = (EditText) findViewById(R.id.name);
        address = (EditText) findViewById(R.id.address);
        phone = (EditText) findViewById(R.id.phone);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        notes = (EditText) findViewById(R.id.notes);

        cashierImage = (CircleImageView) findViewById(R.id.image);

        P1 = (CheckBox) findViewById(R.id.pass1);
        P2 = (CheckBox) findViewById(R.id.pass2);
        P3 = (CheckBox) findViewById(R.id.pass3);
        P4 = (CheckBox) findViewById(R.id.pass4);
        P5 = (CheckBox) findViewById(R.id.pass5);
        P6 = (CheckBox) findViewById(R.id.pass6);
        P7 = (CheckBox) findViewById(R.id.pass7);
        P8 = (CheckBox) findViewById(R.id.pass8);
        P9 = (CheckBox) findViewById(R.id.pass9);
        P10 = (CheckBox) findViewById(R.id.pass10);
        P11 = (CheckBox) findViewById(R.id.pass11);
        P12 = (CheckBox) findViewById(R.id.pass12);
        P13 = (CheckBox) findViewById(R.id.pass13);
        P14 = (CheckBox) findViewById(R.id.pass14);
        P15 = (CheckBox) findViewById(R.id.pass15);
        P16 = (CheckBox) findViewById(R.id.pass16);

        Intent i = getIntent();
        bundle = i.getExtras();
        if (bundle != null) {
            cashier = (Cashier) bundle.getSerializable("cashier");
            if (cashier != null) {
                if (cashier.getImage()!=null){
                    cashierImage.setImageBitmap(BitmapFactory.decodeByteArray(cashier.getImage(), 0, cashier.getImage().length));
                }
                name.setText(cashier.getName());
                address.setText(cashier.getAddress());
                phone.setText(cashier.getPhone());
                username.setText(cashier.getUserName());
                password.setText(cashier.getPassword());
                notes.setText(cashier.getNotes());
                if (cashier.getP1().equals("1")) {
                    P1.setChecked(true);
                }
                if (cashier.getP2().equals("1")) {
                    P2.setChecked(true);
                }
                if (cashier.getP3().equals("1")) {
                    P3.setChecked(true);
                }
                if (cashier.getP4().equals("1")) {
                    P4.setChecked(true);
                }
                if (cashier.getP5().equals("1")) {
                    P5.setChecked(true);
                }
                if (cashier.getP6().equals("1")) {
                    P6.setChecked(true);
                }
                if (cashier.getP7().equals("1")) {
                    P7.setChecked(true);
                }
                if (cashier.getP8().equals("1")) {
                    P8.setChecked(true);
                }
                if (cashier.getP9().equals("1")) {
                    P9.setChecked(true);
                }
                if (cashier.getP10().equals("1")) {
                    P10.setChecked(true);
                }
                if (cashier.getP11().equals("1")) {
                    P11.setChecked(true);
                }
                if (cashier.getP12().equals("1")) {
                    P12.setChecked(true);
                }
                if (cashier.getP13().equals("1")) {
                    P13.setChecked(true);
                }
                if (cashier.getP14().equals("1")) {
                    P14.setChecked(true);
                }
                if (cashier.getP15().equals("1")) {
                    P15.setChecked(true);
                }
                if (cashier.getP16().equals("1")) {
                    P16.setChecked(true);
                }

            }
        }

        db = new DataBaseHelper(getBaseContext());
    }

    public void save(View view) {
        if (bundle == null) {
            if (name.getText().toString().equals("") || address.getText().toString().equals("") || username.getText().toString().equals("") || password.getText().toString().equals("") ) {
                Toast.makeText(getBaseContext(), "رجاء اكمال كافة البيانات المطلوبه !", Toast.LENGTH_SHORT).show();
            } else {

                Cursor res = db.getallcashier();
                if (res != null && res.getCount() > 0) {
                    while (res.moveToNext()) {
                        if (name.getText().toString().trim().equals(res.getString(1))) {
                            Toast.makeText(getBaseContext(), "هذاالاسم تم استخدامه من قبل !", Toast.LENGTH_SHORT).show();
                            break;
                        } else if (username.getText().toString().trim().equals(res.getString(7))) {
                            Toast.makeText(getBaseContext(), "اسم المستخدم هذا مستخدم بالفعل !", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if (res.isLast()) {

                            String name_val = name.getText().toString().trim();
                            String address_val = address.getText().toString();
                            String phone_val = phone.getText().toString();
                            String notes_val = notes.getText().toString() + "";
                            String username_val = username.getText().toString().trim() + "";
                            String password_val = password.getText().toString().trim() + "";

                            if (cashier == null) {
                                cashier = new Cashier();
                            }

                            cashier.setAddress(address_val);
                            cashier.setName(name_val);
                            cashier.setNotes(notes_val);
                            cashier.setPhone(phone_val);
                            cashier.setJob("CASH");
                            cashier.setUserName(username_val);
                            cashier.setPassword(password_val);
                            if (P1.isChecked()) {
                                cashier.setP1("1");
                            } else {
                                cashier.setP1("0");
                            }
                            if (P2.isChecked()) {
                                cashier.setP2("1");
                            } else {
                                cashier.setP2("0");
                            } if (P3.isChecked()) {
                                cashier.setP3("1");
                            } else {
                                cashier.setP3("0");
                            } if (P4.isChecked()) {
                                cashier.setP4("1");
                            } else {
                                cashier.setP4("0");
                            } if (P5.isChecked()) {
                                cashier.setP5("1");
                            } else {
                                cashier.setP5("0");
                            } if (P6.isChecked()) {
                                cashier.setP6("1");
                            } else {
                                cashier.setP6("0");
                            } if (P7.isChecked()) {
                                cashier.setP7("1");
                            } else {
                                cashier.setP7("0");
                            } if (P8.isChecked()) {
                                cashier.setP8("1");
                            } else {
                                cashier.setP8("0");
                            } if (P9.isChecked()) {
                                cashier.setP9("1");
                            } else {
                                cashier.setP9("0");
                            } if (P10.isChecked()) {
                                cashier.setP10("1");
                            } else {
                                cashier.setP10("0");
                            } if (P11.isChecked()) {
                                cashier.setP11("1");
                            } else {
                                cashier.setP11("0");
                            } if (P12.isChecked()) {
                                cashier.setP12("1");
                            } else {
                                cashier.setP12("0");
                            } if (P13.isChecked()) {
                                cashier.setP13("1");
                            } else {
                                cashier.setP13("0");
                            } if (P14.isChecked()) {
                                cashier.setP14("1");
                            } else {
                                cashier.setP14("0");
                            } if (P15.isChecked()) {
                                cashier.setP15("1");
                            } else {
                                cashier.setP15("0");
                            } if (P16.isChecked()) {
                                cashier.setP16("1");
                            } else {
                                cashier.setP16("0");
                            }

                            if (db.insert_date(cashier)) {
                                MediaPlayer mp = MediaPlayer.create(newCashier.this, R.raw.finalsound);
                                mp.start();
                                Toast.makeText(getBaseContext(), "تم تسجيل الكاشير بنجاح", Toast.LENGTH_SHORT).show();
                                db.close();
                                onBackPressed();

                            } else {
                                 Toast.makeText(getBaseContext(), "فشل تسجيل الكاشير تحقق من ادخال بيانات صحيحه", Toast.LENGTH_SHORT).show();

                            }
                        }

                    }
                } else {
                    String name_val = name.getText().toString().trim();
                    String address_val = address.getText().toString();
                    String phone_val = phone.getText().toString();
                    String notes_val = notes.getText().toString() + "";
                    String username_val = username.getText().toString().trim() + "";
                    String password_val = password.getText().toString().trim() + "";

                    if (cashier == null) {
                        cashier = new Cashier();
                    }
                    cashier.setAddress(address_val);
                    cashier.setName(name_val);
                    cashier.setNotes(notes_val);
                    cashier.setPhone(phone_val);
                    cashier.setJob("CASH");
                    cashier.setUserName(username_val);
                    cashier.setPassword(password_val);
                    if (P1.isChecked()) {
                        cashier.setP1("1");
                    } else {
                        cashier.setP1("0");
                    }
                    if (P2.isChecked()) {
                        cashier.setP2("1");
                    } else {
                        cashier.setP2("0");
                    } if (P3.isChecked()) {
                        cashier.setP3("1");
                    } else {
                        cashier.setP3("0");
                    } if (P4.isChecked()) {
                        cashier.setP4("1");
                    } else {
                        cashier.setP4("0");
                    } if (P5.isChecked()) {
                        cashier.setP5("1");
                    } else {
                        cashier.setP5("0");
                    } if (P6.isChecked()) {
                        cashier.setP6("1");
                    } else {
                        cashier.setP6("0");
                    } if (P7.isChecked()) {
                        cashier.setP7("1");
                    } else {
                        cashier.setP7("0");
                    } if (P8.isChecked()) {
                        cashier.setP8("1");
                    } else {
                        cashier.setP8("0");
                    } if (P9.isChecked()) {
                        cashier.setP9("1");
                    } else {
                        cashier.setP9("0");
                    } if (P10.isChecked()) {
                        cashier.setP10("1");
                    } else {
                        cashier.setP10("0");
                    } if (P11.isChecked()) {
                        cashier.setP11("1");
                    } else {
                        cashier.setP11("0");
                    } if (P12.isChecked()) {
                        cashier.setP12("1");
                    } else {
                        cashier.setP12("0");
                    } if (P13.isChecked()) {
                        cashier.setP13("1");
                    } else {
                        cashier.setP13("0");
                    } if (P14.isChecked()) {
                        cashier.setP14("1");
                    } else {
                        cashier.setP14("0");
                    } if (P15.isChecked()) {
                        cashier.setP15("1");
                    } else {
                        cashier.setP15("0");
                    } if (P16.isChecked()) {
                        cashier.setP16("1");
                    } else {
                        cashier.setP16("0");
                    }

                    if (db.insert_date(cashier)) {
                        MediaPlayer mp = MediaPlayer.create(newCashier.this, R.raw.finalsound);
                        mp.start();
                        Toast.makeText(getBaseContext(), "تم تسجيل الكاشير بنجاح", Toast.LENGTH_SHORT).show();
                        onBackPressed();

                    } else {
                        //  Toast.makeText(getBaseContext(), "فشل تسجيل الكاشير تحقق من ادخال بيانات صحيحه", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        } else {
            cashier.setAddress(address.getText().toString());
            cashier.setName(name.getText().toString().trim());
            cashier.setNotes(notes.getText().toString());
            cashier.setPhone(phone.getText().toString());
            cashier.setJob("CASH");
            cashier.setUserName( username.getText().toString().trim());
            cashier.setPassword(password.getText().toString() .trim());
            if (P1.isChecked()) {
                cashier.setP1("1");
            } else {
                cashier.setP1("0");
            }
            if (P2.isChecked()) {
                cashier.setP2("1");
            } else {
                cashier.setP2("0");
            } if (P3.isChecked()) {
                cashier.setP3("1");
            } else {
                cashier.setP3("0");
            } if (P4.isChecked()) {
                cashier.setP4("1");
            } else {
                cashier.setP4("0");
            } if (P5.isChecked()) {
                cashier.setP5("1");
            } else {
                cashier.setP5("0");
            } if (P6.isChecked()) {
                cashier.setP6("1");
            } else {
                cashier.setP6("0");
            } if (P7.isChecked()) {
                cashier.setP7("1");
            } else {
                cashier.setP7("0");
            } if (P8.isChecked()) {
                cashier.setP8("1");
            } else {
                cashier.setP8("0");
            } if (P9.isChecked()) {
                cashier.setP9("1");
            } else {
                cashier.setP9("0");
            } if (P10.isChecked()) {
                cashier.setP10("1");
            } else {
                cashier.setP10("0");
            } if (P11.isChecked()) {
                cashier.setP11("1");
            } else {
                cashier.setP11("0");
            } if (P12.isChecked()) {
                cashier.setP12("1");
            } else {
                cashier.setP12("0");
            } if (P13.isChecked()) {
                cashier.setP13("1");
            } else {
                cashier.setP13("0");
            } if (P14.isChecked()) {
                cashier.setP14("1");
            } else {
                cashier.setP14("0");
            } if (P15.isChecked()) {
                cashier.setP15("1");
            } else {
                cashier.setP15("0");
            } if (P16.isChecked()) {
                cashier.setP16("1");
            } else {
                cashier.setP16("0");
            }
            if (db.updatecashierData(cashier)) {
                Toast.makeText(getBaseContext(), "تم تعديل بيانات الكاشير بنجاح .", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getBaseContext(), "فشل تعديل بيانات الكاشير !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void captureimage(View view) {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.choosecameradialog);

        Button camera = (Button) dialog.findViewById(R.id.camera);
        camera.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
            dialog.dismiss();

        });
        Button gallery = (Button) dialog.findViewById(R.id.gallery);
        gallery.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            startActivityForResult(intent, CHOOSE_IMAGE);
            dialog.dismiss();

        });
        dialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {

            Bitmap theImage = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            theImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            if (cashier == null) {
                cashier = new Cashier();
            }
            byte[] byteArray = stream.toByteArray();
            cashier.setImage(byteArray);
            cashierImage.setImageBitmap(theImage);
        }
        if (resultCode == RESULT_OK && requestCode == CHOOSE_IMAGE && data != null) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Bitmap theImage = selectedImage;
                int size = theImage.getByteCount();
                if (size > 7000000) {
                    Toast.makeText(getBaseContext(), "حجم الصوره كبير جدا  !", Toast.LENGTH_SHORT).show();
                } else {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    theImage.compress(Bitmap.CompressFormat.PNG, 50, stream);
                    if (cashier == null) {
                        cashier = new Cashier();
                    }
                    byte[] byteArray = stream.toByteArray();
                    cashier.setImage(byteArray);
                    cashierImage.setImageBitmap(theImage);


                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    public void go_cashiers(View view) {
        onBackPressed();
    }
}
