package com.mohamedragab.cashpos.modules.notification.wedgits;

import android.annotation.SuppressLint;
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

import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.store.models.product;

import java.util.List;


public class productquantityAdapter extends ArrayAdapter {

    List<product> products;
    Context con;
    DataBaseHelper productDataBaseHelper;

    public productquantityAdapter(Context context, List<product> product) {
        super(context, R.layout.productquantity_item, R.id.name, product);
        this.products = product;
        con = context;
        productDataBaseHelper = new DataBaseHelper(con);


    }

    public void setproductAdapter(List<product> productList) {
        this.products = productList;

    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.productquantity_item, parent, false);

        TextView name = (TextView) row.findViewById(R.id.productname);
        TextView quantity = (TextView) row.findViewById(R.id.quantity);
        LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.linearitem);
        linearLayout.setOnClickListener(view -> Toast.makeText(con, "" + products.get(position).getName(), Toast.LENGTH_SHORT).show());
        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(Color.parseColor("#eaddff"));
        } else {

        }
        try {
            product product = products.get(position);

            name.setText(product.getName() + "");
            quantity.setText(product.getQuantity() + "");

        } catch (Exception e) {
            Toast.makeText(con, "فشل ارجاع المنتجات !", Toast.LENGTH_SHORT).show();

        }


        return row;
    }
}



