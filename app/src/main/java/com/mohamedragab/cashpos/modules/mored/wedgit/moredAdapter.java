package com.mohamedragab.cashpos.modules.mored.wedgit;

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

import com.mohamedragab.cashpos.modules.moredtransactions.views.moredtransactions;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.mored.models.mored;

import java.util.List;


public class moredAdapter extends ArrayAdapter {

    List<mored> moreds;
    Context con;

    public moredAdapter(Context context, List<mored> mored) {
        super(context, R.layout.omla_item, R.id.name, mored);
        this.moreds = mored;
        con = context;


    }

    public void setmoredAdapter(List<mored> moredlist) {
        this.moreds = moredlist;

    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.omla_item, parent, false);

        TextView name = (TextView) row.findViewById(R.id.name);
        ImageView edit = (ImageView) row.findViewById(R.id.edit);
        edit.setVisibility(View.GONE);
        TextView address = (TextView) row.findViewById(R.id.address);
        TextView phone = (TextView) row.findViewById(R.id.phone);
        LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.linearitem);

        linearLayout.setOnClickListener(view -> {
            String value = moreds.get(position).getName();
            Intent i = new Intent(con, moredtransactions.class);
            i.putExtra("key", value);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            con.startActivity(i);
            ((Activity) con).finish();
        });
        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(Color.parseColor("#eaddff"));
        } else {

        }
        try {
            mored mored = moreds.get(position);

            name.setText(mored.getName() + "");
            phone.setText(mored.getPhone() + "");
            address.setText(mored.getAddress() + "");

        } catch (Exception e) {
            Toast.makeText(con, "فشل ارجاع العملاء !", Toast.LENGTH_SHORT).show();

        }


        return row;
    }
}



