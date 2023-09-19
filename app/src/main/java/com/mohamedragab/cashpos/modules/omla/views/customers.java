package com.mohamedragab.cashpos.modules.omla.views;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

public class customers extends AppCompatActivity {
    SearchableSpinner omla;
    DataBaseHelper db;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);

        db = new DataBaseHelper(getBaseContext());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
            }

            @Override
            public void onAdClosed() {
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdImpression() {
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdOpened() {
                // covers the screen.
            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();
        db.close();
        db = new DataBaseHelper(getBaseContext());

    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }

    public void go_addnewcustomer(View view) {
        startActivity(new Intent(this, newcustomer.class));
    }

    public void go_rased_omla(View view) {
        startActivity(new Intent(this, rasedomla.class));

    }

    public void go_view_all(View view) {
        startActivity(new Intent(this, viewallomla.class));

    }

    public void go_omlamoney(View view) {
        startActivity(new Intent(this, omlamoney.class));

    }

    public void go_home(View view) {
        onBackPressed();
    }

    public void openmenu(View view) {
        PopupMenu popup = new PopupMenu(customers.this, view);
        popup.getMenuInflater()
                .inflate(R.menu.omlamenu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            go_dialog();
            return true;
        });

        popup.show();

    }


    public void go_dialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.deletecustomedialog);
        dialog.setCancelable(false);
        Button delete = (Button) dialog.findViewById(R.id.delete);
        delete.setOnClickListener(v -> {
            if (omla.getSelectedItemPosition() == 0) {
                Toast.makeText(getBaseContext(), "برجاء اختيار عميل !", Toast.LENGTH_SHORT).show();
            } else {
                String client_name = omla.getSelectedItem().toString();


                AlertDialog.Builder dialog2 = new AlertDialog.Builder(customers.this);
                dialog2.setCancelable(false);
                dialog2.setTitle("حذف عميل حالي !");
                dialog2.setMessage("في حاله حذف العميل سيتم حذف المبالغ الباقيه عليه وقيمه الاقساط , هل تريد حذف العميل  !");
                dialog2.setPositiveButton("حذف", (dialog12, id) -> {
                    try {
                        db.deleteclient(client_name);
                        dialog.dismiss();
                        Toast.makeText(getBaseContext(), "تم حذف العميل بنجاح .", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        dialog.dismiss();
                        Toast.makeText(getBaseContext(), "فشل حذف العميل !", Toast.LENGTH_SHORT).show();
                    }

                })
                        .setNegativeButton("الغاء ", (dialog1, which) -> {
                            dialog.dismiss();
                        });

                final AlertDialog alert = dialog2.create();
                alert.show();


            }
        });
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> dialog.dismiss());

        omla = (SearchableSpinner) dialog.findViewById(R.id.omla2);
        final Cursor res2 = db.getallomla();
        String omla_names[] = new String[0];
        if (res2 != null && res2.getCount() > 0) {
             omla_names = new String[res2.getCount() + 1];

            int index = 1;
            omla_names[0] = "اضغط لتحديد عميل";
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

    public void go_delete(View view) {
        go_dialog();
    }
}
