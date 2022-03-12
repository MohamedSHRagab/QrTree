package com.mohamedragab.cashpos.modules.dashboard.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mohamedragab.cashpos.modules.dashboard.wedgits.shopAdapter;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.login.models.User;
import com.mohamedragab.cashpos.modules.login.views.addnewshop;

import java.util.ArrayList;
import java.util.List;

public class dashboardadmin extends AppCompatActivity {

    List<User> shopList;
    ListView shopsListView;
    com.mohamedragab.cashpos.modules.dashboard.wedgits.shopAdapter shopAdapter;
    DatabaseReference reference;
    ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboardadmin);
        progress = (ProgressBar) findViewById(R.id.progress);


        shopList = new ArrayList<>();
        shopAdapter = new shopAdapter(dashboardadmin.this, shopList);

        shopsListView = (ListView) findViewById(R.id.list_shops);

        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               shopList.clear();

                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    User user1 = noteDataSnapshot.getValue(User.class);
                    if (user1.getAdminid().equals(SheredPrefranseHelper.getAdminData(dashboardadmin.this).getId())){
                        shopList.add(user1);
                    }
                }
                shopAdapter.setshopAdapter(shopList);
                shopsListView.setAdapter(shopAdapter);
                progress.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), "حدث خطأ في ارجاع البيانات حاول مره اخري !", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);

            }
        });
    }

    public void go_home(View view) {
        onBackPressed();
    }

    public void go_adduser(View view) {
        startActivity(new Intent(this, addnewshop.class));
    }
}
