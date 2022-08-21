package com.mohamedragab.cashpos.modules.latekist.adapters;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.sales.models.Kist;
import com.mohamedragab.cashpos.utils.Round;

import java.util.ArrayList;
import java.util.List;


public class kistlateAdapter extends ArrayAdapter {

    List<Kist> kists;
    Context con;
    DataBaseHelper db;
    Boolean statue;
    ArrayList<Long> Days;

    public kistlateAdapter(Context context, List<Kist> kists, ArrayList<Long> Days) {
        super(context, R.layout.kist_late_item, R.id.name, kists);
        this.kists = kists;
        con = context;
        this.Days=Days;
        db = new DataBaseHelper(con);


    }

    public void setKistlateAdapter(List<Kist> kists, ArrayList<Long> Days) {
        this.kists = kists;
        this.Days=Days;
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.kist_late_item, parent, false);

        TextView num = (TextView) row.findViewById(R.id.number);
        TextView name = (TextView) row.findViewById(R.id.name);
        TextView value = (TextView) row.findViewById(R.id.value);
        TextView paydate = (TextView) row.findViewById(R.id.paydate);
        TextView latedays = (TextView) row.findViewById(R.id.latedays);

        ImageView call = (ImageView) row.findViewById(R.id.call);
        call.setOnClickListener(v -> {
            Uri u = Uri.parse("tel:" + kists.get(position).getDamen_phone());
            Intent i = new Intent(Intent.ACTION_DIAL, u);
            con.startActivity(i);
        });

        LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.linearitem);
        linearLayout.setOnClickListener(view -> {

        });
        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(Color.parseColor("#eaddff"));
        }
        Kist kist = kists.get(position);
        num.setText((position + 1) + "");
        name.setText(kist.getClient_name() + "");
        value.setText(Round.round(kist.getKist_value(),3) + "");
        paydate.setText(kist.getCollectdate() + "");
        latedays.setText(Days.get(position)+"");
        return row;
    }
}



