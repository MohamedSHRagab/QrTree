package com.mohamedragab.cashpos.modules.prizes.wedgits;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.prizes.models.prize;

import java.util.List;


public class prizeAdapter extends ArrayAdapter {

    List<prize> prizes;
    Context con;
    TextView text, createdat;
    DatabaseReference reference2;
    ImageView seen;

    public prizeAdapter(Context context, List<prize> prizes) {
        super(context, R.layout.prize_item, R.id.name, prizes);
        this.prizes = prizes;
        con = context;
    }

    public void setprizeAdapter(List<prize> prizes) {
        this.prizes = prizes;
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.prize_item, parent, false);

        text = (TextView) row.findViewById(R.id.text);
        createdat = (TextView) row.findViewById(R.id.creationdate);
         seen = (ImageView) row.findViewById(R.id.seen);



        prize prize = this.prizes.get(position);

        seen.setOnClickListener(v -> {
            if (SheredPrefranseHelper.getUserData(con) != null) {
                reference2 = FirebaseDatabase.getInstance().getReference("prize").child(SheredPrefranseHelper.getUserData(con).getPhone()).child(prize.getCreatedat());
            } else {
                reference2 = FirebaseDatabase.getInstance().getReference("prize").child(SheredPrefranseHelper.getAdminData(con).getPhone()).child(prize.getCreatedat());
            }
            reference2.child("used").setValue("true").addOnCompleteListener(task -> {
                text.setText( prize.getText()+"");
            }).addOnFailureListener(e -> {
                Toast.makeText(con, "فشل قراءه الرساله تأكد من اتصالك بالانترنت !", Toast.LENGTH_SHORT).show();
            });
        });


        if (prize.getUsed().equals("true")){
            text.setText( prize.getText());
        }
        if (prize.getUsed().equals("false")) {
            text.setText(  "***********");
        }
        createdat.setText(prize.getCreatedat() + "");


        return row;
    }
}



