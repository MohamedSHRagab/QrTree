package com.mohamedragab.cashpos.modules.kist.views;

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
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
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
import com.mohamedragab.cashpos.base.DateUtil;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.home.MainActivity;
import com.mohamedragab.cashpos.modules.invoicedetails.views.invoicedetails;
import com.mohamedragab.cashpos.modules.kist.adapters.kistAdapter;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.sales.models.Kist;
import com.mohamedragab.cashpos.modules.settings.models.shopInfo;
import com.mohamedragab.cashpos.utils.Round;
import com.zxy.tiny.Tiny;

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
import java.util.stream.Collectors;

/*import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.UiExecute;
import net.posprinter.utils.BitmapToByteData;
import net.posprinter.utils.DataForSendToPrinterPos80;
import net.posprinter.utils.PosPrinterDev;
*/

public class customerKist extends AppCompatActivity {

    TextView invoiceid;
    DataBaseHelper db;
    List<Kist> kistList;
    ListView productsListView;
    String value;
    kistAdapter kistAdapter;
    TextView damenName, damenPhone, date2, paid_kist, notpaid_kist, total_kists;
    ImageView printer_status;
    LinearLayout container;
    String invoice_num = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_kist);
        container = (LinearLayout) findViewById(R.id.container);
        Bundle extras = getIntent().getExtras();
        db = new DataBaseHelper(getBaseContext());

        if (extras != null) {
            value = extras.getString("key");
            String paid = extras.getString("paid");
            if (paid != null) {
                String kestnumber = extras.getString("kestnumber");

             /*   if (MainActivity.ISCONNECT) {
                    AlertDialog.Builder alertdialog = new AlertDialog.Builder(customerKist.this);
                    alertdialog.setCancelable(false);
                    alertdialog.setTitle("طباعة الايصال !");
                    alertdialog.setMessage("تأكد من اتصال الطابعه قبل طباعه الايصال ..");
                    alertdialog.setPositiveButton("طباعه", (dialog12, id) -> {
                        paidmoney paid_money = new paidmoney();
                        List<Bitmap> imagesList = paid_money.setpaidmoneyt(customerKist.this, value, kestnumber, paid + "");
                        Date c2 = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.US);
                        String formattedDate = df.format(c2);
                        printImage(imagesList.get(0));
                    })
                            .setNegativeButton("الغاء ", (dialog1, which) -> {
                                paidmoney paid_money = new paidmoney();
                                paid_money.setpaidmoneyt(getBaseContext(), value, kestnumber, paid + "");

                            });

                    final AlertDialog alert = alertdialog.create();
                    alert.show();
                } else {
                    paidmoney paid_money = new paidmoney();
                    paid_money.setpaidmoneyt(getBaseContext(), value, kestnumber, paid + "");

                }
*/
            }

        }
        printer_status = (ImageView) findViewById(R.id.printer);
        if (MainActivity.ISCONNECT) {
            printer_status.setImageResource(R.drawable.printerconnected);
        } else {
            printer_status.setImageResource(R.drawable.printeroffline);
        }
        invoiceid = (TextView) findViewById(R.id.customername);
        damenName = (TextView) findViewById(R.id.damenname);
        damenPhone = (TextView) findViewById(R.id.damenphone);
        paid_kist = (TextView) findViewById(R.id.paid_kist);
        notpaid_kist = (TextView) findViewById(R.id.notpaid_kist);
        total_kists = (TextView) findViewById(R.id.total_kists);

        invoiceid.setText(value + "");
        productsListView = (ListView) findViewById(R.id.list_kist);

        kistList = new ArrayList<>();
        db = new DataBaseHelper(getBaseContext());

        kistAdapter = new kistAdapter(customerKist.this, kistList);

        Cursor res = db.getkestbyclient(value);

        int paid_kist_val = 0, notpaid_kist_val = 0;
        List<String> invoices = new ArrayList<>();

        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                Kist kist = new Kist();

                kist.setId(res.getInt(0));
                kist.setInvoice_id(res.getString(1));
                kist.setDescription(res.getString(2));
                kist.setDayslimit(res.getInt(3));
                kist.setCollectdate(res.getString(4));
                kist.setKist_value(res.getDouble(5));
                kist.setStatue(res.getString(6));
                kist.setClient_name(res.getString(7));
                kist.setPay_type(res.getString(8));
                kist.setDamen_name(res.getString(9));
                kist.setDamen_phone(res.getString(10));
                kist.setPaydate(res.getString(11));

                if (res.getString(7).equals(value)) {
                    kistList.add(kist);
                    damenName.setText(" الضامن : " + res.getString(9));
                    damenPhone.setText(" محمول : " + res.getString(10));
                    if (kist.getStatue().equals("paid")) {
                        paid_kist_val = paid_kist_val + 1;
                    } else {
                        notpaid_kist_val = notpaid_kist_val + 1;
                    }
                    invoices.add(res.getString(1));

                }

            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                List newlist = invoices.stream().distinct().collect(Collectors.toList());
                for (int i = 0; i < newlist.size(); i++) {
                    invoice_num = newlist.get(i).toString();
                }
            }
            kistAdapter.setKistAdapter(kistList);
            productsListView.setAdapter(kistAdapter);
            paid_kist.setText("مدفوع : " + paid_kist_val + " قسط ");
            notpaid_kist.setText("باقي : " + notpaid_kist_val + " قسط ");
            total_kists.setText("الاجمالي : " + kistList.size() + " قسط ");

        } else {
            Toast.makeText(getBaseContext(), "لا يوجد أقساط حاليا !", Toast.LENGTH_SHORT).show();
            kistList.clear();
            kistAdapter.setKistAdapter(kistList);
            productsListView.setAdapter(kistAdapter);
            paid_kist.setText("مدفوع : " + 0 + " قسط ");
            notpaid_kist.setText("باقي : " + 0 + " قسط ");
            total_kists.setText("الاجمالي : " + 0 + " قسط ");
            total_kists.setText("الفواتير : " + "0");

        }


    }

    public void go_invoices(View view) {
        onBackPressed();
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

        Tiny.getInstance().init(getApplication());
    }

    private Calendar mcalendar;
    private int day, month, year;
    private TextView date;

    public void add_previous_kists(View view) {
        final Dialog after_dialog = new Dialog(customerKist.this);
        after_dialog.setContentView(R.layout.oldkistdialog);
        after_dialog.setCancelable(false);

        EditText money = (EditText) after_dialog.findViewById(R.id.money);
        date = (TextView) after_dialog.findViewById(R.id.date);
        date.setOnClickListener(v -> {
            DateDialog(0);
        });
        Button save = (Button) after_dialog.findViewById(R.id.save);
        save.setOnClickListener(v -> {
            Kist kist = new Kist();
            if (!money.getText().toString().trim().equals("")) {

                kist.setKist_value(Double.parseDouble(money.getText().toString().trim()));
                kist.setClient_name(value.trim());
                kist.setDayslimit(0);
                kist.setInvoice_id("قسط سابق");
                kist.setStatue("not_paid");
                kist.setDescription("قسط سابق");
                kist.setPay_type(Double.parseDouble(money.getText().toString().trim()) + "");
                kist.setPaydate("");

                if (SheredPrefranseHelper.getcurrentcashier(customerKist.this) != null) {
                    if (SheredPrefranseHelper.getcurrentcashier(customerKist.this).getName() != null) {
                        kist.setDamen_name(SheredPrefranseHelper.getcurrentcashier(customerKist.this).getName());
                    } else {
                        kist.setDamen_name("admin");
                    }
                    if (SheredPrefranseHelper.getcurrentcashier(customerKist.this).getPhone() != null) {
                        kist.setDamen_phone(SheredPrefranseHelper.getcurrentcashier(customerKist.this).getPhone());
                    } else {
                        kist.setDamen_phone("000");
                    }
                } else {
                    kist.setDamen_name("admin");
                    kist.setDamen_phone("000");

                }
                if (!date.getText().toString().trim().equals("")) {
                    kist.setCollectdate(date.getText().toString() + "");
                    if (db.insert_kist_date(kist)) {
                        after_dialog.dismiss();
                        db.close();
                        this.recreate();
                    } else {
                        Toast.makeText(getBaseContext(), "فشل اضافة القسط تأكد من ادخال بيانات صحيحة !", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getBaseContext(), "ادخل التاريخ !", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getBaseContext(), "أدخل المبلغ الصحيح !", Toast.LENGTH_SHORT).show();
            }
        });
        TextView cancel = (TextView) after_dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> {
            after_dialog.dismiss();
        });
        after_dialog.show();

    }

    public void go_invoices_details(View view) {
        if (!invoice_num.equals("")) {
            Intent i = new Intent(customerKist.this, invoicedetails.class);
            i.putExtra("key", invoice_num);
            startActivity(i);
        } else {
            Toast.makeText(getBaseContext(), "لا يوجد رقم فاتورة", Toast.LENGTH_SHORT).show();
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

    public void DateDialog(int val) {
        Locale.setDefault(Locale.US);

        mcalendar = Calendar.getInstance();
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);
        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener listener = null;
        if (val == 0) {
            listener = (view, year, monthOfYear, dayOfMonth) -> date.setText(DateUtil.getDateOnly(year, monthOfYear, dayOfMonth));

        } else {
            listener = (view, year, monthOfYear, dayOfMonth) -> date2.setText(DateUtil.getDateOnly(year, monthOfYear, dayOfMonth));

        }
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();


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

    public void pay_money(View view) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String formattedDate = df.format(c);

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.addmoneydialog_date);
        dialog.setCancelable(false);

        EditText money_value = (EditText) dialog.findViewById(R.id.value);
        date2 = (TextView) dialog.findViewById(R.id.date);
        date2.setText(formattedDate);
        date2.setOnClickListener(v -> {
            DateDialog(1);
        });
        TextView pay_button = (TextView) dialog.findViewById(R.id.pay);
        pay_button.setOnClickListener(v -> {
            Double paid_money = Double.parseDouble(money_value.getText().toString());
            double total_not_paid = 0;
            for (int j = 0; j < kistList.size(); j++) {
                if (!kistList.get(j).getStatue().equals("paid")) {
                    total_not_paid += kistList.get(j).getKist_value();
                }
            }
            double finalTotal_not_paid = total_not_paid;


            if (!money_value.getText().toString().trim().equals("")) {
                if (paid_money > total_not_paid) {
                    Toast.makeText(this, "المبلغ الذي تريد دفعه اكبر من قيمه الاقساط !", Toast.LENGTH_LONG).show();
                } else {
                    for (int i = 0; i < kistList.size(); i++) {
                        if (kistList.get(i).getStatue().equals("paid")) {
                            continue;
                        } else {
                            if (kistList.get(i).getKist_value() < paid_money) {
                                Boolean statue = db.updatekistbyName(kistList.get(i).getId(), date2.getText().toString(), true, kistList.get(i).getKist_value());
                                if (statue) {
                                    paid_money -= kistList.get(i).getKist_value();
                                    continue;
                                }
                            } else if (kistList.get(i).getKist_value() == paid_money) {
                                Boolean statue = db.updatekistbyName(kistList.get(i).getId(), date2.getText().toString(), true, paid_money);
                                break;
                            } else if (kistList.get(i).getKist_value() > paid_money) {
                                Boolean statue = db.updatekistbyName(kistList.get(i).getId(), date2.getText().toString(), false, kistList.get(i).getKist_value() - paid_money);
                                break;
                            }

                        }
                    }
                    com.mohamedragab.cashpos.modules.moneybox.models.money money2 = new com.mohamedragab.cashpos.modules.moneybox.models.money();
                    money2.setNotes(" قسط العميل:" + value);
                    money2.setValue(Double.parseDouble(money_value.getText().toString()));
                    money2.setDate(date2.getText().toString());
                    final Cursor res3 = db.getallTransactions();
                    double total1 = 0;
                    if (res3 != null && res3.getCount() > 0) {
                        while (res3.moveToNext()) {
                            total1 = res3.getDouble(3);
                        }
                    }
                    money2.setTotalbefore(total1);
                    double totalAfter = total1 + Double.parseDouble(money_value.getText().toString());
                    money2.setTotalAfter(totalAfter);

                    if (db.insert_date(money2)) {
                        final Dialog after_dialog = new Dialog(customerKist.this);
                        after_dialog.setContentView(R.layout.after_pay_dialog2);
                        // after_dialog.getWindow().setLayout(350, ViewGroup.LayoutParams.WRAP_CONTENT);
                        after_dialog.setCancelable(false);


                        ImageView print = (ImageView) after_dialog.findViewById(R.id.print);
                        ImageView share = (ImageView) after_dialog.findViewById(R.id.share);
                        ImageView close = (ImageView) after_dialog.findViewById(R.id.cancel);
                        LinearLayout invoice_image = (LinearLayout) after_dialog.findViewById(R.id.linear_invoice);

                        TextView date = (TextView) after_dialog.findViewById(R.id.date);
                        date.setText(date2.getText().toString());
                        TextView time = (TextView) after_dialog.findViewById(R.id.time);
                        String currentTime = new SimpleDateFormat("hh:mm:ss a", Locale.US).format(new Date());
                        time.setText(currentTime);

                        TextView client = (TextView) after_dialog.findViewById(R.id.client);
                        client.setText(value + "");
                        TextView cashier = (TextView) after_dialog.findViewById(R.id.cashier);
                        if (SheredPrefranseHelper.getcurrentcashier(customerKist.this) != null) {
                            if (SheredPrefranseHelper.getcurrentcashier(customerKist.this).getName() != null) {
                                cashier.setText("" + SheredPrefranseHelper.getcurrentcashier(customerKist.this).getName() + "");
                            } else {
                                cashier.setText("" + "لا يوجد");
                            }
                        } else {
                            cashier.setText("" + "لا يوجد");
                        }

                        TextView paid_text = (TextView) after_dialog.findViewById(R.id.paid);
                        paid_text.setText(Round.round(Double.parseDouble(money_value.getText().toString()), 3) + "");
                        TextView not_paid_text = (TextView) after_dialog.findViewById(R.id.not_paid);
                        not_paid_text.setText((finalTotal_not_paid - Double.parseDouble(money_value.getText().toString())) + "");
                        TextView notification_text = (TextView) after_dialog.findViewById(R.id.notification);
                        TextView address_phone = (TextView) after_dialog.findViewById(R.id.address_phone);
                        TextView shopname = (TextView) after_dialog.findViewById(R.id.shopname);
                        ImageView logo = (ImageView) after_dialog.findViewById(R.id.logo);
                        shopInfo info = SheredPrefranseHelper.getshopData(customerKist.this);
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
                                File file = new File(Environment.getExternalStorageDirectory() + "/cashpos/kestpaid/", client.getText().toString() + formattedDate + ".png");
                                FileOutputStream out = new FileOutputStream(file);
                                returnedBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                                out.close();
                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                Uri screenshotUri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/cashpos/kestpaid/" + client.getText().toString() + formattedDate + ".png");
                                sharingIntent.setType("image/png");
                                sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                                startActivity(Intent.createChooser(sharingIntent, "مشاركة الفاتورة"));
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
                                File file = new File(Environment.getExternalStorageDirectory() + "/cashpos/kestpaid/", client.getText().toString() + formattedDate + ".png");
                                FileOutputStream out = new FileOutputStream(file);
                                returnedBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                                out.close();
                                Toast.makeText(getBaseContext(), " تم دفع " + Double.parseDouble(money_value.getText().toString()) + " بنجاح .", Toast.LENGTH_SHORT).show();
                                after_dialog.dismiss();
                                db.close();
                                this.recreate();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        after_dialog.show();

                    }
                }
            } else {
                Toast.makeText(getBaseContext(), "برجاء ادخال قيمة القسط !", Toast.LENGTH_SHORT).show();
            }


        });

        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showSnackbar(String showstring) {
        Snackbar.make(container, showstring, Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(R.color.button_unable)).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        netReciever = new Receiver();
        db.close();
        registerReceiver(netReciever, new IntentFilter(MainActivity.DISCONNECT));

        if (!MainActivity.ISCONNECT) {
            showSnackbar(getString(R.string.con_has_discon));
        }
        startActivity(new Intent(this, omalkist.class));
        finish();

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
                        if (SheredPrefranseHelper.getprinter_type(customerKist.this) != null) {
                            if (SheredPrefranseHelper.getprinter_type(customerKist.this).equals("58")) {
                                b2 = resizeImage(b2, 335, false);
                            } else {
                                b2 = resizeImage(b2, 790, false);
                            }
                        } else {
                            b2 = resizeImage(b2, 335, false);
                        }
                        printpicCode(b2);
                    } else {

                        if (SheredPrefranseHelper.getprinter_type(customerKist.this) != null) {
                            if (SheredPrefranseHelper.getprinter_type(customerKist.this).equals("58")) {
                                b2 = resizeImage(b2, 335, false);
                            } else {
                                b2 = resizeImage(b2, 790, false);
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
