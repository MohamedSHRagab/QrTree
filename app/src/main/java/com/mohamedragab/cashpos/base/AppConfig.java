package com.mohamedragab.cashpos.base;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.home.MainActivity;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;

import net.posprinter.posprinterface.ProcessData;
import net.posprinter.posprinterface.UiExecute;
import net.posprinter.utils.BitmapToByteData;
import net.posprinter.utils.DataForSendToPrinterPos80;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class AppConfig {


    public static final String GOOGLE_APIS_RESPONNSE_TYPE = "/json";

    public static final String PREF_DEFAULT_KEY = "paycam_pref_key";
    public static final String USERS_PRF = "user_pref";
    public static final String ADMINS_PRF = "admin_pref";
    public static final String SHOPDATA_PRF = "shop_pref";
    public static final String PASSWORD_PRF = "pass_pref";
    public static final String CASHIER_PRF = "cashier_pref";
    public static final String MONEY_TYPE_PRF = "money_pref";
    public static final String PRINTER_TYPE_PRF = "printer_pref";
    public static final String PASSWORD_SCREENS = "screens_pref";
    public static final String RETURN_MONEY_PRF = "returnmoney_pref";
    public static final String LAST_PRINTER_ADDRESS = "lastprinter_pref";
    public static final String DELIVERY_PR = "delivery_pref";


    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("mohamed");

        } catch (Exception e) {
            return false;
        }
    }

    public static void request_permission(Context context, Activity activity) {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(" تشغيل البرنامج يحتاج الي تخزين قاعدة البيانات علي ذاكرة الجهاز برجاء السماح بتخزين قاعدة البيانات !");
            builder.setTitle("تخزين البيانات علي الجهاز");
            builder.setPositiveButton("سماح", (dialog, which) -> ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1));
            builder.setNegativeButton("الغاء", (dialog, which) -> Toast.makeText(context, "سيودي ذلك الي ايقاف البرنامج الرجاء السماح للاذوانات !", Toast.LENGTH_LONG).show());
            builder.show();

        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(" تشغيل الاسكانر يحتاج الي استخدام الكامير الخاصه بالجهاز برجاء السماح باستخدام الكاميرا !");
            builder.setTitle("استخدام كاميرا الجهاز");
            builder.setPositiveButton("سماح", (dialog, which) -> ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA},
                    1));
            builder.setNegativeButton("الغاء", (dialog, which) -> Toast.makeText(context, "سيودي ذلك الي ايقاف البرنامج الرجاء السماح للاذوانات !", Toast.LENGTH_LONG).show());

            builder.show();

        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(" تشغيل البرنامج يحتاج الي تخزين قاعدة البيانات علي ذاكرة الجهاز برجاء السماح بتخزين قاعدة البيانات !");
            builder.setTitle("تخزين البيانات علي الجهاز");
            builder.setPositiveButton("سماح", (dialog, which) -> ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1));
            builder.setNegativeButton("الغاء", (dialog, which) -> Toast.makeText(context, "سيودي ذلك الي ايقاف البرنامج الرجاء السماح للاذوانات !", Toast.LENGTH_LONG).show());

            builder.show();

        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.INTERNET},
                    1);
        }
    }

    public static void printUSBbitamp(final Bitmap printBmp) {

        int height = printBmp.getHeight();
        // if height > 200 cut the bitmap
        if (height > 200) {

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
                    list.add(DataForSendToPrinterPos80.initializePrinter());
                    List<Bitmap> bitmaplist = new ArrayList<>();
                    bitmaplist = cutBitmap(200, printBmp);//cut bitmap
                    if (bitmaplist.size() != 0) {
                        for (int i = 0; i < bitmaplist.size(); i++) {
                            list.add(DataForSendToPrinterPos80.printRasterBmp(0, bitmaplist.get(i), BitmapToByteData.BmpType.Threshold, BitmapToByteData.AlignType.Center, 576));
                        }
                    }
                    return list;
                }
            });
        } else {
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
                    list.add(DataForSendToPrinterPos80.initializePrinter());
                    list.add(DataForSendToPrinterPos80.printRasterBmp(
                            0, printBmp, BitmapToByteData.BmpType.Threshold, BitmapToByteData.AlignType.Center, 576));
                    return list;
                }
            });
        }

    }

    /*
    Cut picture method, equal height cutting
     */
    public static List<Bitmap> cutBitmap(int h, Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        boolean full = height % h == 0;
        int n = height % h == 0 ? height / h : (height / h) + 1;
        Bitmap b;
        List<Bitmap> bitmaps = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (full) {
                b = Bitmap.createBitmap(bitmap, 0, i * h, width, h);
            } else {
                if (i == n - 1) {
                    b = Bitmap.createBitmap(bitmap, 0, i * h, width, height - i * h);
                } else {
                    b = Bitmap.createBitmap(bitmap, 0, i * h, width, h);
                }
            }

            bitmaps.add(b);
        }

        return bitmaps;
    }


    public static DataBaseHelper db;

    public static void check_cashier(String cashier_id, Context context, Class activity, String data) {
        if (cashier_id == null || SheredPrefranseHelper.getAdminData(context) != null) {
            if (data.equals("10")) {
                Intent i = new Intent(context, activity);
                i.putExtra("date", "0");
                context.startActivity(i);
            } else {
                context.startActivity(new Intent(context, activity));
            }
        } else if (SheredPrefranseHelper.getUserData(context) != null) {
            db = new DataBaseHelper(context);
            Cursor res = db.getcashier(cashier_id);

            if (res != null && res.getCount() > 0) {

                while (res.moveToNext()) {

                    String p1 = res.getString(9);
                    String p2 = res.getString(10);
                    String p3 = res.getString(11);
                    String p4 = res.getString(12);
                    String p5 = res.getString(13);
                    String p6 = res.getString(14);
                    String p7 = res.getString(15);
                    String p8 = res.getString(16);
                    String p9 = res.getString(17);
                    String p10 = res.getString(18);
                    String p11 = res.getString(19);
                    String p12 = res.getString(20);
                    String p13 = res.getString(21);
                    String p14 = res.getString(22);
                    String p15 = res.getString(23);
                    String p16 = res.getString(24);

                    if (data.equals("1")) {
                        if (p1.equals("0")) {
                            db.close();

                            context.startActivity(new Intent(context, activity));
                        } else {
                            showDialog(context, activity);
                        }
                    } else if (data.equals("2")) {
                        if (p2.equals("0")) {
                            db.close();
                            context.startActivity(new Intent(context, activity));
                        } else {
                            showDialog(context, activity);
                        }
                    } else if (data.equals("3")) {
                        if (p3.equals("0")) {
                            db.close();
                            context.startActivity(new Intent(context, activity));
                        } else {
                            showDialog(context, activity);
                        }
                    } else if (data.equals("4")) {
                        if (p4.equals("0")) {
                            db.close();
                            context.startActivity(new Intent(context, activity));
                        } else {
                            showDialog(context, activity);
                        }
                    } else if (data.equals("5")) {
                        if (p5.equals("0")) {
                            db.close();
                            context.startActivity(new Intent(context, activity));
                        } else {
                            showDialog(context, activity);
                        }
                    } else if (data.equals("6")) {
                        if (p6.equals("0")) {
                            db.close();
                            context.startActivity(new Intent(context, activity));
                        } else {
                            showDialog(context, activity);
                        }
                    } else if (data.equals("7")) {
                        if (p7.equals("0")) {
                            db.close();
                            context.startActivity(new Intent(context, activity));
                        } else {
                            showDialog(context, activity);
                        }
                    } else if (data.equals("8")) {
                        if (p8.equals("0")) {
                            db.close();
                            context.startActivity(new Intent(context, activity));
                        } else {
                            showDialog(context, activity);
                        }
                    } else if (data.equals("9")) {
                        if (p9.equals("0")) {
                            db.close();
                            context.startActivity(new Intent(context, activity));
                        } else {
                            showDialog(context, activity);
                        }
                    } else if (data.equals("10")) {
                        if (p10.equals("0")) {
                            db.close();
                            Intent i = new Intent(context, activity);
                            i.putExtra("date", "0");
                            context.startActivity(i);
                        } else {
                            showDialog(context, activity);
                        }
                    } else if (data.equals("11")) {
                        if (p11.equals("0")) {
                            db.close();
                            context.startActivity(new Intent(context, activity));
                        } else {
                            showDialog(context, activity);
                        }
                    } else if (data.equals("12")) {
                        if (p12.equals("0")) {
                            db.close();
                            context.startActivity(new Intent(context, activity));
                        } else {
                            showDialog(context, activity);
                        }
                    } else if (data.equals("13")) {
                        if (p13.equals("0")) {
                            db.close();
                            context.startActivity(new Intent(context, activity));
                        } else {
                            showDialog(context, activity);
                        }
                    } else if (data.equals("14")) {
                        if (p14.equals("0")) {
                            db.close();
                            context.startActivity(new Intent(context, activity));
                        } else {
                            showDialog(context, activity);
                        }
                    } else if (data.equals("15")) {
                        if (p15.equals("0")) {
                            db.close();
                            context.startActivity(new Intent(context, activity));
                        } else {
                            showDialog(context, activity);
                        }
                    } else if (data.equals("16")) {
                        if (p16.equals("0")) {
                            db.close();
                            context.startActivity(new Intent(context, activity));
                        } else {
                            showDialog(context, activity);
                        }
                    }
                }

            }
            else {

                context.startActivity(new Intent(context, activity));

            }


        }

    }

    private static void showDialog(Context context, Class activity) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.passworddialog);
        dialog.setCancelable(false);

        TextInputEditText password = (TextInputEditText) dialog.findViewById(R.id.password);

        Button ok = (Button) dialog.findViewById(R.id.ok);
        ok.setOnClickListener(v -> {
            if (SheredPrefranseHelper.getUserData(context).getPassword().equals(password.getText().toString().trim())) {
                dialog.dismiss();
                context.startActivity(new Intent(context, activity));
            } else {
                Toast.makeText(context, "الباسورد غير صحيح !", Toast.LENGTH_SHORT).show();
            }
        });
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
            db.close();
        });
        dialog.show();
    }
}




