package com.mohamedragab.cashpos.modules.invoicedetails.wedgits;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.invoice.views.invoiceView;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.sales.models.invoice;
import com.mohamedragab.cashpos.modules.sales.models.sellproduct;
import com.mohamedragab.cashpos.utils.Round;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class invoicedetailsAdapter extends ArrayAdapter {

    List<sellproduct> products;
    Context con;
    DataBaseHelper db;

    public invoicedetailsAdapter(Context context, List<sellproduct> sellproduct) {
        super(context, R.layout.invoicedetails_item, R.id.productname, sellproduct);
        this.products = sellproduct;
        con = context;
        db = new DataBaseHelper(con);
    }

    public void setinvoicedetailsAdapter(List<sellproduct> sellproduct) {
        this.products = sellproduct;
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.invoicedetails_item, parent, false);

        TextView total = (TextView) row.findViewById(R.id.total);
        TextView price = (TextView) row.findViewById(R.id.price);
        TextView quantity = (TextView) row.findViewById(R.id.quantity);
        TextView proname = (TextView) row.findViewById(R.id.proname);

        LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.linearitem);

        linearLayout.setOnClickListener(view -> {

            showdeleteDialog(products.get(position));
            // Toast.makeText(con, "" + products.get(position).getName(), Toast.LENGTH_SHORT).show();
        });
        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(Color.parseColor("#eaddff"));
        } else {

        }
        try {
            sellproduct product = products.get(position);
            double total_val = Round.round((product.getQuantity() * product.getSellprice()), 3);
            total.setText(total_val + "");
            quantity.setText(product.getQuantity() + "");
            price.setText(product.getSellprice() + "");
            proname.setText(product.getName() + "");

        } catch (Exception e) {
            Toast.makeText(con, "فشل ارجاع المنتجات !", Toast.LENGTH_SHORT).show();

        }


        return row;
    }

    private void showdeleteDialog(sellproduct sellproduct) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(con);
        builder1.setTitle("تحذير !");
        builder1.setIcon(R.drawable.notcompleted);
        builder1.setMessage("هل تريد حذف الصنف من الفاتوره !");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "حذف",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteproduct(sellproduct);
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "الغاء",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();


    }

    private void deleteproduct(sellproduct sellproduct) {
        db.deletesellProduct(sellproduct.getId() + "");
        // Toast.makeText(con, "تم حذف الصنف من الفاتوره .", Toast.LENGTH_SHORT).show();
        String sellproductName = sellproduct.getName();
        double sellproductQuantity = sellproduct.getQuantity();
        DataBaseHelper pdb = new DataBaseHelper(con);

        Cursor res6 = pdb.getQuantites(sellproductName);

        if (res6 != null && res6.getCount() > 0) {
            double newquantity = 0;
            while (res6.moveToNext()) {
                Log.d("test", "result " + res6.getColumnName(4) + " " + res6.getDouble(4));
                newquantity = res6.getDouble(6) + sellproductQuantity;
            }
            if (pdb.updateDatabyName(sellproductName, newquantity)) {
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                String formattedDate2 = df2.format(c);

                com.mohamedragab.cashpos.modules.moneybox.models.money money = new com.mohamedragab.cashpos.modules.moneybox.models.money();
                money.setDate(formattedDate2);
                money.setNotes("ارجاع الصنف :" + sellproduct.getName());
                money.setValue(Round.round((sellproduct.getQuantity() * sellproduct.getSellprice()), 3));

                final Cursor res3 = db.getallTransactions();
                double total = 0;
                if (res3 != null && res3.getCount() > 0) {
                    while (res3.moveToNext()) {
                        total = res3.getDouble(3);

                    }

                }
                money.setTotalbefore(Round.round(total, 3));
                double totalAfter = Round.round((total - (sellproduct.getQuantity() * sellproduct.getSellprice())), 3);
                Log.d("totalAfter", totalAfter + "");
                money.setTotalAfter(totalAfter);

                if (db.insert_date(money)) {
                    //  Toast.makeText(con, "تم خصم المبلغ من حساب المورد !", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(con, "فشل اضافه المبلغ في الصندوق ! ", Toast.LENGTH_SHORT).show();

                }

            } else {
                Toast.makeText(con, "حدث خطا في تعديل البيانات تاكد من ادخال بينات صحيحه !", Toast.LENGTH_SHORT).show();

            }
            Cursor res4 = db.getsellinvoicebyInvoiceId(sellproduct.getInvoice_id());

            if (res4 != null && res4.getCount() > 0) {
                List<invoice> invoices = new ArrayList<>();

                while (res4.moveToNext()) {
                    invoice invoice = new invoice();
                    invoice.setTotal(res4.getDouble(4));
                    invoices.add(invoice);
                }

                if (db.updatesellinvoicebyName(sellproduct.getInvoice_id() + "", Round.round((invoices.get(0).getTotal() - (sellproduct.getQuantity() * sellproduct.getSellprice())), 3))) {
                    // Toast.makeText(con, "تم حذف الصنف من الفاتوره وخصم المبلغ من الخزينه .", Toast.LENGTH_LONG).show();
                    if ((invoices.get(0).getTotal() - (sellproduct.getQuantity() * sellproduct.getSellprice())) < 1) {
                        db.deletesellinvoice(sellproduct.getInvoice_id());
                    }
                    db.close();
                    con.startActivity(new Intent(con, invoiceView.class));
                    ((Activity) con).finish();

                }
            }

        }


    }
}
