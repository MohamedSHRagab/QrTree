package com.mohamedragab.cashpos.modules.invoicebuyback.wedgits;

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

import com.mohamedragab.cashpos.modules.moredtransactions.models.moredtransaction;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.buy.models.invoice;
import com.mohamedragab.cashpos.modules.mored.models.mored;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.utils.Round;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class invoicebuyAdapter extends ArrayAdapter {

    List<invoice> invoiceLists;
    Context con;
    DataBaseHelper db;

    public invoicebuyAdapter(Context context, List<invoice> invoiceList) {
        super(context, R.layout.invoiceview_item, R.id.value, invoiceList);
        this.invoiceLists = invoiceList;
        con = context;
        db = new DataBaseHelper(con);


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

                    SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);
                    String formattedDate2 = df2.format(c);

                            com.mohamedragab.cashpos.modules.moneybox.models.money money = new com.mohamedragab.cashpos.modules.moneybox.models.money();
                            money.setDate(formattedDate2);
                            money.setNotes("ارجاع فاتوره شراء رقم " + invoice.getInvoice_id());
                            money.setValue(Round.round(invoice.getTotal(), 3));

                            final Cursor res3 = db.getallTransactions();
                            double total1 = 0;
                            if (res3 != null && res3.getCount() > 0) {
                                while (res3.moveToNext()) {
                                    total1 = res3.getDouble(3);

                                }
                            }
                            money.setTotalbefore(Round.round(total1, 3));
                            double totalAfter = total1 + invoice.getTotal();

                            money.setTotalAfter(Round.round(totalAfter, 3));
                            db.insert_date(money);



                    if (invoice.getCustomer_name().equals("لا يوجد")) {

                    } else {

                        Cursor res = db.getmored(invoice.getCustomer_name());

                        if (res != null && res.getCount() > 0) {
                            while (res.moveToNext()) {
                                mored mored = new mored();
                                // Log.d("test","id "+res.getInt(0));
                                mored.setId(res.getInt(0));
                                mored.setName(res.getString(1));
                                double newsouldpayvalue = res.getDouble(6) - invoice.getTotal();
                                mored.setPaymoney(newsouldpayvalue);
                                mored.setHasmoney(res.getDouble(5));
                                mored.setAddress(res.getString(2));
                                mored.setPhone(res.getString(3));
                                mored.setNotes(res.getString(4));

                                Boolean result9 = db.updateData(mored);
                                if (result9) {
                                    // Toast.makeText(sales.this, "تم اضافه المبلغ لحساب العميل !", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(con, "لم يحدث تغيير في حساب المورد !", Toast.LENGTH_SHORT).show();

                                }
                            }


                            moredtransaction moredtransaction = new moredtransaction();
                            moredtransaction.setInvoiceId(invoice.getInvoice_id());
                            moredtransaction.setDate(formattedDate2);
                            moredtransaction.setNotpaid(0);
                            moredtransaction.setName(invoice.getCustomer_name());
                            moredtransaction.setProcess("ارجاع فاتوره");
                            moredtransaction.setValue(Round.round(invoice.getTotal(), 3));

                            Boolean result5 = db.insert_date(moredtransaction);

                            if (result5) {

                                //  Toast.makeText(con, "تم تسجيل تعاملات المورد ", Toast.LENGTH_SHORT).show();
                            } else {
                                // Toast.makeText(con, "فشل تسجيل تعاملات المورد", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(con, "لا يوجد موردين بهذا الاسم !", Toast.LENGTH_SHORT).show();
                        }
                    }




                 /*   product pro ;

                    for (int i = 0; i < productList.size(); i++) {
                        pro = productList.get(i);
                        pro.setQuantity(pro.getQuantity() + quantityList.get(i));
                        Boolean result4 = productDataBaseHelper.updateData(pro);
                        if (result4) {
                            //    Toast.makeText(sales.this, "تم تعديل المنتج بنجاح !", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        } else {
                            Toast.makeText(buy.this, "حدث خطا في تعديل البيانات تاكد من ادخال بينات صحيحه !", Toast.LENGTH_SHORT).show();

                        }
                    }
*/
                    Cursor res = db.getsellproduct(invoice.getInvoice_id());

                    if (res != null && res.getCount() > 0) {
                        while (res.moveToNext()) {

                            String sellproductName = res.getString(4);
                            double sellproductQuantity = res.getDouble(2);
                            DataBaseHelper pdb = new DataBaseHelper(con);

                            Cursor res6 = pdb.getQuantites(sellproductName);

                            if (res6 != null && res6.getCount() > 0) {
                                while (res6.moveToNext()) {
                                    Log.d("test", "result " + res6.getColumnName(4) + " " + res6.getDouble(4));

                                    double newquantity = res6.getDouble(6) - sellproductQuantity;
                                    Boolean result4 = pdb.updateDatabyName(sellproductName, newquantity);
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
                    //  Toast.makeText(con, "تم حذف الفاتوره واضافة المبلغ الي الصنوق", Toast.LENGTH_SHORT).show();
                    ((Activity) con).finish();
                    db.close();
                    dialog.dismiss();
                });

                Button cancel = (Button) dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            });


            invoice_id.setText(invoice.getInvoice_id() + "");
            date.setText(invoice.getDate() + "");
            total.setText(Round.round(invoice.getTotal(), 3) + SheredPrefranseHelper.getmoney_type(con));
            customer_name.setText(invoice.getCustomer_name() + "");


        } catch (
                Exception e) {
            Toast.makeText(con, "فشل ارجاع البيانات !", Toast.LENGTH_SHORT).show();

        }


        return row;
    }
}



