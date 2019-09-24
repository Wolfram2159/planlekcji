package com.wolfram.planlekcji.ui.fragments.grades;

import android.app.Application;

import com.wolfram.planlekcji.database.room.AppDatabase;
import com.wolfram.planlekcji.database.room.UserDao;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.grade.Grade;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * @author Wolfram
 * @date 2019-09-21
 */
public class GradesFragmentViewModel extends AndroidViewModel {

    private UserDao dao;

    public GradesFragmentViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application.getApplicationContext()).getUserDao();
    }

    public LiveData<List<Subject>> getSubjects(){
        return dao.getSubjects();
    }

    public LiveData<List<Grade>> getGradesFromSubject(int subjectId){
        return dao.getGradesFromSubject(subjectId);
    }
}
