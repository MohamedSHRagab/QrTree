package com.mohamedragab.cashpos.modules.store.wedgit;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import androidx.appcompat.app.AlertDialog;

import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.store.models.category;

import java.util.List;


public class categoryAdapter extends ArrayAdapter {

    static List<category> categories;
    Context con;
    DataBaseHelper db;

    public categoryAdapter(Context context, List<category> category) {
        super(context, R.layout.category_view_item, R.id.category, category);
        this.categories = category;
        con = context;
        db = new DataBaseHelper(con);


    }

    public void setCategoriesAdapter(List<category> categoriesList) {
        this.categories = categoriesList;

    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.category_view_item, parent, false);

        TextView name = (TextView) row.findViewById(R.id.category);

        LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.linearitem);
        linearLayout.setOnClickListener(view -> {
            AlertDialog.Builder dialog2 = new AlertDialog.Builder(con);
            dialog2.setCancelable(false);
            dialog2.setTitle("حذف التصنيف !");
            dialog2.setPositiveButton("حذف", (dialog12, id) -> {
                db.deletecategory(categories.get(position).getCategory());
                Toast.makeText(con, "تم حـذف التصنيف : "+categories.get(position).getCategory(), Toast.LENGTH_SHORT).show();
                db.close();
                ((Activity) con).recreate();

            })
                    .setNegativeButton("الغاء ", (dialog1, which) -> {
                    });

            final AlertDialog alert = dialog2.create();
            alert.show();
        });
        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(Color.parseColor("#eaddff"));
        } else {

        }
        try {
            category category = categories.get(position);

            name.setText(category.getCategory() + "");

        } catch (Exception e) {

        }


        return row;
    }
}



