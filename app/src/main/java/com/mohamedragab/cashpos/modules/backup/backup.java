package com.mohamedragab.cashpos.modules.backup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class backup extends AppCompatActivity {
    StorageReference storageReference;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        storageReference = FirebaseStorage.getInstance().getReference();
        progress = (ProgressBar) findViewById(R.id.progress);
        zipFolder(Environment.getExternalStorageDirectory() + "/cashpos/database", Environment.getExternalStorageDirectory() + "/cashpos/database.zip");

    }

    public void send_email(View view) {
      /*  final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.emaildialog);
        dialog.show();*/
        openFolder();
    }

    public void openFolder() {

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri screenshotUri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/cashpos/database.zip");
        sharingIntent.setType("image/png");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        startActivity(Intent.createChooser(sharingIntent, "قم بعمل مشاركه لملف قاعدة البيانات "));
    }

    DatabaseReference database_reference;

    public void send_server(View view) {
        if (SheredPrefranseHelper.getUserData(getBaseContext()) != null) {
            database_reference = FirebaseDatabase.getInstance().getReference("Users").child(SheredPrefranseHelper.getUserData(getBaseContext()).getPhone());
        }
        Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/cashpos/" + "database.zip"));

        StorageReference reference;
        if (SheredPrefranseHelper.getUserData(this) == null) {
            reference = storageReference.child("uploads/" + SheredPrefranseHelper.getAdminData(this).getName().trim() + "/" + "database.zip");
        } else {
            reference = storageReference.child("uploads/" + SheredPrefranseHelper.getUserData(this).getName().trim() + "/" + "database.zip");
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

    private static void zipFolder(String inputFolderPath, String outZipPath) {
        try {
            FileOutputStream fos = new FileOutputStream(outZipPath);
            ZipOutputStream zos = new ZipOutputStream(fos);
            File srcFile = new File(inputFolderPath);
            File[] files = srcFile.listFiles();
            Log.d("", "Zip directory: " + srcFile.getName());
            for (int i = 0; i < files.length; i++) {
                Log.d("", "Adding file: " + files[i].getName());
                byte[] buffer = new byte[1024];
                FileInputStream fis = new FileInputStream(files[i]);
                zos.putNextEntry(new ZipEntry(files[i].getName()));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
        } catch (IOException ioe) {
            Log.e("", ioe.getMessage());
        }
    }

    public void download_server(View view) {
        if (SheredPrefranseHelper.getUserData(this) == null) {
            Toast.makeText(getBaseContext(), SheredPrefranseHelper.getAdminData(getBaseContext()).getSupervisor() + "اتصل بالادمن في حالة فقدان البيانات الخاصه بكم ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), SheredPrefranseHelper.getUserData(getBaseContext()).getAdminid() + "اتصل بالادمن في حالة فقدان البيانات الخاصه بكم ", Toast.LENGTH_SHORT).show();
        }
    }

    public void go_home(View view) {
        onBackPressed();
    }
}
