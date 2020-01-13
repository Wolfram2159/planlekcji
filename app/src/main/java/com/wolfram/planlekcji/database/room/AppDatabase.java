package com.wolfram.planlekcji.database.room;

import android.content.Context;

import com.wolfram.planlekcji.database.room.entities.DateConverter;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.database.room.entities.event.EventEntity;
import com.wolfram.planlekcji.database.room.entities.grade.GradeEntity;
import com.wolfram.planlekcji.database.room.entities.notes.ImageNoteEntity;
import com.wolfram.planlekcji.database.room.entities.notes.TextNoteEntity;

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
@Database(entities = {EventEntity.class, SubjectEntity.class, GradeEntity.class, ImageNoteEntity.class, TextNoteEntity.class}, version = 4)
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
                    .addMigrations(MIGRATION_3_4)
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

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE notes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "subject_id INTEGER NOT NULL," +
                    "photoPath TEXT," +
                    "filePath TEXT," +
                    "date INTEGER," +
                    "FOREIGN KEY (subject_id) REFERENCES subjects(id)" +
                    ")");
        }
    };
}
