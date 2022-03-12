package com.mohamedragab.cashpos.modules.omlatransactions.views;

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
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.AppConfig;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.home.MainActivity;
import com.mohamedragab.cashpos.modules.omla.models.omla;
import com.mohamedragab.cashpos.modules.omla.views.customers;
import com.mohamedragab.cashpos.modules.omlatransactions.models.omlatransaction;
import com.mohamedragab.cashpos.modules.omlatransactions.wedgits.omlatransdetailsAdapter;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.settings.models.shopInfo;
import com.mohamedragab.cashpos.utils.Round;
import com.zxy.tiny.Tiny;

import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.UiExecute;
import net.posprinter.utils.BitmapToByteData;
import net.posprinter.utils.DataForSendToPrinterPos80;
import net.posprinter.utils.PosPrinterDev;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class omlatransactions extends AppCompatActivity {
    TextView name;
    List<omlatransaction> omlatransactionList;
    ListView omlatransactionListView;
    String value;
    omlatransdetailsAdapter omlatransdetailsAdapter;
    DataBaseHelper db;
    TextView totalnotpaid;
    ImageView printer_status;
    Receiver netReciever;
    LinearLayout container;
    public static String reciptdate="";

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
        setContentView(R.layout.activity_omlatransactions);
        printer_status = (ImageView) findViewById(R.id.printer);
        container = (LinearLayout) findViewById(R.id.container);
        if (MainActivity.ISCONNECT) {
            printer_status.setImageResource(R.drawable.printerconnected);
        } else {
            printer_status.setImageResource(R.drawable.printeroffline);
        }
        db = new DataBaseHelper(getBaseContext());

        totalnotpaid = (TextView) findViewById(R.id.totalnotpaid);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("key");
        }

        name = (TextView) findViewById(R.id.name);
        name.setText(value + "");

        omlatransactionListView = (ListView) findViewById(R.id.list_transactions);
        Cursor result = db.getomla(value);
        while (result.moveToNext()) {
            totalnotpaid.setText("اجمالي المتبقي :" + Round.round(result.getDouble(6), 3) + SheredPrefranseHelper.getmoney_type(this));
        }


        omlatransactionList = new ArrayList<>();

        omlatransdetailsAdapter = new omlatransdetailsAdapter(omlatransactions.this, omlatransactionList);

        Cursor res = db.gettransaction(value);

        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                omlatransaction omlatransaction = new omlatransaction();

                omlatransaction.setId(res.getInt(0));
                omlatransaction.setName(res.getString(1));
                omlatransaction.setValue(res.getDouble(3));
                omlatransaction.setProcess(res.getString(2));
                omlatransaction.setNotpaid(res.getDouble(6));
                omlatransaction.setDate(res.getString(4));
                omlatransaction.setInvoiceId(res.getString(5));

                omlatransactionList.add(omlatransaction);

            }
            omlatransdetailsAdapter.setomlatransdetailsAdapter(omlatransactionList);
            omlatransactionListView.setAdapter(omlatransdetailsAdapter);

        } else {
            Toast.makeText(getBaseContext(), "لا يوجد تعاملات حاليا !", Toast.LENGTH_SHORT).show();
            omlatransactionList.clear();
            omlatransdetailsAdapter.setomlatransdetailsAdapter(omlatransactionList);
            omlatransactionListView.setAdapter(omlatransdetailsAdapter);

        }

    }

    public void go_omla(View view) {
        onBackPressed();
    }

    public void go_pay(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.addmoneydialog);

        final EditText money_et = (EditText) dialog.findViewById(R.id.value);


        TextView pay = (TextView) dialog.findViewById(R.id.pay);
        pay.setOnClickListener(v -> {

            omlatransaction omlatransaction = new omlatransaction();
            Date c = Calendar.getInstance().getTime();

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            String formattedDate = df.format(c);

            SimpleDateFormat df2 = new SimpleDateFormat("ddMMyyyyhhmmss", Locale.US);
            String formattedDate2 = df2.format(c);
            omlatransaction.setInvoiceId("-");
            omlatransaction.setDate(formattedDate2);
            omlatransaction.setName(name.getText().toString());
            omlatransaction.setNotpaid(0);
            omlatransaction.setProcess("دفع");
            omlatransaction.setValue(Double.parseDouble(money_et.getText().toString()));


            if (db.insert_date(omlatransaction)) {
                Cursor res = db.getomla(name.getText().toString());

                if (res != null && res.getCount() > 0) {
                    while (res.moveToNext()) {
                        omla omla = new omla();
                        // Log.d("test","id "+res.getInt(0));
                        omla.setId(res.getInt(0));
                        omla.setName(res.getString(1));
                        double newsouldpayvalue = res.getDouble(6) - Double.parseDouble(money_et.getText().toString());
                        omla.setPaymoney(Round.round(newsouldpayvalue, 3));
                        omla.setHasmoney(res.getDouble(5));
                        omla.setAddress(res.getString(2));
                        omla.setPhone(res.getString(3));
                        omla.setNotes(res.getString(4));
                        omla.setMaxnotpaid(res.getDouble(7));

                        if (db.updateData(omla)) {

                            com.mohamedragab.cashpos.modules.moneybox.models.money money = new com.mohamedragab.cashpos.modules.moneybox.models.money();
                            money.setDate(formattedDate);
                            money.setNotes("دفع مبلغ العميل :" + name.getText().toString().trim());
                            money.setValue(Round.round(Double.parseDouble(money_et.getText().toString()), 3));

                            final Cursor res3 = db.getallTransactions();
                            double total = 0;
                            if (res3 != null && res3.getCount() > 0) {
                                while (res3.moveToNext()) {
                                    total = res3.getDouble(3);
                                }
                            }
                            money.setTotalbefore(total);
                            double totalAfter = total + Double.parseDouble(money_et.getText().toString());

                            money.setTotalAfter(totalAfter);

                            if (db.insert_date(money)) {
                                final Dialog after_dialog = new Dialog(omlatransactions.this);
                                if (SheredPrefranseHelper.getprinter_type(omlatransactions.this) != null) {
                                    if (SheredPrefranseHelper.getprinter_type(omlatransactions.this).equals("58")) {
                                        after_dialog.setContentView(R.layout.after_pay_dialog2);
                                    } else {
                                        after_dialog.setContentView(R.layout.after_pay_dialog);
                                    }
                                } else {
                                    after_dialog.setContentView(R.layout.after_pay_dialog);
                                }                                // after_dialog.getWindow().setLayout(570, ViewGroup.LayoutParams.WRAP_CONTENT);
                                after_dialog.setCancelable(false);


                                ImageView print = (ImageView) after_dialog.findViewById(R.id.print);
                                ImageView share = (ImageView) after_dialog.findViewById(R.id.share);
                                ImageView close = (ImageView) after_dialog.findViewById(R.id.cancel);
                                LinearLayout invoice_image = (LinearLayout) after_dialog.findViewById(R.id.linear_invoice);

                                TextView date = (TextView) after_dialog.findViewById(R.id.date);
                                date.setText(formattedDate);
                                TextView time = (TextView) after_dialog.findViewById(R.id.time);
                                String currentTime = new SimpleDateFormat("hh:mm:ss a", Locale.US).format(new Date());
                                time.setText(currentTime);

                                TextView client = (TextView) after_dialog.findViewById(R.id.client);
                                client.setText(value + "");
                                TextView cashier = (TextView) after_dialog.findViewById(R.id.cashier);
                                if (SheredPrefranseHelper.getcurrentcashier(omlatransactions.this) != null) {
                                    if (SheredPrefranseHelper.getcurrentcashier(omlatransactions.this).getName() != null) {
                                        cashier.setText("" + SheredPrefranseHelper.getcurrentcashier(omlatransactions.this).getName() + "");
                                    } else {
                                        cashier.setText("" + "لا يوجد");
                                    }
                                } else {
                                    cashier.setText("" + "لا يوجد");
                                }

                                TextView paid_text = (TextView) after_dialog.findViewById(R.id.paid);
                                paid_text.setText(Round.round(Double.parseDouble(money_et.getText().toString()), 3) + "");
                                TextView not_paid_text = (TextView) after_dialog.findViewById(R.id.not_paid);
                                not_paid_text.setText(Round.round(newsouldpayvalue, 3) + "");
                                TextView notification_text = (TextView) after_dialog.findViewById(R.id.notification);
                                TextView address_phone = (TextView) after_dialog.findViewById(R.id.address_phone);
                                TextView shopname = (TextView) after_dialog.findViewById(R.id.shopname);
                                ImageView logo = (ImageView) after_dialog.findViewById(R.id.logo);
                                shopInfo info = SheredPrefranseHelper.getshopData(omlatransactions.this);
                                if (info != null) {
                                    if (!info.getNotification().equals("")) {
                                        notification_text.setText(info.getNotification() + "");
                                    } else {
                                        notification_text.setText("برجاء الاحتفاظ بالفاتوره لاسترجاع المشتريات .");
                                    }
                                    if (!info.getShopName().equals("")) {
                                        shopname.setText(info.getShopName() + "");
                                    } else {
                                        shopname.setText("CashPOS");
                                    }
                                    if (info.getImage() != null) {
                                        logo.setImageBitmap(BitmapFactory.decodeByteArray(info.getImage(), 0, info.getImage().length));
                                    } else {
                                        logo.setVisibility(View.GONE);
                                    }
                                    address_phone.setText(info.getShopaddress() + " - " + info.getPhone());

                                } else {
                                    notification_text.setText("برجاء الاحتفاظ بالفاتوره لاسترجاع المشتريات .");
                                    address_phone.setText("CashPOS لخدمات البيع الذكية ");
                                    shopname.setText("CashPOS");
                                }

                                if (!MainActivity.ISCONNECT) {
                                    print.setImageResource(R.drawable.printeroffline);
                                    print.setEnabled(false);
                                }
                                print.setOnClickListener(v1 -> {
                                    View image_png = invoice_image;
                                    Bitmap returnedBitmap = Bitmap.createBitmap(image_png.getWidth(), image_png.getHeight(), Bitmap.Config.ARGB_8888);
                                    Canvas canvas = new Canvas(returnedBitmap);
                                    Drawable bgDrawable = image_png.getBackground();
                                    if (bgDrawable != null) {
                                        bgDrawable.draw(canvas);
                                    } else {
                                        canvas.drawColor(-1);
                                    }
                                    image_png.draw(canvas);
                                    Bitmap[][] list_pic = splitBitmap(returnedBitmap, 1, (returnedBitmap.getHeight() / 1390) + 1);
                                    for (int i = 0; i < (returnedBitmap.getHeight() / 1390) + 1; i++) {
                                        printImage(list_pic[0][i]);
                                    }
                                });
                                share.setOnClickListener(v1 -> {
                                    View image_png = (View) invoice_image;
                                    Bitmap returnedBitmap = Bitmap.createBitmap(image_png.getWidth(), image_png.getHeight(), Bitmap.Config.ARGB_8888);
                                    Canvas canvas = new Canvas(returnedBitmap);
                                    Drawable bgDrawable = image_png.getBackground();
                                    if (bgDrawable != null) {
                                        bgDrawable.draw(canvas);
                                    } else {
                                        canvas.drawColor(Color.WHITE);
                                    }
                                    image_png.draw(canvas);

                                    try {
                                        File file = new File(Environment.getExternalStorageDirectory() + "/cashpos/agelpaid/", formattedDate2 + ".png");
                                        FileOutputStream out = new FileOutputStream(file);
                                        returnedBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                                        out.close();
                                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                        Uri screenshotUri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/cashpos/agelpaid/" + formattedDate2 + ".png");
                                        sharingIntent.setType("image/png");
                                        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                                        startActivity(Intent.createChooser(sharingIntent, "مشاركة الإيصال"));
                                        Toast.makeText(getBaseContext(), "تم حفظ الايصال في : " + file.toString(), Toast.LENGTH_LONG).show();

                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (Exception e) {
                                        Toast.makeText(getBaseContext(), "الملف غير موجود !", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                close.setOnClickListener(v1 -> {
                                    View image_png = (View) invoice_image;
                                    Bitmap returnedBitmap = Bitmap.createBitmap(image_png.getWidth(), image_png.getHeight(), Bitmap.Config.ARGB_8888);
                                    Canvas canvas = new Canvas(returnedBitmap);
                                    Drawable bgDrawable = image_png.getBackground();
                                    if (bgDrawable != null) {
                                        bgDrawable.draw(canvas);
                                    } else {
                                        canvas.drawColor(Color.WHITE);
                                    }
                                    image_png.draw(canvas);

                                    try {
                                        File file = new File(Environment.getExternalStorageDirectory() + "/cashpos/agelpaid/", formattedDate2 + ".png");
                                        FileOutputStream out = new FileOutputStream(file);
                                        returnedBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                                        out.close();
                                        Toast.makeText(getBaseContext(), " تم دفع " + Round.round(Double.parseDouble(money_et.getText().toString()), 3) + " بنجاح .", Toast.LENGTH_SHORT).show();
                                        db.close();
                                        startActivity(new Intent(omlatransactions.this, customers.class));
                                        this.finish();
                                        after_dialog.dismiss();
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                                after_dialog.show();


                            } else {
                                Toast.makeText(getBaseContext(), "فشل اضافه المبلغ في الخزينه ! ", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "لم يحدث تغيير في حساب العميل !", Toast.LENGTH_SHORT).show();

                        }
                    }

                } else {
                    Toast.makeText(getBaseContext(), "لا يوجد عملاء بهذا الاسم !", Toast.LENGTH_SHORT).show();
                }

                MediaPlayer mp = MediaPlayer.create(getBaseContext(), R.raw.finalsound);
                mp.start();
                dialog.dismiss();
            } else {
                //  Toast.makeText(getBaseContext(), "فشل تسجيل تعاملات العميل", Toast.LENGTH_SHORT).show();

            }


        });

        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();


    }

    public void go_home(View view) {
        onBackPressed();
    }

    private Bitmap b1;//grey-scale bitmap
    private Bitmap b2;//compress bitmap

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
                    0, printBmp, BitmapToByteData.BmpType.Dithering, BitmapToByteData.AlignType.Left, 590));
            return list;
        });
    }


    public void reprint_invoice(View view) {
        final Dialog dialog = new Dialog(omlatransactions.this);
        dialog.setContentView(R.layout.show_image_dialog);
        dialog.setCancelable(false);
        ImageView invoice = (ImageView) dialog.findViewById(R.id.invoice);
        ImageView print = (ImageView) dialog.findViewById(R.id.print);
        if (MainActivity.ISCONNECT) {
            print.setImageResource(R.drawable.printerconnected);
        } else {
            print.setImageResource(R.drawable.printeroffline);
        }
        print.setOnClickListener(view1 -> {
            if (MainActivity.ISCONNECT) {
                File docsFolder = new File(Environment.getExternalStorageDirectory() + "/cashpos/" + "/agelpaid");
                if (!docsFolder.exists()) {
                    docsFolder.mkdir();
                }

                String pdfname = reciptdate + ".png";
                File pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
                Bitmap bitmap = BitmapFactory.decodeFile(pdfFile.getPath());

                try {
                    Bitmap[][] list_pic = splitBitmap(bitmap, 1, (bitmap.getHeight() / 1390) + 1);
                    for (int i = 0; i < (bitmap.getHeight() / 1390) + 1; i++) {
                        printImage(list_pic[0][i]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getBaseContext(), "الطابعه غير متصله !", Toast.LENGTH_SHORT).show();
            }
        });
        ImageView share = (ImageView) dialog.findViewById(R.id.share);
        share.setOnClickListener(view1 -> {
            try {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                Uri screenshotUri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/cashpos/agelpaid/" + reciptdate + ".png");
                sharingIntent.setType("image/png");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                startActivity(Intent.createChooser(sharingIntent, "مشاركة الفاتورة"));

            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "الملف غير موجود !", Toast.LENGTH_SHORT).show();
            }
        });
        ImageView cancel = (ImageView) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(view1 -> {
            dialog.dismiss();
        });
        try {
            String pdfname = Environment.getExternalStorageDirectory() + "/cashpos/agelpaid/" + reciptdate + ".png";
            Bitmap bitmap = BitmapFactory.decodeFile(pdfname);
            invoice.setImageBitmap(bitmap);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "صوره الايصال غير موجوده !", Toast.LENGTH_SHORT).show();
        }
        dialog.show();


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        netReciever = new Receiver();
        // unregisterReceiver(netReciever);

        if (!MainActivity.ISCONNECT) {
            showSnackbar(getString(R.string.con_has_discon));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       /* if (netReciever!=null){
            registerReceiver(netReciever);
        }*/

    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    break;
                case 2:
                    if (PosPrinterDev.PortType.USB != MainActivity.portType) {
                        if (SheredPrefranseHelper.getprinter_type(omlatransactions.this) != null) {
                            if (SheredPrefranseHelper.getprinter_type(omlatransactions.this).equals("58")) {
                                b2 = resizeImage(b2, 335, false);
                            } else {
                                b2 = resizeImage(b2, 590, false);
                            }
                        } else {
                            b2 = resizeImage(b2, 335, false);
                        }
                        printpicCode(b2);
                    } else {

                        if (SheredPrefranseHelper.getprinter_type(omlatransactions.this) != null) {
                            if (SheredPrefranseHelper.getprinter_type(omlatransactions.this).equals("58")) {
                                b2 = resizeImage(b2, 335, false);
                            } else {
                                b2 = resizeImage(b2, 590, false);
                            }
                        } else {
                            b2 = resizeImage(b2, 335, false);
                        }
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

    public static Bitmap resizeImage(Bitmap bitmap, int w, boolean ischecked) {

        Bitmap BitmapOrg = bitmap;
        Bitmap resizedBitmap = null;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        if (width <= w) {
            return bitmap;
        }
        if (!ischecked) {
            int newWidth = w;
            // int newHeight = height * w / width;
            int newHeight = height;

            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;

            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // if you want to rotate the Bitmap
            // matrix.postRotate(45);
            resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                    height, matrix, true);
        } else {
            resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, w, height);
        }

        return resizedBitmap;
    }

    private void printBarcode(String data) {
        MainActivity.binder.writeDataByYouself(new UiExecute() {
            @Override
            public void onsucess() {

            }

            @Override
            public void onfailed() {

            }
        }, new ProcessData() {
            @Override
            public List<byte[]> processDataBeforeSend() {
                List<byte[]> list = new ArrayList<byte[]>();
                //initialize the printer
                list.add(DataForSendToPrinterPos80.initializePrinter());
                //select alignment
                list.add(DataForSendToPrinterPos80.selectAlignment(1));
                //select HRI position
                list.add(DataForSendToPrinterPos80.selectHRICharacterPrintPosition(02));
                //set the width
                list.add(DataForSendToPrinterPos80.setBarcodeWidth(2));
                //set the height ,usually 162
                list.add(DataForSendToPrinterPos80.setBarcodeHeight(100));
                //print barcode ,attention,there are two method for barcode.
                //different barcode type,please refer to the programming manual
                //UPC-A
                list.add(DataForSendToPrinterPos80.printBarcode(69, 14, data));

                // list.add(DataForSendToPrinterPos80.printAndFeedLine());

                return list;
            }
        });
    }

    public Bitmap[][] splitBitmap(Bitmap bitmap, int xCount, int yCount) {
        Bitmap[][] bitmaps = (Bitmap[][]) Array.newInstance(Bitmap.class, new int[]{xCount, yCount});
        int width = bitmap.getWidth() / xCount;
        int height = bitmap.getHeight() / yCount;
        for (int x = 0; x < xCount; x++) {
            for (int y = 0; y < yCount; y++) {
                bitmaps[x][y] = Bitmap.createBitmap(bitmap, x * width, y * height, width, height);
            }
        }
        return bitmaps;
    }
}
