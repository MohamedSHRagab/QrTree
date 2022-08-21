package com.mohamedragab.cashpos.modules.home;

import static com.mohamedragab.cashpos.base.AppConfig.request_permission;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mohamedragab.cashpos.BuildConfig;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.AppConfig;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.adminpanel.models.adminmodel;
import com.mohamedragab.cashpos.modules.adminpanel.views.adminpanel;
import com.mohamedragab.cashpos.modules.backup.backup;
import com.mohamedragab.cashpos.modules.blockscreen.blockscreen;
import com.mohamedragab.cashpos.modules.buy.views.buy;
import com.mohamedragab.cashpos.modules.buyinvoice.views.buyinvoice;
import com.mohamedragab.cashpos.modules.dashboard.views.dashboardadmin;
import com.mohamedragab.cashpos.modules.developerprofile.developerprofile;
import com.mohamedragab.cashpos.modules.employees.models.Cashier;
import com.mohamedragab.cashpos.modules.employees.views.cashiers;
import com.mohamedragab.cashpos.modules.employees.views.delivery;
import com.mohamedragab.cashpos.modules.employees.views.employee_login;
import com.mohamedragab.cashpos.modules.info.info;
import com.mohamedragab.cashpos.modules.invoice.views.invoiceView;
import com.mohamedragab.cashpos.modules.invoicebuyback.views.invoicebuyView;
import com.mohamedragab.cashpos.modules.invoicesalesback.views.invoicesaleView;
import com.mohamedragab.cashpos.modules.kist.views.omalkist;
import com.mohamedragab.cashpos.modules.login.models.User;
import com.mohamedragab.cashpos.modules.masrouf.masrof;
import com.mohamedragab.cashpos.modules.moneybox.views.moneybox;
import com.mohamedragab.cashpos.modules.moneyboxreport.views.moneyboxreport;
import com.mohamedragab.cashpos.modules.mored.views.moward;
import com.mohamedragab.cashpos.modules.notification.views.notification;
import com.mohamedragab.cashpos.modules.omla.views.customers;
import com.mohamedragab.cashpos.modules.prizes.models.prize;
import com.mohamedragab.cashpos.modules.prizes.views.allprizes;
import com.mohamedragab.cashpos.modules.rate.views.rateandcomment;
import com.mohamedragab.cashpos.modules.repair.views.omlarepair;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.sales.views.sales;
import com.mohamedragab.cashpos.modules.settings.views.settings;
import com.mohamedragab.cashpos.modules.shopmove.views.shopmove;
import com.mohamedragab.cashpos.modules.store.views.store;
import com.mohamedragab.cashpos.utils.Conts;
import com.mohamedragab.cashpos.utils.DeviceReceiver;

import net.posprinter.posprinterface.IMyBinder;
import net.posprinter.posprinterface.UiExecute;
import net.posprinter.service.PosprinterService;
import net.posprinter.utils.DataForSendToPrinterPos80;
import net.posprinter.utils.PosPrinterDev;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    DrawerLayout drawerLayout;
    NavigationView navigation;
    TextView  notificationcount, prizecount, cashier_name, cashier_address;
    public static TextView shopname;
    String filename = "myfile", mac;
    public static CircleImageView imageView, cashier_image;
    StorageReference storageReference;
    DatabaseReference reference2, adminreference, prizeReference;
    int not_count = 0, pos;
    DataBaseHelper db;
    public static String DISCONNECT = "com.posconsend.net.disconnetct";
    public static IMyBinder binder;
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //Bind successfully
            binder = (IMyBinder) iBinder;
            Log.e("binder", "connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("disbinder", "disconnected");
        }
    };
    public static boolean ISCONNECT;
    Button BTCon, BTDisconnect, BtSb, btn_scan;
    Spinner conPort;
    EditText showET;
    LinearLayout container, LLlayout;
    private View dialogView;
    BluetoothAdapter bluetoothAdapter;
    private ArrayAdapter<String> adapter1, adapter2;
    private ListView lv1, lv2;
    private ArrayList<String> deviceList_bonded = new ArrayList<String>();
    private ArrayList<String> deviceList_found = new ArrayList<String>();
    android.app.AlertDialog dialog;
    RadioButton small, big;
    private DeviceReceiver myDevice;
    LinearLayout cash_lin;

    private void initView() {

        BTCon = (Button) findViewById(R.id.buttonConnect);
        BTDisconnect = (Button) findViewById(R.id.buttonDisconnect);
        BtSb = (Button) findViewById(R.id.buttonSB);
        conPort = (Spinner) findViewById(R.id.connectport);
        showET = (EditText) findViewById(R.id.showET);
        container = (LinearLayout) findViewById(R.id.container);
        small = (RadioButton) findViewById(R.id.small);
        big = (RadioButton) findViewById(R.id.big);
        prizecount = (TextView) findViewById(R.id.prizescount);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigation = (NavigationView) findViewById(R.id.nav_view);
        db = new DataBaseHelper(getBaseContext());
        View header = navigation.getHeaderView(0);
        shopname = (TextView) header.findViewById(R.id.shopname);
        cashier_name = (TextView) findViewById(R.id.cashier_name);
        cashier_address = (TextView) findViewById(R.id.cashier_address);
        imageView = (CircleImageView) header.findViewById(R.id.image);
        cashier_image = (CircleImageView) findViewById(R.id.cashier_image);
        cash_lin = (LinearLayout) findViewById(R.id.cash_linear);

        if (SheredPrefranseHelper.getprinter_type(this) != null) {
            if (SheredPrefranseHelper.getprinter_type(this).equals("58")) {
                small.setChecked(true);
            } else {
                big.setChecked(true);
            }
        }

        Cashier current = SheredPrefranseHelper.getcurrentcashier(MainActivity.this);
        if (current != null) {
            if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getImage() != null) {
                cashier_image.setImageBitmap(BitmapFactory.decodeByteArray(SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getImage(), 0, SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getImage().length));
            }
            cashier_name.setText(current.getName() + " ");
            cashier_address.setText(SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getAddress() + "");
        } else {
            if (SheredPrefranseHelper.getUserData(this) != null) {
                cashier_name.setText(SheredPrefranseHelper.getUserData(this).getName() + " ");
            } else if (SheredPrefranseHelper.getAdminData(this) != null) {
                cashier_name.setText(SheredPrefranseHelper.getAdminData(this).getName() + " ");
            } else {
                cashier_name.setText(" نسخه تجريبيه");
                cash_lin.setVisibility(View.GONE);
            }
        }
        if (current != null && current.getImage() != null) {
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(current.getImage(), 0, current.getImage().length));
        } else {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.companylogo));
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        notificationcount = (TextView) findViewById(R.id.noticount);

        request_permission(MainActivity.this, this);
        Intent intent = new Intent(this, PosprinterService.class);
        try {
            bindService(intent, conn, BIND_AUTO_CREATE);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "الطابعه غير متصله !!", Toast.LENGTH_SHORT).show();
        }

        initView();
        setlistener();
        actionMenu();


        Cashier current = SheredPrefranseHelper.getcurrentcashier(MainActivity.this);
        if (current != null && current.getImage() != null) {
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(current.getImage(), 0, current.getImage().length));
        }

        imageView.setOnClickListener(view -> {
            adminmodel admin = SheredPrefranseHelper.getAdminData(this);
            if (admin != null) {

                if (admin.getType().equals("normal")) {
                    startActivity(new Intent(MainActivity.this, dashboardadmin.class));
                } else if (admin.getType().equals("super") || admin.getType().equals("admin")) {
                    startActivity(new Intent(MainActivity.this, adminpanel.class));
                }
            }
        });

        getmenudata();
        readFile();
        User user = SheredPrefranseHelper.getUserData(this);
        if (SheredPrefranseHelper.getUserData(this) == null && SheredPrefranseHelper.getAdminData(this) == null) {

        } else {
            getprizes();
            if (user == null) {
                adminmodel adminmodel = SheredPrefranseHelper.getAdminData(this);
                adminreference = FirebaseDatabase.getInstance().getReference("admins").child(adminmodel.getPhone());
                adminreference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        adminmodel admin = dataSnapshot.getValue(adminmodel.class);

                        if (admin != null) {
                            Date c = Calendar.getInstance().getTime();
                            System.out.println("Current time => " + c);

                            SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy/HH:mm:ss", Locale.US);
                            String formattedDate2 = df2.format(c);

                            adminreference.child("lastseen").setValue(formattedDate2);

                            if (admin.getBlocked().equals("true")) {
                                clearAppData();
                                startActivity(new Intent(MainActivity.this, blockscreen.class));
                                finish();
                            }
                        } else {
                            startActivity(new Intent(MainActivity.this, blockscreen.class));
                            finish();
                            clearAppData();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            } else {
                reference2 = FirebaseDatabase.getInstance().getReference("Users").child(user.getPhone());
                storageReference = FirebaseStorage.getInstance().getReference();
                reference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        if (user != null) {
                            Date c = Calendar.getInstance().getTime();
                            System.out.println("Current time => " + c);

                            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                            String formattedDate = df.format(c);

                            SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy/HH:mm:ss", Locale.US);
                            String formattedDate2 = df2.format(c);

                            reference2.child("lastseen").setValue(formattedDate2);

                            if (user.getBlocked().equals("true")) {

                                startActivity(new Intent(MainActivity.this, blockscreen.class));
                                finish();
                                clearAppData();
                            } else {
                                if (formattedDate.equals(user.getBackupdate())) {

                                } else {

                                    reference2.child("backupdate").setValue(formattedDate);

                                }
                            }


                        } else {
                            startActivity(new Intent(MainActivity.this, blockscreen.class));
                            finish();
                            clearAppData();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }


        }

        small.setOnClickListener(v -> {
            SheredPrefranseHelper.addprinter_type(this, "58");
        });
        big.setOnClickListener(v -> {
            SheredPrefranseHelper.addprinter_type(this, "80");
        });
        if (SheredPrefranseHelper.getlast_printer_address(this) != null) {
            showET.setText(SheredPrefranseHelper.getlast_printer_address(this) + "");
            // connetBle();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        db.close();
        db = new DataBaseHelper(getBaseContext());

    }


    private void getprizes() {
        if (SheredPrefranseHelper.getUserData(this) != null) {
            prizeReference = FirebaseDatabase.getInstance().getReference("prize").child(SheredPrefranseHelper.getUserData(this).getPhone());
        } else {
            prizeReference = FirebaseDatabase.getInstance().getReference("prize").child(SheredPrefranseHelper.getAdminData(this).getPhone());
        }
        prizeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<prize> prizeList = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    prize prize = noteDataSnapshot.getValue(prize.class);
                    if (prize.getUsed().equals("false")) {
                        prizeList.add(prize);
                        createNotification(prize.getText(), getBaseContext());
                    }
                }
                MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.notification);
                mp.start();
                prizecount.setText(prizeList.size() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), "حدث خطأ في ارجاع البيانات حاول مره اخري !", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setlistener() {
        BTCon.setOnClickListener(MainActivity.this);
        BTDisconnect.setOnClickListener(this);

        BtSb.setOnClickListener(this);
        conPort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pos = i;
                switch (i) {
                    case 0:
                        //bluetooth connect
                        showET.setText("");
                        BtSb.setVisibility(View.VISIBLE);
                        showET.setHint(getString(R.string.bleselect));
                        showET.setEnabled(false);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void actionMenu() {
        Menu menu = navigation.getMenu();

        menu.getItem(0).setOnMenuItemClickListener(item -> {
            if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()) != null) {
                AppConfig.check_cashier(SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getId() + "", MainActivity.this, invoicesaleView.class, "14");
            } else {
                AppConfig.check_cashier(null, MainActivity.this, invoicesaleView.class, "14");

            }
            return false;

        });
        menu.getItem(2).setOnMenuItemClickListener(item -> {
            if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()) != null) {

                AppConfig.check_cashier(SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getId() + "", MainActivity.this, invoiceView.class, "12");

            } else {
                AppConfig.check_cashier(null, MainActivity.this, invoiceView.class, "12");

            }
            return false;

        });
        menu.getItem(3).setOnMenuItemClickListener(item -> {
            if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()) != null) {

                AppConfig.check_cashier(SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getId() + "", MainActivity.this, buyinvoice.class, "13");
            } else {
                AppConfig.check_cashier(null, MainActivity.this, buyinvoice.class, "13");
            }
            return false;


        });
        menu.getItem(6).setOnMenuItemClickListener(item -> {
            if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()) != null) {
                startActivity(new Intent(MainActivity.this, developerprofile.class));
            } else {
                startActivity(new Intent(MainActivity.this, developerprofile.class));
            }
            return false;
        });
        menu.getItem(4).setOnMenuItemClickListener(item -> {
            if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()) != null) {

                AppConfig.check_cashier(SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getId() + "", MainActivity.this, shopmove.class, "10");

            } else {
                AppConfig.check_cashier(null, MainActivity.this, shopmove.class, "10");

            }
            return false;

        });
        menu.getItem(1).setOnMenuItemClickListener(item -> {
            if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()) != null) {

                AppConfig.check_cashier(SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getId() + "", MainActivity.this, invoicebuyView.class, "15");
            } else {
                AppConfig.check_cashier(null, MainActivity.this, invoicebuyView.class, "7");

            }
            return false;

        });
        menu.getItem(5).setOnMenuItemClickListener(item -> {
            if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()) != null) {

                AppConfig.check_cashier(SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getId() + "", MainActivity.this, moneyboxreport.class, "7");
            } else {
                AppConfig.check_cashier(null, MainActivity.this, moneyboxreport.class, "7");

            }
            return false;


        });
        menu.getItem(7).setOnMenuItemClickListener(item -> {
            if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()) != null) {

                AppConfig.check_cashier(SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getId() + "", MainActivity.this, settings.class, "16");
            } else {
                AppConfig.check_cashier(null, MainActivity.this, settings.class, "16");

            }
            return false;

        });
        menu.getItem(8).setOnMenuItemClickListener(item -> {
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
            return false;

        });
        menu.getItem(9).setOnMenuItemClickListener(item -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage = "\nبرنامج CashPOS علي جوجل بلاي \n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {

            }
            return false;

        });
        menu.getItem(10).setOnMenuItemClickListener(item -> {
            if (SheredPrefranseHelper.getUserData(this) == null && SheredPrefranseHelper.getAdminData(this) == null) {
                Toast.makeText(getBaseContext(), "النسخه غير مفعله قم بتفعيل النسخه عن طريق التواصل مع الادمن 01093957856", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, rateandcomment.class));
            }
            return false;
        });
    }

    private void clearAppData() {
        try {
            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
            } else {
                String packageName = getApplicationContext().getPackageName();
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("pm clear " + packageName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        getmenudata();
        AppConfig.request_permission(this, this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        getmenudata();
        AppConfig.request_permission(this, this);
        db.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getmenudata();
        AppConfig.request_permission(this, this);
        /*if (conn!=null){
            unbindService(conn);
        }*/
        db.close();
    }

    public void getmenudata() {
        db = new DataBaseHelper(getBaseContext());

        File file = new File(Environment.getExternalStorageDirectory() + "/cashpos/database/Database.db");
        File file2 = new File(Environment.getExternalStorageDirectory() + "/cashpos/invoices/default.png");
        File file3 = new File(Environment.getExternalStorageDirectory() + "/cashpos/agelpaid/default.png");
        File file4 = new File(Environment.getExternalStorageDirectory() + "/cashpos/kestpaid/default.png");

        file.getParentFile().mkdirs(); // Will create parent directories if not exists
        file2.getParentFile().mkdirs(); // Will create parent directories if not exists
        file3.getParentFile().mkdirs(); // Will create parent directories if not exists
        file4.getParentFile().mkdirs(); // Will create parent directories if not exists

        if (file.exists()) {

            Cashier current = SheredPrefranseHelper.getcurrentcashier(MainActivity.this);
            if (current != null) {
                shopname.setText(current.getName() + " ");
            } else {
                if (SheredPrefranseHelper.getUserData(this) != null) {
                    shopname.setText(SheredPrefranseHelper.getUserData(this).getName() + " ");
                } else if (SheredPrefranseHelper.getAdminData(this) != null) {
                    shopname.setText(SheredPrefranseHelper.getAdminData(this).getName() + " ");
                } else {
                    shopname.setText(" نسخه تجريبيه");

                }
            }
            if (current != null && current.getImage() != null) {
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(current.getImage(), 0, current.getImage().length));
            } else {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.companylogo));
            }
        } else {
            try {
                file.createNewFile();
                FileOutputStream s = new FileOutputStream(file, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public void go_sales(View view) {
        if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()) != null) {
            AppConfig.check_cashier(SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getId() + "", MainActivity.this, sales.class, "1");
        } else {
            AppConfig.check_cashier(null, MainActivity.this, sales.class, "1");

        }
    }

    public void go_buy(View view) {
        if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()) != null) {

            AppConfig.check_cashier(SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getId() + "", MainActivity.this, buy.class, "2");
        } else {
            AppConfig.check_cashier(null, MainActivity.this, buy.class, "2");

        }
    }

    public void go_store(View view) {
        if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()) != null) {
            AppConfig.check_cashier(SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getId() + "", MainActivity.this, store.class, "3");
        } else {
            AppConfig.check_cashier(null, MainActivity.this, store.class, "3");
        }
    }

    public void go_customers(View view) {
        if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()) != null) {

            AppConfig.check_cashier(SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getId() + "", MainActivity.this, customers.class, "4");
        } else {
            AppConfig.check_cashier(null, MainActivity.this, customers.class, "4");

        }
    }

    public void go_mored(View view) {
        if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()) != null) {

            AppConfig.check_cashier(SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getId() + "", MainActivity.this, moward.class, "5");
        } else {
            AppConfig.check_cashier(null, MainActivity.this, moward.class, "5");

        }
    }

    public void go_money_box(View view) {
        if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()) != null) {

            AppConfig.check_cashier(SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getId() + "", MainActivity.this, moneybox.class, "7");

        } else {
            AppConfig.check_cashier(null, MainActivity.this, moneybox.class, "7");

        }
    }

    public void go_masrouf(View view) {
        if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()) != null) {

            AppConfig.check_cashier(SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getId() + "", MainActivity.this, masrof.class, "8");
        } else {
            AppConfig.check_cashier(null, MainActivity.this, masrof.class, "8");

        }
    }

    public void go_info(View view) {
        if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()) != null) {

            AppConfig.check_cashier(SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getId() + "", MainActivity.this, info.class, "10");
        } else {
            AppConfig.check_cashier(null, MainActivity.this, info.class, "10");
        }

    }

    public void open_drawer(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void readFile() {
        SharedPreferences sharedPreferences = getSharedPreferences(filename, Context.MODE_PRIVATE);
        String default_val = "defaultname";
        String name = sharedPreferences.getString("name", default_val);
        Cashier current = SheredPrefranseHelper.getcurrentcashier(MainActivity.this);
        if (current != null) {
            shopname.setText(current.getName() + "" + " ");
        } else {
            if (SheredPrefranseHelper.getUserData(this) != null) {
                shopname.setText(SheredPrefranseHelper.getUserData(this).getName() + " ");
            } else if (SheredPrefranseHelper.getAdminData(this) != null) {
                shopname.setText(SheredPrefranseHelper.getAdminData(this).getName() + " ");
            } else {
                shopname.setText(" نسخه تجريبيه");

            }
        }

    }

    public void go_notification(View view) {
        startActivity(new Intent(this, notification.class));
    }


    public void go_prizes(View view) {
        if (SheredPrefranseHelper.getUserData(this) == null && SheredPrefranseHelper.getAdminData(this) == null) {
            Toast.makeText(getBaseContext(), "النسخه غير مفعله قم بتفعيل النسخه عن طريق التواصل مع الادمن 01093957856", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(this, allprizes.class));
        }
    }

    public void go_kist(View view) {
        if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()) != null) {

            AppConfig.check_cashier(SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getId() + "", MainActivity.this, omalkist.class, "9");
        } else {
            AppConfig.check_cashier(null, MainActivity.this, omalkist.class, "9");

        }
    }

    public void go_rate(View view) {
        if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()) != null) {

            AppConfig.check_cashier(SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getId() + "", MainActivity.this, rateandcomment.class, "16");
        } else {
            AppConfig.check_cashier(null, MainActivity.this, rateandcomment.class, "16");

        }
    }

    private NotificationManager notifManager;

    public void createNotification(String aMessage, Context context) {
        final int NOTIFY_ID = 0; // ID of notification
        String id = "1"; // default_channel_id
        String title = "رساله جديده"; // Default Channel
        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, allprizes.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(aMessage)                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)
                    .setContentText(context.getString(R.string.app_name)) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, allprizes.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(aMessage)                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    .setContentText(context.getString(R.string.app_name)) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }
        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        //connect button
        if (id == R.id.buttonConnect) {
            switch (pos) {
                case 0:
                    //bluetooth connection
                    connetBle();
                    break;
                case 1:
                    connetUSB();
                    break;

            }
        }
        //device button
        if (id == R.id.buttonSB) {
            switch (pos) {
                case 0:
                    setBluetooth();
                    BTCon.setText(getString(R.string.connect));
                    break;
                case 1:
                    setUSB();
                    BTCon.setText(getString(R.string.connect));
                    break;

            }

        }
        //disconnect
        if (id == R.id.buttonDisconnect) {
            if (ISCONNECT) {
                binder.disconnectCurrentPort(new UiExecute() {
                    @Override
                    public void onsucess() {
                        showSnackbar(getString(R.string.toast_discon_success));
                        showET.setText("");
                        BTCon.setText(getString(R.string.connect));
                    }

                    @Override
                    public void onfailed() {
                        showSnackbar(getString(R.string.toast_discon_faile));

                    }
                });
            } else {
                showSnackbar(getString(R.string.toast_present_con));
            }
        }


    }

    String usbAdrresss;

    private void connetUSB() {
        usbAdrresss = showET.getText().toString();
        if (usbAdrresss.equals(null) || usbAdrresss.equals("")) {
            showSnackbar(getString(R.string.usbselect));
        } else {
            binder.connectUsbPort(getApplicationContext(), usbAdrresss, new UiExecute() {
                @Override
                public void onsucess() {
                    ISCONNECT = true;
                    showSnackbar(getString(R.string.con_success));
                    BTCon.setText(getString(R.string.con_success));
                    setPortType(PosPrinterDev.PortType.USB);
                }

                @Override
                public void onfailed() {
                    ISCONNECT = false;
                    showSnackbar(getString(R.string.con_failed));
                    BTCon.setText(getString(R.string.con_failed));


                }
            });
        }
    }

    View dialogView3;
    private TextView tv_usb;
    private List<String> usbList, usblist;
    ListView lv_usb;
    ArrayAdapter adapter3;

    private void setUSB() {
        LayoutInflater inflater = LayoutInflater.from(this);
        dialogView3 = inflater.inflate(R.layout.usb_link, null);
        tv_usb = (TextView) dialogView3.findViewById(R.id.textView1);
        lv_usb = (ListView) dialogView3.findViewById(R.id.listView1);


        usbList = PosPrinterDev.GetUsbPathNames(this);
        if (usbList == null) {
            usbList = new ArrayList<>();
        }
        usblist = usbList;
        tv_usb.setText(getString(R.string.usb_pre_con) + usbList.size());
        adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usbList);
        lv_usb.setAdapter(adapter3);


        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView3).create();
        dialog.show();

        setUsbLisener(dialog);

    }

    String usbDev = "";

    public void setUsbLisener(final AlertDialog dialog) {

        lv_usb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                usbDev = usbList.get(i);
                showET.setText(usbDev);
                dialog.cancel();
                Log.e("usbDev: ", usbDev);
            }
        });


    }

    private void connetBle() {
        String bleAdrress = showET.getText().toString();
        SheredPrefranseHelper.addlast_printer_address(MainActivity.this, showET.getText().toString());
        if (bleAdrress.equals(null) || bleAdrress.equals("")) {
            showSnackbar(getString(R.string.bleselect));
        } else {
            binder.connectBtPort(bleAdrress, new UiExecute() {
                @Override
                public void onsucess() {
                    ISCONNECT = true;
                    showSnackbar(getString(R.string.con_success));
                    BTCon.setText(getString(R.string.con_success));

                    binder.write(DataForSendToPrinterPos80.openOrCloseAutoReturnPrintState(0x1f), new UiExecute() {
                        @Override
                        public void onsucess() {
                            binder.acceptdatafromprinter(new UiExecute() {
                                @Override
                                public void onsucess() {
                                }

                                @Override
                                public void onfailed() {
                                    ISCONNECT = false;
                                    showSnackbar(getString(R.string.con_has_discon));
                                }
                            });
                        }

                        @Override
                        public void onfailed() {

                        }
                    });


                }

                @Override
                public void onfailed() {

                    ISCONNECT = false;
                    showSnackbar(getString(R.string.con_failed));
                }
            });
        }


    }

    public void setBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!bluetoothAdapter.isEnabled()) {
            //open bluetooth
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, Conts.ENABLE_BLUETOOTH);
        } else {

            showblueboothlist();

        }
    }

    private void showblueboothlist() {
        if (!bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.startDiscovery();
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        dialogView = inflater.inflate(R.layout.printer_list, null);
        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deviceList_bonded);
        lv1 = (ListView) dialogView.findViewById(R.id.listView1);
        btn_scan = (Button) dialogView.findViewById(R.id.btn_scan);
        LLlayout = (LinearLayout) dialogView.findViewById(R.id.ll1);
        lv2 = (ListView) dialogView.findViewById(R.id.listView2);
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deviceList_found);
        lv1.setAdapter(adapter1);
        lv2.setAdapter(adapter2);
        dialog = new android.app.AlertDialog.Builder(this).setTitle("BLE").setView(dialogView).create();
        dialog.show();

        myDevice = new DeviceReceiver(deviceList_found, adapter2, lv2);

        //register the receiver
        IntentFilter filterStart = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        IntentFilter filterEnd = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(myDevice, filterStart);
        registerReceiver(myDevice, filterEnd);

        setDlistener();
        findAvalibleDevice();
    }

    private void setDlistener() {
        // TODO Auto-generated method stub
        btn_scan.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            LLlayout.setVisibility(View.VISIBLE);
            //btn_scan.setVisibility(View.GONE);
        });
        //boned device connect
        lv1.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
            // TODO Auto-generated method stub
            try {
                if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();

                }

                String msg = deviceList_bonded.get(arg2);
                mac = msg.substring(msg.length() - 17);
                String name = msg.substring(0, msg.length() - 18);
                //lv1.setSelection(arg2);
                dialog.cancel();
                showET.setText(mac);
                //Log.i("TAG", "mac="+mac);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        //found device and connect device
        lv2.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
            // TODO Auto-generated method stub
            try {
                if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();

                }
                String msg = deviceList_found.get(arg2);
                mac = msg.substring(msg.length() - 17);
                String name = msg.substring(0, msg.length() - 18);
                //lv2.setSelection(arg2);
                dialog.cancel();
                showET.setText(mac);
                Log.i("TAG", "mac=" + mac);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("تحذير");
        builder.setMessage("هل تريد الخروج من البرنامج ؟");
        builder.setPositiveButton("نعم", (dialog, id) -> {
            MainActivity.super.onBackPressed();
        });
        builder.setNegativeButton("لا", (dialog, id) -> {
        });
        builder.show();
    }

    private void findAvalibleDevice() {
        // TODO Auto-generated method stub

        Set<BluetoothDevice> device = bluetoothAdapter.getBondedDevices();

        deviceList_bonded.clear();
        if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
            adapter1.notifyDataSetChanged();
        }
        if (device.size() > 0) {
            //already
            for (Iterator<BluetoothDevice> it = device.iterator(); it.hasNext(); ) {
                BluetoothDevice btd = it.next();
                deviceList_bonded.add(btd.getName() + '\n' + btd.getAddress());
                adapter1.notifyDataSetChanged();
            }
        } else {
            deviceList_bonded.add("No can be matched to use bluetooth");
            adapter1.notifyDataSetChanged();
        }

    }

    private void showSnackbar(String showstring) {
        Snackbar.make(container, showstring, Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(R.color.button_unable)).show();
    }

    public static PosPrinterDev.PortType portType;//connect type

    private void setPortType(PosPrinterDev.PortType portType) {
        this.portType = portType;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      /*  binder.disconnectCurrentPort(new UiExecute() {
            @Override
            public void onsucess() {

            }

            @Override
            public void onfailed() {

            }
        });*/
       /* if (conn != null) {
            unbindService(conn);
        }*/
    }


    public void go_repair(View view) {
        startActivity(new Intent(this, omlarepair.class));
    }

    public void go_people(View view) {
        Toast.makeText(getBaseContext(), "الموظفين قيد التعديل !", Toast.LENGTH_SHORT).show();
    }

    public void go_youtube(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + "kCQqA-7lrkM"));

        if (intent.resolveActivity(getPackageManager()) == null) {
            // If the youtube app doesn't exist, then use the browser
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + "kCQqA-7lrkM"));
        }

        startActivity(intent);
    }

    public void go_save_info(View view) {
        if (SheredPrefranseHelper.getUserData(this) == null && SheredPrefranseHelper.getAdminData(this) == null) {
            Toast.makeText(getBaseContext(), "النسخه غير مفعله قم بتفعيل النسخه عن طريق التواصل مع الادمن 01093957856", Toast.LENGTH_SHORT).show();
        } else {
            if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()) != null) {

                AppConfig.check_cashier(SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getId() + "", MainActivity.this, backup.class, "11");
            } else {
                AppConfig.check_cashier(SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getId() + "", MainActivity.this, backup.class, "11");

            }
        }
    }


    public void go_factory(View view) {
        Toast.makeText(getBaseContext(), "التصنيع قيد التعديل !", Toast.LENGTH_SHORT).show();
    }

    public void go_setting(View view) {
        if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()) != null) {
            AppConfig.check_cashier(SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getId() + "", MainActivity.this, settings.class, "16");
        } else {
            AppConfig.check_cashier(null, MainActivity.this, settings.class, "16");
        }

    }

    public void go_Video(View view) {
        if (SheredPrefranseHelper.getUserData(this) == null && SheredPrefranseHelper.getAdminData(this) == null) {
            Toast.makeText(getBaseContext(), "النسخه غير مفعله قم بتفعيل النسخه عن طريق التواصل مع الادمن 01093957856", Toast.LENGTH_SHORT).show();
        } else {
            if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()) != null) {

                AppConfig.check_cashier(SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getId() + "", MainActivity.this, rateandcomment.class, "16");
            } else {
                AppConfig.check_cashier(null, MainActivity.this, rateandcomment.class, "16");

            }
        }

    }

    public void go_delivery(View view) {
        startActivity(new Intent(this, delivery.class));
    }

    public void cashier_logout(View view) {
        SheredPrefranseHelper.addcurrentcashier(getBaseContext(), null);
        Intent intent = new Intent(getApplicationContext(), employee_login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    public void go_cashiers(View view) {
        if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()) != null) {

            AppConfig.check_cashier(SheredPrefranseHelper.getcurrentcashier(getBaseContext()).getId() + "", MainActivity.this, cashiers.class, "6");
        } else {
            AppConfig.check_cashier(null, MainActivity.this, cashiers.class, "6");
        }
    }
}

