package com.mohamedragab.cashpos.modules.adminpanel.views;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.adminpanel.SampleFragmentPagerAdapter;
import com.mohamedragab.cashpos.modules.adminpanel.models.adminmodel;
import com.mohamedragab.cashpos.modules.adminprofile.adminprofile;
import com.mohamedragab.cashpos.modules.login.models.User;
import com.mohamedragab.cashpos.modules.login.views.addnewshop;

import java.util.ArrayList;
import java.util.List;

public class adminpanel extends AppCompatActivity {
    public static List<adminmodel> adminList;
    DatabaseReference reference, usersReference;
    ProgressBar progressBar;
    TextView shops_num, admins_num;
    List<User> shopList;
    public static ViewPager viewPager;
    LinearLayout linear3;
    Boolean statisticsvisible = false;
    public static EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpanel);

        shops_num = (TextView) findViewById(R.id.shops_num);
        admins_num = (TextView) findViewById(R.id.admin_num);
        name = (EditText) findViewById(R.id.name);

        adminList = new ArrayList<>();
        shopList = new ArrayList<>();

        progressBar = (ProgressBar) findViewById(R.id.progress);

        linear3 = (LinearLayout) findViewById(R.id.linear3);

        usersReference = FirebaseDatabase.getInstance().getReference("Users");
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shopList.clear();

                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    User user1 = noteDataSnapshot.getValue(User.class);
                    shopList.add(user1);
                }
                shops_num.setText(shopList.size() - 1 + "");
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), "حدث خطأ في ارجاع البيانات حاول مره اخري !", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        });

        reference = FirebaseDatabase.getInstance().getReference("admins");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adminList.clear();
                adminmodel adminsaved = SheredPrefranseHelper.getAdminData(adminpanel.this);

                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    adminmodel admin = noteDataSnapshot.getValue(adminmodel.class);

                    if (adminsaved.getType().equals("admin")) {
                        adminList.add(admin);
                        admins_num.setText("" + adminList.size());
                    } else if (admin.getSupervisor().equals(SheredPrefranseHelper.getAdminData(adminpanel.this).getSupervisor())) {
                        adminList.add(admin);
                        admins_num.setText("" + adminList.size());
                    }
                }

                progressBar.setVisibility(View.GONE);
                viewPager = (ViewPager) findViewById(R.id.viewpager);
                SampleFragmentPagerAdapter pagerAdapter =
                        new SampleFragmentPagerAdapter(getSupportFragmentManager(), adminpanel.this, adminList);
                viewPager.setAdapter(pagerAdapter);
                TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
                tabLayout.setupWithViewPager(viewPager);
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    tab.setCustomView(pagerAdapter.getTabView(i));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), "حدث خطأ في ارجاع البيانات حاول مره اخري !", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        });

    }

    public void Showdetails(View view) {
        if (statisticsvisible) {
            linear3.setVisibility(View.GONE);
            statisticsvisible = false;
        } else {
            linear3.setVisibility(View.VISIBLE);
            statisticsvisible = true;
        }
    }

    public void showdialog(View view) {
        if (SheredPrefranseHelper.getAdminData(this).getType().equals("admin")) {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.choosedialog);

            Button shop = (Button) dialog.findViewById(R.id.shop);
            shop.setOnClickListener(v -> {
                startActivity(new Intent(this, addnewshop.class));
            });
            Button admin = (Button) dialog.findViewById(R.id.admin);
            admin.setOnClickListener(v -> {
                startActivity(new Intent(this, adminprofile.class));
                finish();
            });

            dialog.show();
        } else {
            startActivity(new Intent(this, addnewshop.class));

        }


    }

    public void back(View view) {
        onBackPressed();
    }
}
