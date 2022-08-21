package com.mohamedragab.cashpos.modules.store.wedgit;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.store.models.product;
import com.mohamedragab.cashpos.modules.store.views.addproduct;
import com.mohamedragab.cashpos.utils.Round;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class productAdapter extends ArrayAdapter {

    List<product> products;
    Context con;
    DataBaseHelper productDataBaseHelper;
    public static EditText code_et;
    EditText name_et;
    EditText desc_et;
    EditText quan_et;
    EditText puy_et;
    EditText sell_et;
    TextView expire;
    ImageView scan, productImage;

    public productAdapter(Context context, List<product> product) {
        super(context, R.layout.product_view_item, R.id.name, product);
        this.products = product;
        con = context;
        productDataBaseHelper = new DataBaseHelper(con);

    }

    public void setproductAdapter(List<product> productList) {
        this.products = productList;

       /* double total = 0;
        for (int i = 0; i < productList.size(); i++) {
            Cursor res2 = productDataBaseHelper.getproducts(productList.get(i).getName());
            double total2 = 0;
            if (res2 != null && res2.getCount() > 0) {
                while (res2.moveToNext()) {
                    total2 += res2.getDouble(5) * res2.getInt(6);
                }
            }
            total += total2;
        }
        viewproducts.total_money.setText("اجمالي رأس المال : " + Round.round(total, 3) + SheredPrefranseHelper.getmoney_type(con));
*/
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.product_view_item, parent, false);

        TextView name = (TextView) row.findViewById(R.id.name);
        TextView quantity = (TextView) row.findViewById(R.id.quantity);
        TextView price = (TextView) row.findViewById(R.id.price);
        TextView description = (TextView) row.findViewById(R.id.description);
        CircleImageView productImage = (CircleImageView) row.findViewById(R.id.image);
        LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.linearitem);
        linearLayout.setOnClickListener(view -> {
            Intent yourIntent = new Intent(con, addproduct.class);
            Bundle b = new Bundle();
            b.putSerializable("product", products.get(position));
            yourIntent.putExtras(b); //pass bundle to your intent
            con.startActivity(yourIntent);
            ((Activity)con).finish();




        /*    final Dialog dialog = new Dialog(con);
            dialog.setContentView(R.layout.editproductinstore);
            dialog.setCancelable(false);

            code_et = (EditText) dialog.findViewById(R.id.code);
            name_et = (EditText) dialog.findViewById(R.id.name);
            desc_et = (EditText) dialog.findViewById(R.id.description);
            quan_et = (EditText) dialog.findViewById(R.id.quantity);
            puy_et = (EditText) dialog.findViewById(R.id.buyprice);
            sell_et = (EditText) dialog.findViewById(R.id.sellprice);
            expire = (TextView) dialog.findViewById(R.id.expiredate);
            scan = (ImageView) dialog.findViewById(R.id.scan_image);
            scan.setOnClickListener(view12 -> {
                String value = "editpro";
                Intent i = new Intent(con, scanner.class);
                i.putExtra("activity", value);
                con.startActivity(i);
            });


            final product product = products.get(position);

            name_et.setText(product.getName() + "");
            quan_et.setText(product.getQuantity() + "");
            sell_et.setText(Round.round(product.getSellprice(), 3) + "");
            desc_et.setText(product.getDescription() + "");
            puy_et.setText(Round.round(product.getBuyprice(), 3) + "");
            expire.setText(product.getExpiredate() + "");
            code_et.setText(product.getCode_id() + "");


            Button save = (Button) dialog.findViewById(R.id.save);
            Button delete = (Button) dialog.findViewById(R.id.delete);
            delete.setOnClickListener(v -> {
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(con);
                alertdialog.setCancelable(false);
                alertdialog.setTitle("حذف المنتج ");
                alertdialog.setMessage("هل تريد تأكيد حذف المنتج  !");
                alertdialog.setPositiveButton("تأكيد الحذف", (dialog12, id) -> {
                    productDataBaseHelper.deleteProduct(product.getId() + "");
                   // Toast.makeText(con, "تم حذف المنتج بنجاح .", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                    con.startActivity(new Intent(con, viewproducts.class));
                    ((viewproducts) con).finish();
                })
                        .setNegativeButton("الغاء ", (dialog1, which) -> {

                        });

                final AlertDialog alert = alertdialog.create();
                alert.show();
            });

            TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
            cancel.setOnClickListener(view1 -> dialog.dismiss());

            save.setOnClickListener(v -> {
                product newproduct = new product();

                newproduct.setId(product.getId());
                newproduct.setExpiredate(expire.getText().toString().trim());
                newproduct.setDescription(desc_et.getText().toString().trim());
                newproduct.setCode_id(code_et.getText().toString().trim());
                newproduct.setBuyprice(Round.round(Double.parseDouble(puy_et.getText().toString()), 3));
                newproduct.setSellprice(Round.round(Double.parseDouble(sell_et.getText().toString()), 3));
                newproduct.setName(name_et.getText().toString().trim());
                newproduct.setQuantity(Double.parseDouble(quan_et.getText().toString()));
                newproduct.setItemid(product.getItemid());

                Boolean result = productDataBaseHelper.updateData(newproduct);
                if (result) {
                  //  Toast.makeText(con, "تم تعديل المنتج بنجاح !", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                    con.startActivity(new Intent(con, viewproducts.class));
                    ((viewproducts) con).finish();


                } else {
                    Toast.makeText(con, "حدث خطا في تعديل البيانات تاكد من ادخال بينات صحيحه !", Toast.LENGTH_SHORT).show();

                }


            });

            dialog.show();*/
            //  Toast.makeText(con, "" + products.get(position).toString(), Toast.LENGTH_SHORT).show();
        });
        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(Color.parseColor("#eaddff"));
        } else {

        }
        try {
            product product = products.get(position);

            name.setText(product.getName() + "");
            quantity.setText(product.getQuantity() + "");
            price.setText(Round.round(product.getSellprice(), 3) + "");
            description.setText(product.getDescription() + "");
            if (product.getImage() != null) {
                productImage.setImageBitmap(BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length));
            }else{
                productImage.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            Toast.makeText(con, "فشل ارجاع المنتجات !", Toast.LENGTH_SHORT).show();

        }


        return row;
    }
}



