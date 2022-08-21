package com.mohamedragab.cashpos.modules.employees.views;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.modules.employees.models.Cashier;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

public class cashiers extends AppCompatActivity {

    SearchableSpinner omla;
    DataBaseHelper db;
    String client_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashiers);
       /* cashier_imgage = (CircleImageView) findViewById(R.id.image);
        cashier_name = (TextView) findViewById(R.id.text);

        Cashier current = SheredPrefranseHelper.getcurrentcashier(cashiers.this);
        if (current != null) {
            cashier_name.setText(current.getName() + "");
            if (current.getImage()!=null){
                cashier_imgage.setImageBitmap(BitmapFactory.decodeByteArray(current.getImage(), 0, current.getImage().length));
            }
        }*/

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    public void go_home(View view) {
        onBackPressed();
        if (db==null){

        }else{
            db.close();
        }
    }

    public void add_cashier(View view) {
        startActivity(new Intent(this, newCashier.class));
    }

    public void all_cashier(View view) {
        startActivity(new Intent(this, viewallcashiers.class));
    }

    public void go_dialog() {
        db = new DataBaseHelper(this);

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.deletecustomedialog);

        omla=(SearchableSpinner)dialog.findViewById(R.id.omla2);
        Button delete = (Button) dialog.findViewById(R.id.delete);
        delete.setOnClickListener(v -> {
            if (omla.getSelectedItemPosition() == 0) {
                Toast.makeText(getBaseContext(), "برجاء اختيار كاشير !", Toast.LENGTH_SHORT).show();
            } else {

                if (omla.getSelectedItem()!=null){
                     client_name = omla.getSelectedItem().toString();
                    AlertDialog.Builder dialog2 = new AlertDialog.Builder(cashiers.this);

                    dialog2.setTitle("حذف كاشير حالي !");
                    dialog2.setMessage("في حاله حذف كاشير سيتم حذف البيانات المتعلقه بالكاشير , هل تريد حذف الكاشير  !");
                    dialog2.setPositiveButton("حذف", (dialog12, id) -> {
                        try {
                            db.deletecashier(client_name);
                            if (client_name.equals(SheredPrefranseHelper.getcurrentcashier(cashiers.this).getName())) {
                                SheredPrefranseHelper.addcurrentcashier(cashiers.this,new Cashier());
                                cashiers.this.recreate();
                            }
                            dialog.dismiss();
                            Toast.makeText(getBaseContext(), "تم حذف الكاشير بنجاح .", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            dialog.dismiss();
                            Toast.makeText(getBaseContext(), "فشل حذف الكاشير !", Toast.LENGTH_SHORT).show();
                        }

                    })
                            .setNegativeButton("الغاء ", (dialog1, which) -> {
                                dialog.dismiss();
                            });

                    final AlertDialog alert = dialog2.create();
                    alert.show();
                }
              else {
                    Toast.makeText(getBaseContext(), "لا يوجد كاشير !", Toast.LENGTH_SHORT).show();
                }


            }
        });
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> dialog.dismiss());

        final Cursor res2 = db.getallcashier();
        String omla_names[] = new String[res2.getCount() + 1];

        if (res2 != null && res2.getCount() > 0) {
            int index = 1;
            omla_names[0] = "اضغط لتحديد كاشير";
            while (res2.moveToNext()) {
                omla_names[index] = res2.getString(1);
                index++;
            }

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, omla_names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        omla.setAdapter(adapter);

        dialog.show();
    }


    public void delete_cashier(View view) {
        go_dialog();
    }
}
