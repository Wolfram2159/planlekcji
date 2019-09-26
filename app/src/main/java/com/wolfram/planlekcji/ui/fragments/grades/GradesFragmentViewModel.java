package com.wolfram.planlekcji.ui.fragments.grades;

import android.app.Application;
import android.os.AsyncTask;

import com.wolfram.planlekcji.database.room.AppDatabase;
import com.wolfram.planlekcji.database.room.UserDao;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.grade.Grade;
import com.wolfram.planlekcji.database.room.entities.grade.GradeDisplay;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * @author Wolfram
 * @date 2019-09-21
 */
public class GradesFragmentViewModel extends AndroidViewModel {

    private GradeDisplay modifiedGrade;
    private UserDao dao;

    public GradesFragmentViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application.getApplicationContext()).getUserDao();
    }

    public void setModifiedGrade(@Nullable GradeDisplay modifiedGrade) {
        this.modifiedGrade = modifiedGrade;
    }

    public GradeDisplay getModifiedGrade() {
        return modifiedGrade;
    }

    public LiveData<List<Subject>> getSubjects(){
        return dao.getSubjects();
    }

    public LiveData<List<GradeDisplay>> getGradesFromSubject(int subjectId){
        return dao.getGradesFromSubject(subjectId);
    }

    public void deleteGrade() {
        AsyncTask.execute(() -> dao.deleteGrade(modifiedGrade));
    }

    public void insertGrade(Grade grade) {
        AsyncTask.execute(() -> dao.insertGrade(grade));
    }

}
