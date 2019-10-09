package com.wolfram.planlekcji.database.room;

import android.content.Context;

import com.wolfram.planlekcji.database.room.entities.DateConverter;
import com.wolfram.planlekcji.database.room.entities.event.Event;
import com.wolfram.planlekcji.database.room.entities.grade.Grade;
import com.wolfram.planlekcji.database.room.entities.Subject;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Ignore;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * @author Wolfram
 * @date 2019-07-31
 */
@Database(entities = {Event.class, Subject.class, Grade.class}, version = 3)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    @Ignore
    private final static String APPDATABASE_NAME = "plan.db";

    private static AppDatabase appDatabase;

    public abstract UserDao getUserDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room
                    .databaseBuilder(context, AppDatabase.class, AppDatabase.APPDATABASE_NAME)
                    .addMigrations(MIGRATION_1_2)
                    .addMigrations(MIGRATION_2_3)
                    .build();
        }
        return appDatabase;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE UNIQUE INDEX index_subjects_id ON subjects(id)");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE grades ADD year INTEGER");
            database.execSQL("ALTER TABLE grades ADD month INTEGER");
            database.execSQL("ALTER TABLE grades ADD day INTEGER");
        }
    };
}
