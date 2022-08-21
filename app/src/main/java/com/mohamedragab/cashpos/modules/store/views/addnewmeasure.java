package com.mohamedragab.cashpos.modules.store.views;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.store.models.Measure;
import com.mohamedragab.cashpos.modules.store.wedgit.measureAdapter;
import com.mohamedragab.cashpos.R;

import java.util.ArrayList;
import java.util.List;

public class addnewmeasure extends AppCompatActivity {
    ListView category;
    EditText category_name;
    DataBaseHelper db;
    List<Measure> measureList;
    measureAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewmeasure);
        category = (ListView) findViewById(R.id.list_categories);
        category_name = (EditText) findViewById(R.id.category);
        getcategories();
    }

    private void getcategories() {
        db = new DataBaseHelper(getBaseContext());

        measureList = new ArrayList<>();

        categoryAdapter = new measureAdapter(addnewmeasure.this, measureList);


        Cursor res = db.getallmeasurements();


        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                Measure pro = new Measure();
                pro.setMeasure(res.getString(0));

                measureList.add(pro);

            }
            categoryAdapter.setCategoriesAdapter(measureList);
            category.setAdapter(categoryAdapter);

        } else {
            Toast.makeText(getBaseContext(), "لا يوجد وحدات قياس حاليا برجاء اضافه وحدات قياس !", Toast.LENGTH_SHORT).show();
            measureList.clear();
            categoryAdapter.setCategoriesAdapter(measureList);
            category.setAdapter(categoryAdapter);

        }

    }

    public void go_home(View view) {
        onBackPressed();
        db.close();
    }

    public void sava(View view) {
       Measure measure = new Measure();
        if (category_name.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), "رجاء ادخال وحدة قياس !", Toast.LENGTH_SHORT).show();

        } else {
            measure.setMeasure(category_name.getText().toString());

            Boolean result = db.insert_date(measure);

            if (result) {
                category_name.setText("");
                Toast.makeText(getBaseContext(), "تم تسجيل وحدة القياس", Toast.LENGTH_SHORT).show();
                getcategories();
            } else {
                Toast.makeText(getBaseContext(), "فشل تسجيل وحدة القياس تحقق من ادخال بيانات صحيحة", Toast.LENGTH_SHORT).show();

            }

        }
    }
}
