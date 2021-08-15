package com.mohamedragab.cashpos.modules.kist.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.AppConfig;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.home.MainActivity;
import com.mohamedragab.cashpos.modules.kist.views.customerKist;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class kistAdapter extends ArrayAdapter {

    List<Kist> kists;
    Context con;
    DataBaseHelper db;
    Boolean statue;


    public kistAdapter(Context context, List<Kist> kists) {
        super(context, R.layout.kist_item, R.id.name, kists);
        this.kists = kists;
        con = context;
        db = new DataBaseHelper(con);


    }

    public void setKistAdapter(List<Kist> kists) {
        this.kists = kists;

    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.kist_item, parent, false);

        final TextView num = (TextView) row.findViewById(R.id.number);
        TextView date = (TextView) row.findViewById(R.id.date);
        TextView value = (TextView) row.findViewById(R.id.value);
        TextView paydate = (TextView) row.findViewById(R.id.paydate);
        ImageView paidornot = (ImageView) row.findViewById(R.id.paidornot);

        LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.linearitem);
        linearLayout.setOnClickListener(view -> {
            if (kists.get(position).getStatue().equals("paid")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(con);
                builder.setTitle("تحذير");
                builder.setMessage("هل تريد حذف القسط المدفوع من القائمة ؟");
                builder.setPositiveButton("نعم", (dialog, id) -> {
                    db.deletekistbyid(kists.get(position).getId() + "");
                    String value2 = kists.get(position).getClient_name();
                    Intent i = new Intent(con, customerKist.class);
                    i.putExtra("key", value2);
                    db.close();
                    con.startActivity(i);
                    ((customerKist) con).finish();

                });
                builder.setNegativeButton("لا", (dialog, id) -> {
                });
                builder.show();
            } else {
                final Dialog dialog = new Dialog(con);
                dialog.setContentView(R.layout.addmoneydialog);
                dialog.setCancelable(false);

                EditText money_value = (EditText) dialog.findViewById(R.id.value);
                TextView pay_button = (TextView) dialog.findViewById(R.id.pay);
                pay_button.setOnClickListener(v -> {
                    if (!money_value.getText().toString().trim().equals("")) {
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                        String formattedDate = df.format(c);
                        Double money = Double.parseDouble(money_value.getText().toString());
                        if (money < kists.get(position).getKist_value()) {
                            statue = db.updatekistbyName(kists.get(position).getId(), formattedDate, false, kists.get(position).getKist_value() - money);
                            if (statue) {
                                com.mohamedragab.cashpos.modules.moneybox.models.money money2 = new com.mohamedragab.cashpos.modules.moneybox.models.money();
                                money2.setDate(formattedDate);
                                money2.setNotes(" قسط العميل:" + kists.get(position).getClient_name());
                                money2.setValue(Round.round(money, 3));

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
                                    dialog.dismiss();
                                    reciptdialog(con,formattedDate,kists.get(position).getClient_name(),Double.parseDouble(money_value.getText().toString()));

                                } else {
                                    Toast.makeText(con, "فشل اضافة مبلغ القسط في الخزينه !", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(con, "فشل دفع القسط تأكد من ادخال بيانات صحيحه !", Toast.LENGTH_SHORT).show();
                            }

                        } else if (money == kists.get(position).getKist_value()) {
                            statue = db.updatekistbyName(kists.get(position).getId(), formattedDate, true, money);
                            if (statue) {

                                com.mohamedragab.cashpos.modules.moneybox.models.money money2 = new com.mohamedragab.cashpos.modules.moneybox.models.money();
                                money2.setDate(formattedDate);
                                money2.setNotes(" قسط العميل:" + kists.get(position).getClient_name());
                                money2.setValue(money);

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
                                    dialog.dismiss();
                                    reciptdialog(con,formattedDate,kists.get(position).getClient_name(),Double.parseDouble(money_value.getText().toString()));

                                } else {
                                    Toast.makeText(con, "فشل اضافة مبلغ القسط في الخزينه !", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(con, "فشل دفع القسط تأكد من ادخال بيانات صحيحه !", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(con, "الرقم المدفوع اكبر من القسط !", Toast.LENGTH_SHORT).show();
                            money_value.setText("");
                        }
                    } else {
                        Toast.makeText(con, "برجاء ادخال قيمة القسط !", Toast.LENGTH_SHORT).show();
                    }


                });

                Button cancel = (Button) dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(v -> {
                    dialog.dismiss();
                });

                dialog.show();
            }
        });
        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(Color.parseColor("#eaddff"));
        } else {

        }

        Kist kist = kists.get(position);
        num.setText((position + 1) + "");
        date.setText(kist.getCollectdate() + "");
        if (!kist.getPay_type().equals("month") && !kist.getPay_type().equals("week") && !kist.getPay_type().equals("")){
            if (kist.getStatue().equals("paid")) {
                value.setText(kist.getPay_type() + "/"+kist.getPay_type());
            }else{
                value.setText(Round.round(kist.getKist_value(), 3) + "/"+kist.getPay_type());
            }
        }else{
            value.setText(Round.round(kist.getKist_value(), 3) +"");
        }
        paydate.setText(kist.getPaydate() + "");
        if (kist.getStatue().equals("paid")) {
            paidornot.setImageResource(R.drawable.completedicon);
        } else {
            paidornot.setImageResource(R.drawable.notcompleted);
        }


        return row;
    }

    public void reciptdialog(Context con,String formattedDate,String client_name,double money_val){
        double total_not_paid = 0;
        for (int j = 0; j < kists.size(); j++) {
            if (!kists.get(j).getStatue().equals("paid")) {
                total_not_paid += kists.get(j).getKist_value();
            }
        }
        double finalTotal_not_paid = total_not_paid;

        final Dialog after_dialog = new Dialog(con);
        after_dialog.setContentView(R.layout.after_pay_dialog);
        // after_dialog.getWindow().setLayout(350, ViewGroup.LayoutParams.WRAP_CONTENT);
        after_dialog.setCancelable(false);


        ImageView print = (ImageView) after_dialog.findViewById(R.id.print);
        ImageView share = (ImageView) after_dialog.findViewById(R.id.share);
        ImageView close = (ImageView) after_dialog.findViewById(R.id.cancel);
        LinearLayout invoice_image = (LinearLayout) after_dialog.findViewById(R.id.linear_invoice);

        TextView date = (TextView) after_dialog.findViewById(R.id.date);
        date.setText( formattedDate);
        TextView time = (TextView) after_dialog.findViewById(R.id.time);
        String currentTime = new SimpleDateFormat("hh:mm:ss a", Locale.US).format(new Date());
        time.setText(currentTime);

        TextView client = (TextView) after_dialog.findViewById(R.id.client);
        client.setText(client_name + "");
        TextView cashier = (TextView) after_dialog.findViewById(R.id.cashier);
        if (SheredPrefranseHelper.getcurrentcashier(con) != null) {
            if (SheredPrefranseHelper.getcurrentcashier(con).getName() != null) {
                cashier.setText("" + SheredPrefranseHelper.getcurrentcashier(con).getName() + "");
            } else {
                cashier.setText("" + "لا يوجد");
            }
        } else {
            cashier.setText("" + "لا يوجد");
        }

        TextView paid_text = (TextView) after_dialog.findViewById(R.id.paid);
        paid_text.setText(money_val + "");
        TextView not_paid_text = (TextView) after_dialog.findViewById(R.id.not_paid);
        not_paid_text.setText((finalTotal_not_paid - money_val) + "");
        TextView notification_text = (TextView) after_dialog.findViewById(R.id.notification);
        TextView address_phone = (TextView) after_dialog.findViewById(R.id.address_phone);
        TextView shopname = (TextView) after_dialog.findViewById(R.id.shopname);
        ImageView logo = (ImageView) after_dialog.findViewById(R.id.logo);
        shopInfo info = SheredPrefranseHelper.getshopData(con);
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
            printImage(returnedBitmap);
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
                File file = new File(Environment.getExternalStorageDirectory() + "/cashpos/kestpaid/",  client.getText().toString()+formattedDate + ".png");
                FileOutputStream out = new FileOutputStream(file);
                returnedBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.close();
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                Uri screenshotUri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/cashpos/kestpaid/" + client.getText().toString()+formattedDate  + ".png");
                sharingIntent.setType("image/png");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                con.startActivity(Intent.createChooser(sharingIntent, "مشاركة الفاتورة"));
                Toast.makeText(con, "تم حفظ الايصال في : " + file.toString(), Toast.LENGTH_LONG).show();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Toast.makeText(con, "الملف غير موجود !", Toast.LENGTH_SHORT).show();
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
                File file = new File(Environment.getExternalStorageDirectory() + "/cashpos/kestpaid/",  client.getText().toString()+formattedDate + ".png");
                FileOutputStream out = new FileOutputStream(file);
                returnedBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.close();
                Toast.makeText(con, " تم دفع " + money_val + " بنجاح .", Toast.LENGTH_SHORT).show();
                after_dialog.dismiss();
                db.close();
                ((Activity)con).recreate();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        after_dialog.show();
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
            list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(66, 1));

            list.add(DataForSendToPrinterPos80.printRasterBmp(
                    0, printBmp, BitmapToByteData.BmpType.Threshold, BitmapToByteData.AlignType.Left, 656));
            list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(66, 1));


            return list;

        });


    }
    private Bitmap b1;//grey-scale bitmap
    private Bitmap b2;//compress bitmap
    LinearLayout container;

    private void printImage(Bitmap image) {
        Bitmap bitm = image;
        netReciever = new Receiver();
        con.registerReceiver(netReciever, new IntentFilter(MainActivity.DISCONNECT));

        if (!MainActivity.ISCONNECT) {
            showSnackbar(con.getString(R.string.con_has_discon));
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

        Tiny.getInstance().init( ((Activity)con).getApplication());
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
    Receiver netReciever;
    private void showSnackbar(String showstring) {
        Snackbar.make(container, showstring, Snackbar.LENGTH_LONG)
                .setActionTextColor(con.getResources().getColor(R.color.button_unable)).show();
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
                        if (SheredPrefranseHelper.getprinter_type(con) != null) {
                            if (SheredPrefranseHelper.getprinter_type(con).equals("58")) {
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
                        if (SheredPrefranseHelper.getprinter_type(con) != null) {
                            if (SheredPrefranseHelper.getprinter_type(con).equals("58")) {
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




}



