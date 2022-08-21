package com.mohamedragab.cashpos.modules.sales.wedgit;

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
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.mohamedragab.cashpos.modules.store.models.product;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.sales.views.sales;
import com.mohamedragab.cashpos.utils.Round;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.mohamedragab.cashpos.modules.sales.views.sales.quantityList;


public class productSelectedAdapter extends ArrayAdapter {

    List<product> products;
    Context con;
    ListView listView;
    productSelectedAdapter productAdapter;
    EditText quan_text;
    EditText oneprice, twoprice, threeprice;
    RadioButton one, two, three;

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
        final TextView description = (TextView) row.findViewById(R.id.description);
        final CircleImageView productImage = (CircleImageView) row.findViewById(R.id.image);

        final TextView total = (TextView) row.findViewById(R.id.total);
        productAdapter = new productSelectedAdapter(con, products);


        final LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.linear);
        linearLayout.setOnClickListener(view -> {


            final Dialog dialog = new Dialog(con);
            dialog.setContentView(R.layout.edititeminsale2);
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
            quan_text.setText(quantityList.get(position) + "");
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
            twoprice = (EditText) dialog.findViewById(R.id.price2);
            threeprice = (EditText) dialog.findViewById(R.id.price3);
            one = (RadioButton) dialog.findViewById(R.id.one);
            two = (RadioButton) dialog.findViewById(R.id.two);
            three = (RadioButton) dialog.findViewById(R.id.three);
            if (sales.pricesList.get(position).equals("one")) {
                one.setChecked(true);
            } else if (sales.pricesList.get(position).equals("two")) {
                two.setChecked(true);
            } else {
                three.setChecked(true);
            }
            oneprice.setText(sales.productList.get(position).getSellprice() + "");
            twoprice.setText(sales.productList.get(position).getSellprice2() + "");
            threeprice.setText(sales.productList.get(position).getSellprice3() + "");

            delete.setOnClickListener(v -> {

                double totalrow;
                if (sales.pricesList.get(position).equals("one")) {
                    totalrow = Round.round(quantityList.get(position) * product.getSellprice(), 3);
                } else if (sales.pricesList.get(position).equals("two")) {
                    totalrow = Round.round(quantityList.get(position) * product.getSellprice2(), 3);
                } else {
                    totalrow = Round.round(quantityList.get(position) * product.getSellprice3(), 3);
                }
                sales.total_value = Round.round(sales.total_value - totalrow, 3);

                dialog.dismiss();
                products.remove(products.get(position));
                quantityList.remove(position);
                double total_all = 0.0;
                for (int i = 0; i < quantityList.size(); i++) {
                    if (sales.pricesList.get(i).equals("one")) {
                        total_all += Round.round(quantityList.get(i) * sales.productList.get(i).getSellprice(), 3);
                    } else if (sales.pricesList.get(i).equals("two")) {
                        total_all += Round.round(quantityList.get(i) * sales.productList.get(i).getSellprice2(), 3);
                    } else {
                        total_all += Round.round(quantityList.get(i) * sales.productList.get(i).getSellprice3(), 3);
                    }
                }
                sales.total_value = Round.round(total_all, 3);
                sales.discount.setText(0 + "");
                sales.total.setText(total_all + SheredPrefranseHelper.getmoney_type(con));
                productAdapter.setproductAdapter(products, listView);
                listView.setAdapter(productAdapter);

            });

            Button update = (Button) dialog.findViewById(R.id.update);
            update.setOnClickListener(v -> {
                if(sales.productList.get(position).getQuantity() < Double.parseDouble(quan_text.getText().toString().trim()) ){
                    AlertDialog.Builder builder = new AlertDialog.Builder(con);
                    builder.setTitle("تحذير");
                    builder.setMessage("الكميه غير متاحه في المخزن !  متبقي  "+sales.productList.get(position).getQuantity()+"  هل تريد الاستمرار ؟  ");
                    builder.setPositiveButton("نعم", (dialog2, id) -> {
                        if (one.isChecked()) {
                            sales.pricesList.set(position, "one");
                            if (!oneprice.getText().toString().equals("")) {
                                sales.productList.get(position).setSellprice(Round.round(Double.parseDouble(oneprice.getText().toString().trim()), 3));
                                price.setText(Round.round(sales.productList.get(position).getSellprice(), 3) + "");

                            } else {

                            }
                            quantity.setText(Double.parseDouble(quan_text.getText().toString().trim()) + "");
                            quantityList.set(position, Double.parseDouble(quan_text.getText().toString().trim()));
                            double newtotal = Round.round(quantityList.get(position) * sales.productList.get(position).getSellprice(), 3);
                            total.setText(newtotal + "");
                            double total_all = 0.0;
                            for (int i = 0; i < quantityList.size(); i++) {
                                if (sales.pricesList.get(i).equals("one")) {
                                    total_all += Round.round(quantityList.get(i) * sales.productList.get(i).getSellprice(), 3);
                                } else if (sales.pricesList.get(i).equals("two")) {
                                    total_all += Round.round(quantityList.get(i) * sales.productList.get(i).getSellprice2(), 3);
                                } else {
                                    total_all += Round.round(quantityList.get(i) * sales.productList.get(i).getSellprice3(), 3);
                                }
                            }
                            sales.total_value = Round.round(total_all, 3);
                            sales.discount.setText(0 + "");
                            sales.total.setText(Round.round(total_all, 3) + SheredPrefranseHelper.getmoney_type(con));
                            productAdapter.setproductAdapter(sales.productList, listView);
                            listView.setAdapter(productAdapter);
                            dialog.dismiss();
                        }
                        else if (two.isChecked()) {
                            sales.pricesList.set(position, "two");
                            if (!twoprice.getText().toString().equals("")) {
                                sales.productList.get(position).setSellprice2(Round.round(Double.parseDouble(twoprice.getText().toString().trim()), 3));
                                price.setText(Round.round(sales.productList.get(position).getSellprice2(), 3) + "");

                            } else {

                            }
                            quantity.setText(Double.parseDouble(quan_text.getText().toString().trim()) + "");
                            quantityList.set(position, Double.parseDouble(quan_text.getText().toString().trim()));
                            double newtotal = Round.round(quantityList.get(position) * sales.productList.get(position).getSellprice2(), 3);
                            total.setText(newtotal + "");
                            double total_all = 0.0;
                            for (int i = 0; i < quantityList.size(); i++) {
                                if (sales.pricesList.get(i).equals("one")) {
                                    total_all += Round.round(quantityList.get(i) * sales.productList.get(i).getSellprice(), 3);
                                } else if (sales.pricesList.get(i).equals("two")) {
                                    total_all += Round.round(quantityList.get(i) * sales.productList.get(i).getSellprice2(), 3);
                                } else {
                                    total_all += Round.round(quantityList.get(i) * sales.productList.get(i).getSellprice3(), 3);
                                }
                            }
                            sales.total_value = Round.round(total_all, 3);
                            sales.discount.setText(0 + "");
                            sales.total.setText(Round.round(total_all, 3) + SheredPrefranseHelper.getmoney_type(con));
                            productAdapter.setproductAdapter(sales.productList, listView);
                            listView.setAdapter(productAdapter);
                            dialog.dismiss();
                        }
                        else {
                            sales.pricesList.set(position, "three");
                            if (!threeprice.getText().toString().equals("")) {
                                sales.productList.get(position).setSellprice3(Round.round(Double.parseDouble(threeprice.getText().toString().trim()), 3));
                                price.setText(Round.round(sales.productList.get(position).getSellprice3(), 3) + "");

                            } else {

                            }
                            quantity.setText(Double.parseDouble(quan_text.getText().toString().trim()) + "");
                            quantityList.set(position, Double.parseDouble(quan_text.getText().toString().trim()));
                            double newtotal = Round.round(quantityList.get(position) * sales.productList.get(position).getSellprice3(), 3);
                            total.setText(newtotal + "");
                            double total_all = 0.0;
                            for (int i = 0; i < quantityList.size(); i++) {
                                if (sales.pricesList.get(i).equals("one")) {
                                    total_all += Round.round(quantityList.get(i) * sales.productList.get(i).getSellprice(), 3);
                                } else if (sales.pricesList.get(i).equals("two")) {
                                    total_all += Round.round(quantityList.get(i) * sales.productList.get(i).getSellprice2(), 3);
                                } else {
                                    total_all += Round.round(quantityList.get(i) * sales.productList.get(i).getSellprice3(), 3);
                                }
                            }
                            sales.total_value = Round.round(total_all, 3);
                            sales.discount.setText(0 + "");
                            sales.total.setText(Round.round(total_all, 3) + SheredPrefranseHelper.getmoney_type(con));
                            productAdapter.setproductAdapter(sales.productList, listView);
                            listView.setAdapter(productAdapter);
                            dialog.dismiss();
                        }

                    });
                    builder.setNegativeButton("لا", (dialog2, id) -> {
                    });
                    builder.show();

                }else{
                    if (one.isChecked()) {
                        sales.pricesList.set(position, "one");
                        if (!oneprice.getText().toString().equals("")) {
                            sales.productList.get(position).setSellprice(Round.round(Double.parseDouble(oneprice.getText().toString().trim()), 3));
                            price.setText(Round.round(sales.productList.get(position).getSellprice(), 3) + "");

                        } else {

                        }
                        quantity.setText(Double.parseDouble(quan_text.getText().toString().trim()) + "");
                        quantityList.set(position, Double.parseDouble(quan_text.getText().toString().trim()));
                        double newtotal = Round.round(quantityList.get(position) * sales.productList.get(position).getSellprice(), 3);
                        total.setText(newtotal + "");
                        double total_all = 0.0;
                        for (int i = 0; i < quantityList.size(); i++) {
                            if (sales.pricesList.get(i).equals("one")) {
                                total_all += Round.round(quantityList.get(i) * sales.productList.get(i).getSellprice(), 3);
                            } else if (sales.pricesList.get(i).equals("two")) {
                                total_all += Round.round(quantityList.get(i) * sales.productList.get(i).getSellprice2(), 3);
                            } else {
                                total_all += Round.round(quantityList.get(i) * sales.productList.get(i).getSellprice3(), 3);
                            }
                        }
                        sales.total_value = Round.round(total_all, 3);
                        sales.discount.setText(0 + "");
                        sales.total.setText(Round.round(total_all, 3) + SheredPrefranseHelper.getmoney_type(con));
                        productAdapter.setproductAdapter(sales.productList, listView);
                        listView.setAdapter(productAdapter);
                        dialog.dismiss();
                    }
                    else if (two.isChecked()) {
                        sales.pricesList.set(position, "two");
                        if (!twoprice.getText().toString().equals("")) {
                            sales.productList.get(position).setSellprice2(Round.round(Double.parseDouble(twoprice.getText().toString().trim()), 3));
                            price.setText(Round.round(sales.productList.get(position).getSellprice2(), 3) + "");

                        } else {

                        }
                        quantity.setText(Double.parseDouble(quan_text.getText().toString().trim()) + "");
                        quantityList.set(position, Double.parseDouble(quan_text.getText().toString().trim()));
                        double newtotal = Round.round(quantityList.get(position) * sales.productList.get(position).getSellprice2(), 3);
                        total.setText(newtotal + "");
                        double total_all = 0.0;
                        for (int i = 0; i < quantityList.size(); i++) {
                            if (sales.pricesList.get(i).equals("one")) {
                                total_all += Round.round(quantityList.get(i) * sales.productList.get(i).getSellprice(), 3);
                            } else if (sales.pricesList.get(i).equals("two")) {
                                total_all += Round.round(quantityList.get(i) * sales.productList.get(i).getSellprice2(), 3);
                            } else {
                                total_all += Round.round(quantityList.get(i) * sales.productList.get(i).getSellprice3(), 3);
                            }
                        }
                        sales.total_value = Round.round(total_all, 3);
                        sales.discount.setText(0 + "");
                        sales.total.setText(Round.round(total_all, 3) + SheredPrefranseHelper.getmoney_type(con));
                        productAdapter.setproductAdapter(sales.productList, listView);
                        listView.setAdapter(productAdapter);
                        dialog.dismiss();
                    }
                    else {
                        sales.pricesList.set(position, "three");
                        if (!threeprice.getText().toString().equals("")) {
                            sales.productList.get(position).setSellprice3(Round.round(Double.parseDouble(threeprice.getText().toString().trim()), 3));
                            price.setText(Round.round(sales.productList.get(position).getSellprice3(), 3) + "");

                        } else {

                        }
                        quantity.setText(Double.parseDouble(quan_text.getText().toString().trim()) + "");
                        quantityList.set(position, Double.parseDouble(quan_text.getText().toString().trim()));
                        double newtotal = Round.round(quantityList.get(position) * sales.productList.get(position).getSellprice3(), 3);
                        total.setText(newtotal + "");
                        double total_all = 0.0;
                        for (int i = 0; i < quantityList.size(); i++) {
                            if (sales.pricesList.get(i).equals("one")) {
                                total_all += Round.round(quantityList.get(i) * sales.productList.get(i).getSellprice(), 3);
                            } else if (sales.pricesList.get(i).equals("two")) {
                                total_all += Round.round(quantityList.get(i) * sales.productList.get(i).getSellprice2(), 3);
                            } else {
                                total_all += Round.round(quantityList.get(i) * sales.productList.get(i).getSellprice3(), 3);
                            }
                        }
                        sales.total_value = Round.round(total_all, 3);
                        sales.discount.setText(0 + "");
                        sales.total.setText(Round.round(total_all, 3) + SheredPrefranseHelper.getmoney_type(con));
                        productAdapter.setproductAdapter(sales.productList, listView);
                        listView.setAdapter(productAdapter);
                        dialog.dismiss();
                    }
                }


            });

            dialog.show();


        });
        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(Color.parseColor("#eaddff"));
        } else {

        }

        product product = products.get(position);
        name.setText(product.getName() + "");
        quantity.setText(quantityList.get(position) + "");
        if (sales.pricesList.get(position).equals("one")) {
            price.setText(Round.round(sales.productList.get(position).getSellprice(), 3) + "");
            total.setText(Round.round(quantityList.get(position) * sales.productList.get(position).getSellprice(), 3) + "");
        } else if (sales.pricesList.get(position).equals("two")) {
            price.setText(Round.round(sales.productList.get(position).getSellprice2(), 3) + "");
            total.setText(Round.round(quantityList.get(position) * sales.productList.get(position).getSellprice2(), 3) + "");
        } else {
            price.setText(Round.round(sales.productList.get(position).getSellprice3(), 3) + "");
            total.setText(Round.round(quantityList.get(position) * sales.productList.get(position).getSellprice3(), 3) + "");
        }
        description.setText(product.getDescription() + "");
        if (product.getImage() != null) {
            productImage.setImageBitmap(BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length));
        }

        return row;
    }


}



