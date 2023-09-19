package com.mohamedragab.cashpos.modules.exchange.wedgit;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
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
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.buy.views.buy;
import com.mohamedragab.cashpos.modules.exchange.views.exchange;
import com.mohamedragab.cashpos.modules.store.models.product;

import java.util.List;


public class allproductsAdapter extends ArrayAdapter {

    List<product> product;
    Context con;
    Dialog dialog;
    String activity;

    public allproductsAdapter(Context context, List<product> products, Dialog dialog, String activity_txt) {
        super(context, R.layout.grid_item, R.id.name, products);
        this.product = products;
        con = context;
        this.dialog = dialog;
        activity=activity_txt;
    }

    public void setshopAdapter(List<product> products) {
        this.product = products;

    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.grid_item, parent, false);

        final TextView name = (TextView) row.findViewById(R.id.text);
        final TextView price = (TextView) row.findViewById(R.id.price);
        ImageView productImage = (ImageView) row.findViewById(R.id.image);
        LinearLayout linear = (LinearLayout) row.findViewById(R.id.linear);
        linear.setOnClickListener(v -> {
            if (activity.equals("exchange")){
                exchange.autoCompleteTextView.setText(product.get(position).getCode_id() + "");
            }else {
               buy.autoCompleteTextView.setText(product.get(position).getCode_id() + "");
            }
          //  dialog.dismiss();
        });
        product pro = product.get(position);

        name.setText(pro.getName() + "");
        if (activity.equals("exchange")){
            price.setText(pro.getSellprice() + SheredPrefranseHelper.getmoney_type(con));
        }else if(activity.equals("booking")) {
        }else {
            price.setText(pro.getBuyprice() + SheredPrefranseHelper.getmoney_type(con));
        }
        if (pro.getImage() != null) {
            productImage.setImageBitmap(BitmapFactory.decodeByteArray(pro.getImage(), 0, pro.getImage().length));
        }

        return row;
    }
}



