package com.mohamedragab.cashpos.modules.prizes.views;

import android.media.MediaPlayer;
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
import com.mohamedragab.cashpos.modules.prizes.models.prize;
import com.mohamedragab.cashpos.modules.prizes.wedgits.prizeAdapter;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;

import java.util.ArrayList;
import java.util.List;

public class allprizes extends AppCompatActivity {
    List<prize> prizeList;
    ListView prizeListView;
    com.mohamedragab.cashpos.modules.prizes.wedgits.prizeAdapter prizeAdapter;
    DatabaseReference reference;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allprizes);

        progress = (ProgressBar) findViewById(R.id.progress);

        prizeList = new ArrayList<>();

        prizeListView = (ListView) findViewById(R.id.list_prizes);

        if (SheredPrefranseHelper.getUserData(this) != null) {
            reference = FirebaseDatabase.getInstance().getReference("prize").child(SheredPrefranseHelper.getUserData(this).getPhone());
        } else {
            reference = FirebaseDatabase.getInstance().getReference("prize").child(SheredPrefranseHelper.getAdminData(this).getPhone());
        }
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                prizeList.clear();

                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    prize prize = noteDataSnapshot.getValue(prize.class);
                    prizeList.add(prize);
                }
                MediaPlayer mp = MediaPlayer.create(allprizes.this, R.raw.notification);
                mp.start();
                prizeAdapter = new prizeAdapter(allprizes.this, prizeList);
                prizeAdapter.setprizeAdapter(prizeList);

                prizeListView.setAdapter(prizeAdapter);
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
}
