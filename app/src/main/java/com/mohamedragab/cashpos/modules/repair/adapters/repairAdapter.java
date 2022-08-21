package com.mohamedragab.cashpos.modules.repair.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.repair.models.Repair;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.utils.Round;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class repairAdapter extends ArrayAdapter {

    List<Repair> repairs;
    Context con;
    DataBaseHelper moneydb;
    Boolean statue;


    public repairAdapter(Context context, List<Repair> repairs) {
        super(context, R.layout.repair_item, R.id.name, repairs);
        this.repairs = repairs;
        con = context;
        moneydb = new DataBaseHelper(con);


    }

    public void setrepairAdapter(List<Repair> repairs) {
        this.repairs = repairs;

    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.repair_item, parent, false);

        final TextView num = (TextView) row.findViewById(R.id.number);
        TextView date = (TextView) row.findViewById(R.id.date);
        TextView value = (TextView) row.findViewById(R.id.value);
        TextView notes = (TextView) row.findViewById(R.id.notes);
        ImageView completedornot = (ImageView) row.findViewById(R.id.completedornot);

        LinearLayout linearLayout = (LinearLayout) row.findViewById(R.id.linearitem);
        linearLayout.setOnClickListener(view -> {
            if (repairs.get(position).getStatue().equals("completed")) {

            } else {
                TextView visitedate;
                final Dialog dialog = new Dialog(con);
                dialog.setContentView(R.layout.newrepairdialog);
                dialog.setCancelable(false);

                visitedate = (TextView) dialog.findViewById(R.id.date);
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                String formattedDate = df.format(c);
                visitedate.setText(formattedDate);

                RadioButton completed = (RadioButton) dialog.findViewById(R.id.completed);
                EditText money = (EditText) dialog.findViewById(R.id.money);
                EditText notes2 = (EditText) dialog.findViewById(R.id.notes);
                notes2.setText(repairs.get(position).getNotes() + "");
                money.setText(repairs.get(position).getMoney_collect()+"");
                DataBaseHelper db = new DataBaseHelper(con);
                Button ok = (Button) dialog.findViewById(R.id.save);
                ok.setOnClickListener(v -> {
                    Repair repair = new Repair();
                    repair.setId(repairs.get(position).getId());
                    repair.setClient_name(repair.getClient_name() + "");
                    repair.setCreated_at(formattedDate + "");
                    if (money.getText().toString().trim().equals("")) {
                        repair.setMoney_collect(0);
                    } else {
                        repair.setMoney_collect(Math.ceil(Double.parseDouble(money.getText().toString())));
                    }
                    repair.setNotes(notes2.getText().toString() + "");
                    if (completed.isChecked()) {
                        repair.setStatue("completed");
                    } else {
                        repair.setStatue("not_completed");
                    }
                    repair.setVisit_at(visitedate.getText() + "");
                    if (db.updaterepairbyName(repair.getId(), visitedate.getText().toString(), completed.isChecked(), Double.parseDouble(money.getText().toString()), notes2.getText().toString())) {
                        if (repair.getStatue().equals("completed")) {
                            com.mohamedragab.cashpos.modules.moneybox.models.money moneyobj = new com.mohamedragab.cashpos.modules.moneybox.models.money();
                            moneyobj.setDate(formattedDate);
                            moneyobj.setNotes("صيانه للعميل : " + repairs.get(position).getClient_name());
                            moneyobj.setValue(Round.round(repair.getMoney_collect(), 3));

                            final Cursor res3 = moneydb.getallTransactions();
                            double total1 = 0;
                            if (res3 != null && res3.getCount() > 0) {
                                while (res3.moveToNext()) {
                                    total1 = res3.getLong(3);

                                }
                            }
                            moneyobj.setTotalbefore(Round.round(total1, 3));
                            double totalAfter = Round.round((total1 + repair.getMoney_collect()), 3);

                            moneyobj.setTotalAfter(Round.round(totalAfter, 3));
                            if (moneydb.insert_date(moneyobj)) {
                                // Toast.makeText(con, "تم تعديل الصيانه بنجاح", Toast.LENGTH_SHORT).show();
                            }
                        }
                        // Toast.makeText(con, "تم تعديل الصيانه بنجاح", Toast.LENGTH_SHORT).show();
                        db.close();
                        moneydb.close();
                        ((Activity) con).recreate();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(con, "فشل تعديل الصيانه !", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                });
                TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(v -> {
                    dialog.dismiss();
                });
                dialog.show();
            }
        });
        if (position % 2 == 0) {
            linearLayout.setBackgroundColor(Color.parseColor("#eaddff"));
        } else {

        }

        Repair repair = repairs.get(position);
        num.setText((position + 1) + "");

        date.setText(repair.getVisit_at() + "");

        value.setText(Round.round(repair.getMoney_collect(), 3) + "");
        notes.setText(repair.getNotes() + "");
        if (repair.getStatue().equals("completed")) {
            completedornot.setImageResource(R.drawable.completedicon);
        } else {
            completedornot.setImageResource(R.drawable.notcompleted);
        }


        return row;
    }


}



