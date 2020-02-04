package com.wolfram.planlekcji.common.others;

import android.app.Activity;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public final class SnackbarUtils {
    private SnackbarUtils(){}

    public static void showSnackBar(Activity activity, String message){
        View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }
}
