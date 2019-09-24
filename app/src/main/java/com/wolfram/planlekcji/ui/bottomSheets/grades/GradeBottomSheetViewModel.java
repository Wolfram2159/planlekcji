package com.wolfram.planlekcji.ui.bottomSheets.grades;

import android.app.Application;
import android.os.AsyncTask;

import com.wolfram.planlekcji.database.room.AppDatabase;
import com.wolfram.planlekcji.database.room.UserDao;
import com.wolfram.planlekcji.database.room.entities.grade.Grade;
import com.wolfram.planlekcji.database.room.entities.Subject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * @author Wolfram
 * @date 2019-09-18
 */
public class GradeBottomSheetViewModel extends AndroidViewModel {

    private UserDao dao;

    public GradeBottomSheetViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application.getApplicationContext()).getUserDao();
    }

    public LiveData<List<Subject>> getSubjectsNames(){
        return dao.getSubjectsNames();
    }

    public void insertGrade(Grade grade) {
        AsyncTask.execute(() -> dao.insertGrade(grade));
    }
}
