package com.wolfram.planlekcji.utils.others;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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

    private static void writeToFile(String data, String filePath, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filePath, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private static String readFromFile(Context context, String filePath) {
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(filePath);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
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
