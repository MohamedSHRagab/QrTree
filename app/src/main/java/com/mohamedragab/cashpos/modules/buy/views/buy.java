package com.mohamedragab.cashpos.modules.buy.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.mohamedragab.cashpos.base.DateUtil;
import com.mohamedragab.cashpos.modules.buy.wedgit.productSelectedAdapter;
import com.mohamedragab.cashpos.modules.mored.models.mored;
import com.mohamedragab.cashpos.modules.mored.views.newmored;
import com.mohamedragab.cashpos.modules.moredtransactions.models.moredtransaction;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.sales.wedgit.allproductsAdapter;
import com.mohamedragab.cashpos.modules.store.models.product;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.buy.models.buyproduct;
import com.mohamedragab.cashpos.modules.buy.models.invoice;
import com.mohamedragab.cashpos.modules.home.MainActivity;
import com.mohamedragab.cashpos.modules.scanner.scanner;
import com.mohamedragab.cashpos.utils.Round;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class buy extends AppCompatActivity {
    public static EditText search;
    private Calendar mcalendar;
    private TextView current_date;
    private int day, month, year;

    public static AutoCompleteTextView autoCompleteTextView;
    public static List<product> productList;
    public static ArrayList<Double> quantityList;
    DataBaseHelper db;
    String names[];
    ListView listView;
    productSelectedAdapter productAdapter;
    public static TextView total;
    public static double total_value = 0;
    View parentLayout;
    Spinner Spinner_mored;
    int invoice_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        current_date = (TextView) findViewById(R.id.date);
        current_date.setOnClickListener(view -> DateDialog());
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String formattedDate = df.format(c);
        current_date.setText(formattedDate + "");

        parentLayout = findViewById(android.R.id.content);
        total = (TextView) findViewById(R.id.total);
        db = new DataBaseHelper(getBaseContext());
        invoice_id = db.getbuyinvoicesnextid();

        productList = new ArrayList<>();
        quantityList = new ArrayList<>();
        total_value = 0;

        autoCompleteTextView = findViewById(R.id.search);
        productAdapter = new productSelectedAdapter(buy.this, productList);

        listView = (ListView) findViewById(R.id.list_selected_products);
        db = new DataBaseHelper(getBaseContext());
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final Cursor res = db.getallproducts();

                if (res != null && res.getCount() > 0) {
                    while (res.moveToNext()) {

                        if (autoCompleteTextView.getText().toString().equals(res.getString(2))) {
                            MediaPlayer mp = MediaPlayer.create(buy.this, R.raw.finalsound);
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
                                    total_value += Round.round((res.getDouble(5) * 1),3);
                                    added=true;
                                    break;
                                } else if (i == productList.size() - 1 && res.getInt(0) != productList.get(i).getId()) {
                                    productList.add(pro);
                                    autoCompleteTextView.setText("");
                                    quantityList.add(1.0);
                                    total_value += Round.round((res.getDouble(5) * 1),3);
                                    added=true;
                                    break;
                                }
                            }
                            if (!added){
                                productList.add(pro);
                                autoCompleteTextView.setText("");
                                quantityList.add(1.0);
                                total_value += Round.round((res.getDouble(5) * 1),3);
                            }
                        }

                    }
                    productAdapter.setproductAdapter(productList, listView);
                    listView.setAdapter(productAdapter);
                    total.setText(Round.round(total_value ,3)+ SheredPrefranseHelper.getmoney_type(buy.this));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                final Cursor res = db.getallproducts();

                if (res != null && res.getCount() > 0) {

                    while (res.moveToNext()) {

                        if (autoCompleteTextView.getText().toString().equals(res.getString(1))) {
                            MediaPlayer mp = MediaPlayer.create(buy.this, R.raw.finalsound);
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
                                    total_value += Round.round((res.getDouble(5) * 1),3);
                                    added=true;
                                    break;
                                } else if (i == productList.size() - 1 && res.getInt(0) != productList.get(i).getId()) {
                                    productList.add(pro);
                                    autoCompleteTextView.setText("");
                                    quantityList.add(1.0);
                                    total_value += Round.round((res.getDouble(5) * 1),3);
                                    added=true;
                                    break;
                                }
                            }
                            if (!added){
                                productList.add(pro);
                                autoCompleteTextView.setText("");
                                quantityList.add(1.0);
                                total_value += Round.round((res.getDouble(5) * 1),3);
                            }
                        }


                    }
                    productAdapter.setproductAdapter(productList, listView);
                    listView.setAdapter(productAdapter);
                    total.setText(Round.round(total_value,3) + SheredPrefranseHelper.getmoney_type(buy.this));
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

    public void go_scan(View view) {

        String value = "buy";
        Intent i = new Intent(buy.this, scanner.class);
        i.putExtra("activity", value);
        startActivity(i);

    }


    public void DateDialog() {
        Locale.setDefault(Locale.US);
        mcalendar = Calendar.getInstance();
        year = mcalendar.get(Calendar.YEAR);
        month = mcalendar.get(Calendar.MONTH);
        day = mcalendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener listener = (view, year, monthOfYear, dayOfMonth) -> current_date.setText(DateUtil.getDateOnly(year, monthOfYear, dayOfMonth));
        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }


    public void go_home(View view) {
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    public void go_continue(View view) {

        if (total_value == 0) {
            Toast.makeText(getBaseContext(), "برجاء اضافه اصناف الي الفاتوره !", Toast.LENGTH_SHORT).show();
        } else {

            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.finalinbuy);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);

            final RadioButton agel = (RadioButton) dialog.findViewById(R.id.agel);
            final RadioButton cash = (RadioButton) dialog.findViewById(R.id.cash);
            final EditText paid = (EditText) dialog.findViewById(R.id.paid);
            final EditText notpaid = (EditText) dialog.findViewById(R.id.notpaid);
            TextView total = (TextView) dialog.findViewById(R.id.total);
            TextView newMored = (TextView) dialog.findViewById(R.id.newMored);
            newMored.setOnClickListener(v -> {
                startActivity(new Intent(this, newmored.class));
            });

            total.setText(Round.round(total_value,3) + SheredPrefranseHelper.getmoney_type(buy.this));
            Button save = (Button) dialog.findViewById(R.id.save);
            Button cancel = (Button) dialog.findViewById(R.id.cancel);
            cancel.setOnClickListener(v -> dialog.dismiss());

             Spinner_mored = (Spinner) dialog.findViewById(R.id.omalaspinner);
            final Cursor res2 = db.getallmored();
            String mored_names[] = new String[res2.getCount() + 1];
            if (res2.getCount() == 0) {
                mored_names[0] = "اضغط لتحديد مورد";
            } else if (res2 != null && res2.getCount() > 0) {
                int index = 1;
                mored_names[0] = "اضغط لتحديد مورد";
                while (res2.moveToNext()) {
                    mored_names[index] = res2.getString(1);
                    index++;
                }

            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, mored_names);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner_mored.setAdapter(adapter);
            if (cash.isChecked()) {
                paid.setText(Round.round(total_value,3) + "");
                paid.setEnabled(false);
                notpaid.setText(0 + "");
                notpaid.setEnabled(false);

            } else {
                paid.setEnabled(true);
                notpaid.setEnabled(true);
                paid.setText(0 + "");
                notpaid.setText(Round.round(total_value ,3)+ "");

            }
            cash.setOnClickListener(v -> {
                if (cash.isChecked()) {
                    paid.setText(Round.round(total_value,3) + "");
                    paid.setEnabled(false);
                    notpaid.setText(0 + "");
                    notpaid.setEnabled(false);

                } else {
                    paid.setEnabled(true);
                    notpaid.setEnabled(true);
                    paid.setText(0 + "");
                    notpaid.setText(Round.round(total_value,3) + "");

                }
            });
            agel.setOnClickListener(v -> {
                if (cash.isChecked()) {
                    paid.setText(Round.round(total_value ,3)+ "");
                    paid.setEnabled(false);
                    notpaid.setText(0 + "");
                    notpaid.setEnabled(false);

                } else {
                    paid.setEnabled(true);
                    paid.setText(0 + "");
                    notpaid.setText(Round.round(total_value ,3)+ "");
                    paid.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (paid.getText().toString().equals("")) {

                            } else {
                                if (Double.parseDouble(paid.getText().toString()) > total_value) {
                                    Toast.makeText(getBaseContext(), "المبلغ المدفوع اكبر من الاجمالي !", Toast.LENGTH_SHORT).show();
                                    notpaid.setText(Round.round(total_value,3) + "");
                                    paid.setText("");

                                } else {
                                    double notpaidvalue = total_value - Double.parseDouble(paid.getText().toString());
                                    notpaid.setText(Round.round(notpaidvalue,3) + "");

                                }
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

                    double notpaidval = Double.parseDouble(notpaid.getText().toString());

                    invoice.setInvoice_id(invoice_id+"");
                    invoice.setDate(current_date.getText().toString());
                    invoice.setNotpaid(Round.round(notpaidval,3));
                    invoice.setTotal(Round.round(total_value,3));
                    if (SheredPrefranseHelper.getcurrentcashier(this)!=null){
                        invoice.setCashier(SheredPrefranseHelper.getcurrentcashier(this).getName());
                    }
                    if (Spinner_mored.getSelectedItem().toString().equals("اضغط لتحديد مورد")) {
                        invoice.setCustomer_name("لا يوجد");
                    } else {
                        invoice.setCustomer_name(Spinner_mored.getSelectedItem().toString());
                    }

                    if (db.insert_date(invoice)) {

                        for (int i = 0; i < productList.size(); i++) {
                            buyproduct buyproduct = new buyproduct();
                            buyproduct.setCode_id(productList.get(i).getCode_id() + "");
                            buyproduct.setInvoice_id(invoice_id + "");
                            buyproduct.setQuantity(quantityList.get(i));
                            buyproduct.setName(productList.get(i).getName() + "");
                            buyproduct.setDescription(productList.get(i).getDescription() + "");
                            buyproduct.setSellprice(productList.get(i).getSellprice());
                            buyproduct.setBuyprice(productList.get(i).getBuyprice());
                            buyproduct.setItemid(productList.get(i).getItemid());

                            if (db.insert_date(buyproduct)) {
                            } else {
                                Toast.makeText(getBaseContext(), "فشل تسجيل المنتج " + productList.get(i).getName(), Toast.LENGTH_SHORT).show();

                            }
                        }
                        com.mohamedragab.cashpos.modules.moneybox.models.money money = new com.mohamedragab.cashpos.modules.moneybox.models.money();
                        money.setDate(current_date.getText().toString());
                        money.setNotes("مشتريات فاتوره رقم " + invoice_id);
                        money.setValue( Round.round(total_value,3));

                        final Cursor res3 = db.getallTransactions();
                        double total1 = 0;
                        if (res3 != null && res3.getCount() > 0) {
                            while (res3.moveToNext()) {
                                total1 = res3.getLong(3);

                            }
                        }
                        money.setTotalbefore(Round.round(total1,3));
                        double totalAfter = Round.round((total1 -  total_value),3);

                        money.setTotalAfter(Round.round(totalAfter,3));

                        if (db.insert_date(money)) {
                            product pro;

                            for (int i = 0; i < productList.size(); i++) {
                                pro = productList.get(i);
                                pro.setQuantity(pro.getQuantity() + quantityList.get(i));

                                if (db.updateDatabyName(pro.getName(), pro.getQuantity())) {
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getBaseContext(), "حدث خطا في تعديل البيانات تاكد من ادخال بينات صحيحه !", Toast.LENGTH_SHORT).show();
                                }
                            }
                            moredtransaction moredtransaction = new moredtransaction();

                            moredtransaction.setInvoiceId(invoice_id+"");
                            moredtransaction.setDate(current_date.getText().toString());
                            if (Spinner_mored.getSelectedItem().toString().equals("اضغط لتحديد مورد")) {
                                moredtransaction.setName("لا يوجد");
                            } else {
                                moredtransaction.setName(Spinner_mored.getSelectedItem().toString());
                            }
                            moredtransaction.setNotpaid(0);
                            moredtransaction.setProcess("بيع");
                            moredtransaction.setValue( Round.round(total_value,3));

                            if (db.insert_date(moredtransaction) ) {
                                db .close();
                                Toast.makeText(getBaseContext(), " تم تسجيل الفاتوره برقم  " + invoice_id, Toast.LENGTH_LONG).show();
                                startActivity(new Intent(buy.this, buy.class));
                                finish();
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "فشل اضافه المبلغ في الصندوق ! ", Toast.LENGTH_SHORT).show();

                        }

                    }
                    dialog.dismiss();
                } else {
                    if (Spinner_mored.getSelectedItem().toString().equals("اضغط لتحديد مورد")) {
                        Toast.makeText(getBaseContext(), "برجاء اختيار مورد !", Toast.LENGTH_SHORT).show();
                    } else {

                        double notpaidval = Double.parseDouble(notpaid.getText().toString());

                        invoice.setInvoice_id(invoice_id+"");
                        invoice.setDate(current_date.getText().toString());
                        invoice.setNotpaid(Round.round(notpaidval,3));
                        invoice.setTotal(Round.round(total_value,3));
                        invoice.setCustomer_name(Spinner_mored.getSelectedItem().toString());
                        if (SheredPrefranseHelper.getcurrentcashier(this)!=null){
                            invoice.setCashier(SheredPrefranseHelper.getcurrentcashier(this).getName());
                        }
                        Cursor res = db.getmored(Spinner_mored.getSelectedItem().toString());

                        if (res != null && res.getCount() > 0) {
                            while (res.moveToNext()) {
                                mored mored = new mored();
                                mored.setId(res.getInt(0));
                                mored.setName(res.getString(1));
                                double newsouldpayvalue = res.getDouble(6) +  notpaidval;
                                mored.setPaymoney(newsouldpayvalue);
                                mored.setHasmoney(res.getDouble(5));
                                mored.setAddress(res.getString(2));
                                mored.setPhone(res.getString(3));
                                mored.setNotes(res.getString(4));

                                if (db.updateData(mored)) {
                                    // Toast.makeText(sales.this, "تم اضافه المبلغ لحساب العميل !", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getBaseContext(), "لم يحدث تغيير في حساب المورد !", Toast.LENGTH_SHORT).show();

                                }
                            }

                        } else {
                            Toast.makeText(getBaseContext(), "لا يوجد موردين بهذا الاسم !", Toast.LENGTH_SHORT).show();
                        }
                        if (db.insert_date(invoice)) {

                            for (int i = 0; i < productList.size(); i++) {
                                buyproduct buyproduct = new buyproduct();
                                buyproduct.setCode_id(productList.get(i).getCode_id() + "");
                                buyproduct.setInvoice_id(invoice_id + "");
                                buyproduct.setQuantity(quantityList.get(i));
                                buyproduct.setName(productList.get(i).getName() + "");
                                buyproduct.setDescription(productList.get(i).getDescription() + "");
                                buyproduct.setSellprice(productList.get(i).getSellprice());
                                buyproduct.setBuyprice(productList.get(i).getBuyprice());
                                buyproduct.setItemid(productList.get(i).getItemid());

                                if ( db.insert_date(buyproduct)) {
                                } else {
                                    Toast.makeText(getBaseContext(), "فشل  تسجيل المنتج " + productList.get(i).getName(), Toast.LENGTH_SHORT).show();

                                }
                            }
                            com.mohamedragab.cashpos.modules.moneybox.models.money money = new com.mohamedragab.cashpos.modules.moneybox.models.money();
                            money.setDate(current_date.getText().toString());
                            money.setNotes("مشتريات فاتوره رقم " + invoice_id);
                            money.setValue( Round.round((total_value - notpaidval),3));

                            final Cursor res3 = db.getallTransactions();
                            double total1 = 0;
                            if (res3 != null && res3.getCount() > 0) {
                                while (res3.moveToNext()) {
                                    total1 = res3.getDouble(3);
                                }
                            }
                            money.setTotalbefore(Round.round(total1,3));
                            double totalAfter = total1 -  (total_value - notpaidval);

                            money.setTotalAfter(Round.round(totalAfter,3));

                            if ( db.insert_date(money)) {
                                product pro;

                                for (int i = 0; i < productList.size(); i++) {
                                    pro = productList.get(i);
                                    pro.setQuantity(pro.getQuantity() + quantityList.get(i));

                                    if (db.updateDatabyName(pro.getName(), pro.getQuantity())) {
                                        dialog.dismiss();
                                    } else {
                                        Toast.makeText(getBaseContext(), "حدث خطا في تعديل البيانات تاكد من ادخال بينات صحيحه !", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                moredtransaction moredtransaction = new moredtransaction();

                                moredtransaction.setInvoiceId(invoice_id+"");
                                moredtransaction.setDate(current_date.getText().toString());
                                moredtransaction.setName(Spinner_mored.getSelectedItem().toString());
                                moredtransaction.setNotpaid( Round.round(notpaidval,3));
                                moredtransaction.setProcess("بيع");
                                moredtransaction.setValue( Round.round((total_value - notpaidval),3));

                                if (db.insert_date(moredtransaction)) {
                                    db .close();
                                    Toast.makeText(getBaseContext(), " تم تسجيل الفاتوره برقم  " + invoice_id, Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(buy.this, buy.class));
                                    finish();
                                }
                            } else {
                                Toast.makeText(getBaseContext(), "فشل اضافه المبلغ في الصندوق ! ", Toast.LENGTH_SHORT).show();

                            }

                        }
                        dialog.dismiss();
                    }
                }

            });

            dialog.show();
        }
    }
    Spinner category_spinner;
    public void showallproducts(View view) {
        final Dialog dialog = new Dialog(buy.this);
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
                allproductsAdapter allproductsAdapter = new allproductsAdapter(buy.this, allproductsList, dialog, "buy");

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
                }else {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        db.close();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        super.onResume();

        final Cursor res2 = db.getallmored();
        String mored_names[] = new String[res2.getCount() + 1];
        if (res2.getCount() == 0) {
            mored_names[0] = "اضغط لتحديد مورد";
        } else if (res2 != null && res2.getCount() > 0) {
            int index = 1;
            mored_names[0] = "اضغط لتحديد مورد";
            while (res2.moveToNext()) {
                mored_names[index] = res2.getString(1);
                index++;
            }

        }
        if (Spinner_mored != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, mored_names);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner_mored.setAdapter(adapter);

        }

    }
}
