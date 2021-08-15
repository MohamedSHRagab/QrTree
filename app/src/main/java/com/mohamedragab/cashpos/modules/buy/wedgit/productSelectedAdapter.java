package com.mohamedragab.cashpos.modules.buy.wedgit;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mohamedragab.cashpos.modules.store.models.product;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.buy.views.buy;
import com.mohamedragab.cashpos.utils.Round;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class productSelectedAdapter extends ArrayAdapter {

    List<product> products;
    Context con;
    ListView listView;
    productSelectedAdapter productAdapter;
    EditText quan_text;
    EditText oneprice;


    public productSelectedAdapter(Context context, List<product> product) {
        super(context, R.layout.product_selected_view_item, R.id.name, product);
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
        View row = inflater.inflate(R.layout.product_selected_view_item, parent, false);
        // final int[] quantity_value = {1};
        final TextView name = (TextView) row.findViewById(R.id.productname);
        final TextView quantity = (TextView) row.findViewById(R.id.quantity);
        final TextView price = (TextView) row.findViewById(R.id.productprice);
        final TextView total = (TextView) row.findViewById(R.id.total);
        final TextView description = (TextView) row.findViewById(R.id.description);
        final CircleImageView productImage = (CircleImageView) row.findViewById(R.id.image);

        productAdapter = new productSelectedAdapter(con, products);


        final LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.linear);
        linearLayout.setOnClickListener(view -> {


            final Dialog dialog = new Dialog(con);
            dialog.setContentView(R.layout.edititeminsale);
            dialog.setCancelable(false);

            LinearLayout equation = (LinearLayout) dialog.findViewById(R.id.equation);
            CheckBox check_equation = (CheckBox) dialog.findViewById(R.id.check_equation);
            check_equation.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    equation.setVisibility(View.VISIBLE);
                } else {
                    equation.setVisibility(View.GONE);
                }
            });
            TextView delete = (TextView) dialog.findViewById(R.id.delete);
            quan_text = (EditText) dialog.findViewById(R.id.quantity);
            quan_text.setText(buy.quantityList.get(position) + "");
            EditText tall1 = (EditText) dialog.findViewById(R.id.tall1);
            EditText tall2 = (EditText) dialog.findViewById(R.id.tall2);
            EditText quan = (EditText) dialog.findViewById(R.id.quan);
            tall1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Double tall1_val;
                    if (tall1.getText().toString().trim().equals("")) {
                        tall1_val = 1.0;
                    } else {
                        tall1_val = Double.parseDouble(tall1.getText().toString().trim());
                    }
                    Double tall2_val;
                    if (tall2.getText().toString().trim().equals("")) {
                        tall2_val = 1.0;
                    } else {
                        tall2_val = Double.parseDouble(tall2.getText().toString().trim());
                    }
                    Double quan_val;
                    if (quan.getText().toString().trim().equals("")) {
                        quan_val = 1.0;
                    } else {
                        quan_val = Double.parseDouble(quan.getText().toString().trim());
                    }
                    quan_text.setText((quan_val*tall1_val * tall2_val) + "");

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            tall2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Double tall1_val;
                    if (tall1.getText().toString().trim().equals("")) {
                        tall1_val = 1.0;
                    } else {
                        tall1_val = Double.parseDouble(tall1.getText().toString().trim());
                    }
                    Double tall2_val;
                    if (tall2.getText().toString().trim().equals("")) {
                        tall2_val = 1.0;
                    } else {
                        tall2_val = Double.parseDouble(tall2.getText().toString().trim());
                    }
                    Double quan_val;
                    if (quan.getText().toString().trim().equals("")) {
                        quan_val = 1.0;
                    } else {
                        quan_val = Double.parseDouble(quan.getText().toString().trim());
                    }
                    quan_text.setText((quan_val*tall1_val * tall2_val) + "");

                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            quan.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Double tall1_val;
                    if (tall1.getText().toString().trim().equals("")) {
                        tall1_val = 1.0;
                    } else {
                        tall1_val = Double.parseDouble(tall1.getText().toString().trim());
                    }
                    Double tall2_val;
                    if (tall2.getText().toString().trim().equals("")) {
                        tall2_val = 1.0;
                    } else {
                        tall2_val = Double.parseDouble(tall2.getText().toString().trim());
                    }
                    Double quan_val;
                    if (quan.getText().toString().trim().equals("")) {
                        quan_val = 1.0;
                    } else {
                        quan_val = Double.parseDouble(quan.getText().toString().trim());
                    }
                    quan_text.setText((quan_val*tall1_val * tall2_val) + "");

                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            final product product = products.get(position);
            oneprice = (EditText) dialog.findViewById(R.id.price);
            oneprice.setText(buy.productList.get(position).getBuyprice() + "");

            delete.setOnClickListener(v -> {

                double totalrow = Round.round((buy.quantityList.get(position) * product.getBuyprice()),3);
                buy.total_value = buy.total_value - totalrow;
                buy.total.setText(Round.round(buy.total_value ,3)+ SheredPrefranseHelper.getmoney_type(con));

                dialog.dismiss();
                products.remove(products.get(position));
                buy.quantityList.remove(position);
                double total_all = 0.0;
                for (int i = 0; i < buy.quantityList.size(); i++) {
                    total_all += buy.quantityList.get(i) * buy.productList.get(i).getBuyprice();
                }
                buy.total_value = Round.round(total_all,3);
                buy.total.setText(total_all + SheredPrefranseHelper.getmoney_type(con));
                productAdapter.setproductAdapter(products, listView);
                listView.setAdapter(productAdapter);


            });


            Button plus = (Button) dialog.findViewById(R.id.plus);
            plus.setOnClickListener(v -> {
                double newquan = buy.quantityList.get(position) + 1;
                buy.quantityList.set(position, newquan);
                quan_text.setText(buy.quantityList.get(position) + "");
                buy.total_value += product.getBuyprice();
               // buy.total.setText(buy.total_value + " جنيه ");
                quantity.setText(buy.quantityList.get(position) + "");
                double newtotal = Round.round((buy.quantityList.get(position) * product.getBuyprice()),3);
                total.setText(Round.round(newtotal ,3)+ "");
                productAdapter.setproductAdapter(products, listView);
                listView.setAdapter(productAdapter);


            });
            Button min = (Button) dialog.findViewById(R.id.minus);
            min.setOnClickListener(v -> {
                if (buy.quantityList.get(position) == 0) {

                } else {
                    buy.quantityList.set(position, buy.quantityList.get(position) - 1);
                    quan_text.setText(buy.quantityList.get(position) + "");
                    buy.total_value -= product.getBuyprice();
                 //   buy.total.setText(buy.total_value + " جنيه ");
                    quantity.setText(buy.quantityList.get(position) + "");
                    total.setText(Round.round((buy.quantityList.get(position) * product.getBuyprice()),3) + "");
                    productAdapter.setproductAdapter(products, listView);
                    listView.setAdapter(productAdapter);
                }

            });
            Button update = (Button) dialog.findViewById(R.id.update);
            update.setOnClickListener(v -> {
                if (!oneprice.getText().toString().equals("")) {
                    buy.productList.get(position).setBuyprice(Round.round(Double.parseDouble(oneprice.getText().toString().trim()),3));
                    price.setText(buy.productList.get(position).getBuyprice() + "");

                } else {

                }
                quantity.setText(Double.parseDouble(quan_text.getText().toString().trim())+"");
                buy.quantityList.set(position,Double.parseDouble(quan_text.getText().toString().trim()));

                double newtotal =Round.round(( buy.quantityList.get(position) * product.getBuyprice()),3);
                total.setText(newtotal + "");
                double total_all = 0.0;
                for (int i = 0; i < buy.quantityList.size(); i++) {
                    total_all += Round.round((buy.quantityList.get(i) * buy.productList.get(i).getBuyprice()),3);
                }
                buy.total_value = Round.round(total_all,3);
                buy.total.setText(Round.round(total_all ,3)+ SheredPrefranseHelper.getmoney_type(con));
                productAdapter.setproductAdapter(buy.productList, listView);
                listView.setAdapter(productAdapter);
                dialog.dismiss();

            });

            dialog.show();


        });
        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(Color.parseColor("#eaddff"));
        } else {

        }

            product product = products.get(position);

            name.setText(product.getName() + "");

            quantity.setText(buy.quantityList.get(position) + "");
            price.setText(buy.productList.get(position).getBuyprice() + "");
            total.setText(Round.round((buy.quantityList.get(position) * buy.productList.get(position).getBuyprice() ),3)+ "");
            description.setText( product.getDescription() + "");
            if (product.getImage()!=null){
                productImage.setImageBitmap(BitmapFactory.decodeByteArray(product.getImage(),0,product.getImage().length));
            }



        return row;
    }


}



