package com.mohamedragab.cashpos.base;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {


    public static String format(Date rawDate) {
        String date = rawDate.toString();
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            date = inputFormat.format(rawDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
    public static String getDateOnly(int mYear, int mMonthOfYear, int mDayOfMonth){
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        calendar.set(mYear, mMonthOfYear, mDayOfMonth);
        String dateString = sdf.format(calendar.getTime());
        return dateString;
    }

}
