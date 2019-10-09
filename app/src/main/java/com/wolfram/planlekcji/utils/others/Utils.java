package com.wolfram.planlekcji.utils.others;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Wolfram
 * @date 2019-08-19
 */
public final class Utils {

    private Utils() {
        throw new UnsupportedOperationException();
    }

    public static String getDateString(Date date) {
        @SuppressLint("SimpleDateFormat")
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    public static String getTimeString(Date date) {
        @SuppressLint("SimpleDateFormat")
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(date);
    }
}
