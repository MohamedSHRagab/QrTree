package com.mohamedragab.cashpos.modules.store.wedgit;

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

import com.mohamedragab.cashpos.modules.store.models.Measure;
import com.mohamedragab.cashpos.R;

import java.util.List;


public class measureAdapter extends ArrayAdapter {

    static List<Measure> categories;
    Context con;

    public measureAdapter(Context context, List<Measure> category) {
        super(context, R.layout.category_view_item, R.id.category, category);
        this.categories = category;
        con = context;


    }

    public void setCategoriesAdapter(List<Measure> categoriesList) {
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
        linearLayout.setOnLongClickListener(view -> true);
        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(Color.parseColor("#eaddff"));
        } else {

        }
        try {
            Measure category = categories.get(position);

            name.setText(category.getMeasure() + "");

        } catch (Exception e) {

        }


        return row;
    }
}



