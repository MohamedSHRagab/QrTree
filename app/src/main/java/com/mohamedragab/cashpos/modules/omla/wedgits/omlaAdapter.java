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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mohamedragab.cashpos.modules.omlatransactions.views.omlatransactions;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.omla.models.omla;
import com.mohamedragab.cashpos.modules.omla.views.newcustomer;

import java.util.List;


public class omlaAdapter extends ArrayAdapter {

    List<omla> omlas;
    Context con;

    public omlaAdapter(Context context, List<omla> omla) {
        super(context, R.layout.omla_item, R.id.name, omla);
        this.omlas = omla;
        con = context;


    }

    public void setomlaAdapter(List<omla> omlalist) {
        this.omlas = omlalist;

    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.omla_item, parent, false);

        final TextView name = (TextView) row.findViewById(R.id.name);
        final ImageView edit = (ImageView) row.findViewById(R.id.edit);

        TextView address = (TextView) row.findViewById(R.id.address);
        TextView phone = (TextView) row.findViewById(R.id.phone);
        LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.linearitem);
        edit.setOnClickListener(v -> {
            Intent i = new Intent(con, newcustomer.class);
            i.putExtra("omla",omlas.get(position));
            con.startActivity(i);
            ((Activity) con).finish();

        });
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
            phone.setText(omla.getPhone() + "");
            address.setText(omla.getAddress() + "");

        } catch (Exception e) {
            Toast.makeText(con, "فشل ارجاع العملاء !", Toast.LENGTH_SHORT).show();

        }


        return row;
    }
}



