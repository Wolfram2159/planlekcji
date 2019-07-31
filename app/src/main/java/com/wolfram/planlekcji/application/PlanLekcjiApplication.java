package com.wolfram.planlekcji.application;

import android.app.Application;

import com.wolfram.planlekcji.database.AppDatabase;

/**
 * @author Wolfram
 * @date 2019-07-31
 */
public class PlanLekcjiApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase.getInstance(getApplicationContext());
    }
}
