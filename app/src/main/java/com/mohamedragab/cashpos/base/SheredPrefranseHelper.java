package com.mohamedragab.cashpos.base;


import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mohamedragab.cashpos.modules.adminpanel.models.adminmodel;
import com.mohamedragab.cashpos.modules.employees.models.Cashier;
import com.mohamedragab.cashpos.modules.login.models.User;
import com.mohamedragab.cashpos.modules.settings.models.screens;
import com.mohamedragab.cashpos.modules.settings.models.shopInfo;

import java.lang.reflect.Type;


public class SheredPrefranseHelper {


    public static User getUserData(Context context) {
        User users = null;
        String jsonString = PreferenceManager.getStringPref(context, AppConfig.USERS_PRF);
        if (jsonString.isEmpty()) {
        } else {
            Type type = new TypeToken<User>() {
            }.getType();
            users = new Gson().fromJson(jsonString, type);
        }
        return users;
    }

    public static void addUserData(Context context, User data) {
        String jj = new Gson().toJson(data);
        Log.d("shared pref", "addUser :" + jj);

        PreferenceManager.setStringPref(context, AppConfig.USERS_PRF, jj);

    }

    //------------------------------------------------------------------------------------------

    public static void addAdminData(Context context, adminmodel data) {
        String jj = new Gson().toJson(data);
        Log.d("shared pref", "addAdmin :" + jj);

        PreferenceManager.setStringPref(context, AppConfig.ADMINS_PRF, jj);

    }

    public static adminmodel getAdminData(Context context) {
        adminmodel users = null;
        String jsonString = PreferenceManager.getStringPref(context, AppConfig.ADMINS_PRF);
        if (jsonString.isEmpty()) {
        } else {
            Type type = new TypeToken<adminmodel>() {
            }.getType();
            users = new Gson().fromJson(jsonString, type);
        }
        return users;
    }
    //------------------------------------------------------------------------------------------

    public static void addshopData(Context context, shopInfo data) {
        String jj = new Gson().toJson(data);
        Log.d("shared pref", "addshopData :" + jj);

        PreferenceManager.setStringPref(context, AppConfig.SHOPDATA_PRF, jj);
       // Toast.makeText(context, "تم تعديل بيانات المحل .", Toast.LENGTH_SHORT).show();
        ((Activity) context).finish();

    }

    public static shopInfo getshopData(Context context) {
        shopInfo info = null;
        String jsonString = PreferenceManager.getStringPref(context, AppConfig.SHOPDATA_PRF);
        if (jsonString.isEmpty()) {
        } else {
            Type type = new TypeToken<shopInfo>() {
            }.getType();
            info = new Gson().fromJson(jsonString, type);
        }
        return info;
    }

    //--------------------------------------------------------------------------------------------------
    public static void addpassword(Context context,String password) {
        String jj = new Gson().toJson(password);
        Log.d("shared pref", "addpassword :" + jj);

        PreferenceManager.setStringPref(context, AppConfig.PASSWORD_PRF, jj);

    }

    public static String getpassword(Context context) {
        String password = null;
        String jsonString = PreferenceManager.getStringPref(context, AppConfig.PASSWORD_PRF);
        if (jsonString.isEmpty()) {
        } else {
            Type type = new TypeToken<String>() {
            }.getType();
            password = new Gson().fromJson(jsonString, type);
        }
        return password;
    }
    //---------------------------------------------------------------------------------------------------------
    public static void addcurrentcashier(Context context, Cashier cashier) {
        String jj = new Gson().toJson(cashier);
        Log.d("shared pref", "addCashier :" + jj);

        PreferenceManager.setStringPref(context, AppConfig.CASHIER_PRF, jj);

    }

    public static Cashier getcurrentcashier(Context context) {
        Cashier cashier = null;
        String jsonString = PreferenceManager.getStringPref(context, AppConfig.CASHIER_PRF);
        if (jsonString.isEmpty()) {
        } else {
            Type type = new TypeToken<Cashier>() {
            }.getType();
            cashier = new Gson().fromJson(jsonString, type);
        }
        return cashier;
    }
    //---------------------------------------------------------------------------------------------------
    public static void addmoney_type(Context context,String money_type) {
        String jj = new Gson().toJson(money_type);
        Log.d("shared pref", "money_type :" + jj);

        PreferenceManager.setStringPref(context, AppConfig.MONEY_TYPE_PRF, jj);

    }

    public static String getmoney_type(Context context) {
        String password = null;
        String jsonString = PreferenceManager.getStringPref(context, AppConfig.MONEY_TYPE_PRF);
        if (jsonString.isEmpty()) {
        } else {
            Type type = new TypeToken<String>() {
            }.getType();
            password = new Gson().fromJson(jsonString, type);
        }
        return password;
    }
    //---------------------------------------------------------------------------------------------------
    public static void addprinter_type(Context context,String printer_type) {
        String jj = new Gson().toJson(printer_type);
        Log.d("shared pref", "printer_type :" + jj);

        PreferenceManager.setStringPref(context, AppConfig.PRINTER_TYPE_PRF, jj);

    }

    public static String getprinter_type(Context context) {
        String password = null;
        String jsonString = PreferenceManager.getStringPref(context, AppConfig.PRINTER_TYPE_PRF);
        if (jsonString.isEmpty()) {
        } else {
            Type type = new TypeToken<String>() {
            }.getType();
            password = new Gson().fromJson(jsonString, type);
        }
        return password;
    }
    //---------------------------------------------------------------------------------------------------------
    public static void addpasswordscreens(Context context, screens screen) {
        String jj = new Gson().toJson(screen);
        Log.d("shared pref", "addpasswordscreens :" + jj);

        PreferenceManager.setStringPref(context, AppConfig.PASSWORD_SCREENS, jj);

    }

    public static screens getpasswordscreens(Context context) {
        screens screen = null;
        String jsonString = PreferenceManager.getStringPref(context, AppConfig.PASSWORD_SCREENS);
        if (jsonString.isEmpty()) {
        } else {
            Type type = new TypeToken<screens>() {
            }.getType();
            screen = new Gson().fromJson(jsonString, type);
        }
        return screen;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void addreturn_money(Context context,String return_money) {
        String jj = new Gson().toJson(return_money);
        Log.d("shared pref", "return_money :" + jj);

        PreferenceManager.setStringPref(context, AppConfig.RETURN_MONEY_PRF, jj);
    }
    public static String getreturn_money(Context context) {
        String return_money = null;
        String jsonString = PreferenceManager.getStringPref(context, AppConfig.RETURN_MONEY_PRF);
        if (jsonString.isEmpty()) {
        } else {
            Type type = new TypeToken<String>() {
            }.getType();
            return_money = new Gson().fromJson(jsonString, type);
        }
        return return_money;
    }//------------------------------------------------------------------------------------------------------------
    public static void adddelivery(Context context,String delivery) {
        String jj = new Gson().toJson(delivery);
        Log.d("shared pref", "delivery :" + jj);

        PreferenceManager.setStringPref(context, AppConfig.DELIVERY_PR, jj);
    }
    public static String getdelivery(Context context) {
        String delivery = null;
        String jsonString = PreferenceManager.getStringPref(context, AppConfig.DELIVERY_PR);
        if (jsonString.isEmpty()) {
        } else {
            Type type = new TypeToken<String>() {
            }.getType();
            delivery = new Gson().fromJson(jsonString, type);
        }
        return delivery;
    }
    //---------------------------------------------------------------------------------------------------------
    public static void addlast_printer_address(Context context,String address) {
        String jj = new Gson().toJson(address);
        Log.d("shared pref", "add address :" + jj);

        PreferenceManager.setStringPref(context, AppConfig.LAST_PRINTER_ADDRESS, jj);
    }
    public static String getlast_printer_address(Context context) {
        String address = null;
        String jsonString = PreferenceManager.getStringPref(context, AppConfig.LAST_PRINTER_ADDRESS);
        if (jsonString.isEmpty()) {
        } else {
            Type type = new TypeToken<String>() {
            }.getType();
            address = new Gson().fromJson(jsonString, type);
        }
        return address;
    }
}