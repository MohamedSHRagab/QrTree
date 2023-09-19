package com.mohamedragab.cashpos.modules.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.home.MainActivity;
import com.mohamedragab.cashpos.modules.login.views.login;

public class splash extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(() -> go_intro(), SPLASH_DISPLAY_LENGTH);
    }

    public void go_intro() {
       if (SheredPrefranseHelper.getUserData(this) != null) {
//            if (SheredPrefranseHelper.getcurrentcashier(getBaseContext()) == null) {
//                startActivity(new Intent(this, employee_login.class));
//                finish();
//            } else {
                startActivity(new Intent(this, MainActivity.class));
                finish();
           // }
        } else if (SheredPrefranseHelper.getAdminData(this) != null) {
                startActivity(new Intent(this, MainActivity.class));
                finish();

        } else {
            Intent mainIntent = new Intent(this, login.class);
            startActivity(mainIntent);
            finish();
        }

    }

    private void readFile() {



    }


}
