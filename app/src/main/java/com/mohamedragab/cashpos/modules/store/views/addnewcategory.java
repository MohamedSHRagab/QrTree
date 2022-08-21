package com.mohamedragab.cashpos.modules.store.views;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mohamedragab.cashpos.modules.store.wedgit.categoryAdapter;
import com.mohamedragab.cashpos.R;
import com.mohamedragab.cashpos.modules.sales.dbservice.DataBaseHelper;
import com.mohamedragab.cashpos.modules.store.models.category;

import java.util.ArrayList;
import java.util.List;

public class addnewcategory extends AppCompatActivity {
    ListView category;
    EditText category_name;
    DataBaseHelper db;
    List<category> categoryList;
    com.mohamedragab.cashpos.modules.store.wedgit.categoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewcategory);
        category = (ListView) findViewById(R.id.list_categories);
        category_name = (EditText) findViewById(R.id.category);
        getcategories();
    }

    private void getcategories() {
        db = new DataBaseHelper(getBaseContext());

        categoryList = new ArrayList<>();

        categoryAdapter = new categoryAdapter(addnewcategory.this, categoryList);


        Cursor res = db.getallcategories();


        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                category pro = new category();
                pro.setCategory(res.getString(0));

                categoryList.add(pro);

            }
            categoryAdapter.setCategoriesAdapter(categoryList);
            category.setAdapter(categoryAdapter);

        } else {
            Toast.makeText(getBaseContext(), "لا يوجد تصنيفات حاليا برجاء اضافه تصنيفات !", Toast.LENGTH_SHORT).show();
            categoryList.clear();
            categoryAdapter.setCategoriesAdapter(categoryList);
            category.setAdapter(categoryAdapter);

        }

    }

    public void go_home(View view) {
        onBackPressed();
    }

    public void sava(View view) {
        com.mohamedragab.cashpos.modules.store.models.category category = new category();
        if (category_name.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), "رجاء ادخال اسم التصنيف !", Toast.LENGTH_SHORT).show();

        } else {
            category.setCategory(category_name.getText().toString().trim());

            Boolean result = db.insert_date(category);

            if (result) {
                category_name.setText("");
                Toast.makeText(getBaseContext(), "تم تسجيل التصنيف بنجاح", Toast.LENGTH_SHORT).show();
                getcategories();
            } else {
                Toast.makeText(getBaseContext(), "فشل تسجيل التصنيف تحقق من ادخال بيانات صحيحه", Toast.LENGTH_SHORT).show();

            }

        }
    }
}
