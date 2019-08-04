package com.wolfram.planlekcji.ui.activities;

import android.app.Application;

import com.wolfram.planlekcji.database.room.AppDatabase;
import com.wolfram.planlekcji.database.room.UserDao;
import com.wolfram.planlekcji.database.room.entities.Subject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * @author Wolfram
 * @date 2019-08-04
 */
public class SubjectsViewModel extends AndroidViewModel {

    private UserDao dao;

    public SubjectsViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application.getApplicationContext());
        dao = appDatabase.getUserDao();
    }

    LiveData<List<Subject>> getSubjects(){
        return dao.getSubjects();
    }
}
