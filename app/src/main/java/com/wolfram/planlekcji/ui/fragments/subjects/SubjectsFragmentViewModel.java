package com.wolfram.planlekcji.ui.fragments.subjects;

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
 * @date 2019-09-14
 */
public class SubjectsFragmentViewModel extends AndroidViewModel {

    private UserDao dao;

    public SubjectsFragmentViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application.getApplicationContext()).getUserDao();
    }

    public LiveData<List<Subject>> getSubjects(){
        return dao.getSubjects();
    }
}
