package com.mohamedragab.cashpos.modules.dashboard.wedgits;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.login.models.User;
import com.mohamedragab.cashpos.modules.prizes.views.addprize;

import java.io.File;
import java.util.List;


public class shopAdapter extends ArrayAdapter {

    List<User> shops;
    Context con;
    TextView name2, address2, phone2, password2, lastseen2, backupdate, database;
    Button cancel, blockstatue, delete;
    ImageView imagestatue, minus;
    DatabaseReference reference;
    LinearLayout linearLayout;

    public shopAdapter(Context context, List<User> shops) {
        super(context, R.layout.shop_item, R.id.name, shops);
        this.shops = shops;
        con = context;
    }

    public void setshopAdapter(List<User> shopslist) {
        this.shops = shopslist;

    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.shop_item, parent, false);

        final TextView name = (TextView) row.findViewById(R.id.name);
        final TextView lastseen = (TextView) row.findViewById(R.id.lastseen);
        ImageView imageprize = (ImageView) row.findViewById(R.id.prizeimage);

        TextView address = (TextView) row.findViewById(R.id.address);
        final ImageView statue = (ImageView) row.findViewById(R.id.imagestatue);
        final ImageView used = (ImageView) row.findViewById(R.id.used);
        if (shops.get(position).getUsed().equals("true")) {
            used.setImageResource(R.drawable.check_box);
        } else if (shops.get(position).getUsed().equals("false")) {
            used.setImageResource(R.drawable.check_box2);
        }

        Button details = (Button) row.findViewById(R.id.details);

        details.setOnClickListener(view -> {
            final Dialog dialog = new Dialog(con);
            dialog.setContentView(R.layout.shopdetails);
            dialog.setCancelable(false);


            name2 = (TextView) dialog.findViewById(R.id.name);
            address2 = (TextView) dialog.findViewById(R.id.address);
            phone2 = (TextView) dialog.findViewById(R.id.phone);
            password2 = (TextView) dialog.findViewById(R.id.password);
            lastseen2 = (TextView) dialog.findViewById(R.id.lastseen);
            backupdate = (TextView) dialog.findViewById(R.id.backupdate);
            database = (TextView) dialog.findViewById(R.id.database);
            imagestatue = (ImageView) dialog.findViewById(R.id.imagestatue);
            minus = (ImageView) dialog.findViewById(R.id.minus);
            blockstatue = (Button) dialog.findViewById(R.id.changestatue);
            cancel = (Button) dialog.findViewById(R.id.cancel);
            delete = (Button) dialog.findViewById(R.id.delete);

            linearLayout = (LinearLayout) dialog.findViewById(R.id.linearlink);

            name2.setText(shops.get(position).getName() + "");
            address2.setText(shops.get(position).getAddress() + "");
            phone2.setText(shops.get(position).getPhone() + "");
            password2.setText(shops.get(position).getPassword() + "");
            lastseen2.setText(shops.get(position).getLastseen() + "");
            backupdate.setText(shops.get(position).getBackupdate() + "");
            database.setText("" + shops.get(position).getDatabaseupdate());
            database.setOnClickListener(v -> {
                if (shops.get(position).getDatabaseupdate()!=null){
                    downloadfile(shops.get(position).getDatabaseurl() + "");
                }
            });
            linearLayout.setOnClickListener(view1 -> {
                try {
                    con.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(shops.get(position).getDatalink())));
                } catch (Exception e) {
                    Toast.makeText(con, "لا يوجد تقرير مبيعات لهذا المحل !", Toast.LENGTH_SHORT).show();
                }
            });

            if (shops.get(position).getBlocked().equals("true")) {
                name.setTextColor(Color.RED);
                imagestatue.setImageResource(R.drawable.blockuser);
                blockstatue.setText("الغاء حظر المحل ");
            } else {
                imagestatue.setImageResource(R.drawable.normaluser);
                blockstatue.setText(" حظر المحل ");
            }
            cancel.setOnClickListener(view13 -> dialog.dismiss());
            delete.setOnClickListener(v -> {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(con);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("حذف المحل");
                alertDialog.setMessage("هل تريد حذف المحل ؟");
                alertDialog.setPositiveButton("حذف", (dialog12, id) -> {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(shops.get(position).getPhone()).removeValue()
                            .addOnCompleteListener(task -> {
                                Toast.makeText(con, "تم حذف المحل بنجاح !", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }).addOnCanceledListener(() -> {
                        Toast.makeText(con, "فشل حذف المحل تاكد من اتصالك بالانترنت !", Toast.LENGTH_SHORT).show();

                    });

                })
                        .setNegativeButton("الغاء ", (dialog1, which) -> {


                        });

                final AlertDialog alert = alertDialog.create();
                alert.show();

            });
            minus.setOnClickListener(v -> {
                FirebaseDatabase.getInstance().getReference().child("Users").child(shops.get(position).getPhone()).child("backupdate").setValue("--/--/----")
                        .addOnCompleteListener(task -> {
                            Toast.makeText(con, "تم طلب تقرير جديد من العميل تأكد من اتصال الانترنت عند العميل .", Toast.LENGTH_SHORT).show();
                            backupdate.setText("--/--/----");

                        }).addOnFailureListener(command -> {

                });
            });
            blockstatue.setOnClickListener(view12 -> {

                reference = FirebaseDatabase.getInstance().getReference("Users").child(shops.get(position).getPhone());

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (shops.get(position).getBlocked().equals("true")) {
                            reference.child("blocked").setValue("false");
                            reference.child("used").setValue("false");

                        } else {
                            reference.child("blocked").setValue("true");
                        }
                        dialog.dismiss();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(con, "العمليه فشلت حاول مره اخري !", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                dialog.dismiss();

            });
            dialog.show();


        });

        User user = shops.get(position);


        used.setOnClickListener(v -> {
            reference = FirebaseDatabase.getInstance().getReference("Users").child(shops.get(position).getPhone());
            if (user.getUsed().equals("true")) {
                reference.child("used").setValue("false").addOnSuccessListener(task -> {
                    used.setImageResource(R.drawable.check_box2);
                    Toast.makeText(con, "تم فتح الحساب .", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(con, "فشل فتح الحساب تأكد من اتصالك بالانترنت !", Toast.LENGTH_SHORT).show();
                });
            } else if (user.getUsed().equals("false")) {
                reference.child("used").setValue("true").addOnSuccessListener(task -> {
                    used.setImageResource(R.drawable.check_box);
                    Toast.makeText(con, "تم غلق الحساب .", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(con, "فشل غلق الحساب تأكد من اتصالك بالانترنت !", Toast.LENGTH_SHORT).show();
                });
            }


        });


        name.setText(user.getName() + "");
        address.setText(user.getAddress() + "");
        if (user.getBlocked().equals("true")) {
            statue.setImageResource(R.drawable.blockuser);
            name.setTextColor(Color.RED);

        } else {
            statue.setImageResource(R.drawable.shop2);
        }
        imageprize.setOnClickListener(v -> {
            Intent myIntent = new Intent(con, addprize.class);
            myIntent.putExtra("shopphone", user.getPhone());
            myIntent.putExtra("shopname", user.getName());
            con.startActivity(myIntent);
        });
        lastseen.setText(user.getLastseen() + "");


        return row;
    }

    Boolean download_statue = false;

    private Boolean downloadfile(String url) {

        ProgressDialog progressdialog = new ProgressDialog(con);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(url);
        final File rootPath = new File(Environment.getExternalStorageDirectory(), "cashpos/database");

        if (!rootPath.exists()) {
            rootPath.mkdirs();
        }

        final File localFile = new File(rootPath, "Database.db");

        storageRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {


            progressdialog.dismiss();
            Toast.makeText(con, "تم تحميل قاعده البيانات المحل .", Toast.LENGTH_SHORT).show();

            download_statue = true;
        }).addOnFailureListener(exception -> {
            progressdialog.dismiss();

            Toast.makeText(con, "حدثت مشكله في تحميل قاعده البيانات حاول تاكد من اتصالك بالانترنت وحاول مره اخري !", Toast.LENGTH_LONG).show();
        });
        return download_statue;
    }

}



