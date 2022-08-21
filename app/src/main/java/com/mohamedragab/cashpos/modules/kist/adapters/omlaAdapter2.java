package com.mohamedragab.cashpos.modules.kist.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import androidx.appcompat.app.AlertDialog;

import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.kist.views.customerKist;
import com.mohamedragab.cashpos.modules.omla.models.omla;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.sales.models.Kist;

import java.util.List;


public class omlaAdapter2 extends ArrayAdapter {

    List<omla> omlas;
    Context con;

    public omlaAdapter2(Context context, List<omla> omla) {
        super(context, R.layout.omla_view_item_kist, R.id.name, omla);
        this.omlas = omla;
        con = context;

    }

    public void setomlaAdapter(List<omla> omlalist) {
        this.omlas = omlalist;
       /* double total = 0;
        kestDataBaseHelper = new DataBaseHelper(con);

        for (int i = 0; i < omlas.size(); i++) {
            Cursor res2 = kestDataBaseHelper.getkestbyclient(omlas.get(i).getName());
            double totalNotPaid2 = 0;
            if (res2 != null && res2.getCount() > 0) {
                while (res2.moveToNext()) {
                    Kist kist = new Kist();

                    kist.setId(res2.getInt(0));
                    kist.setInvoice_id(res2.getString(1));
                    kist.setDescription(res2.getString(2));
                    kist.setDayslimit(res2.getInt(3));
                    kist.setCollectdate(res2.getString(4));
                    kist.setKist_value(res2.getDouble(5));
                    kist.setStatue(res2.getString(6));
                    kist.setClient_name(res2.getString(7));
                    kist.setPay_type(res2.getString(8));
                    kist.setDamen_name(res2.getString(9));
                    kist.setDamen_phone(res2.getString(10));
                    kist.setPaydate(res2.getString(11));

                    if (res2.getString(6).equals("not_paid")) {
                        totalNotPaid2 += res2.getDouble(5);
                    }
                }
                res2.close();

            }
            total += totalNotPaid2;
        }
        omalkist.not_paid_total.setText("الاجمالي : "+ Round.round(total,3) + SheredPrefranseHelper.getmoney_type(con));
        kestDataBaseHelper.close();*/
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.omla_view_item_kist, parent, false);

        final TextView name = (TextView) row.findViewById(R.id.name);
        TextView address = (TextView) row.findViewById(R.id.address);
        TextView notpaid = (TextView) row.findViewById(R.id.notpaid);
        notpaid.setOnClickListener(view -> {
            omla omla = omlas.get(position);
            DataBaseHelper db=new DataBaseHelper(con);
            Cursor res = db.getkestbyclient(omlas.get(position).getName());
            int totalNotPaid = 0;
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    Kist kist = new Kist();

                    kist.setId(res.getInt(0));
                    kist.setInvoice_id(res.getString(1));
                    kist.setDescription(res.getString(2));
                    kist.setDayslimit(res.getInt(3));
                    kist.setCollectdate(res.getString(4));
                    kist.setKist_value(res.getDouble(5));
                    kist.setStatue(res.getString(6));
                    kist.setClient_name(res.getString(7));
                    kist.setPay_type(res.getString(8));
                    kist.setDamen_name(res.getString(9));
                    kist.setDamen_phone(res.getString(10));
                    kist.setPaydate(res.getString(11));

                    if (res.getString(6).equals("not_paid")) {
                        totalNotPaid += res.getDouble(5);
                    }
                }
                AlertDialog.Builder dialog2 = new AlertDialog.Builder(con);
                dialog2.setCancelable(false);
                dialog2.setTitle("اجمالي الاقساط الغير مدفوعه");
                dialog2.setMessage(totalNotPaid+"");
                dialog2.setPositiveButton("الغاء", (dialog12, id) -> {
                });

                final AlertDialog alert = dialog2.create();
                alert.show();
                res.close();
            }
        });
        ImageView call = (ImageView) row.findViewById(R.id.call);
        call.setOnClickListener(v -> {
            Uri u = Uri.parse("tel:" + omlas.get(position).getPhone());
            Intent i = new Intent(Intent.ACTION_DIAL, u);
            con.startActivity(i);
        });

        LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.linearitem);

        linearLayout.setOnClickListener(view -> {
            String value = omlas.get(position).getName();
            Intent i = new Intent(con, customerKist.class);
            i.putExtra("key", value);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            con.startActivity(i);
            ((Activity) con).finish();

        });
        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(Color.parseColor("#eaddff"));
        }
        try {
            omla omla = omlas.get(position);

          /*  Cursor res = kestDataBaseHelper.getkestbyclient(omlas.get(position).getName());
            int totalNotPaid = 0;
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    Kist kist = new Kist();

                    kist.setId(res.getInt(0));
                    kist.setInvoice_id(res.getString(1));
                    kist.setDescription(res.getString(2));
                    kist.setDayslimit(res.getInt(3));
                    kist.setCollectdate(res.getString(4));
                    kist.setKist_value(res.getDouble(5));
                    kist.setStatue(res.getString(6));
                    kist.setClient_name(res.getString(7));
                    kist.setPay_type(res.getString(8));
                    kist.setDamen_name(res.getString(9));
                    kist.setDamen_phone(res.getString(10));
                    kist.setPaydate(res.getString(11));

                    if (res.getString(6).equals("not_paid")) {
                        totalNotPaid += res.getDouble(5);
                    }
                }
                res.close();

            }
*/
            name.setText(omla.getName() + "");
            notpaid.setText( "عرض");
            address.setText(omla.getAddress() + "");
        } catch (Exception e) {
            Toast.makeText(con, "فشل ارجاع العملاء !", Toast.LENGTH_SHORT).show();
        }


        return row;
    }
}



