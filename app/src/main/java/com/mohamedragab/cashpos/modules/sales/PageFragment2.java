package com.mohamedragab.cashpos.modules.sales;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.sales.views.sales;
import com.mohamedragab.cashpos.modules.sales.wedgit.allproductsAdapter;
import com.mohamedragab.cashpos.modules.store.models.product;
import com.mohamedragab.cashpos.R;

import java.util.ArrayList;
import java.util.List;

public class PageFragment2 extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    GridView listView;
    private int mPage;
    List<product> shopList;
    ProgressBar progressBar;

    public static PageFragment2 newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment2 fragment = new PageFragment2();

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
        View view = inflater.inflate(R.layout.fragment_page2, container, false);
        listView = (GridView) view.findViewById(R.id.listview);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        shopList = new ArrayList<>();

        DataBaseHelper db = new DataBaseHelper(container.getContext());

        Cursor res = db.getallproducts();

        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                product pro = new product();

                pro.setId(res.getInt(0));
                pro.setCode_id(res.getString(1));
                pro.setName(res.getString(2));
                pro.setSellprice(res.getDouble(4));
                pro.setQuantity(res.getDouble(6));
                pro.setDescription(res.getString(3));
                pro.setBuyprice(res.getDouble(5));
                pro.setExpiredate(res.getString(7));
                byte[] imgByte = res.getBlob(9);
                pro.setMeasure1(res.getString(10));
                pro.setMeasure2(res.getString(11));
                pro.setMeasure3(res.getString(12));
                pro.setSellprice2(res.getDouble(13));
                pro.setSellprice3(res.getDouble(14));
                pro.setCategory(res.getString(15));
                pro.setFactor2(res.getInt(16));
                pro.setFactor3(res.getInt(17));

                pro.setImage(imgByte);
                try {
                    if (pro.getCategory().equals(sales.adminList.get(mPage - 1).getCategory())) {
                        shopList.add(pro);
                    }
                } catch (Exception e) {

                }

            }
            allproductsAdapter shopAdapter = new allproductsAdapter(container.getContext(), shopList, null, "sales");
            shopAdapter.setshopAdapter(shopList);
            listView.setAdapter(shopAdapter);
            progressBar.setVisibility(View.GONE);
            db.close();
        }

        return view;
    }


}

