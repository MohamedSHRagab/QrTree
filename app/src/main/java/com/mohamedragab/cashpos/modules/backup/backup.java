package com.mohamedragab.cashpos.modules.backup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.login.models.User;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class backup extends AppCompatActivity {
    StorageReference storageReference;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        storageReference = FirebaseStorage.getInstance().getReference();
        progress = (ProgressBar) findViewById(R.id.progress);

    }

    public void send_email(View view) {
      /*  final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.emaildialog);
        dialog.show();*/
        openFolder();
    }

   /* public void openFolder() {

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri screenshotUri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/cashpos/database/Database.db");
        sharingIntent.setType("image/png");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        startActivity(Intent.createChooser(sharingIntent, "قم بعمل مشاركه لملف قاعدة البيانات "));
    }*/
    private static final String AUTHORITY = "com.mohamedragab.cashpos.fileprovider";

    private void openFolder() {
        String storage = Environment.getExternalStorageDirectory() + "/cashpos/database/Database.db";
        File internalFile = new File(storage);
        Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), AUTHORITY, internalFile);

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("application/pdf");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        startActivity(Intent.createChooser(sharingIntent, "choose"));
    }

    DatabaseReference database_reference;

    public void send_server(View view) {
        if (SheredPrefranseHelper.getUserData(getBaseContext()) != null) {
            database_reference = FirebaseDatabase.getInstance().getReference("Users").child(SheredPrefranseHelper.getUserData(getBaseContext()).getPhone());
        }
        Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/cashpos/database/Database.db"));

        StorageReference reference;
        if (SheredPrefranseHelper.getUserData(this) == null) {
            reference = storageReference.child("uploads/" + SheredPrefranseHelper.getAdminData(this).getName().trim() + "/" + "/Database.db");
        } else {
            reference = storageReference.child("uploads/" + SheredPrefranseHelper.getUserData(this).getName().trim() + "/" + "/Database.db");
        }
        reference.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete()) ;
                    Uri uri1 = uriTask.getResult();
                    if (SheredPrefranseHelper.getUserData(getBaseContext()) != null) {
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy/HH:mm:ss", Locale.US);
                        String formattedDate2 = df2.format(c);
                        database_reference.child("databaseurl").setValue(uri1.toString()+"").addOnSuccessListener(aVoid -> {
                            database_reference.child("databaseupdate").setValue(formattedDate2).addOnSuccessListener(aVoid1 -> {
                                Toast.makeText(getBaseContext(), "تم الاحتفاظ بالنسخه الاحتياطيه", Toast.LENGTH_LONG).show();
                                progress.setVisibility(View.GONE);
                            });
                        });
                    }
                }).addOnProgressListener(taskSnapshot -> {
            progress.setVisibility(View.VISIBLE);

        }).addOnFailureListener(e -> {
            Toast.makeText(getBaseContext(), "فشل تسجيل النسخه الاحتياطيه تأكد من اتصالك بالانترنت !", Toast.LENGTH_LONG).show();
            progress.setVisibility(View.GONE);

        });
    }


    public void download_server(View view) {

            AlertDialog.Builder dialog2 = new AlertDialog.Builder(backup.this);
            dialog2.setCancelable(false);
            dialog2.setTitle("تاكيد تنزيل اخر قاعده بيانات علي السيرفر  !");
            dialog2.setPositiveButton("تنزيل ", (dialog12, id) -> {
                DatabaseReference reference;
                progress.setVisibility(View.VISIBLE);
                reference = FirebaseDatabase.getInstance().getReference("Users").child(SheredPrefranseHelper.getUserData(backup.this).getPhone());

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final User user = dataSnapshot.getValue(User.class);

                        progress.setVisibility(View.GONE);
                        assert user != null;
                        if (user.getDatabaseupdate()!= null) {
                            downloadfile(user.getDatabaseurl() + "");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progress.setVisibility(View.GONE);
                    }
                });

            })
                    .setNegativeButton("الغاء ", (dialog1, which) -> {

                    });

            final AlertDialog alert = dialog2.create();
            alert.show();


    }


    Boolean download_statue = false;

    private Boolean downloadfile(String url) {
       /* if (new File(Environment.getExternalStorageDirectory() + "/cashpos/database/Database.db").delete()){

        }
        if (new File(Environment.getExternalStorageDirectory() + "/cashpos/database/Database.db-journal").delete()){

        }
        */
        ProgressDialog progressdialog = new ProgressDialog(backup.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(url);
        final File rootPath = new File(Environment.getExternalStorageDirectory(), "cashpos/database");

        if (!rootPath.exists()) {
            rootPath.mkdirs();
        }

        final File localFile = new File(rootPath, "/Database.db");

        storageRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {

            // unpackZip(Environment.getExternalStorageDirectory() + "/cashpos/database/");
            progressdialog.dismiss();
            //Toast.makeText(backup.this, "تم تحميل قاعده البيانات المحل .", Toast.LENGTH_SHORT).show();
            download_statue = true;


        }).addOnFailureListener(exception -> {
            progressdialog.dismiss();

            Toast.makeText(backup.this, "حدثت مشكله في تحميل قاعده البيانات حاول تاكد من اتصالك بالانترنت وحاول مره اخري !", Toast.LENGTH_LONG).show();
        });
        return download_statue;
    }


    public void go_home(View view) {
        onBackPressed();
    }
}
