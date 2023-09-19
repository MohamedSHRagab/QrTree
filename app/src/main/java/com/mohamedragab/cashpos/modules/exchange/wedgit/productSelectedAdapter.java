package com.mohamedragab.cashpos.modules.exchange.wedgit;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.exchange.views.exchange;
import com.mohamedragab.cashpos.modules.store.models.product;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.mohamedragab.cashpos.modules.exchange.views.exchange.quantityList;


public class productSelectedAdapter extends ArrayAdapter {

    List<product> products;
    Context con;
    ListView listView;
    productSelectedAdapter productAdapter;
    EditText quan_text;

    public productSelectedAdapter(Context context, List<product> product) {
        super(context, R.layout.product_selected_view_item2, R.id.name, product);
        this.products = product;
        con = context;


    }

    public void setproductAdapter(List<product> productList, ListView listView) {
        this.products = productList;
        this.listView = listView;


    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.product_selected_view_item2, parent, false);

        final TextView name = (TextView) row.findViewById(R.id.productname);
        final TextView quantity = (TextView) row.findViewById(R.id.quantity);
        final CircleImageView productImage = (CircleImageView) row.findViewById(R.id.image);


        productAdapter = new productSelectedAdapter(con, products);


        final LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.linear);
        linearLayout.setOnClickListener(view -> {


            final Dialog dialog = new Dialog(con);
            dialog.setContentView(R.layout.edititeminsale3);
            dialog.setCancelable(false);

            TextView delete = (TextView) dialog.findViewById(R.id.delete);
            quan_text = (EditText) dialog.findViewById(R.id.quantity);
            quan_text.setText(quantityList.get(position) + "");
            final product product = products.get(position);

            delete.setOnClickListener(v -> {

                dialog.dismiss();
                products.remove(products.get(position));
                quantityList.remove(position);
                productAdapter.setproductAdapter(products, listView);
                listView.setAdapter(productAdapter);

            });

            Button update = (Button) dialog.findViewById(R.id.update);
            update.setOnClickListener(v -> {
                        continue_1(position, quantity, dialog);
            });

            dialog.show();


        });
        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(Color.parseColor("#fff7f7"));
        } else {

        }

        product product = products.get(position);
        name.setText(product.getName() + "");
        quantity.setText(quantityList.get(position) + "");
        if (product.getImage() != null) {
            productImage.setImageBitmap(BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length));
        }

        return row;
    }

    public void continue_1(int position, TextView quantity, Dialog dialog) {

            quantity.setText(Double.parseDouble(quan_text.getText().toString().trim()) + "");
            quantityList.set(position, Double.parseDouble(quan_text.getText().toString().trim()));

            productAdapter.setproductAdapter(exchange.productList, listView);
            listView.setAdapter(productAdapter);
            dialog.dismiss();

    }

}



