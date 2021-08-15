package com.mohamedragab.cashpos.modules.invoice.wedgits;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mohamedragab.cashpos.modules.invoicedetails.views.invoicedetails;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.sales.models.invoice;
import com.mohamedragab.cashpos.utils.Round;

import java.util.List;


public class invoiceAdapter extends ArrayAdapter {

    List<invoice> invoiceLists;
    Context con;

    public invoiceAdapter(Context context, List<invoice> invoiceList) {
        super(context, R.layout.invoiceview_item, R.id.value, invoiceList);
        this.invoiceLists = invoiceList;
        con = context;
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
        TextView cashier = (TextView) row.findViewById(R.id.cashier_name);
        TextView discount = (TextView) row.findViewById(R.id.discount);

        LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.linear);

        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(Color.parseColor("#eaddff"));
        } else {

        }
        try {
            invoice invoice = invoiceLists.get(position);

            linearLayout.setOnClickListener(view -> {
                String value = invoiceLists.get(position).getInvoice_id();
                Intent i = new Intent(con, invoicedetails.class);
                i.putExtra("key", value);
                con.startActivity(i);
                ((Activity) con).finish();

                //Toast.makeText(con, invoiceLists.get(position).getInvoice_id(), Toast.LENGTH_SHORT).show();
            });

            invoice_id.setText(invoice.getInvoice_id() + "");
            date.setText(invoice.getDate() + "");
            total.setText(Round.round(invoice.getTotal(), 3) + SheredPrefranseHelper.getmoney_type(con));
            customer_name.setText(invoice.getCustomer_name() + "");
            if (invoice.getCashier() != null) {
                cashier.setText("الكاشير : " + invoice.getCashier());
            }
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



