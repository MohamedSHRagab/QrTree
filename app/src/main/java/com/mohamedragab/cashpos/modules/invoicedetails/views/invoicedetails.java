package com.mohamedragab.cashpos.modules.invoicedetails.views;

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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.AppConfig;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.home.MainActivity;
import com.mohamedragab.cashpos.modules.invoice.views.invoiceView;
import com.mohamedragab.cashpos.modules.invoicedetails.wedgits.invoicedetailsAdapter;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.sales.models.sellproduct;
import com.zxy.tiny.Tiny;

import net.posprinter.posprinterface.UiExecute;
import net.posprinter.utils.BitmapToByteData;
import net.posprinter.utils.DataForSendToPrinterPos80;
import net.posprinter.utils.PosPrinterDev;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class invoicedetails extends AppCompatActivity {

    TextView invoiceid;
    DataBaseHelper db;
    List<sellproduct> productsList;
    ListView productsListView;
    String value,client_name;
    invoicedetailsAdapter invoicedetailsAdapter;
    FloatingActionButton print_icon;
    ImageView printer_status;
    public static List<Bitmap> imagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoicedetails);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("key");
            client_name = extras.getString("client");
        }
        printer_status = (ImageView) findViewById(R.id.printer);
        if (MainActivity.ISCONNECT) {
            printer_status.setImageResource(R.drawable.printerconnected);
        } else {
            printer_status.setImageResource(R.drawable.printeroffline);
        }
        invoiceid = (TextView) findViewById(R.id.invoice_id);
        invoiceid.setText(value + "");
        productsListView = (ListView) findViewById(R.id.list_products);
        print_icon = (FloatingActionButton) findViewById(R.id.print_icon);

        productsList = new ArrayList<>();
        db = new DataBaseHelper(getBaseContext());

        invoicedetailsAdapter = new invoicedetailsAdapter(invoicedetails.this, productsList);

        Cursor res = db.getsellproduct(value);

        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                sellproduct sellproduct = new sellproduct();

                sellproduct.setName(res.getString(4));
                sellproduct.setInvoice_id(res.getString(1));
                sellproduct.setQuantity(res.getDouble(2));
                sellproduct.setSellprice(res.getDouble(6));
                sellproduct.setId(res.getInt(0));

                productsList.add(sellproduct);

            }
            invoicedetailsAdapter.setinvoicedetailsAdapter(productsList,client_name);
            productsListView.setAdapter(invoicedetailsAdapter);

        } else {
            Toast.makeText(getBaseContext(), "لا يوجد منتجات حاليا !", Toast.LENGTH_SHORT).show();
            productsList.clear();
            invoicedetailsAdapter.setinvoicedetailsAdapter(productsList,client_name);
            productsListView.setAdapter(invoicedetailsAdapter);

        }

    }

    public void go_invoices(View view) {
        startActivity(new Intent(getBaseContext(), invoiceView.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(invoicedetails.this, invoiceView.class));
    }

    public void reprint_invoice(View view) {
      try {
          Intent sharingIntent = new Intent(Intent.ACTION_SEND);
          Uri screenshotUri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/cashpos/invoices/"+invoiceid.getText().toString()+".png");
          sharingIntent.setType("image/png");
          sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
          startActivity(Intent.createChooser(sharingIntent, "مشاركة الفاتورة"));

      }catch (Exception e){
          Toast.makeText(getBaseContext(), "الملف غير موجود !", Toast.LENGTH_SHORT).show();
      }

    }

    public void reshare_invoice(View view) {
      if (MainActivity.ISCONNECT){
          File docsFolder = new File(Environment.getExternalStorageDirectory() + "/cashpos/" + "/invoices");
          if (!docsFolder.exists()) {
              docsFolder.mkdir();
          }

          String pdfname = invoiceid.getText().toString() + ".png";
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
      }else{
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
        Toast.makeText(getBaseContext(), ""+showstring, Toast.LENGTH_SHORT).show();
    }
    Receiver netReciever;

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
                        if (SheredPrefranseHelper.getprinter_type(invoicedetails.this) != null) {
                            if (SheredPrefranseHelper.getprinter_type(invoicedetails.this).equals("58")) {
                                b2 = resizeImage(b2, 335, false);
                            } else {
                                b2 = resizeImage(b2, 576, false);
                            }
                        } else {
                            b2 = resizeImage(b2, 335, false);
                        }
                        printpicCode(b2);
                    } else {
                        showSnackbar("bimap  " + b2.getWidth() + "  height: " + b2.getHeight());
                        if (SheredPrefranseHelper.getprinter_type(invoicedetails.this) != null) {
                            if (SheredPrefranseHelper.getprinter_type(invoicedetails.this).equals("58")) {
                                b2 = resizeImage(b2, 335, false);
                            } else {
                                b2 = resizeImage(b2, 576, false);
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
            int newHeight = height ;

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

    LinearLayout container;
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
                    0, printBmp, BitmapToByteData.BmpType.Dithering, BitmapToByteData.AlignType.Left, 576));
//                list.add(DataForSendToPrinterPos80.printAndFeedForward(3));

            return list;
        });


    }

}
