package com.mohamedragab.cashpos.modules.invoicesalesback.wedgits;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.omla.models.omla;
import com.mohamedragab.cashpos.modules.omlatransactions.models.omlatransaction;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.sales.models.invoice;
import com.mohamedragab.cashpos.utils.Round;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class invoicesaleAdapter extends ArrayAdapter {

    List<invoice> invoiceLists;
    Context con;
    DataBaseHelper db;
    DataBaseHelper kestdb;

    public invoicesaleAdapter(Context context, List<invoice> invoiceList) {
        super(context, R.layout.invoiceview_item, R.id.value, invoiceList);
        this.invoiceLists = invoiceList;
        con = context;
        db = new DataBaseHelper(con);
        kestdb = new DataBaseHelper(con);

    }

    public void setinvoiceAdapter(List<invoice> invoicelist) {
        this.invoiceLists = invoicelist;

    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.invoiceview_item, parent, false);

        TextView invoice_id = (TextView) row.findViewById(R.id.invoice_id);
        TextView total = (TextView) row.findViewById(R.id.total);
        TextView customer_name = (TextView) row.findViewById(R.id.customer_name);
        TextView date = (TextView) row.findViewById(R.id.date);
        TextView discount = (TextView) row.findViewById(R.id.discount);

        LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.linear);

        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(Color.parseColor("#eaddff"));
        } else {

        }
        try {
            final invoice invoice = invoiceLists.get(position);
            final DataBaseHelper db = new DataBaseHelper(con);

            linearLayout.setOnClickListener(view -> {

                final Dialog dialog = new Dialog(con);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.deletedialog);


                Button delete = (Button) dialog.findViewById(R.id.delete);
                delete.setOnClickListener(v -> {

                    int result = db.deletesellData(invoice.getInvoice_id() + "");
                    kestdb.deletekistbyinvoice(invoice.getInvoice_id());

                    SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);
                    String formattedDate2 = df2.format(c);


                            com.mohamedragab.cashpos.modules.moneybox.models.money money = new com.mohamedragab.cashpos.modules.moneybox.models.money();
                            money.setDate(formattedDate2);
                            money.setNotes("ارجاع فاتوره بيع رقم " + invoice.getInvoice_id());
                            if (invoice.getKind().equals("أجل")) {
                                money.setValue(Round.round((invoice.getTotal() - invoice.getNotpaid()), 3));
                            } else {
                                money.setValue(Round.round(invoice.getTotal(), 3));
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
                            if (invoice.getKind().equals("أجل")) {
                                totalAfter = total1 - Round.round((invoice.getTotal() - invoice.getNotpaid()), 3);
                            } else {
                                totalAfter = total1 - invoice.getTotal();
                            }

                            money.setTotalAfter(Round.round(totalAfter, 3));
                            db.insert_date(money);


                    if (invoice.getCustomer_name().equals("لا يوجد")) {

                    } else {

                        Cursor res = db.getomla(invoice.getCustomer_name());
                        if (res != null && res.getCount() > 0) {
                            while (res.moveToNext()) {
                                omla omla = new omla();

                                omla.setId(res.getInt(0));
                                omla.setName(res.getString(1));
                                double newsouldpayvalue;
                                if (invoice.getKind().equals("أجل")) {
                                    newsouldpayvalue = res.getDouble(6) - invoice.getNotpaid();
                                } else {
                                    newsouldpayvalue = res.getDouble(6) ;//- invoice.getTotal();
                                }
                                omla.setPaymoney(Round.round(newsouldpayvalue, 3));
                                omla.setHasmoney(res.getDouble(5));
                                omla.setAddress(res.getString(2));
                                omla.setPhone(res.getString(3));
                                omla.setNotes(res.getString(4));
                                omla.setMaxnotpaid(res.getDouble(7));

                                if (db.updateData(omla)) {
                                    // Toast.makeText(sales.this, "تم اضافه المبلغ لحساب العميل !", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(con, "لم يحدث تغيير في حساب العميل !", Toast.LENGTH_SHORT).show();
                                }
                            }

                            omlatransaction omlatransaction = new omlatransaction();
                            omlatransaction.setInvoiceId(invoice.getInvoice_id());
                            omlatransaction.setDate(formattedDate2);
                            omlatransaction.setNotpaid(0);
                            omlatransaction.setName(invoice.getCustomer_name());
                            omlatransaction.setProcess("ارجاع فاتورة");
                            if (invoice.getKind().equals("أجل")) {
                                omlatransaction.setValue(Round.round(invoice.getNotpaid(), 3));
                            } else {
                                omlatransaction.setValue(Round.round(invoice.getTotal(), 3));
                            }

                            if (db.insert_date(omlatransaction)) {

                                //  Toast.makeText(con, "تم تسجيل تعاملات العميل ", Toast.LENGTH_SHORT).show();
                            } else {
                                // Toast.makeText(con, "فشل تسجيل تعاملات العميل", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(con, "لا يوجد عملاء بهذا الاسم !", Toast.LENGTH_SHORT).show();
                        }
                    }


                    Cursor res = db.getsellproduct(invoice.getInvoice_id());

                    if (res != null && res.getCount() > 0) {
                        while (res.moveToNext()) {

                            String sellproductName = res.getString(4);
                            double sellproductQuantity = res.getDouble(2);
                            Cursor res6 = db.getQuantites(sellproductName);

                            if (res6 != null && res6.getCount() > 0) {
                                while (res6.moveToNext()) {
                                    Log.d("test", "result " + res6.getColumnName(4) + " " + res6.getDouble(4));

                                    double newquantity = res6.getDouble(6) + sellproductQuantity;
                                    Boolean result4 = db.updateDatabyName(sellproductName, newquantity);
                                    if (result4) {
                                        // Toast.makeText(sales.this, "تم تعديل المنتج بنجاح !", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();

                                    } else {
                                        Toast.makeText(con, "حدث خطا في تعديل البيانات تاكد من ادخال بينات صحيحه !", Toast.LENGTH_SHORT).show();

                                    }
                                }

                            }


                        }


                    }
                    db.deletesellProducts(invoice.getInvoice_id());
                    // Toast.makeText(con, "تم حذف الفاتوره ", Toast.LENGTH_SHORT).show();
                    db.close();

                    ((Activity) con).finish();
                    dialog.dismiss();
                });

                Button cancel = (Button) dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(v -> dialog.dismiss());

                dialog.show();
            });


            invoice_id.setText(invoice.getInvoice_id() + "");
            date.setText(invoice.getDate() + "");
            total.setText(Round.round(invoice.getTotal(), 3) + SheredPrefranseHelper.getmoney_type(con));
            customer_name.setText(invoice.getCustomer_name() + "");
            if (invoiceLists.get(position).getDiscount() == 0) {
                discount.setVisibility(View.GONE);
            } else {
                if (invoiceLists.get(position).getDiscount_kind().equals("percentage")) {
                    discount.setText(" خصم " + invoiceLists.get(position).getDiscount() + " % ");
                } else {
                    discount.setText(" خصم " + invoiceLists.get(position).getDiscount() + " " + SheredPrefranseHelper.getmoney_type(con));
                }
            }


        } catch (
                Exception e) {
            Toast.makeText(con, "فشل ارجاع البيانات !", Toast.LENGTH_SHORT).show();

        }


        return row;
    }

}



