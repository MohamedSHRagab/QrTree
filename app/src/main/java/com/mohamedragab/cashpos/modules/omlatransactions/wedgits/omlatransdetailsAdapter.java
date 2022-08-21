package com.mohamedragab.cashpos.modules.omlatransactions.wedgits;

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

import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.invoicedetails.views.invoicedetails;
import com.mohamedragab.cashpos.modules.omlatransactions.models.omlatransaction;
import com.mohamedragab.cashpos.modules.omlatransactions.views.omlatransactions;
import com.mohamedragab.cashpos.utils.Round;

import java.util.List;


public class omlatransdetailsAdapter extends ArrayAdapter {

    List<omlatransaction> omlatransactionList;
    Context con;

    public omlatransdetailsAdapter(Context context, List<omlatransaction> omlatransactionList) {
        super(context, R.layout.omlatransaction_item, R.id.productname, omlatransactionList);
        this.omlatransactionList = omlatransactionList;
        con = context;


    }

    public void setomlatransdetailsAdapter(List<omlatransaction> omlatransactionList) {
        this.omlatransactionList = omlatransactionList;

    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.omlatransaction_item, parent, false);

        TextView notpaid = (TextView) row.findViewById(R.id.notpaid);
        TextView invoice_id = (TextView) row.findViewById(R.id.invoice_id);
        TextView date = (TextView) row.findViewById(R.id.date);
        TextView value = (TextView) row.findViewById(R.id.value);
        TextView process = (TextView) row.findViewById(R.id.process);

        LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.linearitem);

        linearLayout.setOnClickListener(view -> {
            if (omlatransactionList.get(position).getInvoiceId() != null) {
                if (!omlatransactionList.get(position).getInvoiceId().equals("-")) {
                    String value2 = omlatransactionList.get(position).getInvoiceId();
                    Intent i = new Intent(con, invoicedetails.class);
                    i.putExtra("key", value2);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    con.startActivity(i);
                }
                if (omlatransactionList.get(position).getInvoiceId().equals("-")&&omlatransactionList.get(position).getProcess().equals("دفع")){
                    omlatransactions.reciptdate=omlatransactionList.get(position).getDate();
                }
            }

        });
        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(Color.parseColor("#eaddff"));
        } else {

        }
        try {
            omlatransaction transaction = omlatransactionList.get(position);
            if (transaction.getNotpaid() == 0) {
                notpaid.setText("");
            } else {
                notpaid.setText(Round.round(transaction.getNotpaid(), 3) + "");
            }
            invoice_id.setText(transaction.getInvoiceId() + "");
            date.setText(transaction.getDate() + "");
            if (transaction.getValue() == 0) {
                value.setText("");
            } else {
                value.setText(Round.round(transaction.getValue(), 3) + "");
            }
            process.setText(transaction.getProcess() + "");


        } catch (Exception e) {
            Toast.makeText(con, "فشل ارجاع التعاملات !", Toast.LENGTH_SHORT).show();

        }


        return row;
    }
}



