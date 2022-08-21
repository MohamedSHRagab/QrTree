package com.mohamedragab.cashpos.modules.repair.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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

import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.omla.models.omla;
import com.mohamedragab.cashpos.modules.repair.views.customerRepair;

import java.util.List;


public class omlaAdapter3 extends ArrayAdapter {

    List<omla> omlas;
    Context con;

    public omlaAdapter3(Context context, List<omla> omla) {
        super(context, R.layout.omla_view_item_kist, R.id.name, omla);
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
        View row = inflater.inflate(R.layout.omla_view_item_kist, parent, false);

        final TextView name = (TextView) row.findViewById(R.id.name);
        TextView address = (TextView) row.findViewById(R.id.address);
        TextView phone = (TextView) row.findViewById(R.id.notpaid);
        ImageView call = (ImageView) row.findViewById(R.id.call);
        call.setOnClickListener(v -> {
            Uri u = Uri.parse("tel:" + omlas.get(position).getPhone());
            Intent i = new Intent(Intent.ACTION_DIAL, u);
            con.startActivity(i);
        });

        LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.linearitem);

        linearLayout.setOnClickListener(view -> {
            String value = omlas.get(position).getName();
            Intent i = new Intent(con, customerRepair.class);
            i.putExtra("key", value);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            con.startActivity(i);
        });
        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(Color.parseColor("#eaddff"));
        }
        try {
            omla omla = omlas.get(position);

            name.setText(omla.getName() + "");
            phone.setText(omla.getPhone()+ "");
            address.setText(omla.getAddress() + "");

        } catch (Exception e) {
            Toast.makeText(con, "فشل ارجاع العملاء !", Toast.LENGTH_SHORT).show();
        }


        return row;
    }
}



