package com.mohamedragab.cashpos.modules.repair.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.repair.models.Repair;

import java.util.ArrayList;
import java.util.List;


public class repairlateAdapter extends ArrayAdapter {

    List<Repair> repairs;
    Context con;
    Boolean statue;
    ArrayList<Long> Days;

    public repairlateAdapter(Context context, List<Repair> repairs, ArrayList<Long> Days) {
        super(context, R.layout.repair_late_item, R.id.name, repairs);
        this.repairs = repairs;
        con = context;
        this.Days=Days;
    }

    public void setrepairlateAdapter(List<Repair> repairs, ArrayList<Long> Days) {
        this.repairs = repairs;
        this.Days=Days;
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.repair_late_item, parent, false);

        TextView num = (TextView) row.findViewById(R.id.number);
        TextView name = (TextView) row.findViewById(R.id.name);
        TextView visitedate = (TextView) row.findViewById(R.id.visitedate);
        TextView latedays = (TextView) row.findViewById(R.id.latedays);



        LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.linearitem);
        linearLayout.setOnClickListener(view -> {

        });
        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(Color.parseColor("#eaddff"));
        }
        Repair repair = repairs.get(position);
        num.setText((position + 1) + "");
        name.setText(repair.getClient_name() + "");
        visitedate.setText(repair.getVisit_at() + "");
        latedays.setText(Days.get(position)+"");
        return row;
    }
}



