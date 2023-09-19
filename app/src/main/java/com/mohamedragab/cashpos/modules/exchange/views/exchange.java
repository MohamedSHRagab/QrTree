package com.mohamedragab.cashpos.modules.exchange.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.snackbar.Snackbar;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.DateUtil;
import com.mohamedragab.cashpos.modules.exchange.pdf.productsreport;
import com.mohamedragab.cashpos.modules.exchange.wedgit.allproductsAdapter;
import com.mohamedragab.cashpos.modules.exchange.wedgit.productSelectedAdapter;
import com.mohamedragab.cashpos.modules.home.MainActivity;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.scanner.scanner;
import com.mohamedragab.cashpos.modules.store.models.category;
import com.mohamedragab.cashpos.modules.store.models.product;
import com.mohamedragab.cashpos.modules.store.views.addproduct;
import com.mohamedragab.cashpos.utils.Round;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class exchange extends AppCompatActivity {
    public static AutoCompleteTextView autoCompleteTextView;
    public static EditText discount;

    private Calendar mcalendar;
    private int day2, month2, year2;
    private TextView current_date2;

    public static List<product> productList;
    public static ArrayList<Double> quantityList;
    String names[];
    ListView listView;
    productSelectedAdapter productAdapter;
    public static TextView total, after_textviewvalue;
    public static double total_value = 0.0;
    DataBaseHelper db;
    TextView total_after_discount;
    RadioButton money, percentage;
    View parentLayout;
    ImageView printer_status;
    CardView final_card;
    Boolean card_final_visibility = true;
    int invoice_id;


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
        setContentView(R.layout.activity_exchange);

        views();

    }


    public void views() {
        total = (TextView) findViewById(R.id.total);
        total_after_discount = (TextView) findViewById(R.id.totalafterdiscount);
        discount = (EditText) findViewById(R.id.discount);
        printer_status = (ImageView) findViewById(R.id.printer);
        final_card = (CardView) findViewById(R.id.final_card);

        LinearLayout container = (LinearLayout) findViewById(R.id.linear2);

        percentage = (RadioButton) findViewById(R.id.percentage);
        money = (RadioButton) findViewById(R.id.money);

        if (MainActivity.ISCONNECT) {
            printer_status.setImageResource(R.drawable.printerconnected);
        } else {
            printer_status.setImageResource(R.drawable.printeroffline);
        }
        db = new DataBaseHelper(getBaseContext());


        invoice_id = db.getsellnextid();

        parentLayout = findViewById(android.R.id.content);
        current_date2 = (TextView) findViewById(R.id.date);
        after_textviewvalue = (TextView) findViewById(R.id.aftervalue);
        current_date2.setOnClickListener(view -> DateDialog2());
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String formattedDate = df.format(c);
        current_date2.setText(formattedDate + "");

        productList = new ArrayList<>();
        quantityList = new ArrayList<>();

        total_value = 0.0;

        autoCompleteTextView = findViewById(R.id.search);
        productAdapter = new productSelectedAdapter(exchange.this, productList);

        listView = (ListView) findViewById(R.id.list_selected_products);
        View footer = new View(exchange.this);
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
                            MediaPlayer mp = MediaPlayer.create(exchange.this, R.raw.finalsound);
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
                                    total_value += Round.round((res.getDouble(4) * 1.0), 2);
                                    added = true;
                                    break;
                                } else if (i == productList.size() - 1 && res.getInt(0) != productList.get(i).getId()) {
                                    productList.add(pro);
                                    autoCompleteTextView.setText("");
                                    quantityList.add(1.0);
                                    total_value += Round.round((res.getDouble(4) * 1.0), 2);
                                    added = true;
                                    break;
                                }
                            }
                            if (!added) {
                                productList.add(pro);
                                autoCompleteTextView.setText("");
                                quantityList.add(1.0);
                            }

                        }
                    }
                    productAdapter.setproductAdapter(productList, listView);
                    listView.setAdapter(productAdapter);
                    listView.addFooterView(footer, null, false);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                final Cursor res = db.getallproducts();

                if (res != null && res.getCount() > 0) {
                    while (res.moveToNext()) {

                        if (autoCompleteTextView.getText().toString().trim().equals(res.getString(1))) {
                            MediaPlayer mp = MediaPlayer.create(exchange.this, R.raw.finalsound);
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
                                    added = true;
                                    break;
                                } else if (i == productList.size() - 1 && res.getInt(0) != productList.get(i).getId()) {
                                    productList.add(pro);
                                    autoCompleteTextView.setText("");
                                    quantityList.add(1.0);
                                    added = true;
                                    break;
                                }
                            }
                            if (!added) {
                                productList.add(pro);
                                autoCompleteTextView.setText("");
                                quantityList.add(1.0);
                            }
                        }

                    }
                    productAdapter.setproductAdapter(productList, listView);
                    listView.setAdapter(productAdapter);
                    listView.addFooterView(footer, null, false);
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

    }

    public static List<category> adminList;


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
        String value = "exchange";
        Intent i = new Intent(exchange.this, scanner.class);
        i.putExtra("activity", value);
        startActivity(i);

    }

    public void go_home(View view) {
        onBackPressed();
    }

    public void go_continue(View view) {
        if (productList.size() == 0) {
            Toast.makeText(getBaseContext(), "رجاء اضافه اصناف الي الجرد !", Toast.LENGTH_SHORT).show();
            return;
        }
        ProgressDialog progressdialog = new ProgressDialog(exchange.this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            productsreport report = new productsreport();
            String report_name = report.setMoneyOmlareport(getBaseContext(), productList,quantityList);

            String storage = Environment.getExternalStorageDirectory().toString() + "/cashpos/" + report_name + ".pdf";
            File file = new File(storage);
            Uri uri;
            if (Build.VERSION.SDK_INT < 24) {
                uri = Uri.fromFile(file);
            } else {
                uri = Uri.parse(file.getPath());
            }
            Intent viewFile = new Intent(Intent.ACTION_SEND);
            viewFile.setDataAndType(uri, "application/pdf");
            viewFile.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(viewFile);
            progressdialog.dismiss();
        }, 1000);


    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    Spinner category_spinner;

    public void showallproducts(View view) {
        final Dialog dialog = new Dialog(exchange.this);
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
                allproductsAdapter allproductsAdapter = new allproductsAdapter(exchange.this, allproductsList, dialog, "exchange");

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


    public void showcard(View view) {
        if (card_final_visibility) {
            final_card.setVisibility(View.GONE);
            ImageView newimg = (ImageView) view;
            newimg.setImageResource(R.drawable.ic_baseline_arrow_upward_24);
            card_final_visibility = false;
        } else {
            final_card.setVisibility(View.VISIBLE);
            ImageView newimg = (ImageView) view;
            newimg.setImageResource(R.drawable.download);
            card_final_visibility = true;
        }
    }

    public void clear_text(View view) {
        this.autoCompleteTextView.setText("");
    }

    public void add_product(View view) {
        startActivity(new Intent(this, addproduct.class));
        finish();
    }


}





