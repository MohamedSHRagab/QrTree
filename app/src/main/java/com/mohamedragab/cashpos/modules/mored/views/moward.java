package com.mohamedragab.cashpos.modules.mored.views;

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
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

public class moward extends AppCompatActivity {
    SearchableSpinner mored;
    DataBaseHelper db;
    String client_name="";
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moward);
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
    public void openmenu(View view) {
        PopupMenu popup = new PopupMenu(moward.this, view);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.omlamenu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(item -> {
            go_dialog();
            return true;
        });

        popup.show();

    }
    public void go_newmored(View view) {
        startActivity(new Intent(this, newmored.class));
    }

    public void go_rasedmored(View view) {
        startActivity(new Intent(this, rasedmored.class));

    }

    public void go_moredmoney(View view) {
        startActivity(new Intent(this, moredmoney.class));

    }

    public void go_viewallmored(View view) {
        startActivity(new Intent(this, viewallmored.class));

    }

    public void go_home(View view) {
        onBackPressed();
    }

    public void go_dialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.deletecustomedialog);
        dialog.setCancelable(false);
        Button delete = (Button) dialog.findViewById(R.id.delete);
        delete.setOnClickListener(v -> {
            if (mored.getSelectedItemPosition() == 0) {
                Toast.makeText(getBaseContext(), "برجاء اختيار مورد !", Toast.LENGTH_SHORT).show();
            } else {

                try {
                     client_name = mored.getSelectedItem().toString();
                }catch (Exception e){
                    Toast.makeText(getBaseContext(), "لا يوجد موردين حاليين !", Toast.LENGTH_SHORT).show();
                }


                AlertDialog.Builder dialog2 = new AlertDialog.Builder(moward.this);
                dialog2.setCancelable(false);
                dialog2.setTitle("حذف مورد حالي !");
                dialog2.setMessage("في حاله حذف المورد سيتم حذف المبالغ الباقيه ليه  , هل تريد حذف المورد  !");
                dialog2.setPositiveButton("حذف", (dialog12, id) -> {
                    try {
                        db.deletemored(client_name);
                        dialog.dismiss();
                        Toast.makeText(getBaseContext(), "تم حذف المورد بنجاح .", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        dialog.dismiss();
                        Toast.makeText(getBaseContext(), "فشل حذف المورد !", Toast.LENGTH_SHORT).show();
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

        mored = (SearchableSpinner) dialog.findViewById(R.id.omla2);
            Cursor res2 = db.getallmored();
            String mored_names[] = new String[0];
            if (res2 != null && res2.getCount() > 0) {
                 mored_names = new String[res2.getCount() + 1];

                int index = 1;
                mored_names[0] = "اضغط لتحديد مورد";
                while (res2.moveToNext()) {
                    mored_names[index] = res2.getString(1);
                    index++;
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, mored_names);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mored.setAdapter(adapter);

            dialog.show();


    }

    public void go_delete(View view) {
        try {
            go_dialog();
        }catch (Exception e){
            Toast.makeText(getBaseContext(), "لا يوجد موردين حاليين !", Toast.LENGTH_SHORT).show();
        }

    }
}
