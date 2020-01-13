package com.wolfram.planlekcji.ui.fragments.grades;

import android.app.Application;
import android.os.AsyncTask;

import com.wolfram.planlekcji.database.room.AppDatabase;
import com.wolfram.planlekcji.database.room.UserDao;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.database.room.entities.grade.GradeDisplayEntity;
import com.wolfram.planlekcji.database.room.entities.grade.GradeEntity;

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

    private GradeDisplayEntity modifiedGrade;
    private UserDao dao;

    public GradesFragmentViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application.getApplicationContext()).getUserDao();
    }

    public void setModifiedGrade(@Nullable GradeDisplayEntity modifiedGrade) {
        this.modifiedGrade = modifiedGrade;
    }

    public GradeDisplayEntity getModifiedGrade() {
        return modifiedGrade;
    }

    public LiveData<List<SubjectEntity>> getSubjects(){
        return dao.getSubjects();
    }

    public LiveData<List<GradeDisplayEntity>> getGradesFromSubject(int subjectId){
        return dao.getGradesFromSubject(subjectId);
    }

    public void deleteGrade() {
        AsyncTask.execute(() -> dao.deleteGrade(modifiedGrade));
    }

    public void insertGrade(GradeEntity grade) {
        AsyncTask.execute(() -> dao.insertGrade(grade));
    }

}
