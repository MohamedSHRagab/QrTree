package com.mohamedragab.cashpos.modules.developerprofile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.R;

public class developerprofile extends AppCompatActivity {

    TextView facebook, linkedin, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developerprofile);
        facebook = (TextView) findViewById(R.id.facebook);
        linkedin = (TextView) findViewById(R.id.linkedin);
        phone = (TextView) findViewById(R.id.phone);
    }

    public void goface(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebook.getText().toString()));
        startActivity(browserIntent);
    }

    public void golinkedin(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkedin.getText().toString()));
        startActivity(browserIntent);
    }

    public void gophone(View view) {
        Uri u = Uri.parse("tel:" + phone.getText().toString());
        Intent i = new Intent(Intent.ACTION_DIAL, u);
        startActivity(i);
    }

    public void go_company(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/SoftwareAsAServiceEgypt/"));
        startActivity(browserIntent);
    }
}
