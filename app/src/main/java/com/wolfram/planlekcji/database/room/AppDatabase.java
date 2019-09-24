package com.wolfram.planlekcji.database.room;

import android.content.Context;

import com.wolfram.planlekcji.database.room.entities.event.Event;
import com.wolfram.planlekcji.database.room.entities.grade.Grade;
import com.wolfram.planlekcji.database.room.entities.Subject;

import androidx.room.Database;
import androidx.room.Ignore;
import androidx.room.Room;
import androidx.room.RoomDatabase;
/**
 * @author Wolfram
 * @date 2019-07-31
 */
@Database(entities = {Event.class, Subject.class, Grade.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    @Ignore
    public final static String APPDATABASE_NAME = "plan.db";

    private static AppDatabase appDatabase;

    public abstract UserDao getUserDao();
    //todo: make few userdaos
    public static synchronized AppDatabase getInstance(Context context){
        if (appDatabase==null){
            appDatabase = Room
                    .databaseBuilder(context, AppDatabase.class, AppDatabase.APPDATABASE_NAME)
                    .build();
        }
        return appDatabase;
    }
}
