package com.mohamedragab.cashpos.modules.sales.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.mohamedragab.cashpos.base.DateUtil;
import com.mohamedragab.cashpos.modules.employees.models.DelivTrans;
import com.mohamedragab.cashpos.modules.omlatransactions.models.omlatransaction;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.sales.models.Kist;
import com.mohamedragab.cashpos.modules.sales.models.invoice;
import com.mohamedragab.cashpos.modules.sales.wedgit.allproductsAdapter;
import com.mohamedragab.cashpos.modules.sales.wedgit.productSelectedAdapter;
import com.mohamedragab.cashpos.modules.settings.models.shopInfo;
import com.mohamedragab.cashpos.modules.store.models.category;
import com.mohamedragab.cashpos.modules.store.models.product;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.AppConfig;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.home.MainActivity;
import com.mohamedragab.cashpos.modules.omla.models.omla;
import com.mohamedragab.cashpos.modules.omla.views.newcustomer;
import com.mohamedragab.cashpos.modules.sales.SampleFragmentPagerAdapter2;
import com.mohamedragab.cashpos.modules.sales.models.sellproduct;
import com.mohamedragab.cashpos.modules.scanner.scanner;
import com.mohamedragab.cashpos.utils.Round;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;


public class sales extends AppCompatActivity {
    public static AutoCompleteTextView autoCompleteTextView;
    public static EditText discount;

    private Calendar mcalendar;
    private int day2, month2, year2;
    private TextView current_date2;
    private int day, month, year;

    public static List<product> productList;
    public static ArrayList<Double> quantityList;
    public static ArrayList<String> pricesList;
    String names[];
    ListView listView;
    productSelectedAdapter productAdapter;
    public static TextView total;
    public static double total_value = 0.0;
    DataBaseHelper db;
    double totalpluspercentage;
    TextView total_after_first, total_after_discount;
    SearchableSpinner omla, omla2;
    TextView monthorweekname, firstday, create_date, get_date;
    RadioButton before_radio, after_radio, percentage, money;
    EditText first_inkest2, profit_percentage2, first_inkest, profit_percentage;
    View parentLayout;
    String invoice_number = "";
    ImageView printer_status;
    double total_after_discount_value = 0.0;
    CardView final_card;
    Boolean card_final_visibility = true;
    int invoice_id;
    Bitmap invoice_png_image;
    private double Client_not_paid=0.0;

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
        setContentView(R.layout.activity_sales);

        total = (TextView) findViewById(R.id.total);
        total_after_discount = (TextView) findViewById(R.id.totalafterdiscount);
        discount = (EditText) findViewById(R.id.discount);
        printer_status = (ImageView) findViewById(R.id.printer);
        final_card = (CardView) findViewById(R.id.final_card);

        percentage = (RadioButton) findViewById(R.id.percentage);
        money = (RadioButton) findViewById(R.id.money);

        if (MainActivity.ISCONNECT) {
            printer_status.setImageResource(R.drawable.printerconnected);
        } else {
            printer_status.setImageResource(R.drawable.printeroffline);
        }
        db = new DataBaseHelper(getBaseContext());

        checkOriantation();

        container = (LinearLayout) findViewById(R.id.linear2);
        invoice_id = db.getsellnextid();

        parentLayout = findViewById(android.R.id.content);
        current_date2 = (TextView) findViewById(R.id.date);
        current_date2.setOnClickListener(view -> DateDialog2());
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String formattedDate = df.format(c);
        current_date2.setText(formattedDate + "");

        productList = new ArrayList<>();
        quantityList = new ArrayList<>();
        pricesList = new ArrayList<>();

        total_value = 0.0;
        discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (percentage.isChecked()) {
                    if (!discount.getText().toString().trim().equals("")) {
                        if (Double.parseDouble(discount.getText().toString().trim()) <= 100) {
                            double discount_value = Double.parseDouble(discount.getText().toString().trim());
                            double discount_money = total_value * (discount_value / 100);
                            total_after_discount_value = Round.round((total_value - discount_money), 3);
                            total_after_discount.setText(total_after_discount_value + SheredPrefranseHelper.getmoney_type(sales.this));
                        } else {
                            Toast.makeText(getBaseContext(), "الخصم اكبر من 100 %", Toast.LENGTH_SHORT).show();
                            discount.setText("");
                            total_after_discount_value = total_value;
                            total_after_discount.setText(Round.round(total_value, 3) + SheredPrefranseHelper.getmoney_type(sales.this));
                        }
                    } else {
                        total_after_discount_value = total_value;
                        total_after_discount.setText(Round.round(total_value, 3) + SheredPrefranseHelper.getmoney_type(sales.this));
                    }
                } else {
                    if (!discount.getText().toString().trim().equals("")) {
                        if (Double.parseDouble(discount.getText().toString().trim()) <= Round.round((total_value), 3)) {
                            double discount_value = Double.parseDouble(discount.getText().toString().trim());
                            total_after_discount_value = Round.round((total_value - discount_value), 3);
                            total_after_discount.setText(total_after_discount_value + SheredPrefranseHelper.getmoney_type(sales.this));
                        } else {
                            Toast.makeText(getBaseContext(), "الخصم اكبر من المبلغ !", Toast.LENGTH_SHORT).show();
                            discount.setText("");
                            total_after_discount_value = total_value;
                            total_after_discount.setText(Round.round(total_value, 3) + SheredPrefranseHelper.getmoney_type(sales.this));
                        }
                    } else {
                        total_after_discount_value = total_value;
                        total_after_discount.setText(Round.round(total_value, 3) + SheredPrefranseHelper.getmoney_type(sales.this));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        autoCompleteTextView = findViewById(R.id.search);
        productAdapter = new productSelectedAdapter(sales.this, productList);

        listView = (ListView) findViewById(R.id.list_selected_products);
        View footer = new View(sales.this);
        footer.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 100));
        listView.addFooterView(footer, null, false);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //autoCompleteTextView.setText("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final Cursor res = db.getallproducts();

                if (res != null && res.getCount() > 0) {
                    while (res.moveToNext()) {

                        if (autoCompleteTextView.getText().toString().trim().equals(res.getString(2))) {
                            MediaPlayer mp = MediaPlayer.create(sales.this, R.raw.finalsound);
                            mp.start();

                            product pro = new product();

                            pro.setId(res.getInt(0));
                            pro.setCode_id(res.getString(1));
                            pro.setName(res.getString(2));
                            pro.setSellprice(res.getDouble(4));
                            pro.setQuantity(res.getDouble(6));
                            pro.setDescription(res.getString(3));
                            pro.setBuyprice(res.getDouble(5));
                            pro.setExpiredate(res.getString(7));
                            byte[] imgByte = res.getBlob(9);

                            pro.setMeasure1(res.getString(10));
                            pro.setMeasure2(res.getString(11));
                            pro.setMeasure3(res.getString(12));
                            pro.setSellprice2(res.getDouble(13));
                            pro.setSellprice3(res.getDouble(14));
                            pro.setCategory(res.getString(15));
                            pro.setFactor2(res.getInt(16));
                            pro.setFactor3(res.getInt(17));
                            // BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
                            pro.setImage(imgByte);

                            if (res.getDouble(6) <= 0) {
                                Snackbar.make(parentLayout, res.getString(2) + " نفذ من المخزن ", Snackbar.LENGTH_LONG)
                                        .setAction("اخفاء", view -> {

                                        })
                                        .setActionTextColor(getResources().getColor(R.color.red))
                                        .show();
                            }
                            Boolean added = false;

                            for (int i = 0; i < productList.size(); i++) {

                                if (res.getInt(0) == productList.get(i).getId()) {
                                    quantityList.set(i, quantityList.get(i) + 1);
                                    autoCompleteTextView.setText("");
                                    total_value += Round.round((res.getDouble(4) * 1.0), 3);
                                    added = true;
                                    break;
                                } else if (i == productList.size() - 1 && res.getInt(0) != productList.get(i).getId()) {
                                    productList.add(pro);
                                    autoCompleteTextView.setText("");
                                    quantityList.add(1.0);
                                    pricesList.add("one");
                                    total_value += Round.round((res.getDouble(4) * 1.0), 3);
                                    added = true;
                                    break;
                                }
                            }
                            if (!added) {
                                productList.add(pro);
                                autoCompleteTextView.setText("");
                                quantityList.add(1.0);
                                pricesList.add("one");
                            }

                        }
                    }
                    productAdapter.setproductAdapter(productList, listView);
                    listView.setAdapter(productAdapter);
                    listView.addFooterView(footer, null, false);
                    double total_all = 0.0;
                    for (int i = 0; i < quantityList.size(); i++) {
                        total_all += Round.round((quantityList.get(i) * productList.get(i).getSellprice()), 3);
                    }
                    total_value = Round.round(total_all, 3);
                    discount.setText(0 + "");
                    total.setText(total_all + SheredPrefranseHelper.getmoney_type(sales.this));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                final Cursor res = db.getallproducts();

                if (res != null && res.getCount() > 0) {
                    while (res.moveToNext()) {

                        if (autoCompleteTextView.getText().toString().trim().equals(res.getString(1))) {
                            MediaPlayer mp = MediaPlayer.create(sales.this, R.raw.finalsound);
                            mp.start();
                            product pro = new product();

                            pro.setId(res.getInt(0));
                            pro.setCode_id(res.getString(1));
                            pro.setName(res.getString(2));
                            pro.setSellprice(res.getDouble(4));
                            pro.setQuantity(res.getDouble(6));
                            pro.setDescription(res.getString(3));
                            pro.setBuyprice(res.getDouble(5));
                            pro.setExpiredate(res.getString(7));
                            byte[] imgByte = res.getBlob(9);

                            pro.setMeasure1(res.getString(10));
                            pro.setMeasure2(res.getString(11));
                            pro.setMeasure3(res.getString(12));
                            pro.setSellprice2(res.getDouble(13));
                            pro.setSellprice3(res.getDouble(14));
                            pro.setCategory(res.getString(15));
                            pro.setFactor2(res.getInt(16));
                            pro.setFactor3(res.getInt(17));
                            // BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
                            pro.setImage(imgByte);
                            if (res.getDouble(6) <= 0) {
                                Snackbar.make(parentLayout, res.getString(2) + " نفذ من المخزن ", Snackbar.LENGTH_LONG)
                                        .setAction("اخفاء", view -> {

                                        })
                                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                                        .show();
                            }

                            Boolean added = false;
                            for (int i = 0; i < productList.size(); i++) {

                                if (res.getInt(0) == productList.get(i).getId()) {
                                    quantityList.set(i, quantityList.get(i) + 1);
                                    autoCompleteTextView.setText("");
                                    total_value += Round.round((res.getDouble(4) * 1.0), 3);
                                    added = true;
                                    break;
                                } else if (i == productList.size() - 1 && res.getInt(0) != productList.get(i).getId()) {
                                    productList.add(pro);
                                    autoCompleteTextView.setText("");
                                    quantityList.add(1.0);
                                    pricesList.add("one");
                                    total_value += Round.round((res.getDouble(4) * 1.0), 3);
                                    added = true;
                                    break;
                                }
                            }
                            if (!added) {
                                productList.add(pro);
                                autoCompleteTextView.setText("");
                                quantityList.add(1.0);
                                pricesList.add("one");
                            }
                        }

                    }
                    productAdapter.setproductAdapter(productList, listView);
                    listView.setAdapter(productAdapter);
                    listView.addFooterView(footer, null, false);

                    double total_all = 0.0;
                    for (int i = 0; i < quantityList.size(); i++) {
                        if (sales.pricesList.get(i).equals("one")) {
                            total_all += Round.round(quantityList.get(i) * sales.productList.get(i).getSellprice(), 3);
                        } else if (sales.pricesList.get(i).equals("two")) {
                            total_all += Round.round(quantityList.get(i) * sales.productList.get(i).getSellprice2(), 3);
                        } else {
                            total_all += Round.round(quantityList.get(i) * sales.productList.get(i).getSellprice3(), 3);
                        }
                    }
                    total_value = Round.round(total_all, 3);
                    discount.setText(0 + "");
                    total.setText(total_all + SheredPrefranseHelper.getmoney_type(sales.this));
                }
            }
        });
        final Cursor res = db.getallproducts();

        int index = 0;
        if (res != null && res.getCount() > 0) {
            index = 0;
            names = new String[res.getCount()];
            while (res.moveToNext()) {

                names[index] = res.getString(2);
                index++;

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
                autoCompleteTextView.setAdapter(adapter);

            }

        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String reserveproduct = extras.getString("reserveproduct");
            autoCompleteTextView.setText(reserveproduct);
        }
    }

    public static List<category> adminList;
    public static List<product> product_List;

    private void checkOriantation() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            adminList = new ArrayList<>();
            product_List = new ArrayList<>();

            Cursor res = db.getallcategories();

            category x = new category();
            x.setCategory("تصنيف0");
            adminList.add(x);

            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    category pro = new category();
                    pro.setCategory(res.getString(0));
                    adminList.add(pro);

                }
            }
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            SampleFragmentPagerAdapter2 pagerAdapter =
                    new SampleFragmentPagerAdapter2(getSupportFragmentManager(), sales.this, adminList);
            viewPager.setAdapter(pagerAdapter);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
            tabLayout.setupWithViewPager(viewPager);
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                tab.setCustomView(pagerAdapter.getTabView(i));
            }

        }
    }

    public void DateDialog2() {
        Locale.setDefault(Locale.US);
        mcalendar = Calendar.getInstance();
        year2 = mcalendar.get(Calendar.YEAR);
        month2 = mcalendar.get(Calendar.MONTH);
        day2 = mcalendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener listener = (view, year, monthOfYear, dayOfMonth) -> current_date2.setText(DateUtil.getDateOnly(year, monthOfYear, dayOfMonth));
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year2, month2, day2);
        dpDialog.show();
    }

    public void go_scan(View view) {
        String value = "sales";
        Intent i = new Intent(sales.this, scanner.class);
        i.putExtra("activity", value);
        startActivity(i);

    }

    public void go_home(View view) {
        onBackPressed();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void go_continue(View view) {

        if (total_value == 0) {
            Toast.makeText(getBaseContext(), "برجاء اضافه اصناف الي الفاتوره !", Toast.LENGTH_SHORT).show();
        } else {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.finalinsale);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);

            monthorweekname = (TextView) dialog.findViewById(R.id.monthorweekname);

            final LinearLayout profitfirst = (LinearLayout) dialog.findViewById(R.id.profitfirst);
            final LinearLayout profitafter = (LinearLayout) dialog.findViewById(R.id.profitafter);

            final TextView invoice_text_num = (TextView) dialog.findViewById(R.id.invoice_id);
            invoice_text_num.setText(" فاتوره مبيعات رقم : " + invoice_id);

            final RadioButton agel = (RadioButton) dialog.findViewById(R.id.agel);
            final RadioButton cash = (RadioButton) dialog.findViewById(R.id.cash);
            final RadioButton kest = (RadioButton) dialog.findViewById(R.id.kest);
            final RadioButton month = (RadioButton) dialog.findViewById(R.id.month);

            before_radio = (RadioButton) dialog.findViewById(R.id.before);
            before_radio.setOnClickListener(v -> {
                profitfirst.setVisibility(View.GONE);
                profitafter.setVisibility(View.VISIBLE);
                total_after_first.setText("" + total_after_discount_value);
                first_inkest.setText("");
                profit_percentage.setText("");
            });
            after_radio = (RadioButton) dialog.findViewById(R.id.after);
            after_radio.setOnClickListener(v -> {
                profitfirst.setVisibility(View.VISIBLE);
                profitafter.setVisibility(View.GONE);
                total_after_first.setText("" + total_after_discount_value);
                first_inkest2.setText("");
                profit_percentage2.setText("");

            });
            month.setOnClickListener(v -> {
                if (month.isChecked()) {
                    monthorweekname.setText("عدد الاشهر");
                } else {
                    monthorweekname.setText("عدد الاسابيع");
                }
            });
            if (month.isChecked()) {
                monthorweekname.setText("عدد الاشهر");
            } else {
                monthorweekname.setText("عدد الاسابيع");
            }
            final RadioButton week = (RadioButton) dialog.findViewById(R.id.week);
            week.setOnClickListener(v -> {
                if (month.isChecked()) {
                    monthorweekname.setText("عدد الاشهر");
                } else {
                    monthorweekname.setText("عدد الاسابيع");
                }
            });
            LinearLayout Linear = (LinearLayout) dialog.findViewById(R.id.firstlinear);
            LinearLayout Linear2 = (LinearLayout) dialog.findViewById(R.id.kestlinear);

            TextView total = (TextView) dialog.findViewById(R.id.total);
            TextView total2 = (TextView) dialog.findViewById(R.id.total2);
            total.setText(total_after_discount_value + SheredPrefranseHelper.getmoney_type(sales.this));
            total2.setText(total_after_discount_value + SheredPrefranseHelper.getmoney_type(sales.this));
            total_after_first = (TextView) dialog.findViewById(R.id.totalafterfirst);
            TextView kest_value = (TextView) dialog.findViewById(R.id.kest_value);
            TextView newCustomer = (TextView) dialog.findViewById(R.id.newCustomer);
            newCustomer.setOnClickListener(v -> {
                startActivity(new Intent(this, newcustomer.class));
            });
            TextView newCustomer2 = (TextView) dialog.findViewById(R.id.newCustomer2);
            newCustomer2.setOnClickListener(v -> {
                startActivity(new Intent(this, newcustomer.class));
            });
            firstday = (TextView) dialog.findViewById(R.id.firstdate);
            firstday.setOnClickListener(v -> {
                DateDialog();
            });

            totalpluspercentage = total_after_discount_value;
            final EditText paid = (EditText) dialog.findViewById(R.id.paid);
            final EditText notpaid = (EditText) dialog.findViewById(R.id.notpaid);
            profit_percentage = (EditText) dialog.findViewById(R.id.profit_percentage);
            first_inkest = (EditText) dialog.findViewById(R.id.first);
            profit_percentage2 = (EditText) dialog.findViewById(R.id.profit_percentage2);
            first_inkest2 = (EditText) dialog.findViewById(R.id.first2);
            final EditText damenname = (EditText) dialog.findViewById(R.id.damenname);
            final EditText damenphone = (EditText) dialog.findViewById(R.id.damenphone);

            profit_percentage.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (profit_percentage.getText().toString().equals("")) {
                        if (first_inkest.getText().toString().equals("")) {
                            totalpluspercentage = total_after_discount_value;
                        } else {
                            totalpluspercentage = total_after_discount_value - Double.parseDouble(first_inkest.getText().toString() + "");
                        }

                        total_after_first.setText("" + totalpluspercentage);

                    } else {
                        if (Double.parseDouble(profit_percentage.getText().toString()) > 100) {
                            Toast.makeText(getBaseContext(), "النسبه اكبر من 100 % !", Toast.LENGTH_SHORT).show();
                            total_after_first.setText(totalpluspercentage + "");
                            profit_percentage.setText("");

                        } else {
                            totalpluspercentage = total_after_discount_value + (total_after_discount_value * Double.parseDouble(profit_percentage.getText().toString()) / 100);
                            total_after_first.setText(totalpluspercentage + "");

                        }
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            first_inkest.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (first_inkest.getText().toString().equals("")) {
                        total_after_first.setText(totalpluspercentage + "");

                    } else {
                        if (Double.parseDouble(first_inkest.getText().toString()) > total_after_discount_value) {
                            Toast.makeText(getBaseContext(), "المبلغ المدفوع اكبر من الاجمالي !", Toast.LENGTH_SHORT).show();
                            total_after_first.setText(totalpluspercentage + "");
                            first_inkest.setText("");

                        } else {
                            double notpaidvalue = totalpluspercentage - Double.parseDouble(first_inkest.getText().toString());
                            total_after_first.setText(notpaidvalue + "");

                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });


            first_inkest2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (first_inkest2.getText().toString().equals("")) {
                        total_after_first.setText(total_after_discount_value + "");

                    } else {
                        if (Double.parseDouble(first_inkest2.getText().toString().trim() + "") > total_after_discount_value) {
                            Toast.makeText(getBaseContext(), "المبلغ المدفوع اكبر من الاجمالي !", Toast.LENGTH_SHORT).show();
                            total_after_first.setText(total_after_discount_value + "");
                            first_inkest2.setText("");

                        } else {
                            profit_percentage2.setText("");
                            double notpaidvalue = total_after_discount_value - Double.parseDouble(first_inkest2.getText().toString());
                            total_after_first.setText(notpaidvalue + "");
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });


            profit_percentage2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (profit_percentage2.getText().toString().equals("")) {
                        if (first_inkest2.getText().toString().equals("")) {
                            totalpluspercentage = Round.round(total_after_discount_value, 3);
                        } else {
                            totalpluspercentage = Round.round(total_after_discount_value - Double.parseDouble(first_inkest2.getText().toString() + ""), 3);
                        }

                        total_after_first.setText("" + totalpluspercentage);

                    } else {
                        if (Double.parseDouble(profit_percentage2.getText().toString()) > 100) {
                            Toast.makeText(getBaseContext(), "النسبه اكبر من 100 % !", Toast.LENGTH_SHORT).show();
                            if (first_inkest2.getText().toString().equals("")) {
                                totalpluspercentage = Round.round(total_after_discount_value, 3);
                            } else {
                                totalpluspercentage = Round.round((total_after_discount_value - Double.parseDouble(first_inkest2.getText().toString() + "")), 3);
                            }

                            total_after_first.setText("" + totalpluspercentage);
                            profit_percentage2.setText("");

                        } else {
                            double afterkist = Round.round((total_after_discount_value - Double.parseDouble(first_inkest2.getText().toString() + "")), 3);

                            double totalpluspercentage2 = Round.round((afterkist + (afterkist * Double.parseDouble(profit_percentage2.getText().toString()) / 100)), 3);
                            total_after_first.setText(totalpluspercentage2 + "");

                        }
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });


            final EditText mon_num = (EditText) dialog.findViewById(R.id.mon_num);
            mon_num.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mon_num.getText().toString().equals("")) {
                        kest_value.setText(Round.round(Double.parseDouble(total_after_first.getText().toString()), 3) + "");

                    } else {
                        if (Double.parseDouble(mon_num.getText().toString()) > 200) {
                            Toast.makeText(getBaseContext(), "العدد اكبر من 200 !", Toast.LENGTH_SHORT).show();
                            mon_num.setText("");
                            first_inkest.setText(Round.round(totalpluspercentage, 3) + "");

                        } else {
                            kest_value.setText(Round.round((Double.parseDouble(total_after_first.getText().toString()) / Double.parseDouble(mon_num.getText().toString())), 3) + "");
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            Button save = (Button) dialog.findViewById(R.id.save);

            Button cancel = (Button) dialog.findViewById(R.id.cancel);
            cancel.setOnClickListener(v -> dialog.dismiss());

            omla = (SearchableSpinner) dialog.findViewById(R.id.omla);
            omla2 = (SearchableSpinner) dialog.findViewById(R.id.omla2);

            final Cursor res2 = db.getallomla();
            String omla_names[] = new String[res2.getCount() + 1];

            if (res2.getCount() == 0) {
                omla_names[0] = "اضغط لتحديد عميل";
            } else if (res2 != null && res2.getCount() > 0) {
                int index = 1;
                omla_names[0] = "اضغط لتحديد عميل";
                while (res2.moveToNext()) {
                    omla_names[index] = res2.getString(1);
                    index++;
                }
            }


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, omla_names);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            omla.setAdapter(adapter);
            omla2.setAdapter(adapter);

            if (cash.isChecked()) {
                paid.setText(total_after_discount_value + "");
                paid.setEnabled(false);
                notpaid.setText(0 + "");
                notpaid.setEnabled(false);
                Linear.setVisibility(View.VISIBLE);
                Linear2.setVisibility(View.GONE);

            } else if (agel.isChecked()) {

                paid.setEnabled(true);
                notpaid.setEnabled(true);
                paid.setText(0 + "");
                notpaid.setText(total_after_discount_value + "");
                Linear.setVisibility(View.VISIBLE);
                Linear2.setVisibility(View.GONE);

            } else if (kest.isChecked()) {
                Linear.setVisibility(View.GONE);
                Linear2.setVisibility(View.VISIBLE);
            }
            cash.setOnClickListener(v -> {
                if (cash.isChecked()) {

                    paid.setText(total_after_discount_value + "");
                    paid.setEnabled(false);
                    notpaid.setText(0 + "");
                    notpaid.setEnabled(false);
                    Linear.setVisibility(View.VISIBLE);
                    Linear2.setVisibility(View.GONE);

                } else if (agel.isChecked()) {

                    paid.setEnabled(true);
                    notpaid.setEnabled(true);
                    paid.setText(0 + "");
                    notpaid.setText(total_after_discount_value + "");
                    Linear.setVisibility(View.VISIBLE);
                    Linear2.setVisibility(View.GONE);

                } else if (kest.isChecked()) {

                    Linear.setVisibility(View.GONE);
                    Linear2.setVisibility(View.VISIBLE);
                }
            });
            kest.setOnClickListener(v -> {

                Linear.setVisibility(View.GONE);
                Linear2.setVisibility(View.VISIBLE);
                total_after_first.setText(total_after_discount_value + "");

            });
            agel.setOnClickListener(v -> {
                if (cash.isChecked()) {

                    paid.setText(total_after_discount_value + "");
                    paid.setEnabled(false);
                    notpaid.setText(0 + "");
                    notpaid.setEnabled(false);
                    Linear.setVisibility(View.VISIBLE);
                    Linear2.setVisibility(View.GONE);

                } else if (agel.isChecked()) {

                    paid.setEnabled(true);
                    paid.setText(0 + "");
                    notpaid.setText(total_after_discount_value + "");
                    Linear.setVisibility(View.VISIBLE);
                    Linear2.setVisibility(View.GONE);
                    paid.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (paid.getText().toString().equals("")) {
                                notpaid.setText(total_after_discount_value + "");

                            } else {
                              /*  if (Double.parseDouble(paid.getText().toString()) > total_after_discount_value) {
                                    Toast.makeText(getBaseContext(), "المبلغ المدفوع اكبر من الاجمالي !", Toast.LENGTH_SHORT).show();
                                    notpaid.setText(total_after_discount_value + "");
                                    paid.setText("");

                                } else {*/
                                    double notpaidvalue = Round.round((total_after_discount_value - Double.parseDouble(paid.getText().toString())), 3);
                                    notpaid.setText(notpaidvalue + "");

                              // }
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });


                }
            });
            save.setOnClickListener(v -> {

                invoice invoice = new invoice();

                if (cash.isChecked()) {

                    double notpaidval = Round.round(Double.parseDouble(notpaid.getText().toString()), 3);
                    Cursor res = db.getomla(omla.getSelectedItem().toString());
                    if (res != null && res.getCount() > 0) {
                        while (res.moveToNext()) {
                            if ((res.getDouble(6) + notpaidval) < res.getDouble(7)) {

                            } else {
                                Toast.makeText(getBaseContext(), " فشل البيع العميل تخطي الحد الاقصي للمديونيه ! ", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                return;
                            }
                            Client_not_paid=res.getDouble(6);
                        }

                    }

                    invoice.setInvoice_id(invoice_id + "");
                    if (discount.getText().toString().trim().equals("")) {
                        invoice.setDiscount(0);
                    } else {
                        invoice.setDiscount(Double.parseDouble(discount.getText().toString().trim()));
                    }
                    invoice.setDate(current_date2.getText().toString());
                    invoice.setKind("كاش");
                    if (percentage.isChecked()) {
                        invoice.setDiscount_kind("percentage");
                    } else {
                        invoice.setDiscount_kind("money");
                    }
                    invoice.setNotpaid(Round.round(notpaidval, 3));
                    invoice.setTotal(Round.round(total_after_discount_value, 3));
                    if (SheredPrefranseHelper.getcurrentcashier(this) != null) {
                        invoice.setCashier(SheredPrefranseHelper.getcurrentcashier(this).getName());
                    }
                    if (omla.getSelectedItem().toString().equals("اضغط لتحديد عميل")) {
                        invoice.setCustomer_name("لا يوجد");
                    } else {
                        invoice.setCustomer_name(omla.getSelectedItem().toString());
                    }

                    if (db.insert_date(invoice)) {

                        for (int i = 0; i < productList.size(); i++) {
                            sellproduct sellproduct = new sellproduct();
                            sellproduct.setCode_id(productList.get(i).getCode_id() + "");
                            sellproduct.setInvoice_id(invoice_id + "");
                            sellproduct.setQuantity(quantityList.get(i));
                            sellproduct.setName(productList.get(i).getName() + "");
                            sellproduct.setDescription(productList.get(i).getDescription() + "");
                            if (sales.pricesList.get(i).equals("one")) {
                                sellproduct.setSellprice(Round.round(productList.get(i).getSellprice(), 3));
                            } else if (sales.pricesList.get(i).equals("two")) {
                                sellproduct.setSellprice(Round.round(productList.get(i).getSellprice2(), 3));
                            } else {
                                sellproduct.setSellprice(Round.round(productList.get(i).getSellprice3(), 3));
                            }
                            sellproduct.setBuyprice(productList.get(i).getBuyprice());
                            sellproduct.setDate(current_date2.getText().toString());
                            sellproduct.setItemid(productList.get(i).getItemid());

                            if (db.insert_date(sellproduct)) {
                            } else {
                                Toast.makeText(getBaseContext(), "فشل تسجيل المنتج " + productList.get(i).getName(), Toast.LENGTH_SHORT).show();

                            }
                        }
                        com.mohamedragab.cashpos.modules.moneybox.models.money money = new com.mohamedragab.cashpos.modules.moneybox.models.money();
                        money.setDate(current_date2.getText().toString());
                        money.setNotes("مبيعات فاتوره رقم " + invoice_id);
                        money.setValue(Round.round(total_after_discount_value, 3));

                        final Cursor res3 = db.getallTransactions();
                        double total1 = 0;
                        if (res3 != null && res3.getCount() > 0) {
                            while (res3.moveToNext()) {
                                total1 = res3.getDouble(3);

                            }

                        }
                        money.setTotalbefore(Round.round(total1, 3));
                        double totalAfter = total1 + total_after_discount_value;

                        money.setTotalAfter(Round.round(totalAfter, 3));

                        if (db.insert_date(money)) {
                            product pro;

                            for (int i = 0; i < productList.size(); i++) {
                                pro = productList.get(i);
                                pro.setQuantity(pro.getQuantity() - quantityList.get(i));
                                if (db.updateDatabyName(pro.getName(), pro.getQuantity())) {
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getBaseContext(), "حدث خطا في تعديل البيانات تاكد من ادخال بينات صحيحه !", Toast.LENGTH_SHORT).show();
                                }
                            }
                            omlatransaction omlatransaction = new omlatransaction();

                            omlatransaction.setInvoiceId(invoice_id + "");
                            omlatransaction.setDate(current_date2.getText().toString());
                            if (omla.getSelectedItem().toString().equals("اضغط لتحديد عميل")) {
                                omlatransaction.setName("لا يوجد");
                            } else {
                                omlatransaction.setName(omla.getSelectedItem().toString());
                            }
                            omlatransaction.setNotpaid(0);
                            omlatransaction.setProcess("شراء");
                            omlatransaction.setValue(Round.round(total_after_discount_value, 3));

                            if (db.insert_date(omlatransaction)) {

                            }
                            AtomicReference<Double> delivery_val = new AtomicReference<>(0.0);
                            if (SheredPrefranseHelper.getdelivery(sales.this) != null && SheredPrefranseHelper.getdelivery(sales.this).equals("true")) {
                                final Dialog paydialog = new Dialog(sales.this);
                                paydialog.setContentView(R.layout.adddelivmoneydialog);
                                paydialog.setCancelable(false);

                                TextView title = (TextView) paydialog.findViewById(R.id.title);
                                title.setText("خدمة التوصيل !");
                                EditText money_value = (EditText) paydialog.findViewById(R.id.value);
                                TextView pay_button = (TextView) paydialog.findViewById(R.id.pay);
                                TextView address = (TextView) paydialog.findViewById(R.id.address);
                                Spinner delivery = (Spinner) paydialog.findViewById(R.id.deliv);

                                final Cursor res10 = db.getalldelivery();
                                String deliv_names[] = new String[res10.getCount() + 1];

                                if (res10.getCount() == 0) {
                                    deliv_names[0] = "اضغط لتحديد دليفري";
                                } else if (res10 != null && res10.getCount() > 0) {
                                    int index = 1;
                                    deliv_names[0] = "اضغط لتحديد دليفري";
                                    while (res10.moveToNext()) {
                                        deliv_names[index] = res10.getString(1);
                                        index++;
                                    }
                                }

                                ArrayAdapter<String> adapter10 = new ArrayAdapter<String>(this,
                                        android.R.layout.simple_spinner_item, deliv_names);
                                adapter10.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                delivery.setAdapter(adapter10);

                                pay_button.setOnClickListener(v2 -> {
                                    if (delivery.getSelectedItem().toString().equals("اضغط لتحديد دليفري")) {
                                        Toast.makeText(this, "برجاء تحديد اسم الدليفري !", Toast.LENGTH_SHORT).show();
                                    } else {

                                        if (money_value.getText() != null && !money_value.getText().toString().trim().equals("")) {
                                            delivery_val.set(Double.parseDouble(money_value.getText().toString() + ""));

                                        } else {
                                            delivery_val.set(0.0);
                                        }
                                        DelivTrans trans = new DelivTrans();
                                        trans.setInvoice_id(invoice_id + "");
                                        trans.setDate(current_date2.getText().toString() + "");
                                        trans.setAddress(address.getText().toString() + "");
                                        trans.setMoney(delivery_val.get());
                                        trans.setDeliv_name(delivery.getSelectedItem().toString() + "");
                                        if (db.insert_date(trans)) {

                                            paydialog.dismiss();
                                            final Dialog after_dialog = new Dialog(sales.this);
                                            after_dialog.setContentView(R.layout.after_sales_dialog);
                                            after_dialog.setCancelable(false);


                                            ImageView print = (ImageView) after_dialog.findViewById(R.id.print);
                                            ImageView share = (ImageView) after_dialog.findViewById(R.id.share);
                                            ImageView close = (ImageView) after_dialog.findViewById(R.id.cancel);
                                            LinearLayout invoice_image = (LinearLayout) after_dialog.findViewById(R.id.linear_invoice);
                                            TableLayout table = (TableLayout) after_dialog.findViewById(R.id.table);
                                            for (int i = 0; i < sales.productList.size(); i++) {
                                                TableRow row = (TableRow) LayoutInflater.from(sales.this).inflate(R.layout.row_item, null);
                                                ((TextView) row.findViewById(R.id.name)).setText(productList.get(i).getName() + "");
                                                ((TextView) row.findViewById(R.id.price)).setText(productList.get(i).getSellprice() + "");
                                                ((TextView) row.findViewById(R.id.quantity)).setText(quantityList.get(i) + "");
                                                if (sales.pricesList.get(i).equals("one")) {
                                                    ((TextView) row.findViewById(R.id.price)).setText(productList.get(i).getSellprice() + "");
                                                    ((TextView) row.findViewById(R.id.total)).setText((quantityList.get(i) * productList.get(i).getSellprice()) + "");
                                                } else if (sales.pricesList.get(i).equals("two")) {
                                                    ((TextView) row.findViewById(R.id.price)).setText(productList.get(i).getSellprice2() + "");
                                                    ((TextView) row.findViewById(R.id.total)).setText((quantityList.get(i) * productList.get(i).getSellprice2()) + "");
                                                } else {
                                                    ((TextView) row.findViewById(R.id.price)).setText(productList.get(i).getSellprice3() + "");
                                                    ((TextView) row.findViewById(R.id.total)).setText((quantityList.get(i) * productList.get(i).getSellprice3()) + "");
                                                }
                                                table.addView(row);
                                            }
                                            table.requestLayout();

                                            String ClientName = "";
                                            if (omla.getSelectedItem().toString().equals("اضغط لتحديد عميل")) {
                                                ClientName = "عميل جديد";
                                            } else {
                                                ClientName = omla.getSelectedItem().toString();
                                            }
                                            invoice_number = invoice_id + "";

                                            TextView date = (TextView) after_dialog.findViewById(R.id.date);
                                            date.setText(current_date2.getText().toString());
                                            TextView time = (TextView) after_dialog.findViewById(R.id.time);
                                            String currentTime = new SimpleDateFormat("hh:mm:ss a", Locale.US).format(new Date());
                                            time.setText(currentTime);
                                            TextView invoice_num = (TextView) after_dialog.findViewById(R.id.invoice);
                                            invoice_num.setText(invoice_number + "");
                                            TextView client = (TextView) after_dialog.findViewById(R.id.client);
                                            client.setText(ClientName + "");
                                            TextView cashier = (TextView) after_dialog.findViewById(R.id.cashier);
                                            if (SheredPrefranseHelper.getcurrentcashier(sales.this) != null) {
                                                if (SheredPrefranseHelper.getcurrentcashier(sales.this).getName() != null) {
                                                    cashier.setText("" + SheredPrefranseHelper.getcurrentcashier(sales.this).getName() + "");
                                                } else {
                                                    cashier.setText("" + "لا يوجد");
                                                }
                                            } else {
                                                cashier.setText("" + "لا يوجد");
                                            }
                                            TextView total_all_text = (TextView) after_dialog.findViewById(R.id.total_all);
                                            total_all_text.setText(Round.round(total_value, 3) + "");
                                            TextView total_after_discount_text = (TextView) after_dialog.findViewById(R.id.afterdiscount);
                                            total_after_discount_text.setText(Round.round(total_after_discount_value, 3) + "");
                                            TextView invoice_kind = (TextView) after_dialog.findViewById(R.id.pay_kind);
                                            invoice_kind.setText("كاش");
                                            TextView paid_text = (TextView) after_dialog.findViewById(R.id.paid);
                                            paid_text.setText(Round.round(total_after_discount_value, 3) + "");
                                            TextView not_paid_text = (TextView) after_dialog.findViewById(R.id.not_paid);
                                            not_paid_text.setText(0 + "");
                                            TextView notification_text = (TextView) after_dialog.findViewById(R.id.notification);
                                            TextView address_phone = (TextView) after_dialog.findViewById(R.id.address_phone);
                                            TextView shopname = (TextView) after_dialog.findViewById(R.id.shopname);
                                            ImageView logo = (ImageView) after_dialog.findViewById(R.id.logo);
                                            shopInfo info = SheredPrefranseHelper.getshopData(sales.this);
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
                                            TextView total_not_paid_txt = (TextView) after_dialog.findViewById(R.id.total_not_paid);
                                            total_not_paid_txt.setVisibility(View.VISIBLE);
                                            total_not_paid_txt.setText((Client_not_paid)+"");

                                            LinearLayout delivery_lin = (LinearLayout) after_dialog.findViewById(R.id.delivery_lin);
                                            if (SheredPrefranseHelper.getdelivery(sales.this) != null) {
                                                if (SheredPrefranseHelper.getdelivery(sales.this).equals("true")) {
                                                } else {
                                                    delivery_lin.setVisibility(View.GONE);
                                                }
                                            } else {
                                                delivery_lin.setVisibility(View.GONE);
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
                                                    File file = new File(Environment.getExternalStorageDirectory() + "/cashpos/invoices/", invoice_number + ".png");
                                                    FileOutputStream out = new FileOutputStream(file);
                                                    returnedBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                                                    out.close();
                                                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                                    Uri screenshotUri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/cashpos/invoices/" + invoice_number + ".png");
                                                    sharingIntent.setType("image/png");
                                                    sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                                                    startActivity(Intent.createChooser(sharingIntent, "مشاركة الفاتورة"));
                                                    Toast.makeText(getBaseContext(), "تم حفظ الفاتورة في : " + file.toString(), Toast.LENGTH_LONG).show();

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
                                                    File file = new File(Environment.getExternalStorageDirectory() + "/cashpos/invoices/", invoice_number + ".png");
                                                    FileOutputStream out = new FileOutputStream(file);
                                                    returnedBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                                                    out.close();
                                                    Toast.makeText(getBaseContext(), "تم حفظ الفاتورة في : " + file.toString(), Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(sales.this, sales.class));
                                                    finish();
                                                } catch (FileNotFoundException e) {
                                                    e.printStackTrace();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            });
                                            after_dialog.show();
                                        }
                                    }

                                });
                                Button cancel2 = (Button) paydialog.findViewById(R.id.cancel);
                                cancel2.setOnClickListener(v2 -> {
                                    paydialog.dismiss();
                                    final Dialog after_dialog = new Dialog(sales.this);
                                    after_dialog.setContentView(R.layout.after_sales_dialog);
                                    after_dialog.setCancelable(false);

                                    ImageView print = (ImageView) after_dialog.findViewById(R.id.print);
                                    ImageView share = (ImageView) after_dialog.findViewById(R.id.share);
                                    ImageView close = (ImageView) after_dialog.findViewById(R.id.cancel);
                                    LinearLayout invoice_image = (LinearLayout) after_dialog.findViewById(R.id.linear_invoice);
                                    TableLayout table = (TableLayout) after_dialog.findViewById(R.id.table);
                                    for (int i = 0; i < sales.productList.size(); i++) {
                                        TableRow row = (TableRow) LayoutInflater.from(sales.this).inflate(R.layout.row_item, null);
                                        ((TextView) row.findViewById(R.id.name)).setText(productList.get(i).getName() + "");
                                        ((TextView) row.findViewById(R.id.price)).setText(productList.get(i).getSellprice() + "");
                                        ((TextView) row.findViewById(R.id.quantity)).setText(quantityList.get(i) + "");
                                        if (sales.pricesList.get(i).equals("one")) {
                                            ((TextView) row.findViewById(R.id.price)).setText(productList.get(i).getSellprice() + "");
                                            ((TextView) row.findViewById(R.id.total)).setText((quantityList.get(i) * productList.get(i).getSellprice()) + "");
                                        } else if (sales.pricesList.get(i).equals("two")) {
                                            ((TextView) row.findViewById(R.id.price)).setText(productList.get(i).getSellprice2() + "");
                                            ((TextView) row.findViewById(R.id.total)).setText((quantityList.get(i) * productList.get(i).getSellprice2()) + "");
                                        } else {
                                            ((TextView) row.findViewById(R.id.price)).setText(productList.get(i).getSellprice3() + "");
                                            ((TextView) row.findViewById(R.id.total)).setText((quantityList.get(i) * productList.get(i).getSellprice3()) + "");
                                        }
                                        table.addView(row);
                                    }
                                    table.requestLayout();

                                    String ClientName = "";
                                    if (omla.getSelectedItem().toString().equals("اضغط لتحديد عميل")) {
                                        ClientName = "عميل جديد";
                                    } else {
                                        ClientName = omla.getSelectedItem().toString();
                                    }
                                    invoice_number = invoice_id + "";

                                    TextView date = (TextView) after_dialog.findViewById(R.id.date);
                                    date.setText(current_date2.getText().toString());
                                    TextView time = (TextView) after_dialog.findViewById(R.id.time);
                                    String currentTime = new SimpleDateFormat("hh:mm:ss a", Locale.US).format(new Date());
                                    time.setText(currentTime);
                                    TextView invoice_num = (TextView) after_dialog.findViewById(R.id.invoice);
                                    invoice_num.setText(invoice_number + "");
                                    TextView client = (TextView) after_dialog.findViewById(R.id.client);
                                    client.setText(ClientName + "");
                                    TextView cashier = (TextView) after_dialog.findViewById(R.id.cashier);
                                    if (SheredPrefranseHelper.getcurrentcashier(sales.this) != null) {
                                        if (SheredPrefranseHelper.getcurrentcashier(sales.this).getName() != null) {
                                            cashier.setText("" + SheredPrefranseHelper.getcurrentcashier(sales.this).getName() + "");
                                        } else {
                                            cashier.setText("" + "لا يوجد");
                                        }
                                    } else {
                                        cashier.setText("" + "لا يوجد");
                                    }
                                    TextView total_all_text = (TextView) after_dialog.findViewById(R.id.total_all);
                                    total_all_text.setText(Round.round(total_value, 3) + "");
                                    TextView total_after_discount_text = (TextView) after_dialog.findViewById(R.id.afterdiscount);
                                    total_after_discount_text.setText(Round.round(total_after_discount_value, 3) + "");
                                    TextView invoice_kind = (TextView) after_dialog.findViewById(R.id.pay_kind);
                                    invoice_kind.setText("كاش");
                                    TextView paid_text = (TextView) after_dialog.findViewById(R.id.paid);
                                    paid_text.setText(Round.round(total_after_discount_value, 3) + "");
                                    TextView not_paid_text = (TextView) after_dialog.findViewById(R.id.not_paid);
                                    not_paid_text.setText(0 + "");
                                    TextView notification_text = (TextView) after_dialog.findViewById(R.id.notification);
                                    TextView address_phone = (TextView) after_dialog.findViewById(R.id.address_phone);
                                    TextView shopname = (TextView) after_dialog.findViewById(R.id.shopname);
                                    ImageView logo = (ImageView) after_dialog.findViewById(R.id.logo);
                                    shopInfo info = SheredPrefranseHelper.getshopData(sales.this);
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
                                    TextView total_not_paid_txt = (TextView) after_dialog.findViewById(R.id.total_not_paid);
                                    total_not_paid_txt.setVisibility(View.VISIBLE);
                                    total_not_paid_txt.setText((Client_not_paid)+"");

                                    LinearLayout delivery_lin = (LinearLayout) after_dialog.findViewById(R.id.delivery_lin);
                                    if (SheredPrefranseHelper.getdelivery(sales.this) != null) {
                                        if (SheredPrefranseHelper.getdelivery(sales.this).equals("true")) {
                                        } else {
                                            delivery_lin.setVisibility(View.GONE);
                                        }
                                    } else {
                                        delivery_lin.setVisibility(View.GONE);
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
                                            File file = new File(Environment.getExternalStorageDirectory() + "/cashpos/invoices/", invoice_number + ".png");
                                            FileOutputStream out = new FileOutputStream(file);
                                            returnedBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                                            out.close();
                                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                            Uri screenshotUri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/cashpos/invoices/" + invoice_number + ".png");
                                            sharingIntent.setType("image/png");
                                            sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                                            startActivity(Intent.createChooser(sharingIntent, "مشاركة الفاتورة"));
                                            Toast.makeText(getBaseContext(), "تم حفظ الفاتورة في : " + file.toString(), Toast.LENGTH_LONG).show();

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
                                            File file = new File(Environment.getExternalStorageDirectory() + "/cashpos/invoices/", invoice_number + ".png");
                                            FileOutputStream out = new FileOutputStream(file);
                                            returnedBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                                            out.close();
                                            Toast.makeText(getBaseContext(), "تم حفظ الفاتورة في : " + file.toString(), Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(sales.this, sales.class));
                                            finish();
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    });
                                    try {
                                        after_dialog.show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                });
                                paydialog.show();
                            } else {
                                final Dialog after_dialog = new Dialog(sales.this);
                                after_dialog.setContentView(R.layout.after_sales_dialog);
                                after_dialog.setCancelable(false);

                                ImageView print = (ImageView) after_dialog.findViewById(R.id.print);
                                ImageView share = (ImageView) after_dialog.findViewById(R.id.share);
                                ImageView close = (ImageView) after_dialog.findViewById(R.id.cancel);
                                LinearLayout invoice_image = (LinearLayout) after_dialog.findViewById(R.id.linear_invoice);
                                TableLayout table = (TableLayout) after_dialog.findViewById(R.id.table);
                                for (int i = 0; i < sales.productList.size(); i++) {
                                    TableRow row = (TableRow) LayoutInflater.from(sales.this).inflate(R.layout.row_item, null);
                                    ((TextView) row.findViewById(R.id.name)).setText(productList.get(i).getName() + "");
                                    ((TextView) row.findViewById(R.id.price)).setText(productList.get(i).getSellprice() + "");
                                    ((TextView) row.findViewById(R.id.quantity)).setText(quantityList.get(i) + "");
                                    if (sales.pricesList.get(i).equals("one")) {
                                        ((TextView) row.findViewById(R.id.price)).setText(productList.get(i).getSellprice() + "");
                                        ((TextView) row.findViewById(R.id.total)).setText((quantityList.get(i) * productList.get(i).getSellprice()) + "");
                                    } else if (sales.pricesList.get(i).equals("two")) {
                                        ((TextView) row.findViewById(R.id.price)).setText(productList.get(i).getSellprice2() + "");
                                        ((TextView) row.findViewById(R.id.total)).setText((quantityList.get(i) * productList.get(i).getSellprice2()) + "");
                                    } else {
                                        ((TextView) row.findViewById(R.id.price)).setText(productList.get(i).getSellprice3() + "");
                                        ((TextView) row.findViewById(R.id.total)).setText((quantityList.get(i) * productList.get(i).getSellprice3()) + "");
                                    }
                                    table.addView(row);
                                }
                                table.requestLayout();

                                String ClientName = "";
                                if (omla.getSelectedItem().toString().equals("اضغط لتحديد عميل")) {
                                    ClientName = "عميل جديد";
                                } else {
                                    ClientName = omla.getSelectedItem().toString();
                                }
                                invoice_number = invoice_id + "";

                                TextView date = (TextView) after_dialog.findViewById(R.id.date);
                                date.setText(current_date2.getText().toString());
                                TextView time = (TextView) after_dialog.findViewById(R.id.time);
                                String currentTime = new SimpleDateFormat("hh:mm:ss a", Locale.US).format(new Date());
                                time.setText(currentTime);
                                TextView invoice_num = (TextView) after_dialog.findViewById(R.id.invoice);
                                invoice_num.setText(invoice_number + "");
                                TextView client = (TextView) after_dialog.findViewById(R.id.client);
                                client.setText(ClientName + "");
                                TextView cashier = (TextView) after_dialog.findViewById(R.id.cashier);
                                if (SheredPrefranseHelper.getcurrentcashier(sales.this) != null) {
                                    if (SheredPrefranseHelper.getcurrentcashier(sales.this).getName() != null) {
                                        cashier.setText("" + SheredPrefranseHelper.getcurrentcashier(sales.this).getName() + "");
                                    } else {
                                        cashier.setText("" + "لا يوجد");
                                    }
                                } else {
                                    cashier.setText("" + "لا يوجد");
                                }
                                TextView total_all_text = (TextView) after_dialog.findViewById(R.id.total_all);
                                total_all_text.setText(Round.round(total_value, 3) + "");
                                TextView total_after_discount_text = (TextView) after_dialog.findViewById(R.id.afterdiscount);
                                total_after_discount_text.setText(Round.round(total_after_discount_value, 3) + "");
                                TextView invoice_kind = (TextView) after_dialog.findViewById(R.id.pay_kind);
                                invoice_kind.setText("كاش");
                                TextView paid_text = (TextView) after_dialog.findViewById(R.id.paid);
                                paid_text.setText(Round.round(total_after_discount_value, 3) + "");
                                TextView not_paid_text = (TextView) after_dialog.findViewById(R.id.not_paid);
                                not_paid_text.setText(0 + "");
                                TextView notification_text = (TextView) after_dialog.findViewById(R.id.notification);
                                TextView address_phone = (TextView) after_dialog.findViewById(R.id.address_phone);
                                TextView shopname = (TextView) after_dialog.findViewById(R.id.shopname);
                                ImageView logo = (ImageView) after_dialog.findViewById(R.id.logo);
                                shopInfo info = SheredPrefranseHelper.getshopData(sales.this);
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
                                TextView total_not_paid_txt = (TextView) after_dialog.findViewById(R.id.total_not_paid);
                                total_not_paid_txt.setVisibility(View.VISIBLE);
                                total_not_paid_txt.setText(Client_not_paid+"");

                                LinearLayout delivery_lin = (LinearLayout) after_dialog.findViewById(R.id.delivery_lin);
                                if (SheredPrefranseHelper.getdelivery(sales.this) != null) {
                                    if (SheredPrefranseHelper.getdelivery(sales.this).equals("true")) {
                                    } else {
                                        delivery_lin.setVisibility(View.GONE);
                                    }
                                } else {
                                    delivery_lin.setVisibility(View.GONE);
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
                                        File file = new File(Environment.getExternalStorageDirectory() + "/cashpos/invoices/", invoice_number + ".png");
                                        FileOutputStream out = new FileOutputStream(file);
                                        returnedBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                                        out.close();
                                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                        Uri screenshotUri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/cashpos/invoices/" + invoice_number + ".png");
                                        sharingIntent.setType("image/png");
                                        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                                        startActivity(Intent.createChooser(sharingIntent, "مشاركة الفاتورة"));
                                        Toast.makeText(getBaseContext(), "تم حفظ الفاتورة في : " + file.toString(), Toast.LENGTH_LONG).show();

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
                                        File file = new File(Environment.getExternalStorageDirectory() + "/cashpos/invoices/", invoice_number + ".png");
                                        FileOutputStream out = new FileOutputStream(file);
                                        returnedBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                                        out.close();
                                        Toast.makeText(getBaseContext(), "تم حفظ الفاتورة في : " + file.toString(), Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(sales.this, sales.class));
                                        finish();
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                });
                                after_dialog.show();
                            }


                        } else {
                            Toast.makeText(getBaseContext(), "فشل اضافه المبلغ في الصندوق ! ", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        //Toast.makeText(getBaseContext(), "فشل تسجيل الفاتوره تحقق من ادخال بيانات صحيحه", Toast.LENGTH_SHORT).show();
                    }

                    dialog.dismiss();

                }
                else if (agel.isChecked()) {

                    if (omla.getSelectedItem().toString().equals("اضغط لتحديد عميل")) {
                        Toast.makeText(getBaseContext(), "برجاء اختيار عميل !", Toast.LENGTH_SHORT).show();
                    } else {

                        double notpaidval = Round.round(Double.parseDouble(notpaid.getText().toString()), 3);
                        Cursor res = db.getomla(omla.getSelectedItem().toString());
                        if (res != null && res.getCount() > 0) {
                            while (res.moveToNext()) {
                                if ((res.getDouble(6) + notpaidval) < res.getDouble(7)) {

                                } else {

                                    Toast.makeText(getBaseContext(), " فشل البيع العميل تخطي الحد الاقصي للمديونيه ! ", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    return;
                                }
                                Client_not_paid=res.getDouble(6);

                            }

                        }

                        invoice.setInvoice_id(invoice_id + "");
                        invoice.setDate(current_date2.getText().toString());
                        if (discount.getText().toString().trim().equals("")) {
                            invoice.setDiscount(0);
                        } else {
                            invoice.setDiscount(Double.parseDouble(discount.getText().toString().trim()));
                        }
                        if (percentage.isChecked()) {
                            invoice.setDiscount_kind("percentage");
                        } else {
                            invoice.setDiscount_kind("money");
                        }
                        invoice.setKind("أجل");
                        invoice.setNotpaid(notpaidval);
                        invoice.setTotal(total_after_discount_value);
                        if (SheredPrefranseHelper.getcurrentcashier(this) != null) {
                            invoice.setCashier(SheredPrefranseHelper.getcurrentcashier(this).getName());
                        }
                        invoice.setCustomer_name(omla.getSelectedItem().toString());
                        Cursor res4 = db.getomla(omla.getSelectedItem().toString());
                        double notpaidval2 = Round.round(Double.parseDouble(notpaid.getText().toString()), 3);

                        if (res4 != null && res4.getCount() > 0) {
                            while (res4.moveToNext()) {
                                com.mohamedragab.cashpos.modules.omla.models.omla omla1 = new omla();
                                // Log.d("test","id "+res.getInt(0));
                                omla1.setId(res4.getInt(0));
                                omla1.setName(res4.getString(1));
                                double newsouldpayvalue = res4.getDouble(6) + notpaidval2;
                                omla1.setPaymoney(newsouldpayvalue);
                                omla1.setHasmoney(res4.getDouble(5));
                                omla1.setAddress(res4.getString(2));
                                omla1.setPhone(res4.getString(3));
                                omla1.setNotes(res4.getString(4));
                                omla1.setMaxnotpaid(res4.getDouble(7));

                                if (db.updateData(omla1)) {
                                    // Toast.makeText(sales.this, "تم اضافه المبلغ لحساب العميل !", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getBaseContext(), "لم يحدث تغيير في حساب العميل !", Toast.LENGTH_SHORT).show();

                                }
                            }

                        } else {
                            Toast.makeText(getBaseContext(), "لا يوجد عملاء بهذا الاسم !", Toast.LENGTH_SHORT).show();
                        }
                        if (db.insert_date(invoice)) {

                            for (int i = 0; i < productList.size(); i++) {
                                sellproduct sellproduct = new sellproduct();
                                sellproduct.setCode_id(productList.get(i).getCode_id() + "");
                                sellproduct.setInvoice_id(invoice_id + "");
                                sellproduct.setQuantity(quantityList.get(i));
                                sellproduct.setName(productList.get(i).getName() + "");
                                sellproduct.setDescription(productList.get(i).getDescription() + "");
                                if (sales.pricesList.get(i).equals("one")) {
                                    sellproduct.setSellprice(Round.round(productList.get(i).getSellprice(), 3));
                                } else if (sales.pricesList.get(i).equals("two")) {
                                    sellproduct.setSellprice(Round.round(productList.get(i).getSellprice2(), 3));
                                } else {
                                    sellproduct.setSellprice(Round.round(productList.get(i).getSellprice3(), 3));
                                }
                                sellproduct.setBuyprice(productList.get(i).getBuyprice());
                                sellproduct.setDate(current_date2.getText().toString());
                                sellproduct.setItemid(productList.get(i).getItemid());

                                if (db.insert_date(sellproduct)) {
                                } else {
                                    Toast.makeText(getBaseContext(), "فشل تسجيل المنتج " + productList.get(i).getName(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            com.mohamedragab.cashpos.modules.moneybox.models.money money = new com.mohamedragab.cashpos.modules.moneybox.models.money();
                            money.setDate(current_date2.getText().toString());
                            money.setNotes("مبيعات فاتوره رقم " + invoice_id);
                            money.setValue(Round.round((total_after_discount_value - notpaidval), 3));

                            final Cursor res3 = db.getallTransactions();
                            double total1 = 0;
                            if (res3 != null && res3.getCount() > 0) {
                                while (res3.moveToNext()) {
                                    total1 = res3.getDouble(3);

                                }

                            }
                            money.setTotalbefore(Round.round(total1, 3));
                            double totalAfter = total1 + (total_after_discount_value - notpaidval);

                            money.setTotalAfter(Round.round(totalAfter, 3));

                            if (db.insert_date(money)) {
                                product pro;

                                for (int i = 0; i < productList.size(); i++) {
                                    pro = productList.get(i);
                                    pro.setQuantity(pro.getQuantity() - quantityList.get(i));
                                    Boolean result4 = db.updateDatabyName(pro.getName(), pro.getQuantity());
                                    if (result4) {
                                        //    Toast.makeText(sales.this, "تم تعديل المنتج بنجاح !", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();

                                    } else {
                                        Toast.makeText(getBaseContext(), "حدث خطا في تعديل البيانات تاكد من ادخال بينات صحيحه !", Toast.LENGTH_SHORT).show();

                                    }
                                }
                                omlatransaction omlatransaction = new omlatransaction();

                                omlatransaction.setInvoiceId(invoice_id + "");
                                omlatransaction.setDate(current_date2.getText().toString());
                                omlatransaction.setName(omla.getSelectedItem().toString());
                                omlatransaction.setNotpaid(Round.round(notpaidval, 3));
                                omlatransaction.setProcess("شراء");
                                omlatransaction.setValue(Round.round((total_after_discount_value - notpaidval), 3));

                                if (db.insert_date(omlatransaction)) {
                                    // Toast.makeText(sales.this, "تم تسجيل تعاملات العميل ", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Toast.makeText(getBaseContext(), "فشل تسجيل تعاملات العميل", Toast.LENGTH_SHORT).show();

                                }
                                final Dialog after_dialog = new Dialog(sales.this);
                                after_dialog.setContentView(R.layout.after_sales_dialog);
                                after_dialog.setCancelable(false);

                                TextView invoice_text = (TextView) after_dialog.findViewById(R.id.invoice_num);
                                ImageView print = (ImageView) after_dialog.findViewById(R.id.print);
                                ImageView share = (ImageView) after_dialog.findViewById(R.id.share);
                                ImageView close = (ImageView) after_dialog.findViewById(R.id.cancel);

                                LinearLayout invoice_image = (LinearLayout) after_dialog.findViewById(R.id.linear_invoice);
                                TableLayout table = (TableLayout) after_dialog.findViewById(R.id.table);
                                for (int i = 0; i < sales.productList.size(); i++) {
                                    TableRow row = (TableRow) LayoutInflater.from(sales.this).inflate(R.layout.row_item, null);
                                    ((TextView) row.findViewById(R.id.name)).setText(productList.get(i).getName() + "");
                                    ((TextView) row.findViewById(R.id.price)).setText(productList.get(i).getSellprice() + "");
                                    ((TextView) row.findViewById(R.id.quantity)).setText(quantityList.get(i) + "");
                                    if (sales.pricesList.get(i).equals("one")) {
                                        ((TextView) row.findViewById(R.id.price)).setText(productList.get(i).getSellprice() + "");
                                        ((TextView) row.findViewById(R.id.total)).setText((quantityList.get(i) * productList.get(i).getSellprice()) + "");
                                    } else if (sales.pricesList.get(i).equals("two")) {
                                        ((TextView) row.findViewById(R.id.price)).setText(productList.get(i).getSellprice2() + "");
                                        ((TextView) row.findViewById(R.id.total)).setText((quantityList.get(i) * productList.get(i).getSellprice2()) + "");
                                    } else {
                                        ((TextView) row.findViewById(R.id.price)).setText(productList.get(i).getSellprice3() + "");
                                        ((TextView) row.findViewById(R.id.total)).setText((quantityList.get(i) * productList.get(i).getSellprice3()) + "");
                                    }
                                    table.addView(row);
                                }
                                table.requestLayout();

                                String ClientName = "";
                                if (omla.getSelectedItem().toString().equals("اضغط لتحديد عميل")) {
                                    ClientName = "عميل جديد";
                                } else {
                                    ClientName = omla.getSelectedItem().toString();
                                }
                                invoice_number = invoice_id + "";

                                TextView date = (TextView) after_dialog.findViewById(R.id.date);
                                date.setText(current_date2.getText().toString());
                                TextView time = (TextView) after_dialog.findViewById(R.id.time);
                                String currentTime = new SimpleDateFormat("hh:mm:ss a", Locale.US).format(new Date());
                                time.setText(currentTime);
                                TextView invoice_num = (TextView) after_dialog.findViewById(R.id.invoice);
                                invoice_num.setText(invoice_number + "");
                                TextView client = (TextView) after_dialog.findViewById(R.id.client);
                                client.setText(ClientName + "");
                                TextView cashier = (TextView) after_dialog.findViewById(R.id.cashier);
                                if (SheredPrefranseHelper.getcurrentcashier(sales.this) != null) {
                                    if (SheredPrefranseHelper.getcurrentcashier(sales.this).getName() != null) {
                                        cashier.setText("" + SheredPrefranseHelper.getcurrentcashier(sales.this).getName() + "");
                                    } else {
                                        cashier.setText("" + "لا يوجد");
                                    }
                                } else {
                                    cashier.setText("" + "لا يوجد");
                                }
                                TextView total_all_text = (TextView) after_dialog.findViewById(R.id.total_all);
                                total_all_text.setText(Round.round(total_value, 3) + "");
                                TextView total_after_discount_text = (TextView) after_dialog.findViewById(R.id.afterdiscount);
                                total_after_discount_text.setText(Round.round(total_after_discount_value, 3) + "");
                                TextView invoice_kind = (TextView) after_dialog.findViewById(R.id.pay_kind);
                                invoice_kind.setText("أجل");
                                TextView paid_text = (TextView) after_dialog.findViewById(R.id.paid);
                                paid_text.setText(paid.getText().toString().trim() + "");
                                TextView not_paid_text = (TextView) after_dialog.findViewById(R.id.not_paid);
                                not_paid_text.setText(notpaidval + "");
                                TextView notification_text = (TextView) after_dialog.findViewById(R.id.notification);
                                TextView address_phone = (TextView) after_dialog.findViewById(R.id.address_phone);
                                TextView shopname = (TextView) after_dialog.findViewById(R.id.shopname);
                                ImageView logo = (ImageView) after_dialog.findViewById(R.id.logo);
                                shopInfo info = SheredPrefranseHelper.getshopData(sales.this);
                                TextView total_not_paid_txt = (TextView) after_dialog.findViewById(R.id.total_not_paid);
                                total_not_paid_txt.setVisibility(View.VISIBLE);
                                total_not_paid_txt.setText((Client_not_paid+notpaidval)+"");
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
                                LinearLayout delivery_lin = (LinearLayout) after_dialog.findViewById(R.id.delivery_lin);
                                if (SheredPrefranseHelper.getdelivery(sales.this) != null) {
                                    if (SheredPrefranseHelper.getdelivery(sales.this).equals("true")) {
                                        delivery_lin.setVisibility(View.GONE);

                                    } else {
                                        delivery_lin.setVisibility(View.GONE);
                                    }
                                } else {
                                    delivery_lin.setVisibility(View.GONE);
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
                                        File file = new File(Environment.getExternalStorageDirectory() + "/cashpos/invoices/", invoice_number + ".png");
                                        FileOutputStream out = new FileOutputStream(file);
                                        returnedBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                                        out.close();
                                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                        Uri screenshotUri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/cashpos/invoices/" + invoice_number + ".png");
                                        sharingIntent.setType("image/png");
                                        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                                        startActivity(Intent.createChooser(sharingIntent, "مشاركة الفاتورة"));
                                        Toast.makeText(getBaseContext(), "تم حفظ الفاتورة في : " + file.toString(), Toast.LENGTH_LONG).show();

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
                                        File file = new File(Environment.getExternalStorageDirectory() + "/cashpos/invoices/", invoice_number + ".png");
                                        FileOutputStream out = new FileOutputStream(file);
                                        returnedBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                                        out.close();
                                        Toast.makeText(getBaseContext(), "تم حفظ الفاتورة في : " + file.toString(), Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(sales.this, sales.class));
                                        finish();
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                                after_dialog.show();

                            } else {
                                Toast.makeText(getBaseContext(), "فشل اضافه المبلغ في الصندوق ! ", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            // Toast.makeText(sales.this, "فشل تسجيل الفاتوره تحقق من ادخال بيانات صحيحه", Toast.LENGTH_SHORT).show();

                        }


                        dialog.dismiss();

                    }
                } else if (kest.isChecked()) {

                    if (mon_num.getText().toString().equals("") || firstday.getText().toString().equals("")) {
                        if (before_radio.isChecked()) {
                            if (first_inkest.getText().toString().equals("")) {
                                Toast.makeText(getBaseContext(), "برجاء ادخال قيمة المقدم !", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (first_inkest2.getText().toString().equals("")) {
                                Toast.makeText(getBaseContext(), "برجاء ادخال قيمة المقدم !", Toast.LENGTH_SHORT).show();
                            }
                        }
                        Toast.makeText(getBaseContext(), "برجاء تكملة البيانات الفارغه !", Toast.LENGTH_SHORT).show();
                    } else {

                        if (omla2.getSelectedItem().toString().equals("اضغط لتحديد عميل")) {
                            Toast.makeText(getBaseContext(), "برجاء اختيار عميل !", Toast.LENGTH_SHORT).show();
                        } else {

                            invoice.setInvoice_id(invoice_id + "");
                            invoice.setDate(current_date2.getText().toString());
                            if (discount.getText().toString().trim().equals("")) {
                                invoice.setDiscount(0);
                            } else {
                                invoice.setDiscount(Double.parseDouble(discount.getText().toString().trim()));
                            }
                            invoice.setKind("قسط");
                            if (percentage.isChecked()) {
                                invoice.setDiscount_kind("percentage");
                            } else {
                                invoice.setDiscount_kind("money");
                            }
                            invoice.setNotpaid(Round.round(Double.parseDouble(total_after_first.getText().toString() + ""), 3));
                            invoice.setTotal(Round.round(total_after_discount_value, 3));
                            if (SheredPrefranseHelper.getcurrentcashier(this) != null) {
                                invoice.setCashier(SheredPrefranseHelper.getcurrentcashier(this).getName());
                            }
                            invoice.setCustomer_name(omla2.getSelectedItem().toString());

                            if (db.insert_date(invoice)) {

                                for (int i = 0; i < productList.size(); i++) {
                                    sellproduct sellproduct = new sellproduct();
                                    sellproduct.setCode_id(productList.get(i).getCode_id() + "");
                                    sellproduct.setInvoice_id(invoice_id + "");
                                    sellproduct.setQuantity(quantityList.get(i));
                                    sellproduct.setName(productList.get(i).getName() + "");
                                    sellproduct.setDescription(productList.get(i).getDescription() + "");
                                    if (sales.pricesList.get(i).equals("one")) {
                                        sellproduct.setSellprice(Round.round(productList.get(i).getSellprice(), 3));
                                    } else if (sales.pricesList.get(i).equals("two")) {
                                        sellproduct.setSellprice(Round.round(productList.get(i).getSellprice2(), 3));
                                    } else {
                                        sellproduct.setSellprice(Round.round(productList.get(i).getSellprice3(), 3));
                                    }
                                    sellproduct.setBuyprice(Round.round(productList.get(i).getBuyprice(), 3));
                                    sellproduct.setDate(current_date2.getText().toString());
                                    sellproduct.setItemid(productList.get(i).getItemid());

                                    if (db.insert_date(sellproduct)) {
                                    } else {
                                        Toast.makeText(getBaseContext(), "فشل تسجيل المنتج " + productList.get(i).getName(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                com.mohamedragab.cashpos.modules.moneybox.models.money money = new com.mohamedragab.cashpos.modules.moneybox.models.money();
                                money.setDate(current_date2.getText().toString());
                                money.setNotes("مقدم قسط فاتوره رقم " + invoice_id);
                                if (before_radio.isChecked()) {
                                    money.setValue(Round.round(Double.parseDouble(first_inkest2.getText().toString()), 3));
                                } else {
                                    money.setValue(Round.round(Double.parseDouble(first_inkest.getText().toString()), 3));
                                }
                                final Cursor res3 = db.getallTransactions();
                                double total1 = 0;
                                if (res3 != null && res3.getCount() > 0) {
                                    while (res3.moveToNext()) {
                                        total1 = res3.getDouble(3);

                                    }

                                }
                                money.setTotalbefore(Round.round(total1, 3));
                                double totalAfter;
                                if (before_radio.isChecked()) {
                                    totalAfter = total1 + Double.parseDouble(first_inkest2.getText().toString());
                                } else {
                                    totalAfter = total1 + Double.parseDouble(first_inkest.getText().toString());
                                }

                                money.setTotalAfter(Round.round(totalAfter, 3));

                                if (db.insert_date(money)) {
                                    product pro;

                                    for (int i = 0; i < productList.size(); i++) {
                                        pro = productList.get(i);
                                        pro.setQuantity(pro.getQuantity() - quantityList.get(i));
                                        if (db.updateDatabyName(pro.getName(), pro.getQuantity())) {
                                            dialog.dismiss();

                                        } else {
                                            Toast.makeText(getBaseContext(), "حدث خطا في تعديل البيانات تاكد من ادخال بينات صحيحه !", Toast.LENGTH_SHORT).show();
                                        }
                                    }


                                    Kist kist = new Kist();
                                    kist.setInvoice_id(invoice_id + "");
                                    kist.setClient_name(omla2.getSelectedItem().toString());
                                    kist.setCollectdate(current_date2.getText().toString());
                                    kist.setDamen_name(damenname.getText().toString());
                                    kist.setDamen_phone(damenphone.getText().toString());
                                    kist.setDayslimit(0);
                                    if (before_radio.isChecked()) {
                                        kist.setKist_value(Round.round(Double.parseDouble(first_inkest2.getText().toString()), 3));
                                    } else {
                                        kist.setKist_value(Round.round(Double.parseDouble(first_inkest.getText().toString()), 3));
                                    }
                                    kist.setStatue("paid");
                                    if (month.isChecked()) {
                                        kist.setPay_type("month");
                                    } else {
                                        kist.setPay_type("week");
                                    }
                                    kist.setDescription(productList.get(0).getName() + "");
                                    kist.setPaydate(current_date2.getText().toString());
                                    db.insert_kist_date(kist);

                                    String oldDate = firstday.getText().toString();

                                    for (int i = 0; i < Integer.parseInt(mon_num.getText().toString()); i++) {
                                        Kist kist2 = new Kist();
                                        kist2.setInvoice_id(invoice_id + "");
                                        kist2.setClient_name(omla2.getSelectedItem().toString());
                                        kist2.setDamen_name(damenname.getText().toString());
                                        kist2.setDamen_phone(damenphone.getText().toString());
                                        kist2.setDayslimit(0);
                                        kist2.setKist_value(Math.ceil(Double.parseDouble(kest_value.getText().toString())));
                                        kist2.setStatue("not_paid");
                                        kist2.setPaydate("");

                                        if (month.isChecked()) {
                                            kist2.setPay_type("month");
                                        } else {
                                            kist2.setPay_type("week");
                                        }
                                        kist2.setDescription(productList.get(0).getName() + "");

                                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                                        Calendar c2 = Calendar.getInstance();
                                        try {
                                            c2.setTime(sdf.parse(oldDate));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        if (month.isChecked()) {
                                            c2.add(Calendar.MONTH, 1);
                                        } else {
                                            c2.add(Calendar.DAY_OF_MONTH, 7);
                                        }
                                        String newDate = sdf.format(c2.getTime());
                                        kist2.setCollectdate(oldDate);
                                        oldDate = newDate;
                                        db.insert_kist_date(kist2);
                                    }
                                    final Dialog after_dialog = new Dialog(sales.this);
                                    after_dialog.setContentView(R.layout.after_sales_dialog);
                                    after_dialog.setCancelable(false);

                                    TextView invoice_text = (TextView) after_dialog.findViewById(R.id.invoice_num);
                                    ImageView print = (ImageView) after_dialog.findViewById(R.id.print);
                                    ImageView share = (ImageView) after_dialog.findViewById(R.id.share);
                                    ImageView close = (ImageView) after_dialog.findViewById(R.id.cancel);

                                    LinearLayout invoice_image = (LinearLayout) after_dialog.findViewById(R.id.linear_invoice);
                                    TableLayout table = (TableLayout) after_dialog.findViewById(R.id.table);
                                    for (int i = 0; i < sales.productList.size(); i++) {
                                        TableRow row = (TableRow) LayoutInflater.from(sales.this).inflate(R.layout.row_item, null);
                                        ((TextView) row.findViewById(R.id.name)).setText(productList.get(i).getName() + "");
                                        ((TextView) row.findViewById(R.id.price)).setText(productList.get(i).getSellprice() + "");
                                        ((TextView) row.findViewById(R.id.quantity)).setText(quantityList.get(i) + "");
                                        if (sales.pricesList.get(i).equals("one")) {
                                            ((TextView) row.findViewById(R.id.price)).setText(productList.get(i).getSellprice() + "");
                                            ((TextView) row.findViewById(R.id.total)).setText((quantityList.get(i) * productList.get(i).getSellprice()) + "");
                                        } else if (sales.pricesList.get(i).equals("two")) {
                                            ((TextView) row.findViewById(R.id.price)).setText(productList.get(i).getSellprice2() + "");
                                            ((TextView) row.findViewById(R.id.total)).setText((quantityList.get(i) * productList.get(i).getSellprice2()) + "");
                                        } else {
                                            ((TextView) row.findViewById(R.id.price)).setText(productList.get(i).getSellprice3() + "");
                                            ((TextView) row.findViewById(R.id.total)).setText((quantityList.get(i) * productList.get(i).getSellprice3()) + "");
                                        }
                                        table.addView(row);
                                    }
                                    table.requestLayout();

                                    String ClientName = "";
                                    if (omla2.getSelectedItem().toString().equals("اضغط لتحديد عميل")) {
                                        ClientName = "عميل جديد";
                                    } else {
                                        ClientName = omla2.getSelectedItem().toString();
                                    }
                                    invoice_number = invoice_id + "";
                                    double mo2dam;
                                    if (before_radio.isChecked()) {
                                        mo2dam = Double.parseDouble(first_inkest2.getText().toString());
                                    } else {
                                        mo2dam = Double.parseDouble(first_inkest.getText().toString());
                                    }

                                    TextView date = (TextView) after_dialog.findViewById(R.id.date);
                                    date.setText(current_date2.getText().toString());
                                    TextView time = (TextView) after_dialog.findViewById(R.id.time);
                                    String currentTime = new SimpleDateFormat("hh:mm:ss a", Locale.US).format(new Date());
                                    time.setText(currentTime);
                                    TextView invoice_num = (TextView) after_dialog.findViewById(R.id.invoice);
                                    invoice_num.setText(invoice_number + "");
                                    TextView client = (TextView) after_dialog.findViewById(R.id.client);
                                    client.setText(ClientName + "");
                                    TextView cashier = (TextView) after_dialog.findViewById(R.id.cashier);
                                    if (SheredPrefranseHelper.getcurrentcashier(sales.this) != null) {
                                        if (SheredPrefranseHelper.getcurrentcashier(sales.this).getName() != null) {
                                            cashier.setText("" + SheredPrefranseHelper.getcurrentcashier(sales.this).getName() + "");
                                        } else {
                                            cashier.setText("" + "لا يوجد");
                                        }
                                    } else {
                                        cashier.setText("" + "لا يوجد");
                                    }
                                    TextView total_all_text = (TextView) after_dialog.findViewById(R.id.total_all);
                                    total_all_text.setText(Round.round(total_value, 3) + "");
                                    TextView total_after_discount_text = (TextView) after_dialog.findViewById(R.id.afterdiscount);
                                    total_after_discount_text.setText(Round.round(total_after_discount_value, 3) + "");
                                    TextView invoice_kind = (TextView) after_dialog.findViewById(R.id.pay_kind);
                                    invoice_kind.setText("قسط");
                                    TextView paid_text = (TextView) after_dialog.findViewById(R.id.paid);
                                    paid_text.setText(Round.round(mo2dam, 3) + "");
                                    TextView not_paid_text = (TextView) after_dialog.findViewById(R.id.not_paid);
                                    not_paid_text.setText("أقساط");
                                    TextView notification_text = (TextView) after_dialog.findViewById(R.id.notification);
                                    TextView address_phone = (TextView) after_dialog.findViewById(R.id.address_phone);
                                    TextView shopname = (TextView) after_dialog.findViewById(R.id.shopname);
                                    ImageView logo = (ImageView) after_dialog.findViewById(R.id.logo);
                                    TextView total_not_paid_txt = (TextView) after_dialog.findViewById(R.id.total_not_paid);
                                    total_not_paid_txt.setVisibility(View.INVISIBLE);
                                    shopInfo info = SheredPrefranseHelper.getshopData(sales.this);
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
                                    LinearLayout delivery_lin = (LinearLayout) after_dialog.findViewById(R.id.delivery_lin);
                                    if (SheredPrefranseHelper.getdelivery(sales.this) != null) {
                                        if (SheredPrefranseHelper.getdelivery(sales.this).equals("true")) {
                                            delivery_lin.setVisibility(View.GONE);

                                        } else {
                                            delivery_lin.setVisibility(View.GONE);
                                        }
                                    } else {
                                        delivery_lin.setVisibility(View.GONE);
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
                                        try {
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
                                                File file = new File(Environment.getExternalStorageDirectory() + "/cashpos/invoices/", invoice_number + ".png");
                                                FileOutputStream out = new FileOutputStream(file);
                                                returnedBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                                                out.close();
                                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                                Uri screenshotUri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/cashpos/invoices/" + invoice_number + ".png");
                                                sharingIntent.setType("image/png");
                                                sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                                                startActivity(Intent.createChooser(sharingIntent, "مشاركة الفاتورة"));
                                                Toast.makeText(getBaseContext(), "تم حفظ الفاتورة في : " + file.toString(), Toast.LENGTH_LONG).show();

                                            } catch (FileNotFoundException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }


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
                                            File file = new File(Environment.getExternalStorageDirectory() + "/cashpos/invoices/", invoice_number + ".png");
                                            FileOutputStream out = new FileOutputStream(file);
                                            returnedBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                                            out.close();
                                            Toast.makeText(getBaseContext(), "تم حفظ الفاتورة في : " + file.toString(), Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(sales.this, sales.class));
                                            finish();
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    });
                                    after_dialog.show();

                                } else {
                                    Toast.makeText(getBaseContext(), "فشل اضافه المبلغ في الصندوق ! ", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                //Toast.makeText(getBaseContext(), "فشل تسجيل الفاتوره تحقق من ادخال بيانات صحيحه", Toast.LENGTH_SHORT).show();

                            }
                        }


                    }
                }

            });
            dialog.show();
        }
    }


    public void DateDialog() {
        Locale.setDefault(Locale.US);

        mcalendar = Calendar.getInstance();
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);
        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener listener = (view, year, monthOfYear, dayOfMonth) -> firstday.setText(DateUtil.getDateOnly(year, monthOfYear, dayOfMonth));
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        super.onResume();
        DataBaseHelper omlaDataBaseHelper = new DataBaseHelper(this);
        final Cursor res2 = omlaDataBaseHelper.getallomla();
        String omla_names[] = new String[res2.getCount() + 1];

        if (res2.getCount() == 0) {
            omla_names[0] = "اضغط لتحديد عميل";
        } else if (res2 != null && res2.getCount() > 0) {
            int index = 1;
            omla_names[0] = "اضغط لتحديد عميل";
            while (res2.moveToNext()) {
                omla_names[index] = res2.getString(1);
                index++;
            }
        }

        if (omla != null || omla2 != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, omla_names);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            omla.setAdapter(adapter);
            omla2.setAdapter(adapter);

        }


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
                        if (SheredPrefranseHelper.getprinter_type(sales.this) != null) {
                            if (SheredPrefranseHelper.getprinter_type(sales.this).equals("58")) {
                                b2 = resizeImage(b2, 335, false);
                            } else {
                                b2 = resizeImage(b2, 790, false);
                            }
                        } else {
                            b2 = resizeImage(b2, 335, false);
                        }
                        printpicCode(b2);
                    } else {

                        if (SheredPrefranseHelper.getprinter_type(sales.this) != null) {
                            if (SheredPrefranseHelper.getprinter_type(sales.this).equals("58")) {
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
                    0, printBmp, BitmapToByteData.BmpType.Dithering, BitmapToByteData.AlignType.Left, 590));
            return list;
        });


    }

    Spinner category_spinner;

    public void showallproducts(View view) {
        final Dialog dialog = new Dialog(sales.this);
        dialog.setContentView(R.layout.productsdialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        category_spinner = (Spinner) dialog.findViewById(R.id.category_spinner);
        GridView gridview = (GridView) dialog.findViewById(R.id.listview);
        List<product> allproductsList = new ArrayList<>();

        final Cursor res2 = db.getallcategories();
        String cat_names[] = new String[res2.getCount() + 1];
        if (res2.getCount() == 0) {
            cat_names[0] = "تصنيف0";
        } else if (res2 != null && res2.getCount() > 0) {
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

        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cursor res = db.getproductsbycategory(category_spinner.getSelectedItem().toString());
                allproductsList.clear();
                allproductsAdapter allproductsAdapter = new allproductsAdapter(sales.this, allproductsList, dialog, "sales");

                if (res != null && res.getCount() > 0) {
                    while (res.moveToNext()) {

                        product pro = new product();

                        pro.setId(res.getInt(0));
                        pro.setCode_id(res.getString(1));
                        pro.setName(res.getString(2));
                        pro.setSellprice(res.getDouble(4));
                        pro.setQuantity(res.getDouble(6));
                        pro.setDescription(res.getString(3));
                        pro.setBuyprice(res.getDouble(5));
                        pro.setExpiredate(res.getString(7));
                        byte[] imgByte = res.getBlob(9);

                        pro.setMeasure1(res.getString(10));
                        pro.setMeasure2(res.getString(11));
                        pro.setMeasure3(res.getString(12));
                        pro.setSellprice2(res.getDouble(13));
                        pro.setSellprice3(res.getDouble(14));
                        pro.setCategory(res.getString(15));
                        pro.setFactor2(res.getInt(16));
                        pro.setFactor3(res.getInt(17));
                        pro.setImage(imgByte);

                        allproductsList.add(pro);

                    }
                    allproductsAdapter.setshopAdapter(allproductsList);
                    gridview.setAdapter(allproductsAdapter);
                } else {
                    Toast.makeText(getBaseContext(), "لا يوجد منتجات تطابق التصنيف !", Toast.LENGTH_SHORT).show();
                    allproductsList.clear();
                    allproductsAdapter.setshopAdapter(allproductsList);
                    gridview.setAdapter(allproductsAdapter);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();

    }


    public void DateDialog(int statu) {
        Locale.setDefault(Locale.US);

        mcalendar = Calendar.getInstance();
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);
        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        if (statu == 0) {
            DatePickerDialog.OnDateSetListener listener = (view, year, monthOfYear, dayOfMonth) -> create_date.setText(DateUtil.getDateOnly(year, monthOfYear, dayOfMonth));
            DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
            dpDialog.show();
        } else {
            DatePickerDialog.OnDateSetListener listener = (view, year, monthOfYear, dayOfMonth) -> get_date.setText(DateUtil.getDateOnly(year, monthOfYear, dayOfMonth));
            DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
            dpDialog.show();
        }

    }

    public void showcard(View view) {
        if (card_final_visibility) {
            final_card.setVisibility(View.GONE);
            card_final_visibility = false;
        } else {
            final_card.setVisibility(View.VISIBLE);
            card_final_visibility = true;
        }
    }

    public void clear_text(View view) {
        this.autoCompleteTextView.setText("");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(sales.this);
        builder.setTitle("تحذير");
        builder.setMessage("هل تريد الخروج من شاشة المبيعات ؟");
        builder.setPositiveButton("نعم", (dialog, id) -> {
            netReciever = new Receiver();

            if (!MainActivity.ISCONNECT) {
                showSnackbar(getString(R.string.con_has_discon));
            }
            finish();
            sales.super.onBackPressed();
        });
        builder.setNegativeButton("لا", (dialog, id) -> {
        });
        builder.show();


    }


    /*
        use the Matrix compress the bitmap
	 *   */
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

}





