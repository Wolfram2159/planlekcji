package com.wolfram.planlekcji.database;

import android.content.Context;

import com.wolfram.planlekcji.database.entities.Grade;
import com.wolfram.planlekcji.database.entities.Subject;

import androidx.room.Database;
import androidx.room.Ignore;
import androidx.room.Room;
import androidx.room.RoomDatabase;
/**
 * @author Wolfram
 * @date 2019-07-31
 */
@Database(entities = {Subject.class, Grade.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    @Ignore
    public final static String APPDATABASE_NAME = "plan.db";

    private static AppDatabase appDatabase;

    public abstract UserDao getUserDao();

    public static synchronized AppDatabase getInstance(Context context){
        if (appDatabase==null){
            appDatabase = Room
                    .databaseBuilder(context, AppDatabase.class, AppDatabase.APPDATABASE_NAME)
                    .build();
        }
        return appDatabase;
    }
}