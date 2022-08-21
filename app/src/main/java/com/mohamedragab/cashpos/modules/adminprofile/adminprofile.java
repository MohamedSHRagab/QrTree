package com.mohamedragab.cashpos.modules.adminprofile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;

import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class adminprofile extends AppCompatActivity {

    EditText name, phone, email, address, password;
    CircleImageView imageView;
    DatabaseReference reference;

    private static final int CHOOSE_IMAGE = 101;
    Uri uriprofileImage;
    String profileImageUrl = "";
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminprofile);

        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        address = (EditText) findViewById(R.id.address);
        password = (EditText) findViewById(R.id.password);
        imageView = (CircleImageView) findViewById(R.id.image);
        imageView.setOnClickListener(v -> change(v));
        progressBar = (ProgressBar) findViewById(R.id.progress);

    }

    public void go_home(View view) {
        onBackPressed();
    }

    public void register(View view) {
        if (name.getText().toString().equals("") || phone.getText().toString().equals("") || address.getText().toString().equals("") || password.getText().toString().equals("") || email.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), "برجاء تكمله البيانات الفارغه !", Toast.LENGTH_SHORT).show();
        } else {

            final ProgressDialog dialog2 = ProgressDialog.show(adminprofile.this, "انتظر ...",
                    "برجاء الانتظار جاري اضافه بيانات الادمن ", true);
            dialog2.show();

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("name", name.getText().toString().trim());
            hashMap.put("phone", phone.getText().toString().trim());
            hashMap.put("address", address.getText().toString().trim());
            hashMap.put("password", password.getText().toString().trim());
            hashMap.put("email", email.getText().toString().trim());
            hashMap.put("id", phone.getText().toString().trim());
            hashMap.put("supervisor", SheredPrefranseHelper.getAdminData(this).getId());
            hashMap.put("type", "normal");
            hashMap.put("blocked", "false");
            hashMap.put("lastseen", "false");
            hashMap.put("used", "false");
            if (profileImageUrl.equals("")) {
                hashMap.put("imageurl", "");
            } else {
                hashMap.put("imageurl", Uri.parse(profileImageUrl).toString());
            }

            reference = FirebaseDatabase.getInstance().getReference("admins").child(phone.getText().toString().trim());

            reference.setValue(hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    name.setText("");
                    phone.setText("");
                    address.setText("");
                    password.setText("");
                    email.setText("");
                    dialog2.dismiss();
                    Toast.makeText(getBaseContext(), "تم تسجيل بيانات الادمن بنجاح !", Toast.LENGTH_SHORT).show();

                } else {
                    name.setText("");
                    phone.setText("");
                    address.setText("");
                    password.setText("");
                    email.setText("");
                    dialog2.dismiss();

                    Toast.makeText(getBaseContext(), "لم يتم تسجيل الادمن رجاء تحقق من ادخال بيانات صحيحه !", Toast.LENGTH_SHORT).show();

                }
            });

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriprofileImage = data.getData();
            if (uriprofileImage!=null){
                UploadImageToFireBaseStorage();
            }
        }
    }

    private void UploadImageToFireBaseStorage() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriprofileImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(bitmap);
        progressBar.setVisibility(View.VISIBLE);
        StorageReference profileimageref = FirebaseStorage.getInstance().getReference("images/" + System.currentTimeMillis() + ".jpg");
        if (uriprofileImage != null) {
            profileimageref.putFile(uriprofileImage).addOnSuccessListener(taskSnapshot -> {

                Task<Uri> imageUri = taskSnapshot.getStorage().getDownloadUrl();
                imageUri.addOnSuccessListener(uri -> {
                    profileImageUrl = uri.toString();
                    progressBar.setVisibility(View.GONE);
                });
            }).addOnFailureListener(e -> Toast.makeText(adminprofile.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    public void change(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select  profile image !"), CHOOSE_IMAGE);

    }
}
