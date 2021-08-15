package com.mohamedragab.cashpos.modules.store.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.mohamedragab.cashpos.base.DateUtil;
import com.mohamedragab.cashpos.modules.store.models.product;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.AppConfig;
import com.mohamedragab.cashpos.modules.home.MainActivity;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.scanner.scanner;
import com.mohamedragab.cashpos.utils.Round;
import com.zxy.tiny.Tiny;

import net.posprinter.posprinterface.UiExecute;
import net.posprinter.utils.BitmapToByteData;
import net.posprinter.utils.DataForSendToPrinterPos80;
import net.posprinter.utils.PosPrinterDev;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class addproduct extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 100;
    ImageView back;
    ImageView scan;
    static ImageView barcode;
    public EditText currentDate, pro_name_et, pro_description_et, pro_sellprice_et, pro_buyprice_et, pro_quantity_et, sellproduct2_et, sellproduct3_et;//, amount2, amount3;
    private Calendar mcalendar;
    private int day, month, year;
    DataBaseHelper db;
    CircleImageView imageView;
    public static EditText code;
    com.mohamedragab.cashpos.modules.store.models.product product, updated;
    private static final int CHOOSE_IMAGE = 101;
    Spinner category_spinner;//;, measure1_spinner, measure2_spinner, measure3_spinner;
    Bundle bundle;
    Button delete_product;
    TextView barcode_text, barcode_price;
    LinearLayout barcode_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);

        Intent i = getIntent();
        bundle = i.getExtras();


        back = (ImageView) findViewById(R.id.back);
        scan = (ImageView) findViewById(R.id.scan);
        barcode = (ImageView) findViewById(R.id.barcode);
        barcode_text = (TextView) findViewById(R.id.barcodetext);
        barcode_price = (TextView) findViewById(R.id.barcodeprice);
        barcode_image = (LinearLayout) findViewById(R.id.barcodeimage);

        imageView = (CircleImageView) findViewById(R.id.image);
        category_spinner = (Spinner) findViewById(R.id.category_spinner);
        // measure1_spinner = (Spinner) findViewById(R.id.measure);
        // measure2_spinner = (Spinner) findViewById(R.id.measure2);
        // measure3_spinner = (Spinner) findViewById(R.id.measure3);
        sellproduct2_et = (EditText) findViewById(R.id.sellprice2);
        sellproduct3_et = (EditText) findViewById(R.id.sellprice3);
        //  amount2 = (EditText) findViewById(R.id.amount2);
        //  amount3 = (EditText) findViewById(R.id.amount3);
        delete_product = (Button) findViewById(R.id.delete_product);

        db = new DataBaseHelper(this);

        final Cursor res2 = db.getallcategories();
        String cat_names[] = new String[res2.getCount() + 1];
        if (res2.getCount() == 0) {
            cat_names[0] = "تصنيف0";
        } else if (res2 != null && res2.getCount() > 0) {
            cat_names = new String[res2.getCount() + 1];
            int index = 1;
            cat_names[0] = "تصنيف0";
            while (res2.moveToNext()) {
                cat_names[index] = res2.getString(0);
                index++;
            }

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, cat_names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(adapter);

     /*   final Cursor res3 = db.getallmeasurements();
        String measurements_names[] = new String[0];
        if (res3 != null && res3.getCount() > 0) {
            measurements_names = new String[res3.getCount() + 1];
            int index = 1;
            measurements_names[0] = "قياس0";
            while (res3.moveToNext()) {
                measurements_names[index] = res3.getString(0);
                index++;
            }

        }
     /*   ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, measurements_names);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        measure1_spinner.setAdapter(adapter2);
        measure2_spinner.setAdapter(adapter2);
        measure3_spinner.setAdapter(adapter2);*/

        currentDate = (EditText) findViewById(R.id.expiredate);
        currentDate.setOnClickListener(view -> DateDialog());
        code = (EditText) findViewById(R.id.code);

        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (code.getText().toString().trim().equals("")) {
                    barcode.setImageBitmap(null);
                } else {
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(code.getText().toString(), BarcodeFormat.CODE_128, 656, 184);
                        Bitmap bitmap = Bitmap.createBitmap(656, 184, Bitmap.Config.RGB_565);
                        for (int f = 0; f < 656; f++) {
                            for (int j = 0; j < 184; j++) {
                                bitmap.setPixel(f, j, bitMatrix.get(f, j) ? Color.BLACK : Color.WHITE);
                            }
                        }
                        barcode.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        code.setText(db.getproductsnextid() + "");

        back.setOnClickListener(view -> {
            startActivity(new Intent(addproduct.this, store.class));
            finish();
        });
        scan.setOnClickListener(view -> {
            String value = "add";
            Intent i2 = new Intent(addproduct.this, scanner.class);
            i2.putExtra("activity", value);
            startActivity(i2);


        });
        pro_name_et = (EditText) findViewById(R.id.name);
        pro_name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                barcode_text.setText(pro_name_et.getText().toString() + "");
            }
        });

        pro_description_et = (EditText) findViewById(R.id.description);
        pro_sellprice_et = (EditText) findViewById(R.id.sellprice);
        pro_buyprice_et = (EditText) findViewById(R.id.buyprice);
        pro_quantity_et = (EditText) findViewById(R.id.quantity);
        LinearLayout equation = (LinearLayout) findViewById(R.id.equation);
        CheckBox check_equation = (CheckBox) findViewById(R.id.check_equation);
        check_equation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                equation.setVisibility(View.VISIBLE);
            } else {
                equation.setVisibility(View.GONE);
            }
        });
        EditText tall1 = (EditText) findViewById(R.id.tall1);
        EditText tall2 = (EditText) findViewById(R.id.tall2);
        EditText quan = (EditText) findViewById(R.id.quan);
        tall1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Double tall1_val;
                if (tall1.getText().toString().trim().equals("")) {
                    tall1_val = 1.0;
                } else {
                    tall1_val = Double.parseDouble(tall1.getText().toString().trim());
                }
                Double tall2_val;
                if (tall2.getText().toString().trim().equals("")) {
                    tall2_val = 1.0;
                } else {
                    tall2_val = Double.parseDouble(tall2.getText().toString().trim());
                }
                Double quan_val;
                if (quan.getText().toString().trim().equals("")) {
                    quan_val = 1.0;
                } else {
                    quan_val = Double.parseDouble(quan.getText().toString().trim());
                }
                pro_quantity_et.setText((quan_val * tall1_val * tall2_val) + "");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tall2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Double tall1_val;
                if (tall1.getText().toString().trim().equals("")) {
                    tall1_val = 1.0;
                } else {
                    tall1_val = Double.parseDouble(tall1.getText().toString().trim());
                }
                Double tall2_val;
                if (tall2.getText().toString().trim().equals("")) {
                    tall2_val = 1.0;
                } else {
                    tall2_val = Double.parseDouble(tall2.getText().toString().trim());
                }
                Double quan_val;
                if (quan.getText().toString().trim().equals("")) {
                    quan_val = 1.0;
                } else {
                    quan_val = Double.parseDouble(quan.getText().toString().trim());
                }
                pro_quantity_et.setText((quan_val * tall1_val * tall2_val) + "");

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        quan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Double tall1_val;
                if (tall1.getText().toString().trim().equals("")) {
                    tall1_val = 1.0;
                } else {
                    tall1_val = Double.parseDouble(tall1.getText().toString().trim());
                }
                Double tall2_val;
                if (tall2.getText().toString().trim().equals("")) {
                    tall2_val = 1.0;
                } else {
                    tall2_val = Double.parseDouble(tall2.getText().toString().trim());
                }
                Double quan_val;
                if (quan.getText().toString().trim().equals("")) {
                    quan_val = 1.0;
                } else {
                    quan_val = Double.parseDouble(quan.getText().toString().trim());
                }
                pro_quantity_et.setText((quan_val * tall1_val * tall2_val) + "");

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        pro_sellprice_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sellproduct2_et.setText("");
                sellproduct3_et.setText("");
                sellproduct2_et.setText("" + pro_sellprice_et.getText());
                sellproduct3_et.setText("" + pro_sellprice_et.getText());
                barcode_price.setText("$" + pro_sellprice_et.getText());

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        if (bundle != null) {
            delete_product.setVisibility(View.VISIBLE);
            updated = (product) bundle.getSerializable("product");
            pro_name_et.setText(updated.getName());
            pro_sellprice_et.setText(updated.getSellprice() + "");
            pro_buyprice_et.setText(updated.getBuyprice() + "");
            pro_description_et.setText(updated.getDescription());
            pro_quantity_et.setText(updated.getQuantity() + "");
            sellproduct3_et.setText(updated.getSellprice3() + "");
            sellproduct2_et.setText(updated.getSellprice2() + "");
            code.setText(updated.getCode_id() + "");
            currentDate.setText(updated.getExpiredate());
            //   amount2.setText(updated.getFactor2() + "");
            //   amount3.setText(updated.getFactor3() + "");


          /*  if (updated.getMeasure1() != null) {
                int spinnerPosition = adapter2.getPosition(updated.getMeasure1().trim());
                measure1_spinner.setSelection(spinnerPosition);
            }
            if (updated.getMeasure2() != null) {
                int spinnerPosition = adapter2.getPosition(updated.getMeasure2().trim());
                measure2_spinner.setSelection(spinnerPosition);
            }
            if (updated.getMeasure3() != null) {
                int spinnerPosition = adapter2.getPosition(updated.getMeasure3().trim());
                measure3_spinner.setSelection(spinnerPosition);
            }*/
            if (updated.getCategory() != null) {
                int spinnerPosition = adapter.getPosition(updated.getCategory());
                category_spinner.setSelection(spinnerPosition);
            }
            if (updated.getImage() != null) {
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(updated.getImage(), 0, updated.getImage().length));
            }
        }


    }


    public void DateDialog() {
        Locale.setDefault(Locale.US);

        mcalendar = Calendar.getInstance();
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);
        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener listener = (view, year, monthOfYear, dayOfMonth) -> currentDate.setText(DateUtil.getDateOnly(year, monthOfYear, dayOfMonth));
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }


    public void save(View view) {
        if (bundle != null) {
            updated.setCode_id(code.getText().toString().trim());
            updated.setName(pro_name_et.getText().toString().trim());
            updated.setDescription(pro_description_et.getText().toString().trim());
            updated.setBuyprice(Double.parseDouble(pro_buyprice_et.getText().toString().trim()));
            updated.setSellprice(Double.parseDouble(pro_sellprice_et.getText().toString().trim()));
            updated.setQuantity(Double.parseDouble(pro_quantity_et.getText().toString().trim()));
            updated.setExpiredate(currentDate.getText().toString());
            updated.setCategory(category_spinner.getSelectedItem().toString() + "");
           /* updated.setMeasure1(measure1_spinner.getSelectedItem().toString() + "");
            updated.setMeasure2(measure2_spinner.getSelectedItem().toString() + "");
            updated.setMeasure3(measure3_spinner.getSelectedItem().toString() + "");
            updated.setFactor2(Double.parseDouble(amount2.getText().toString() + ""));
            updated.setFactor3(Double.parseDouble(amount3.getText().toString() + ""));*/
            updated.setSellprice2(Double.parseDouble(sellproduct2_et.getText().toString() + ""));
            updated.setSellprice3(Double.parseDouble(sellproduct3_et.getText().toString() + ""));
            if (db.updateData(updated)) {
                MediaPlayer mp = MediaPlayer.create(addproduct.this, R.raw.finalsound);
                mp.start();
                Toast.makeText(getBaseContext(), "تم تعديل المنتج بنجاح", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, viewproducts.class));
                db.close();
                finish();
            } else {
                Toast.makeText(getBaseContext(), "فشل تعديل المنتج تحقق من ادخال كافة البيانات صحيحة !", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (product == null) {
                product = new product();
            }
            if (code.getText().toString().equals("") || pro_name_et.getText().toString().equals("") || pro_buyprice_et.getText().toString().equals("") || pro_sellprice_et.getText().toString().equals("") || pro_buyprice_et.getText().toString().equals("") || pro_quantity_et.getText().toString().equals("")) {
                Toast.makeText(getBaseContext(), "رجاء اكمال كافة البيانات المطلوبه !", Toast.LENGTH_SHORT).show();

            } else {

                Cursor res = db.getallproducts();
                if (res != null && res.getCount() > 0) {
                    while (res.moveToNext()) {
                        if (code.getText().toString().trim().equals(res.getInt(1) + "")) {
                            Toast.makeText(getBaseContext(), "الكود تم استخدامه من قبل في منتج اخر !", Toast.LENGTH_SHORT).show();
                            break;
                        } else if (pro_name_et.getText().toString().trim().equals(res.getString(2))) {
                            Toast.makeText(getBaseContext(), "اسم المنتج مستخدم بالفعل !", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if (res.isLast()) {
                            product.setCode_id(code.getText().toString().trim());
                            product.setName(pro_name_et.getText().toString().trim());
                            product.setDescription(pro_description_et.getText().toString().trim());
                            product.setBuyprice(Round.round(Double.parseDouble(pro_buyprice_et.getText().toString().trim()), 3));
                            product.setSellprice(Round.round(Double.parseDouble(pro_sellprice_et.getText().toString().trim()), 3));
                            product.setQuantity(Double.parseDouble(pro_quantity_et.getText().toString().trim()));
                            product.setExpiredate(currentDate.getText().toString());
                            product.setItemid(0);
                            product.setCategory(category_spinner.getSelectedItem().toString() + "");
                           /* product.setMeasure1(measure1_spinner.getSelectedItem().toString() + "");
                            product.setMeasure2(measure2_spinner.getSelectedItem().toString() + "");
                            product.setMeasure3(measure3_spinner.getSelectedItem().toString() + "");
                            product.setFactor2(Double.parseDouble(amount2.getText().toString() + ""));
                            product.setFactor3(Double.parseDouble(amount3.getText().toString() + ""));*/
                            product.setSellprice2(Double.parseDouble(sellproduct2_et.getText().toString() + ""));
                            product.setSellprice3(Double.parseDouble(sellproduct3_et.getText().toString() + ""));

                            if (db.insert_date(product)) {
                                MediaPlayer mp = MediaPlayer.create(addproduct.this, R.raw.finalsound);
                                mp.start();
                                db.close();
                                Toast.makeText(getBaseContext(), "تم تسجيل المنتج بنجاح", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, addproduct.class));
                                finish();
                            } else {
                                //  Toast.makeText(getBaseContext(), "فشل تسجيل المنتج تحقق من ادخال بيانات صحيحه", Toast.LENGTH_SHORT).show();

                            }
                        }

                    }


                } else {
                    product.setCode_id(code.getText().toString().trim());
                    product.setName(pro_name_et.getText().toString().trim());
                    product.setDescription(pro_description_et.getText().toString().trim());
                    product.setBuyprice(Double.parseDouble(pro_buyprice_et.getText().toString().trim()));
                    product.setSellprice(Double.parseDouble(pro_sellprice_et.getText().toString().trim()));
                    product.setQuantity(Double.parseDouble(pro_quantity_et.getText().toString().trim()));
                    product.setExpiredate(currentDate.getText().toString());
                    product.setItemid(0);
                    product.setCategory(category_spinner.getSelectedItem().toString() + "");
                 /*   product.setMeasure1(measure1_spinner.getSelectedItem().toString() + "");
                    product.setMeasure2(measure2_spinner.getSelectedItem().toString() + "");
                    product.setMeasure3(measure3_spinner.getSelectedItem().toString() + "");
                    product.setFactor2(Double.parseDouble(amount2.getText().toString() + ""));
                    product.setFactor3(Double.parseDouble(amount3.getText().toString() + ""));*/
                    product.setSellprice2(Double.parseDouble(sellproduct2_et.getText().toString() + ""));
                    product.setSellprice3(Double.parseDouble(sellproduct3_et.getText().toString() + ""));

                    if (db.insert_date(product)) {
                        MediaPlayer mp = MediaPlayer.create(addproduct.this, R.raw.finalsound);
                        mp.start();
                        Toast.makeText(getBaseContext(), "تم تسجيل المنتج بنجاح", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, addproduct.class));
                        db.close();
                        finish();
                    } else {
                        //Toast.makeText(getBaseContext(), "فشل تسجيل المنتج تحقق من ادخال بيانات صحيحه", Toast.LENGTH_SHORT).show();
                    }
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
            theImage.compress(Bitmap.CompressFormat.PNG, 50, stream);

            byte[] byteArray = stream.toByteArray();
            if (bundle != null) {
                updated.setImage(byteArray);
            } else {
                if (product == null) {
                    product = new product();
                }
                product.setImage(byteArray);
            }
            imageView.setImageBitmap(theImage);
        }


        if (resultCode == RESULT_OK && requestCode == CHOOSE_IMAGE && data != null) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Bitmap theImage = selectedImage;
                int size = theImage.getByteCount();
                if (size > 2000000) {
                    Toast.makeText(getBaseContext(), "حجم الصوره كبير !", Toast.LENGTH_SHORT).show();
                } else {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    theImage.compress(Bitmap.CompressFormat.PNG, 50, stream);

                    byte[] byteArray = stream.toByteArray();
                    if (bundle != null) {
                        updated.setImage(byteArray);
                    } else {
                        if (product == null) {
                            product = new product();
                        }
                        product.setImage(byteArray);
                    }
                    imageView.setImageBitmap(theImage);


                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    LinearLayout container;
    private Bitmap b1;//grey-scale bitmap
    private Bitmap b2;//compress bitmap


    private void printpicCode(final Bitmap printBmp) {


        MainActivity.binder.writeDataByYouself(new UiExecute() {
            @Override
            public void onsucess() {

            }

            @Override
            public void onfailed() {
                showSnackbar("failed");
            }
        }, () -> {

            List<byte[]> list = new ArrayList<byte[]>();
            list.add(DataForSendToPrinterPos80.initializePrinter());
            list.add(DataForSendToPrinterPos80.printRasterBmp(
                    0, printBmp, BitmapToByteData.BmpType.Dithering, BitmapToByteData.AlignType.Left, 576));
            return list;
        });


    }

    Receiver netReciever;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    break;
                case 2:
                    if (PosPrinterDev.PortType.USB != MainActivity.portType) {
                        printpicCode(b2);
                    } else {
                        AppConfig.printUSBbitamp(b2);
                    }
                    break;
                case 3://disconnect
                    break;
                case 4:
                    break;


            }

        }
    };


    private void printImage(Bitmap image) {
        Bitmap bitm = image;
        netReciever = new Receiver();
        registerReceiver(netReciever, new IntentFilter(MainActivity.DISCONNECT));

        if (!MainActivity.ISCONNECT) {
            showSnackbar(getString(R.string.con_has_discon));
        }

        b1 = Bitmap.createBitmap(bitm.getWidth(), bitm.getHeight(), bitm.getConfig());
        Canvas canvas = new Canvas(b1);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitm, 0, 0, null);

        Message message = new Message();
        message.what = 1;
        handler.handleMessage(message);

        Tiny.BitmapCompressOptions options = new Tiny.BitmapCompressOptions();
        Tiny.getInstance().source(b1).asBitmap().withOptions(options).compress((isSuccess, bitmap) -> {
            if (isSuccess) {
                b2 = bitmap;

                Message message1 = new Message();
                message1.what = 2;
                handler.handleMessage(message1);
            }
        });
        //init the tiny (use to compress the bitmap).
        Tiny.getInstance().init(getApplication());
    }


    public void go_delete_product(View view) {
        db.deleteProduct(updated.getId() + "");
        db.close();
        onBackPressed();
    }

    public void print_barcode(View view) {
        if (MainActivity.ISCONNECT) {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.addbarcodedialog);
            dialog.setCancelable(false);
            EditText value = (EditText) dialog.findViewById(R.id.value);
            TextView print = (TextView) dialog.findViewById(R.id.print);
            print.setOnClickListener(v -> {
                if (value.getText().toString().trim().equals("")) {
                    Toast.makeText(getBaseContext(), "اختر عدد صحيح !", Toast.LENGTH_SHORT).show();
                } else {

                    for (int i = 0; i < Integer.parseInt(value.getText().toString()); i++) {
                        View image_png = barcode_image;
                        Bitmap returnedBitmap = Bitmap.createBitmap(image_png.getWidth(), image_png.getHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(returnedBitmap);
                        Drawable bgDrawable = image_png.getBackground();
                        if (bgDrawable != null) {
                            bgDrawable.draw(canvas);
                        } else {
                            canvas.drawColor(-1);
                        }
                        image_png.draw(canvas);
                        printImage(returnedBitmap);
                    }
                }
            });
            Button cancel = (Button) dialog.findViewById(R.id.cancel);
            cancel.setOnClickListener(v -> dialog.dismiss());

            dialog.show();

        } else {
            Toast.makeText(getBaseContext(), "الطابعه غير متصله !", Toast.LENGTH_SHORT).show();
        }


    }

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MainActivity.DISCONNECT)) {
                Message message = new Message();
                message.what = 4;
                handler.handleMessage(message);
            }
        }

    }

    private void showSnackbar(String showstring) {
        Snackbar.make(container, showstring, Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(R.color.button_unable)).show();
    }

}



