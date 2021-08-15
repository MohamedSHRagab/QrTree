package com.mohamedragab.cashpos.modules.employees.wedgits;

import android.annotation.SuppressLint;
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
import com.mohamedragab.cashpos.modules.employees.models.DelivTrans;

import java.util.List;


public class deliveryTransAdapter extends ArrayAdapter {

    List<DelivTrans> cashiers;
    Context con;

    public deliveryTransAdapter(Context context, List<DelivTrans> cashiers) {
        super(context, R.layout.delivery_trans_view_item, R.id.name, cashiers);
        this.cashiers = cashiers;
        con = context;
    }

    public void setdeliveryTransAdapter(List<DelivTrans> cashiers_list) {
        this.cashiers = cashiers_list;
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.delivery_trans_view_item, parent, false);

        final TextView date = (TextView) row.findViewById(R.id.date);
        TextView address = (TextView) row.findViewById(R.id.address);
        TextView money = (TextView) row.findViewById(R.id.money);
        TextView invoice = (TextView) row.findViewById(R.id.invoice_id);

        LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.linearitem);
        linearLayout.setOnClickListener(view -> {

            String value = cashiers.get(position).getInvoice_id();
            Intent i = new Intent(con, invoicedetails.class);
            i.putExtra("key", value);
            con.startActivity(i);

        });
        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(Color.parseColor("#eaddff"));
        } else {

        }
        try {
            DelivTrans cashier = cashiers.get(position);

            date.setText(cashier.getDate() + "");
            money.setText(cashier.getMoney() + "");
            address.setText(cashier.getAddress() + "");
            invoice.setText(cashier.getInvoice_id() );


        } catch (Exception e) {
            Toast.makeText(con, e.getMessage() + "", Toast.LENGTH_SHORT).show();
        }


        return row;
    }
}



