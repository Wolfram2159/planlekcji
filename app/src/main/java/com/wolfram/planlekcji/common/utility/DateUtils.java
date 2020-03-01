package com.wolfram.planlekcji.common.utility;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Wolfram
 * @date 2019-08-19
 */
public final class DateUtils {

    private DateUtils() {
        throw new UnsupportedOperationException();
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateString(Date date) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTimeString(Date date) {
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(date);
    }

    /*@SuppressLint("SimpleDateFormat")
    public static String getTimeStringForImage(Date date){
        DateFormat formatter = new SimpleDateFormat("MM/dd - HH:mm");
        return formatter.format(date);
    }*/

    @SuppressLint("SimpleDateFormat")
    public static String getTimeStringForSaveFile(Date date){
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return formatter.format(date);
    }
}
