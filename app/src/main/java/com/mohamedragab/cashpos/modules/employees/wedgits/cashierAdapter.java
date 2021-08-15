package com.mohamedragab.cashpos.modules.employees.wedgits;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
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
import com.mohamedragab.cashpos.modules.employees.models.Cashier;
import com.mohamedragab.cashpos.modules.employees.views.delivery_profile;
import com.mohamedragab.cashpos.modules.employees.views.newCashier;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class cashierAdapter extends ArrayAdapter {

    List<Cashier> cashiers;
    Context con;
    String activity;

    public cashierAdapter(Context context, List<Cashier> cashiers, String activity) {
        super(context, R.layout.cashier_view_item, R.id.name, cashiers);
        this.cashiers = cashiers;
        con = context;
        this.activity=activity;
    }

    public void setcashierAdapter(List<Cashier> cashiers_list) {
        this.cashiers = cashiers_list;
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.cashier_view_item, parent, false);

        final TextView name = (TextView) row.findViewById(R.id.name);
        TextView address = (TextView) row.findViewById(R.id.address);
        TextView phone = (TextView) row.findViewById(R.id.phone);
        LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.linearitem);
        CircleImageView imageView = (CircleImageView) row.findViewById(R.id.image);
        ImageView edit = (ImageView) row.findViewById(R.id.edit);
        edit.setOnClickListener(v -> {
            Intent yourIntent = new Intent(con, newCashier.class);
            Bundle b = new Bundle();
            b.putSerializable("cashier", cashiers.get(position));
            yourIntent.putExtras(b); //pass bundle to your intent
            con.startActivity(yourIntent);
            ((Activity)con).finish();
        });

        linearLayout.setOnClickListener(view -> {
            if (activity.equals("CASH")) {
              /*  final Dialog dialog = new Dialog(con);
                dialog.setContentView(R.layout.choosecurrentcashierdialog);

                Button current = (Button) dialog.findViewById(R.id.current);
                current.setOnClickListener(v -> {
                    SheredPrefranseHelper.addcurrentcashier(con, cashiers.get(position));
                    Toast.makeText(con, "اصبح الكاشير الحالي" + " : " + cashiers.get(position).getName(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });


                dialog.show();*/
            }else{
                String value = cashiers.get(position).getId()+"";
                Intent i = new Intent(con, delivery_profile.class);
                i.putExtra("key", value);
                con.startActivity(i);
            }
        });
        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(Color.parseColor("#eaddff"));
        } else {

        }
        try {
            Cashier cashier = cashiers.get(position);

            name.setText(cashier.getName() + "");
            phone.setText(cashier.getPhone() + "");
            address.setText(cashier.getAddress() + "");
            if (cashier.getImage() != null) {
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(cashier.getImage(), 0, cashier.getImage().length));
            }

        } catch (Exception e) {
            Toast.makeText(con, "فشل ارجاع المندوب !", Toast.LENGTH_SHORT).show();

        }


        return row;
    }
}



