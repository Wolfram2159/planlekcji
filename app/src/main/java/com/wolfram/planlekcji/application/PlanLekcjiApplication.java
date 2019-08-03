package com.wolfram.planlekcji.application;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.wolfram.planlekcji.database.room.AppDatabase;
import com.wolfram.planlekcji.database.room.UserDao;

import java.io.File;

/**
 * @author Wolfram
 * @date 2019-07-31
 */
public class PlanLekcjiApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());

        File dbFile = getApplicationContext().getDatabasePath(AppDatabase.APPDATABASE_NAME);

        Log.e("exist","");

        if (!dbFile.exists()) {
            Log.e("exist","");
            AsyncTask.execute(() -> {
                UserDao dao = appDatabase.getUserDao();

            });
        }


    }
}
