package com.mohamedragab.cashpos.modules.omla.wedgits;

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

import com.mohamedragab.cashpos.modules.omlatransactions.views.omlatransactions;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.omla.models.omla;
import com.mohamedragab.cashpos.modules.omla.views.omlamoney;
import com.mohamedragab.cashpos.utils.Round;

import java.util.List;


public class omlamoneyAdapter extends ArrayAdapter {

    List<omla> omlas;
    Context con;

    public omlamoneyAdapter(Context context, List<omla> omla) {
        super(context, R.layout.omla_view_item, R.id.name, omla);
        this.omlas = omla;
        con = context;

    }

    public void setomlaAdapter(List<omla> omlalist) {
        this.omlas = omlalist;
        double total = 0;
        for (int i = 0; i < omlalist.size(); i++) {
            total += omlalist.get(i).getPaymoney();
        }
        omlamoney.not_paid_total.setText("اجمالي المبلغ المتبقي عند العملاء : " + Round.round(total, 3) + SheredPrefranseHelper.getmoney_type(con));

    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.omla_view_item, parent, false);

        TextView name = (TextView) row.findViewById(R.id.name);
        TextView address = (TextView) row.findViewById(R.id.address);
        TextView pay = (TextView) row.findViewById(R.id.phone);
        TextView maxnotpaid = (TextView) row.findViewById(R.id.maxnotpaid);

        LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.linearitem);

        linearLayout.setOnClickListener(view -> {
            String value = omlas.get(position).getName();
            Intent i = new Intent(con, omlatransactions.class);
            i.putExtra("key", value);
            con.startActivity(i);
            ((Activity) con).finish();
        });
        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(Color.parseColor("#eaddff"));
        } else {

        }
        try {
            omla omla = omlas.get(position);

            name.setText(omla.getName() + "");
            pay.setText(Round.round(omla.getPaymoney(), 3) + "");
            address.setText(omla.getAddress() + "");
            maxnotpaid.setText(omla.getMaxnotpaid() + "");
        } catch (Exception e) {
            Toast.makeText(con, "فشل ارجاع العملاء !", Toast.LENGTH_SHORT).show();

        }


        return row;
    }
}



