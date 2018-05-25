package com.angelova.w510.calmmom.utils;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by W510 on 24.5.2018 г..
 */

public class DateTimeUtils {
    public static String parseDateTime(String dateString, String originalFormat, String outputFromat){

        SimpleDateFormat formatter = new SimpleDateFormat(originalFormat, Locale.US);
        Date date = null;
        try {
            date = formatter.parse(dateString);

            SimpleDateFormat dateFormat=new SimpleDateFormat(outputFromat, new Locale("US"));

            return dateFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getRelativeTimeSpan(String dateString, String originalFormat){

        SimpleDateFormat formatter = new SimpleDateFormat(originalFormat, Locale.US);
        Date date = null;
        try {
            date = formatter.parse(dateString);

            return DateUtils.getRelativeTimeSpanString(date.getTime()).toString();

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
