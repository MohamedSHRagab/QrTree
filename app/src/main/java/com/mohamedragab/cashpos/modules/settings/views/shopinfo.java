package com.mohamedragab.cashpos.modules.settings.views;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.modules.settings.models.shopInfo;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class shopinfo extends AppCompatActivity {
    EditText shopName, Phone, Address, notification;
    SeekBar Title_font, normal_font;
    CheckBox bold;
    TextView hint1, hint2;
    private static final int CAMERA_REQUEST = 100;
    CircleImageView logo;
    private static final int CHOOSE_IMAGE = 101;
    shopInfo info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopinfo);

        shopName = (EditText) findViewById(R.id.name);
        logo = (CircleImageView) findViewById(R.id.logo);
        Phone = (EditText) findViewById(R.id.phone);
        Address = (EditText) findViewById(R.id.address);
        notification = (EditText) findViewById(R.id.notification);
        Title_font = (SeekBar) findViewById(R.id.title);
        normal_font = (SeekBar) findViewById(R.id.normal);
        bold = (CheckBox) findViewById(R.id.bold);
        hint1 = (TextView) findViewById(R.id.hint1);
        hint2 = (TextView) findViewById(R.id.hint2);

         info = SheredPrefranseHelper.getshopData(this);
        if (info != null) {
            shopName.setText(info.getShopName() + "");
            Phone.setText(info.getPhone() + "");
            Address.setText(info.getShopaddress() + "");
            notification.setText(info.getNotification() + "");
            Title_font.setProgress(info.getOne());
            normal_font.setProgress(info.getTwo());
            byte[] byteArray = info.getImage();
            if (byteArray != null) {
                logo.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
            } else {
                logo.setImageDrawable(getResources().getDrawable(R.drawable.logo));
            }
            if (info.getBold() != null) {
                bold.setChecked(info.getBold());
            } else {
                bold.setChecked(false);
            }
            hint1.setText(" حجم الخط في الفاتوره " + info.getTwo());
            hint2.setText(" حجم خط اسم الشركه في الفاتوره " + info.getOne());
        } else {
            hint1.setText("حجم الخط في الفاتوره 30");
            hint2.setText("حجم خط اسم الشركة في الفاتوره 50");
        }
        Title_font.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hint2.setText(" حجم خط اسم الشركة في الفاتوره " + progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        normal_font.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hint1.setText(" حجم الخط في الفاتوره " + progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void go_home(View view) {
        onBackPressed();
    }

    public void saveshopinfo(View view) {
        if (info==null){
            info=new shopInfo();
        }
        info.setShopName(shopName.getText().toString().trim() + "");
        info.setPhone(Phone.getText().toString().trim() + "");
        info.setShopaddress(Address.getText().toString().trim() + "");
        info.setNotification(notification.getText().toString().trim() + "");
        info.setOne(Title_font.getProgress());
        info.setTwo(normal_font.getProgress());
        info.setBold(bold.isChecked());

        SheredPrefranseHelper.addshopData(this, info);

    }

    public void captureimage(View view) {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.choosecameradialog);

        info=null;
        logo.setImageBitmap(null);


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
            if (info == null) {
                info = new shopInfo();
            }
            byte[] byteArray = stream.toByteArray();
            info.setImage(byteArray);
            logo.setImageBitmap(theImage);
        }
        if (resultCode == RESULT_OK && requestCode == CHOOSE_IMAGE && data != null) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Bitmap theImage = selectedImage;
                int size = theImage.getByteCount();
                if (size > 10000000) {
                    Toast.makeText(getBaseContext(), "حجم الصوره كبير جدا  !", Toast.LENGTH_SHORT).show();
                } else {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    theImage.compress(Bitmap.CompressFormat.PNG, 50, stream);
                    if (info == null) {
                        info = new shopInfo();
                    }
                    byte[] byteArray = stream.toByteArray();
                    info.setImage(byteArray);
                    logo.setImageBitmap(theImage);


                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
