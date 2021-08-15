package com.mohamedragab.cashpos.modules.moredtransactions.wedgits;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.mohamedragab.cashpos.modules.moredtransactions.models.moredtransaction;
import com.mohamedragab.cashpos.utils.Round;

import java.util.List;


public class moredtransdetailsAdapter extends ArrayAdapter {

    List<moredtransaction> moredtransactionList;
    Context con;

    public moredtransdetailsAdapter(Context context, List<moredtransaction> moredtransactionList) {
        super(context, R.layout.omlatransaction_item, R.id.productname, moredtransactionList);
        this.moredtransactionList = moredtransactionList;
        con = context;


    }

    public void setmoredtransdetailsAdapter(List<moredtransaction> moredtransactionList) {
        this.moredtransactionList = moredtransactionList;

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

        linearLayout.setOnClickListener(view -> Toast.makeText(con, "" + moredtransactionList.get(position).getName(), Toast.LENGTH_SHORT).show());
        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(Color.parseColor("#eaddff"));
        } else {

        }
        try {
            moredtransaction transaction = moredtransactionList.get(position);
            if (transaction.getNotpaid()==0){
                notpaid.setText( "");
            }else{
                notpaid.setText(Round.round(transaction.getNotpaid(),3) + "");
            }
            invoice_id.setText(transaction.getInvoiceId() + "");
            date.setText(transaction.getDate() + "");
            if (transaction.getValue()==0){
                value.setText( "");
            }else{
                value.setText(Round.round(transaction.getValue(),3) + "");
            }
            process.setText(transaction.getProcess() + "");


        } catch (Exception e) {
            Toast.makeText(con, "فشل ارجاع التعاملات !", Toast.LENGTH_SHORT).show();

        }


        return row;
    }
}



