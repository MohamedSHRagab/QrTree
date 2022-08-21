package com.mohamedragab.cashpos.modules.adminpanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mohamedragab.cashpos.modules.adminpanel.views.adminpanel;
import com.mohamedragab.cashpos.modules.dashboard.wedgits.shopAdapter;
import com.mohamedragab.cashpos.modules.login.models.User;
import com.mohamedragab.cashpos.R;

import java.util.ArrayList;
import java.util.List;

public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    ListView listView;
    private int mPage;
    DatabaseReference  usersReference;
    List<User> shopList;
    ProgressBar progressBar;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPage = getArguments().getInt(ARG_PAGE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
         listView = (ListView) view.findViewById(R.id.listview);
         progressBar = (ProgressBar) view.findViewById(R.id.progress);
        shopList=new ArrayList<>();


        usersReference = FirebaseDatabase.getInstance().getReference("Users");
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shopList.clear();

                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    User user1 = noteDataSnapshot.getValue(User.class);

                   try {
                       if (user1.getAdminid().equals(adminpanel.adminList.get(mPage-1).getId())){

                           shopList.add(user1);
                       }
                   }catch (Exception e){
                     //  startActivity(new Intent(getContext(), MainActivity.class));
                      // getActivity().finish();
                     //  break;
                   }

                }
                shopAdapter shopAdapter = new shopAdapter(container.getContext(), shopList);
                shopAdapter.setshopAdapter(shopList);
                listView.setAdapter(shopAdapter);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(container.getContext(), "حدث خطأ في ارجاع البيانات حاول مره اخري !", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        });



        return view;
    }


}

