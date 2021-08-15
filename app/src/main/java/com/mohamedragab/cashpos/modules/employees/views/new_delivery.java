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

public class new_delivery extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 100;
    EditText name, address, phone, notes, pay;
    DataBaseHelper db;
    CircleImageView cashierImage;
    Cashier cashier;
    CheckBox first, second, third, forth;
    private static final int CHOOSE_IMAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_delivery);
        name = (EditText) findViewById(R.id.name);
        address = (EditText) findViewById(R.id.address);
        phone = (EditText) findViewById(R.id.phone);
        notes = (EditText) findViewById(R.id.notes);
        cashierImage = (CircleImageView) findViewById(R.id.image);


        db = new DataBaseHelper(getBaseContext());
    }

    public void go_back(View view) {
        onBackPressed();
    }

    public void save(View view) {
        if (name.getText().toString().equals("") || address.getText().toString().equals("") || phone.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), "رجاء اكمال كافة البيانات المطلوبه !", Toast.LENGTH_SHORT).show();
        } else {

            Cursor res = db.getallcashier();
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

                        if (cashier == null) {
                            cashier = new Cashier();
                        }

                        cashier.setAddress(address_val);
                        cashier.setName(name_val);
                        cashier.setNotes(notes_val);
                        cashier.setPhone(phone_val);
                        cashier.setJob("DEL");


                        if (db.insert_date(cashier)) {
                            MediaPlayer mp = MediaPlayer.create(new_delivery.this, R.raw.finalsound);
                            mp.start();
                            db.close();
                            Toast.makeText(getBaseContext(), "تم تسجيل الدليفري بنجاح", Toast.LENGTH_SHORT).show();
                            onBackPressed();

                        } else {
                            // Toast.makeText(getBaseContext(), "فشل تسجيل الكاشير تحقق من ادخال بيانات صحيحه", Toast.LENGTH_SHORT).show();

                        }
                    }

                }
            } else {
                String name_val = name.getText().toString().trim();
                String address_val = address.getText().toString();
                String phone_val = phone.getText().toString();
                String notes_val = notes.getText().toString() + "";

                if (cashier == null) {
                    cashier = new Cashier();
                }
                cashier.setAddress(address_val);
                cashier.setName(name_val);
                cashier.setNotes(notes_val);
                cashier.setPhone(phone_val);
                cashier.setJob("DEL");

                if (db.insert_date(cashier)) {
                    MediaPlayer mp = MediaPlayer.create(new_delivery.this, R.raw.finalsound);
                    mp.start();
                    Toast.makeText(getBaseContext(), "تم تسجيل الدليفري بنجاح", Toast.LENGTH_SHORT).show();
                    onBackPressed();

                } else {
                    //  Toast.makeText(getBaseContext(), "فشل تسجيل الكاشير تحقق من ادخال بيانات صحيحه", Toast.LENGTH_SHORT).show();
                }
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
        if (resultCode == RESULT_OK&&requestCode == CHOOSE_IMAGE&& data != null) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Bitmap theImage = selectedImage;
                int size = theImage.getByteCount();
                if (size > 2000000) {
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

}
