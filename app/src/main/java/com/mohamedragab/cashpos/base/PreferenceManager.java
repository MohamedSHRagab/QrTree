package com.mohamedragab.cashpos.base;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferenceManager {
    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(AppConfig.PREF_DEFAULT_KEY, Context.MODE_PRIVATE);
    }

    public static String getStringPref(Context context, String prefName){
       return getSharedPreferences(context).getString(prefName, "");
    }
    public static void setStringPref(Context context, String prefName, String value){
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(prefName, value).apply();

    }
    public static int getIntPref(Context context, String prefName){
        return getSharedPreferences(context).getInt(prefName, -1);
    }
    public static void setIntPref(Context context, String prefName, int value){
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putInt(prefName, value).apply();

    }
}
