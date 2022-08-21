package com.mohamedragab.cashpos.modules.adminpanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bumptech.glide.Glide;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.adminpanel.models.adminmodel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    private String images_url[];
    private String names[];
    private String address[];
    private String types[];
    private String lastseen[];
    private String phones[];

    List<adminmodel> adminList;
    private Context context;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context, List<adminmodel> adminList) {
        super(fm);
        this.context = context;
        this.adminList = adminList;

        images_url = new String[adminList.size()];
        names = new String[adminList.size()];
        address = new String[adminList.size()];
        types = new String[adminList.size()];
        lastseen = new String[adminList.size()];
        phones = new String[adminList.size()];

        for (int i = 0; i < adminList.size(); i++) {
            names[i] = adminList.get(i).getName();
            images_url[i] = adminList.get(i).getImageurl();
            address[i] = adminList.get(i).getAddress();
            types[i] = adminList.get(i).getType();
            lastseen[i] = adminList.get(i).getLastseen();
            phones[i] = adminList.get(i).getPhone();
        }


    }

    @Override
    public int getCount() {
        return adminList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position + 1);
    }


    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(context).inflate(R.layout.admin_info, null);
        CircleImageView image = (CircleImageView) v.findViewById(R.id.image);
        ImageView superimage = (ImageView) v.findViewById(R.id.superimage);

        TextView name = (TextView) v.findViewById(R.id.number);
        TextView address = (TextView) v.findViewById(R.id.address);
        TextView lastseen = (TextView) v.findViewById(R.id.lastseen);
        TextView phone = (TextView) v.findViewById(R.id.phone);

        name.setText(names[position]);
        lastseen.setText(this.lastseen[position]);
        phone.setText(this.phones[position]);

        address.setText(this.address[position]);
        if (types[position].equals("super")) {
            superimage.setVisibility(View.VISIBLE);
        } else {
            superimage.setVisibility(View.GONE);
        }
        try{
            Glide.with(context).load(images_url[position]).into(image);
        }
        catch (Exception e){

        }


        return v;
    }
}

