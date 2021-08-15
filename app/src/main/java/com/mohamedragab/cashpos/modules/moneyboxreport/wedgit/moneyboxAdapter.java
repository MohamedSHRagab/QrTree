package com.mohamedragab.cashpos.modules.moneyboxreport.wedgit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.moneybox.models.money;
import com.mohamedragab.cashpos.utils.Round;

import java.util.List;


public class moneyboxAdapter extends ArrayAdapter {

    List<money> moneyLists;
    Context con;

    public moneyboxAdapter(Context context, List<money> moneyList) {
        super(context, R.layout.moneybox_item, R.id.value, moneyList);
        this.moneyLists = moneyList;
        con = context;


    }

    public void setmoneyboxAdapter(List<money> moneylist) {
        this.moneyLists = moneylist;

    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.moneybox_item, parent, false);

        TextView process = (TextView) row.findViewById(R.id.process);
        TextView value = (TextView) row.findViewById(R.id.value);
        TextView total = (TextView) row.findViewById(R.id.totalafter);
        TextView date = (TextView) row.findViewById(R.id.date);
        TextView notes = (TextView) row.findViewById(R.id.notes);

        LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.linear);

        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(Color.parseColor("#eaddff"));
        } else {

        }
        try {
            money money = moneyLists.get(position);

            if (money.getTotalbefore() > money.getTotalAfter()) {
                process.setText(" خصم ");
               // linearLayout.setOnClickListener(view -> Toast.makeText(con, " خصم " + moneyLists.get(position).getValue(), Toast.LENGTH_SHORT).show());

            } else if (money.getTotalbefore() < money.getTotalAfter()) {
                process.setText(" اضافه ");
               // linearLayout.setOnClickListener(view -> Toast.makeText(con, " اضافه " + moneyLists.get(position).getValue(), Toast.LENGTH_SHORT).show());

            }
            value.setText(Round.round(money.getValue(),3) + SheredPrefranseHelper.getmoney_type(con));
            date.setText(money.getDate() + "");
            total.setText(Round.round(money.getTotalAfter(),3) + "");
            notes.setText(money.getNotes() + "");

        } catch (Exception e) {
            Toast.makeText(con, "فشل ارجاع البيانات !", Toast.LENGTH_SHORT).show();

        }

        return row;
    }
}



