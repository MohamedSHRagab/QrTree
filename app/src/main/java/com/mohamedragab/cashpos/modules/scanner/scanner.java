package com.mohamedragab.cashpos.modules.scanner;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.buy.views.buy;
import com.mohamedragab.cashpos.modules.exchange.views.exchange;
import com.mohamedragab.cashpos.modules.sales.views.sales;
import com.mohamedragab.cashpos.modules.store.views.addproduct;
import com.mohamedragab.cashpos.modules.store.views.viewproducts;
import com.mohamedragab.cashpos.modules.store.wedgit.productAdapter;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class scanner extends AppCompatActivity {
    private ZXingScannerView scannerView;
    String value;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("activity");
        }

        scannerView = new ZXingScannerView(this);
        scannerView.setResultHandler(new ZXingScannerResultHandler());

        setContentView(scannerView);
        scannerView.startCamera();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.scanner_bar, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.flash:
                scannerView.setFlash(!scannerView.getFlash());
            return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class ZXingScannerResultHandler implements ZXingScannerView.ResultHandler {

        public ZXingScannerResultHandler() {

        }

        @Override
        public void handleResult(Result result) {

            String resultcode = result.getText();


            if (value.equals("view")) {
                viewproducts.search.setText(resultcode);

            }
            if (value.equals("sales")) {
                sales.autoCompleteTextView.setText(resultcode);

            }
            if (value.equals("add")) {
                addproduct.code.setText(resultcode);

            }
            if (value.equals("buy")) {
                buy.autoCompleteTextView.setText(resultcode);

            }
            if (value.equals("editpro")) {
                productAdapter.code_et.setText(resultcode);

            } if (value.equals("exchange")) {
                exchange.autoCompleteTextView.setText(resultcode);

            }
            finish();


            scannerView.stopCamera();
            // activity.recreate();
            // Toast.makeText(sales.this,"data : "+resultcode,Toast.LENGTH_LONG).show();
            //  code = (EditText) findViewById(R.id.code);


        }
    }
}
