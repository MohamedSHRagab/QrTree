package com.mohamedragab.cashpos.modules.sales.dbservice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import com.mohamedragab.cashpos.modules.employees.models.Cashier;
import com.mohamedragab.cashpos.modules.employees.models.DelivTrans;
import com.mohamedragab.cashpos.modules.moredtransactions.models.moredtransaction;
import com.mohamedragab.cashpos.modules.omlatransactions.models.omlatransaction;
import com.mohamedragab.cashpos.modules.store.models.Measure;
import com.mohamedragab.cashpos.modules.store.models.category;
import com.mohamedragab.cashpos.modules.store.models.product;
import com.mohamedragab.cashpos.base.SheredPrefranseHelper;
import com.mohamedragab.cashpos.modules.buy.models.buyproduct;
import com.mohamedragab.cashpos.modules.moneybox.models.money;
import com.mohamedragab.cashpos.modules.mored.models.mored;
import com.mohamedragab.cashpos.modules.omla.models.omla;
import com.mohamedragab.cashpos.modules.repair.models.Repair;
import com.mohamedragab.cashpos.modules.sales.models.Kist;
import com.mohamedragab.cashpos.modules.sales.models.invoice;
import com.mohamedragab.cashpos.modules.sales.models.sellproduct;

public class DataBaseHelper extends SQLiteOpenHelper {
    Context context;
    public static final String DATABASE_NAME = "Database.db";
    //--------------------------------------------------tables names-------------------------------------------------------

    public static final String TABLE_sellInvoices = "invoice_table";
    public static final String TABLE_Kest = "kest_table";
    public static final String TABLE_sellproducts = "sellproducts_table";
    public static final String TABLE_Product = "Product_table";
    public static final String TABLE_money = "money_table";
    public static final String TABLE_repair = "repair_table";
    public static final String TABLE_omla = "omla_table";
    public static final String TABLE_omla_transactions = "omla_transactions_table";
    public static final String TABLE_mored = "mored_table";
    public static final String TABLE_mored_transactions = "mored_transactions_table";
    public static final String TABLE_buy_invoices = "buy_invoices_table";
    public static final String TABLE_buyproducts = "buyproducts_table";
    public static final String TABLE_employee = "employee_table";
    public static final String TABLE_DelivTrans = "DelivTrans_table";
    public static final String TABLE_category = "category_table";
    public static final String TABLE_MEASURE = "measure_table";

    //---------------------------------------------------columns names------------------------------------------------------
    public static final String TABLE_sellInvoices_COL_2 = "INVOICEID";
    public static final String TABLE_sellInvoices_COL_3 = "DATE";
    public static final String TABLE_sellInvoices_COL_4 = "CUSTOMERNAME";
    public static final String TABLE_sellInvoices_COL_5 = "TOTAL";
    public static final String TABLE_sellInvoices_COL_6 = "NOTPAID";
    public static final String TABLE_sellInvoices_COL_7 = "CASHIER";
    public static final String TABLE_sellInvoices_COL_8 = "DISCOUNT";
    public static final String TABLE_sellInvoices_COL_9 = "KIND";
    public static final String TABLE_sellInvoices_COL_10 = "DISCOUNTKIND";

    public static final String TABLE_Kest_COL_2 = "INVOICEID";
    public static final String TABLE_Kest_COL_3 = "DESCRIPTION";
    public static final String TABLE_Kest_COL_4 = "DAYSLIMIT";
    public static final String TABLE_Kest_COL_5 = "COLLECTDATE";
    public static final String TABLE_Kest_COL_6 = "KESTVALUE";
    public static final String TABLE_Kest_COL_7 = "STATUE";
    public static final String TABLE_Kest_COL_8 = "CLIENTNAME";
    public static final String TABLE_Kest_COL_9 = "PAYTYPE";
    public static final String TABLE_Kest_COL_10 = "DAMENNAME";
    public static final String TABLE_Kest_COL_11 = "DAMENPHONE";
    public static final String TABLE_Kest_COL_12 = "PAYDATE";
    public static final String TABLE_sellproducts_COL_1 = "ID";
    public static final String TABLE_sellproducts_COL_2 = "INVOICEID";
    public static final String TABLE_sellproducts_COL_3 = "QUANTITY";
    public static final String TABLE_sellproducts_COL_4 = "CODEID";
    public static final String TABLE_sellproducts_COL_5 = "NAME";
    public static final String TABLE_sellproducts_COL_6 = "DESCRIPTION";
    public static final String TABLE_sellproducts_COL_7 = "SELLPRICE";
    public static final String TABLE_sellproducts_COL_8 = "BUYPRICE";
    public static final String TABLE_sellproducts_COL_9 = "DATE";
    public static final String TABLE_sellproducts_COL_10 = "ITEMID";
    public static final String TABLE_Product_COL_1 = "ID";
    public static final String TABLE_Product_COL_2 = "CODEID";
    public static final String TABLE_Product_COL_3 = "NAME";
    public static final String TABLE_Product_COL_4 = "DESCRIPTION";
    public static final String TABLE_Product_COL_5 = "SELLPRICE";
    public static final String TABLE_Product_COL_6 = "BUYPRICE";
    public static final String TABLE_Product_COL_7 = "QUANTITY";
    public static final String TABLE_Product_COL_8 = "EXPIREDATE";
    public static final String TABLE_Product_COL_9 = "ITEMID";
    public static final String TABLE_Product_COL_10 = "IMAGE";
    public static final String TABLE_Product_COL_11 = "UNIT1";
    public static final String TABLE_Product_COL_12 = "UNIT2";
    public static final String TABLE_Product_COL_13 = "UNIT3";
    public static final String TABLE_Product_COL_14 = "SELLPRICE2";
    public static final String TABLE_Product_COL_15 = "SELLPRICE3";
    public static final String TABLE_Product_COL_16 = "CATEGORY";
    public static final String TABLE_Product_COL_17 = "FACTOR2";
    public static final String TABLE_Product_COL_18 = "FACTOR3";
    public static final String TABLE_money_COL_2 = "MONEYVALUE";
    public static final String TABLE_money_COL_3 = "TOTALBEFORE";
    public static final String TABLE_money_COL_4 = "TOTALAFTER";
    public static final String TABLE_money_COL_5 = "CURRENTDATE";
    public static final String TABLE_money_COL_6 = "NOTES";
    public static final String TABLE_repair_COL_1 = "ID";
    public static final String TABLE_repair_COL_2 = "CLIENTNAME";
    public static final String TABLE_repair_COL_3 = "CREATEDAT";
    public static final String TABLE_repair_COL_4 = "VISITAT";
    public static final String TABLE_repair_COL_5 = "MONEYCOLLECTED";
    public static final String TABLE_repair_COL_6 = "STATUE";
    public static final String TABLE_repair_COL_7 = "NOTES";
    public static final String TABLE_omla_COL_1 = "ID";
    public static final String TABLE_omla_COL_2 = "NAME";
    public static final String TABLE_omla_COL_3 = "ADDRESS";
    public static final String TABLE_omla_COL_4 = "PHONE";
    public static final String TABLE_omla_COL_5 = "NOTES";
    public static final String TABLE_omla_COL_6 = "HASMONEY";
    public static final String TABLE_omla_COL_7 = "PAYMONEY";
    public static final String TABLE_omla_COL_8 = "MAXNOTPAID";
    public static final String omla_transactions_COL_2 = "NAME";
    public static final String omla_transactions_COL_3 = "PROCESS";
    public static final String omla_transactions_COL_4 = "PAID";
    public static final String omla_transactions_COL_5 = "DATE";
    public static final String omla_transactions_COL_6 = "INVOICEID";
    public static final String omla_transactions_COL_7 = "NOTPAID";
    public static final String mored_COL_2 = "NAME";
    public static final String mored_COL_3 = "ADDRESS";
    public static final String mored_COL_4 = "PHONE";
    public static final String mored_COL_5 = "NOTES";
    public static final String mored_COL_6 = "HASMONEY";
    public static final String mored_COL_7 = "PAYMONEY";
    public static final String mored_transactions_COL_2 = "NAME";
    public static final String mored_transactions_COL_3 = "PROCESS";
    public static final String mored_transactions_COL_4 = "PAID";
    public static final String mored_transactions_COL_5 = "DATE";
    public static final String mored_transactions_COL_6 = "INVOICEID";
    public static final String mored_transactions_COL_7 = "NOTPAID";
    public static final String buy_invoices_COL_2 = "INVOICEID";
    public static final String buy_invoices_COL_3 = "DATE";
    public static final String buy_invoices_COL_4 = "MOREDNAME";
    public static final String buy_invoices_COL_5 = "TOTAL";
    public static final String buy_invoices_COL_6 = "NOTPAID";
    public static final String buy_invoices_COL_7 = "CASHIER";
    public static final String buyproducts_COL_1 = "ID";
    public static final String buyproducts_COL_2 = "INVOICEID";
    public static final String buyproducts_COL_3 = "QUANTITY";
    public static final String buyproducts_COL_4 = "CODEID";
    public static final String buyproducts_COL_5 = "NAME";
    public static final String buyproducts_COL_6 = "DESCRIPTION";
    public static final String buyproducts_COL_7 = "SELLPRICE";
    public static final String buyproducts_COL_8 = "BUYPRICE";
    public static final String buyproducts_COL_9 = "ITEMID";
    public static final String employee_COL_2 = "NAME";
    public static final String employee_COL_3 = "ADDRESS";
    public static final String employee_COL_4 = "PHONE";
    public static final String employee_COL_5 = "NOTES";
    public static final String employee_COL_6 = "IMAGE";
    public static final String employee_COL_7 = "JOB";
    public static final String DelivTrans_COL_1 = "ID";
    public static final String DelivTrans_COL_2 = "DELIV_NAME";
    public static final String DelivTrans_COL_3 = "MONEY";
    public static final String DelivTrans_COL_4 = "ADDRESS";
    public static final String DelivTrans_COL_5 = "INVOICE_ID";
    public static final String DelivTrans_COL_6 = "DATE";
    public static final String category_COL_2 = "CATEGORY";
    public static final String measure_COL_2 = "MEASURE";


    public DataBaseHelper(Context context) {
        super(context, Environment.getExternalStorageDirectory() + "/cashpos/database/" + DATABASE_NAME, null, 1);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_sellInvoices + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,INVOICEID TEXT,DATE TEXT,CUSTOMERNAME TEXT, TOTAL DOUBLE, NOTPAID DOUBLE,CASHIER TEXT,DISCOUNT DOUBLE,KIND TEXT,DISCOUNTKIND TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_Kest + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,INVOICEID TEXT,DESCRIPTION TEXT, DAYSLIMIT INTEGER, COLLECTDATE TEXT,KESTVALUE DOUBLE,STATUE TEXT,CLIENTNAME TEXT,PAYTYPE TEXT,DAMENNAME TEXT,DAMENPHONE TEXT,PAYDATE TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_sellproducts + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,INVOICEID TEXT,QUANTITY DOUBLE, CODEID TEXT, NAME TEXT,DESCRIPTION TEXT,SELLPRICE DOUBLE,BUYPRICE DOUBLE,DATE TEXT, ITEMID TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_Product + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,CODEID TEXT,NAME TEXT,DESCRIPTION TEXT, SELLPRICE DOUBLE, BUYPRICE DOUBLE,QUANTITY DOUBLE,EXPIREDATE TEXT,ITEMID INTEGER,IMAGE BLOB,UNIT1 TEXT,UNIT2 TEXT,UNIT3 TEXT,SELLPRICE2 DOUBLE,SELLPRICE3 DOUBLE,CATEGORY TEXT,FACTOR2 INTEGER,FACTOR3 INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_money + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,MONEYVALUE DOUBLE,TOTALBEFORE DOUBLE,TOTALAFTER DOUBLE, CURRENTDATE TEXT, NOTES TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_repair + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,CLIENTNAME TEXT,CREATEDAT TEXT, VISITAT TEXT, MONEYCOLLECTED DOUBLE,STATUE TEXT,NOTES TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_omla + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,ADDRESS TEXT, PHONE TEXT, NOTES TEXT,HASMONEY DOUBLE,PAYMONEY DOUBLE,MAXNOTPAID DOUBLE)");
        db.execSQL("CREATE TABLE " + TABLE_omla_transactions + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,PROCESS TEXT,PAID DOUBLE,DATE TEXT, INVOICEID TEXT, NOTPAID DOUBLE)");
        db.execSQL("CREATE TABLE " + TABLE_mored + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,ADDRESS TEXT, PHONE TEXT, NOTES TEXT,HASMONEY DOUBLE,PAYMONEY DOUBLE)");
        db.execSQL("CREATE TABLE " + TABLE_mored_transactions + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,PROCESS TEXT,PAID DOUBLE,DATE TEXT, INVOICEID TEXT, NOTPAID DOUBLE)");
        db.execSQL("CREATE TABLE " + TABLE_buy_invoices + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,INVOICEID TEXT,DATE TEXT,MOREDNAME TEXT, TOTAL DOUBLE, NOTPAID DOUBLE,CASHIER TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_buyproducts + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,INVOICEID TEXT,QUANTITY DOUBLE, CODEID TEXT, NAME TEXT,DESCRIPTION TEXT,SELLPRICE DOUBLE,BUYPRICE DOUBLE, ITEMID TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_employee + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,ADDRESS TEXT, PHONE TEXT, NOTES TEXT,IMAGE BLOB,JOB TEXT,USERNAME TEXT,PASSWORD TEXT,P1 TEXT ,P2 TEXT ,P3 TEXT ,P4 TEXT ,P5 TEXT ,P6 TEXT ,P7 TEXT ,P8 TEXT ,P9 TEXT ,P10 TEXT  ,P11 TEXT ,P12 TEXT ,P13 TEXT ,P14 TEXT ,P15 TEXT ,P16 TEXT )");
        db.execSQL("CREATE TABLE " + TABLE_DelivTrans + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,DELIV_NAME TEXT,MONEY TEXT, ADDRESS TEXT, INVOICE_ID TEXT,DATE TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_category + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,CATEGORY TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_MEASURE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,MEASURE TEXT)");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("Drop table if exists " + TABLE_sellInvoices);
        db.execSQL("Drop table if exists " + TABLE_Kest);
        db.execSQL("Drop table if exists " + TABLE_sellproducts);
        db.execSQL("Drop table if exists " + TABLE_Product);
        db.execSQL("Drop table if exists " + TABLE_money);
        db.execSQL("Drop table if exists " + TABLE_repair);
        db.execSQL("Drop table if exists " + TABLE_omla);
        db.execSQL("Drop table if exists " + TABLE_omla_transactions);
        db.execSQL("Drop table if exists " + TABLE_mored);
        db.execSQL("Drop table if exists " + TABLE_mored_transactions);
        db.execSQL("Drop table if exists " + TABLE_buy_invoices);
        db.execSQL("Drop table if exists " + TABLE_buyproducts);
        db.execSQL("Drop table if exists " + TABLE_employee);
        db.execSQL("Drop table if exists " + TABLE_DelivTrans);
        db.execSQL("Drop table if exists " + TABLE_category);
        db.execSQL("Drop table if exists " + TABLE_MEASURE);


    }

    public boolean insert_date(invoice invoice) {

        Cursor res = getallsellinvoices();
        if (SheredPrefranseHelper.getAdminData(context) == null && SheredPrefranseHelper.getUserData(context) == null) {
            if (res.getCount() <= 20) {
            } else {
                Toast.makeText(context, "النسخه غير مفعله قم بتفعيل النسخه عن طريق التواصل مع الادمن 01093957856", Toast.LENGTH_LONG).show();
                close();
                return false;
            }
        }
        String invoice_id = invoice.getInvoice_id();
        String date = invoice.getDate();
        String customer_name = invoice.getCustomer_name();
        double total = invoice.getTotal();
        double notpaid = invoice.getNotpaid();
        String cashier_name = invoice.getCashier();
        double discount = invoice.getDiscount();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_sellInvoices_COL_2, invoice_id);
        contentValues.put(TABLE_sellInvoices_COL_3, date);
        contentValues.put(TABLE_sellInvoices_COL_4, customer_name);
        contentValues.put(TABLE_sellInvoices_COL_5, total);
        contentValues.put(TABLE_sellInvoices_COL_6, notpaid);
        contentValues.put(TABLE_sellInvoices_COL_7, cashier_name);
        contentValues.put(TABLE_sellInvoices_COL_8, discount);
        contentValues.put(TABLE_sellInvoices_COL_9, invoice.getKind());
        contentValues.put(TABLE_sellInvoices_COL_10, invoice.getDiscount_kind());

        long result = db.insert(TABLE_sellInvoices, null, contentValues);
        db.close();
        if (result == -1) {
            return false;

        } else {
            return true;

        }
    }

    public Cursor getsellinvoice(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_sellInvoices + " WHERE " + TABLE_sellInvoices_COL_3 + " LIKE \'%" + date + "%\' ;", null);

        return res;
    }

    public int getsellnextid() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select max(id) from " + TABLE_sellInvoices + ";", null);
        int id = 0;
        if (res.moveToFirst()) {
            do {
                id = res.getInt(0);
            } while (res.moveToNext());
        }
        return id;
    }

    public Cursor getsellinvoicebyInvoiceId(String invoiceId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_sellInvoices + " WHERE " + TABLE_Kest_COL_2 + " LIKE \'" + invoiceId + "\';", null);

        return res;
    }

    public boolean updatesellinvoicebyName(String name, double newtotal) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_Kest_COL_2, name);
        contentValues.put(TABLE_sellInvoices_COL_5, newtotal);

        int result = db.update(TABLE_sellInvoices, contentValues, "INVOICEID=?", new String[]{String.valueOf(name)});
        if (result > 0) {
            return true;

        } else {
            return false;
        }
    }

    public Cursor getallsellinvoices() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_sellInvoices, null);
        return res;
    }

    public int deletesellData(String invoiceid) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABLE_sellInvoices, "INVOICEID=?", new String[]{invoiceid});
        return i;
    }

    public void deletesellinvoice(String invoiceid) {
        getWritableDatabase().delete(TABLE_sellInvoices, TABLE_Kest_COL_2 + "=?", new String[]{invoiceid});
    }

    public void deleteallsellInvoices() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_sellInvoices);
    }

    //-----------------------------------------------------------------kists table --------------------------------------------------------
    public boolean insert_kist_date(Kist kist) {
        DataBaseHelper pdb = new DataBaseHelper(context);
        Cursor res = pdb.getallkest();
        if (SheredPrefranseHelper.getAdminData(context) == null && SheredPrefranseHelper.getUserData(context) == null) {
            if (res.getCount() <= 100) {
            } else {
                Toast.makeText(context, "النسخه غير مفعله قم بتفعيل النسخه عن طريق التواصل مع الادمن 01093957856", Toast.LENGTH_LONG).show();
                pdb.close();
                return false;
            }
        }
        pdb.close();
        String invoice_id = kist.getInvoice_id();
        String description = kist.getDescription();
        int dayslimit = kist.getDayslimit();
        String collectdate = kist.getCollectdate();
        double kest_value = kist.getKist_value();
        String statue = kist.getStatue();
        String client_name = kist.getClient_name();
        String pay_type = kist.getKist_value()+"";
        String damenname = kist.getDamen_name();
        String damenphone = kist.getDamen_phone();
        String paydate = kist.getPaydate();


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_Kest_COL_2, invoice_id);
        contentValues.put(TABLE_Kest_COL_3, description);
        contentValues.put(TABLE_Kest_COL_4, dayslimit);
        contentValues.put(TABLE_Kest_COL_5, collectdate);
        contentValues.put(TABLE_Kest_COL_6, kest_value);
        contentValues.put(TABLE_Kest_COL_7, statue);
        contentValues.put(TABLE_Kest_COL_8, client_name);
        contentValues.put(TABLE_Kest_COL_9, pay_type);
        contentValues.put(TABLE_Kest_COL_10, damenname);
        contentValues.put(TABLE_Kest_COL_11, damenphone);
        contentValues.put(TABLE_Kest_COL_12, paydate);

        long result = db.insert(TABLE_Kest, null, contentValues);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void deletekistbyinvoice(String id) {
        getWritableDatabase().delete(TABLE_Kest, TABLE_Kest_COL_2 + "=?", new String[]{id});
    }
    public void deletekistbyid(String id) {
        getWritableDatabase().delete(TABLE_Kest, "ID" + "=?", new String[]{id});
    }

    public Cursor getkestbyclient(String client_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_Kest + " WHERE " + TABLE_Kest_COL_8 + " LIKE \'" + client_name + "\';", null);

        return res;
    }

    public Cursor getallkest() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_Kest, null);

        return res;
    }
  public Cursor getlatekest() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_Kest+ " WHERE " + TABLE_Kest_COL_7 + " LIKE \'" + "not_paid" + "\';", null);

        return res;
    }

    public Cursor getkestbydate(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_Kest + " WHERE " + TABLE_Kest_COL_5 + " LIKE \'" + date + "\';", null);

        return res;
    }

    public boolean updatekistbyName(int id, String payDate, Boolean paid, double kist_value) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_Kest_COL_12, payDate);
        if (paid) {
            contentValues.put(TABLE_Kest_COL_7, "paid");
        } else {
            contentValues.put(TABLE_Kest_COL_6, kist_value);
        }

        int result = db.update(TABLE_Kest, contentValues, "ID=?", new String[]{String.valueOf(id)});
        if (result > 0) {
            return true;

        } else {
            return false;

        }

    }

    public void deleteallKest() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_Kest);
    }

    //-----------------------------------------------------------------------sell products------------------------------------------------------
    public boolean insert_date(sellproduct sellproduct) {

        String invoice_id = sellproduct.getInvoice_id();
        double quantity = sellproduct.getQuantity();
        String code_id = sellproduct.getCode_id();
        String name = sellproduct.getName();
        String description = sellproduct.getDescription();
        double sellprice = sellproduct.getSellprice();
        double buyprice = sellproduct.getBuyprice();
        String date = sellproduct.getDate();
        int itemid = sellproduct.getItemid();


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_sellproducts_COL_2, invoice_id);
        contentValues.put(TABLE_sellproducts_COL_3, quantity);
        contentValues.put(TABLE_sellproducts_COL_4, code_id);
        contentValues.put(TABLE_sellproducts_COL_5, name);
        contentValues.put(TABLE_sellproducts_COL_6, description);
        contentValues.put(TABLE_sellproducts_COL_7, sellprice);
        contentValues.put(TABLE_sellproducts_COL_8, buyprice);
        contentValues.put(TABLE_sellproducts_COL_9, date);
        contentValues.put(TABLE_sellproducts_COL_10, itemid);

        long result = db.insert(TABLE_sellproducts, null, contentValues);
        db.close();
        if (result == -1) {
            return false;

        } else {
            return true;

        }

    }

    public Cursor getsellproduct(String invoice_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_sellproducts + " WHERE " + TABLE_sellproducts_COL_2 + " LIKE \'" + invoice_id + "\';", null);

        return res;
    }

    public Cursor getallsellproducts() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_sellproducts, null);

        return res;
    }
//        db.execSQL("CREATE TABLE " + TABLE_sellproducts + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,INVOICEID TEXT,QUANTITY DOUBLE, CODEID TEXT, NAME TEXT,DESCRIPTION TEXT,SELLPRICE DOUBLE,BUYPRICE DOUBLE,DATE TEXT, ITEMID TEXT)");
    public Cursor getsellproductbydate(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT ID,INVOICEID,SUM(QUANTITY),CODEID,NAME,DESCRIPTION,SELLPRICE FROM " + TABLE_sellproducts + " WHERE " + TABLE_sellproducts_COL_9 + " LIKE \'%" + date + "%\' Group by Name ;", null);

        return res;
    }

    public void deletesellProduct(String id) {
        getWritableDatabase().delete(TABLE_sellproducts, TABLE_sellproducts_COL_1 + "=?", new String[]{id});
    }

    public void deletesellProducts(String invoice_id) {
        Cursor res = this.getsellproduct(invoice_id);
        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                getWritableDatabase().delete(TABLE_sellproducts, TABLE_sellproducts_COL_1 + "=?", new String[]{res.getInt(0) + ""});
            }
        }
    }

    public void deleteallsellproducts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_sellproducts);
    }

    //-----------------------------------------------------------------products table ----------------------------------------------------------
    public boolean insert_date(product product) {

        String code_id = product.getCode_id();
        String name = product.getName();
        String description = product.getDescription();
        Double sell_price = product.getSellprice();
        Double buy_price = product.getBuyprice();
        double quantity = product.getQuantity();
        String expiredate = product.getExpiredate();
        int itemid = product.getItemid();
        byte[] image = product.getImage();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


       /* int id = res.getCount() + 1;
        contentValues.put(COL_1, id);*/
        contentValues.put(TABLE_Product_COL_2, code_id);
        contentValues.put(TABLE_Product_COL_3, name);
        contentValues.put(TABLE_Product_COL_4, description);
        contentValues.put(TABLE_Product_COL_5, sell_price);
        contentValues.put(TABLE_Product_COL_6, buy_price);
        contentValues.put(TABLE_Product_COL_7, quantity);
        contentValues.put(TABLE_Product_COL_8, String.valueOf(expiredate));
        contentValues.put(TABLE_Product_COL_9, itemid);
        contentValues.put(TABLE_Product_COL_10, image);
        contentValues.put(TABLE_Product_COL_11, product.getMeasure1());
        contentValues.put(TABLE_Product_COL_12, product.getMeasure2());
        contentValues.put(TABLE_Product_COL_13, product.getMeasure3());
        contentValues.put(TABLE_Product_COL_14, product.getSellprice2());
        contentValues.put(TABLE_Product_COL_15, product.getSellprice3());
        contentValues.put(TABLE_Product_COL_16, product.getCategory());
        contentValues.put(TABLE_Product_COL_17, product.getFactor2());
        contentValues.put(TABLE_Product_COL_18, product.getFactor3());

        Cursor res = getallproducts();
        if (SheredPrefranseHelper.getAdminData(context) == null && SheredPrefranseHelper.getUserData(context) == null) {
            if (res.getCount() <= 10) {
            } else {
                Toast.makeText(context, "النسخه غير مفعله قم بتفعيل النسخه عن طريق التواصل مع الادمن 01093957856", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        long result = db.insert(TABLE_Product, null, contentValues);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean insert_date(category category) {

        String name = category.getCategory();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(category_COL_2, name);
        long result = db.insert(TABLE_category, null, contentValues);
        db.close();
        if (result == -1) {
            return false;

        } else {
            return true;

        }

    }
    public void deletecategory(String name) {
        getWritableDatabase().delete(TABLE_category, category_COL_2 + "=?", new String[]{name});
    }
    public boolean insert_date(Measure measure) {

        String name = measure.getMeasure();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(measure_COL_2, name);
        long result = db.insert(TABLE_MEASURE, null, contentValues);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getallmeasurements() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT Distinct MEASURE FROM " + TABLE_MEASURE, null);

        return res;
    }

    public Cursor getallcategories() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT Distinct CATEGORY FROM " + TABLE_category, null);
        return res;
    }

    public int getproductsnextid() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select max(id) from " + TABLE_Product + ";", null);
        int id = 0;
        if (res.moveToFirst()) {
            do {
                id = res.getInt(0);
            } while (res.moveToNext());
        }
        return id + 10001;
    }

    public boolean updateData(product product) {

        int id = product.getId();
        String code_id = product.getCode_id();
        String name = product.getName();
        String description = product.getDescription();
        Double sell_price = (Double) product.getSellprice();
        Double buy_price = (Double) product.getBuyprice();
        double quantity = product.getQuantity();
        String expiredate = product.getExpiredate();
        // int itemid = product.getItemid();
        byte[] image = product.getImage();


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TABLE_Product_COL_2, code_id);
        contentValues.put(TABLE_Product_COL_3, name);
        contentValues.put(TABLE_Product_COL_4, description);
        contentValues.put(TABLE_Product_COL_5, sell_price);
        contentValues.put(TABLE_Product_COL_6, buy_price);
        contentValues.put(TABLE_Product_COL_7, quantity);
        contentValues.put(TABLE_Product_COL_8, String.valueOf(expiredate));
        contentValues.put(TABLE_Product_COL_10, image);
        contentValues.put(TABLE_Product_COL_11, product.getMeasure1());
        contentValues.put(TABLE_Product_COL_12, product.getMeasure2());
        contentValues.put(TABLE_Product_COL_13, product.getMeasure3());
        contentValues.put(TABLE_Product_COL_14, product.getSellprice2());
        contentValues.put(TABLE_Product_COL_15, product.getSellprice3());
        contentValues.put(TABLE_Product_COL_16, product.getCategory());
        contentValues.put(TABLE_Product_COL_17, product.getFactor2());
        contentValues.put(TABLE_Product_COL_18, product.getFactor3());
        //  contentValues.put(COL_9, itemid);


        int result = db.update(TABLE_Product, contentValues, "ID=?", new String[]{String.valueOf(id)});
        if (result > 0) {

            return true;
        } else {

            return false;
        }
    }

    public boolean updateDatabyName(String name, double quantity) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_Product_COL_3, name);
        contentValues.put(TABLE_Product_COL_7, quantity);

        int result = db.update(TABLE_Product, contentValues, "NAME=?", new String[]{String.valueOf(name)});
        if (result > 0) {
            db.close();
            return true;

        } else {

            return false;

        }

    }public boolean updateDatabyName2(String name, double quantity,double buyprice) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_Product_COL_3, name);
        contentValues.put(TABLE_Product_COL_7, quantity);
        contentValues.put(TABLE_Product_COL_6, buyprice);

        int result = db.update(TABLE_Product, contentValues, "NAME=?", new String[]{String.valueOf(name)});
        if (result > 0) {
            db.close();
            return true;

        } else {

            return false;

        }

    }

    public Cursor getproduct(String search) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("SELECT * FROM " + TABLE_Product + " WHERE " + TABLE_Product_COL_2 + " LIKE \'" + search + "\';", null);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {

        }
        return res;
    }

    public Cursor getproductsbycategory(String search) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("SELECT * FROM " + TABLE_Product + " WHERE " + TABLE_Product_COL_16 + " LIKE \'" + search + "\' ;", null);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {

        }
        return res;
    }

    public Cursor getproducts2(String search) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("SELECT * FROM " + TABLE_Product + " WHERE " + TABLE_Product_COL_3 + " LIKE \'%" + search + "%\' OR " + TABLE_Product_COL_2 + " LIKE \'" + search + "\';", null);

        } catch (
                SQLiteException e) {
            e.printStackTrace();
        } finally {

        }
        return res;
    }

    public Cursor getQuantites(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("SELECT * FROM " + TABLE_Product + " WHERE " + TABLE_Product_COL_3 + " LIKE \'" + name + "\';", null);
        } catch (
                SQLiteException e) {
            e.printStackTrace();
        } finally {

        }
        return res;
    }

    public Cursor getproductsbyquantity(Double quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("SELECT * FROM " + TABLE_Product + " WHERE " + TABLE_Product_COL_7 + " < " + quantity + " ;", null);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {

        }
        return res;

    }

    public Cursor getallproducts() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("SELECT * FROM " + TABLE_Product, null);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return res;

    }

    public void deleteProduct(String id) {
        getWritableDatabase().delete(TABLE_Product, TABLE_Product_COL_1 + "=?", new String[]{id});
    }

    public void deleteallproducts() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("delete from " + TABLE_Product);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    //------------------------------------------------------------------money box table --------------------------------------------------------
    public boolean insert_date(money money) {
        Cursor res = getallTransactions();
        if (SheredPrefranseHelper.getAdminData(context) == null && SheredPrefranseHelper.getUserData(context) == null) {
            if (res.getCount() <= 30) {
            } else {
                Toast.makeText(context, "النسخه غير مفعله قم بتفعيل النسخه عن طريق التواصل مع الادمن 01093957856", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        String date = money.getDate();
        String notes = money.getNotes();
        double totalafter = money.getTotalAfter();
        double totalbefore = money.getTotalbefore();
        double value = money.getValue();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_money_COL_2, value);
        contentValues.put(TABLE_money_COL_3, totalbefore);
        contentValues.put(TABLE_money_COL_4, totalafter);
        contentValues.put(TABLE_money_COL_5, String.valueOf(date));
        contentValues.put(TABLE_money_COL_6, notes);
        long result = db.insert(TABLE_money, null, contentValues);
        if (result == -1) {

            return false;

        } else {

            return true;

        }

    }

    public Cursor getallTransactions() {
        Cursor res = null;
        SQLiteDatabase db = this.getWritableDatabase();
        ;
        try {
            res = db.rawQuery("SELECT * FROM " + TABLE_money, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return res;

    }

    public Cursor getTransactions(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ;
        Cursor res = null;
        try {
            res = db.rawQuery("SELECT * FROM " + TABLE_money + " WHERE " + TABLE_money_COL_5 + " LIKE \'%" + date + "%\' ;", null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return res;
    }

    public void deleteallmoney() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("delete from " + TABLE_money);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    //-----------------------------------------------------------------Repair table ---------------------------------------------------------
    public boolean insert_date(Repair repair) {

        String clientname = repair.getClient_name();
        String createdat = repair.getCreated_at();
        String visitat = repair.getVisit_at();
        double moneycollect = repair.getMoney_collect();
        String statue = repair.getStatue();
        String notes = repair.getNotes();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_repair_COL_2, clientname);
        contentValues.put(TABLE_repair_COL_3, createdat);
        contentValues.put(TABLE_repair_COL_4, visitat);
        contentValues.put(TABLE_repair_COL_5, moneycollect);
        contentValues.put(TABLE_repair_COL_6, statue);
        contentValues.put(TABLE_repair_COL_7, notes);


        long result = db.insert(TABLE_repair, null, contentValues);
        db.close();
        if (result == -1) {
            return false;
        } else {

            return true;
        }
    }

    public Cursor getrepairbyclient(String client_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_repair + " WHERE " + TABLE_repair_COL_2 + " LIKE \'" + client_name + "\';", null);

        return res;
    }

    public Cursor getallrepair() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_repair, null);

        return res;
    }

    public Cursor getrepairbydate(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_repair + " WHERE " + TABLE_repair_COL_4 + " LIKE \'" + date + "\';", null);

        return res;
    }

    public boolean updaterepairbyName(int id, String visitDate, Boolean visit,
                                      double money, String notes) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_repair_COL_4, visitDate);
        contentValues.put(TABLE_repair_COL_5, money);
        contentValues.put(TABLE_repair_COL_7, notes);

        if (visit) {
            contentValues.put(TABLE_repair_COL_6, "completed");
        } else {
            contentValues.put(TABLE_repair_COL_6, "not_completed");
        }

        int result = db.update(TABLE_repair, contentValues, "ID=?", new String[]{String.valueOf(id)});
        if (result > 0) {

            return true;

        } else {

            return false;

        }

    }

    //------------------------------------------------------------------omla table -------------------------------------------------------------
    public boolean insert_date(omla omla) {
        Cursor res = getallomla();
        if (SheredPrefranseHelper.getAdminData(context) == null && SheredPrefranseHelper.getUserData(context) == null) {
            if (res.getCount() <= 10) {
            } else {
                Toast.makeText(context, "النسخه غير مفعله قم بتفعيل النسخه عن طريق التواصل مع الادمن 01093957856", Toast.LENGTH_LONG).show();

                return false;
            }
        }
        String name = omla.getName();
        String address = omla.getAddress();
        String phone = omla.getPhone();
        String notes = omla.getNotes();
        double hasmoney = omla.getHasmoney();
        double paymoney = omla.getPaymoney();
        double maxnotpaid = omla.getMaxnotpaid();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_omla_COL_2, name);
        contentValues.put(TABLE_omla_COL_3, address);
        contentValues.put(TABLE_omla_COL_4, phone);
        contentValues.put(TABLE_omla_COL_5, notes);
        contentValues.put(TABLE_omla_COL_6, hasmoney);
        contentValues.put(TABLE_omla_COL_7, paymoney);
        contentValues.put(TABLE_omla_COL_8, maxnotpaid);

        long result = db.insert(TABLE_omla, null, contentValues);
        db.close();
        if (result == -1) {
            return false;

        } else {
            return true;
        }

    }

    public boolean updateData(omla omla) {

        int id = omla.getId();
        String name = omla.getName();
        String address = omla.getAddress();
        String phone = omla.getPhone();
        String notes = omla.getNotes();
        double hasmoney = omla.getHasmoney();
        double paymoney = omla.getPaymoney();
        double maxnotpaid = omla.getMaxnotpaid();


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_omla_COL_2, name);
        contentValues.put(TABLE_omla_COL_3, address);
        contentValues.put(TABLE_omla_COL_4, phone);
        contentValues.put(TABLE_omla_COL_5, notes);
        contentValues.put(TABLE_omla_COL_6, hasmoney);
        contentValues.put(TABLE_omla_COL_7, paymoney);
        contentValues.put(TABLE_omla_COL_8, maxnotpaid);

        int result = db.update(TABLE_omla, contentValues, "ID=?", new String[]{String.valueOf(id)});
        if (result > 0) {
            return true;

        } else {
            return false;

        }

    }

    public Cursor getomla(String search) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_omla + " WHERE " + TABLE_omla_COL_2 + " LIKE \'" + search + "\' ;", null);

        return res;
    }

    public Cursor getomla2(String search) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_omla + " WHERE " + TABLE_omla_COL_2 + " LIKE \'%" + search + "%\' OR PHONE LIKE \'%" + search + "%\' ;", null);

        return res;
    }

    public Cursor getallomla() {
        Cursor res = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            res = db.rawQuery("SELECT * FROM " + TABLE_omla, null);
        } catch (Exception e) {
        }
        return res;

    }

    public void deleteclient(String name) {
        getWritableDatabase().delete(TABLE_omla, TABLE_omla_COL_2 + "=?", new String[]{name});
    }

    public void deleteallomla() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_omla);
    }

    //-----------------------------------------------------------------------omla transactions--------------------------------------------------
    public boolean insert_date(omlatransaction omlatransaction) {
        Cursor res = getallomlatransactions();
        if (SheredPrefranseHelper.getAdminData(context) == null && SheredPrefranseHelper.getUserData(context) == null) {
            if (res.getCount() <= 40) {
            } else {
                Toast.makeText(context, "النسخه غير مفعله قم بتفعيل النسخه عن طريق التواصل مع الادمن 01093957856", Toast.LENGTH_LONG).show();
                close();
                return false;
            }
        }
        String invoice_id = omlatransaction.getInvoiceId();
        String date = omlatransaction.getDate();
        String customer_name = omlatransaction.getName();
        double notpaid = omlatransaction.getNotpaid();
        double paid = omlatransaction.getValue();
        String process = omlatransaction.getProcess();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(omla_transactions_COL_2, customer_name);
        contentValues.put(omla_transactions_COL_3, process);
        contentValues.put(omla_transactions_COL_4, paid);
        contentValues.put(omla_transactions_COL_5, date);
        contentValues.put(omla_transactions_COL_6, invoice_id);
        contentValues.put(omla_transactions_COL_7, notpaid);

        long result = db.insert(TABLE_omla_transactions, null, contentValues);
        db.close();
        if (result == -1) {
            return false;

        } else {
            return true;

        }

    }

    public Cursor gettransaction(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_omla_transactions + " WHERE " + omla_transactions_COL_2 + " LIKE \'" + value + "\';", null);
        return res;
    }

    public Cursor getallomlatransactions() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_omla_transactions, null);
        return res;
    }

    public void deleteallomlatrans() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_omla_transactions);
    }

    //-------------------------------------------------------------------mored -----------------------------------------------------------
    public boolean insert_date(mored mored) {
        Cursor res = getallmored();
        if (SheredPrefranseHelper.getAdminData(context) == null && SheredPrefranseHelper.getUserData(context) == null) {
            if (res.getCount() <= 10) {
            } else {
                Toast.makeText(context, "النسخه غير مفعله قم بتفعيل النسخه عن طريق التواصل مع الادمن 01093957856", Toast.LENGTH_LONG).show();
                close();
                return false;
            }
        }
        String name = mored.getName();
        String address = mored.getAddress();
        String phone = mored.getPhone();
        String notes = mored.getNotes();
        double hasmoney = mored.getHasmoney();
        double paymoney = mored.getPaymoney();


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(mored_COL_2, name);
        contentValues.put(mored_COL_3, address);
        contentValues.put(mored_COL_4, phone);
        contentValues.put(mored_COL_5, notes);
        contentValues.put(mored_COL_6, hasmoney);
        contentValues.put(mored_COL_7, paymoney);
        long result = db.insert(TABLE_mored, null, contentValues);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateData(mored mored) {

        int id = mored.getId();
        String name = mored.getName();
        String address = mored.getAddress();
        String phone = mored.getPhone();
        String notes = mored.getNotes();
        double hasmoney = mored.getHasmoney();
        double paymoney = mored.getPaymoney();


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(mored_COL_2, name);
        contentValues.put(mored_COL_3, address);
        contentValues.put(mored_COL_4, phone);
        contentValues.put(mored_COL_5, notes);
        contentValues.put(mored_COL_6, hasmoney);
        contentValues.put(mored_COL_7, paymoney);
        int result = db.update(TABLE_mored, contentValues, "ID=?", new String[]{String.valueOf(id)});
        if (result > 0) {
            return true;

        } else {
            return false;

        }

    }

    public Cursor getmored(String search) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_mored + " WHERE " + mored_COL_2 + " LIKE \'" + search + "\' ;", null);

        return res;
    }

    public Cursor getmored2(String search) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_mored + " WHERE " + mored_COL_2 + " LIKE \'%" + search + "%\' ;", null);

        return res;
    }

    public Cursor getallmored() {


        Cursor res = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            res = db.rawQuery("SELECT * FROM " + TABLE_mored, null);
        } catch (Exception e) {

        } finally {
            return res;
        }

    }

    public void deletemored(String name) {
        getWritableDatabase().delete(TABLE_mored, mored_COL_2 + "=?", new String[]{name});
    }

    public void deleteallmoreds() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_mored);
    }

    //-------------------------------------------------------mored transactions -----------------------------------------------------
    public boolean insert_date(moredtransaction moredtransaction) {
        Cursor res = getallmored();
        if (SheredPrefranseHelper.getAdminData(context) == null && SheredPrefranseHelper.getUserData(context) == null) {
            if (res.getCount() <= 30) {
            } else {
                Toast.makeText(context, "النسخه غير مفعله قم بتفعيل النسخه عن طريق التواصل مع الادمن 01093957856", Toast.LENGTH_LONG).show();
                close();
                return false;
            }
        }
        String invoice_id = moredtransaction.getInvoiceId();
        String date = moredtransaction.getDate();
        String customer_name = moredtransaction.getName();
        double notpaid = moredtransaction.getNotpaid();
        double paid = moredtransaction.getValue();
        String process = moredtransaction.getProcess();


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(mored_transactions_COL_2, customer_name);
        contentValues.put(mored_transactions_COL_3, process);
        contentValues.put(mored_transactions_COL_4, paid);
        contentValues.put(mored_transactions_COL_5, date);
        contentValues.put(mored_transactions_COL_6, invoice_id);
        contentValues.put(mored_transactions_COL_7, notpaid);

        long result = db.insert(TABLE_mored_transactions, null, contentValues);
        db.close();
        if (result == -1) {
            return false;

        } else {
            return true;

        }

    }

    public Cursor getmoredtransaction(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_mored_transactions + " WHERE " + mored_transactions_COL_2 + " LIKE \'" + value + "\';", null);

        return res;
    }

    public Cursor getallmoredtransactions() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_mored_transactions, null);
        return res;
    }

    public void deleteallmoredtrans() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_mored_transactions);
    }

    //------------------------------------------------------------------------------buy invoices ---------------------------------------------------------------
    public Cursor getbuyinvoicebyInvoiceId(String invoiceId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_buy_invoices + " WHERE " + buy_invoices_COL_2 + " LIKE \'" + invoiceId + "\';", null);

        return res;
    }

    public int getbuyinvoicesnextid() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select max(id) from " + TABLE_buy_invoices + ";", null);
        int id = 0;
        if (res.moveToFirst()) {
            do {
                id = res.getInt(0);
            } while (res.moveToNext());
        }
        return id;
    }


    public boolean insert_date(com.mohamedragab.cashpos.modules.buy.models.invoice invoice) {

        String invoice_id = invoice.getInvoice_id();
        String date = invoice.getDate();
        String mored_name = invoice.getCustomer_name();
        double total = invoice.getTotal();
        double notpaid = invoice.getNotpaid();
        String cashier_name = invoice.getCashier();


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(buy_invoices_COL_2, invoice_id);
        contentValues.put(buy_invoices_COL_3, date);
        contentValues.put(buy_invoices_COL_4, mored_name);
        contentValues.put(buy_invoices_COL_5, total);
        contentValues.put(buy_invoices_COL_6, notpaid);
        contentValues.put(buy_invoices_COL_7, cashier_name);

        Cursor res = getbuyallinvoices();
        if (SheredPrefranseHelper.getAdminData(context) == null && SheredPrefranseHelper.getUserData(context) == null) {
            if (res.getCount() <= 10) {
            } else {
                Toast.makeText(context, "النسخه غير مفعله قم بتفعيل النسخه عن طريق التواصل مع الادمن 01093957856", Toast.LENGTH_LONG).show();
                close();
                return false;
            }
        }

        long result = db.insert(TABLE_buy_invoices, null, contentValues);
        db.close();
        if (result == -1) {
            return false;

        } else {
            return true;

        }

    }

    public Cursor getbuyinvoice(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_buy_invoices + " WHERE " + buy_invoices_COL_3 + " LIKE  \'%" + date + "%\' ;", null);

        return res;
    }

    public boolean updatebuyinvoicebyName(String name, double newtotal) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(buy_invoices_COL_2, name);
        contentValues.put(buy_invoices_COL_5, newtotal);

        int result = db.update(TABLE_buy_invoices, contentValues, "INVOICEID=?", new String[]{String.valueOf(name)});
        if (result > 0) {
            return true;

        } else {
            return false;
        }
    }

    public Cursor getbuyallinvoices() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_buy_invoices, null);
        return res;
    }

    public int deletebuyData(String invoiceid) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABLE_buy_invoices, "INVOICEID=?", new String[]{invoiceid});
        return i;
    }

    public void deletebuyinvoice(String invoiceid) {
        getWritableDatabase().delete(TABLE_buy_invoices, buy_invoices_COL_2 + "=?", new String[]{invoiceid});
    }

    public void deleteallbuyinvoices() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_buy_invoices);
    }

    //-------------------------------------------------------------------buy products ----------------------------------------
    public boolean insert_date(buyproduct buyproduct) {
        String invoice_id = buyproduct.getInvoice_id();
        double quantity = buyproduct.getQuantity();
        String code_id = buyproduct.getCode_id();
        String name = buyproduct.getName();
        String description = buyproduct.getDescription();
        double sellprice = buyproduct.getSellprice();
        double buyprice = buyproduct.getBuyprice();
        int itemid = buyproduct.getItemid();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(buyproducts_COL_2, invoice_id);
        contentValues.put(buyproducts_COL_3, quantity);
        contentValues.put(buyproducts_COL_4, code_id);
        contentValues.put(buyproducts_COL_5, name);
        contentValues.put(buyproducts_COL_6, description);
        contentValues.put(buyproducts_COL_7, sellprice);
        contentValues.put(buyproducts_COL_8, buyprice);
        contentValues.put(buyproducts_COL_9, itemid);

        long result = db.insert(TABLE_buyproducts, null, contentValues);
        db.close();
        if (result == -1) {
            return false;

        } else {
            return true;

        }

    }

    public Cursor getbuyproduct(String invoice_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_buyproducts + " WHERE " + buyproducts_COL_2 + " LIKE \'" + invoice_id + "\';", null);

        return res;
    }

    public void deletebuyProduct(String id) {
        getWritableDatabase().delete(TABLE_buyproducts, buyproducts_COL_1 + "=?", new String[]{id});
    }

    public void deleteallbuyproducts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_buyproducts);

    }

    //------------------------------------------------------------------------------------employees table ---------------------------------------------------------------
    public boolean insert_date(Cashier cashier) {
        Cursor res = getallcashier();
        if (SheredPrefranseHelper.getAdminData(context) == null && SheredPrefranseHelper.getUserData(context) == null) {
            if (res.getCount() <= 10) {
            } else {
                Toast.makeText(context, "النسخه غير مفعله قم بتفعيل النسخه عن طريق التواصل مع الادمن 01093957856", Toast.LENGTH_LONG).show();
                close();
                return false;
            }
        }

        String name = cashier.getName();
        String address = cashier.getAddress();
        String phone = cashier.getPhone();
        String notes = cashier.getNotes();
        byte[] image = cashier.getImage();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(employee_COL_2, name);
        contentValues.put(employee_COL_3, address);
        contentValues.put(employee_COL_4, phone);
        contentValues.put(employee_COL_5, notes);
        contentValues.put(employee_COL_6, image);
        contentValues.put(employee_COL_7, cashier.getJob());
        contentValues.put("USERNAME", cashier.getUserName());
        contentValues.put("PASSWORD", cashier.getPassword());
        contentValues.put("P1", cashier.getP1());
        contentValues.put("P2", cashier.getP2());
        contentValues.put("P3", cashier.getP3());
        contentValues.put("P4", cashier.getP4());
        contentValues.put("P5", cashier.getP5());
        contentValues.put("P6", cashier.getP6());
        contentValues.put("P7", cashier.getP7());
        contentValues.put("P8", cashier.getP8());
        contentValues.put("P9", cashier.getP9());
        contentValues.put("P10", cashier.getP10());
        contentValues.put("P11", cashier.getP11());
        contentValues.put("P12", cashier.getP12());
        contentValues.put("P13", cashier.getP13());
        contentValues.put("P14", cashier.getP14());
        contentValues.put("P15", cashier.getP15());
        contentValues.put("P16", cashier.getP16());


        long result = db.insert(TABLE_employee, null, contentValues);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean updatecashierData(Cashier cashier) {

        int id = cashier.getId();
        String name = cashier.getName();
        String address = cashier.getAddress();
        String phone = cashier.getPhone();
        String notes = cashier.getNotes();
        byte[] image = cashier.getImage();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(employee_COL_2, name);
        contentValues.put(employee_COL_3, address);
        contentValues.put(employee_COL_4, phone);
        contentValues.put(employee_COL_5, notes);
        contentValues.put(employee_COL_6, cashier.getImage());
        contentValues.put(employee_COL_7, cashier.getJob());
        contentValues.put("USERNAME", cashier.getUserName());
        contentValues.put("PASSWORD", cashier.getPassword());
        contentValues.put("P1", cashier.getP1() + "");
        contentValues.put("P2", cashier.getP2() + "");
        contentValues.put("P3", cashier.getP3() + "");
        contentValues.put("P4", cashier.getP4() + "");
        contentValues.put("P5", cashier.getP5() + "");
        contentValues.put("P6", cashier.getP6() + "");
        contentValues.put("P7", cashier.getP7() + "");
        contentValues.put("P8", cashier.getP8() + "");
        contentValues.put("P9", cashier.getP9() + "");
        contentValues.put("P10", cashier.getP10() + "");
        contentValues.put("P11", cashier.getP11() + "");
        contentValues.put("P12", cashier.getP12() + "");
        contentValues.put("P13", cashier.getP13() + "");
        contentValues.put("P14", cashier.getP14() + "");
        contentValues.put("P15", cashier.getP15() + "");
        contentValues.put("P16", cashier.getP16() + "");
        int result = db.update(TABLE_employee, contentValues, "ID=?", new String[]{String.valueOf(id)});
        if (result > 0) {
            return true;

        } else {
            return false;

        }

    }

    public Cursor getcashier2(String search) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_employee + " WHERE " + employee_COL_2 + " LIKE \'%" + search + "%\' ;", null);

        return res;
    }

    public Cursor getcashier(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_employee + " WHERE " + "ID" + " LIKE \'" + id + "\' ;", null);

        return res;
    }

    public Cursor getcashierbyusert_pass(String user, String pass) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_employee + " WHERE " + "USERNAME" + " LIKE \'" + user + "\' AND " + "PASSWORD" + " LIKE \'" + pass + "\' ;", null);

        return res;
    }

    public Cursor getalldelivery() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_employee + " WHERE " + employee_COL_7 + " LIKE \'" + "DEL" + "\' ;", null);

        return res;
    }

    public Cursor getallcashier() {
        Cursor res = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            res = db.rawQuery("SELECT * FROM " + TABLE_employee, null);
        } catch (Exception e) {

        } finally {
            return res;
        }
    }

    public void deletecashier(String name) {
        getWritableDatabase().delete(TABLE_employee, employee_COL_2 + "=?", new String[]{name});
    }

    //--------------------------------------------------------------------------delivery transactions--------------------------------------------------------
    public boolean insert_date(DelivTrans trans) {

        Cursor res = getalldelivery();
        if (SheredPrefranseHelper.getAdminData(context) == null && SheredPrefranseHelper.getUserData(context) == null) {
            if (res.getCount() <= 10) {
            } else {
                Toast.makeText(context, "النسخه غير مفعله قم بتفعيل النسخه عن طريق التواصل مع الادمن 01093957856", Toast.LENGTH_LONG).show();
                close();
                return false;
            }
        }
        String deliv_name = trans.getDeliv_name();
        String address = trans.getAddress();
        double money = trans.getMoney();
        String invoice_id = trans.getInvoice_id();
        String date = trans.getDate();


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DelivTrans_COL_2, deliv_name);
        contentValues.put(DelivTrans_COL_3, money);
        contentValues.put(DelivTrans_COL_4, address);
        contentValues.put(DelivTrans_COL_5, invoice_id);
        contentValues.put(DelivTrans_COL_6, date);


        long result = db.insert(TABLE_DelivTrans, null, contentValues);
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getdeliverytransbydate(String search, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_DelivTrans + " WHERE " + DelivTrans_COL_6 + " LIKE \'" + search + "\' AND " + DelivTrans_COL_2 + " LIKE \'" + id + "\' ;", null);

        return res;
    }

    public Cursor getalldeliverytrans(String id) {
        Cursor res = null;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            res = db.rawQuery("SELECT * FROM " + TABLE_DelivTrans + " WHERE " + DelivTrans_COL_2 + " LIKE \'" + id + "\' ;", null);
        } catch (Exception e) {
        } finally {
            return res;
        }
    }
}
