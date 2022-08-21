package com.mohamedragab.cashpos.modules.sales;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mohamedragab.cashpos.modules.store.models.category;
import com.mohamedragab.cashpos.R;

import java.util.List;

public class SampleFragmentPagerAdapter2 extends FragmentPagerAdapter {
    private String names[];


    List<category> adminList;
    private Context context;

    public SampleFragmentPagerAdapter2(FragmentManager fm, Context context, List<category> adminList) {
        super(fm);
        this.context = context;
        this.adminList = adminList;

        names = new String[adminList.size()];

        for (int i = 0; i < adminList.size(); i++) {
            names[i] = adminList.get(i).getCategory();
        }


    }

    @Override
    public int getCount() {
        return adminList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment2.newInstance(position + 1);
    }


    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(context).inflate(R.layout.category_info, null);

        TextView name = (TextView) v.findViewById(R.id.name);

        name.setText(names[position]);

        return v;
    }
}

