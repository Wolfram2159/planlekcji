package com.wolfram.planlekcji.ui.fragments;

import android.app.Application;
import android.os.AsyncTask;

import com.wolfram.planlekcji.database.room.AppDatabase;
import com.wolfram.planlekcji.database.room.UserDao;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.utils.enums.Day;

import java.util.EnumMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * @author Wolfram
 * @date 2019-08-31
 */
public class SubjectsFragmentViewModel extends AndroidViewModel {

    private UserDao dao;
    private LiveData<List<Subject>> subjects;
    private EnumMap<Day, LiveData<List<Subject>>> subjectsFromDay;

    public SubjectsFragmentViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application.getApplicationContext());
        dao = appDatabase.getUserDao();
        subjectsFromDay = new EnumMap<>(Day.class);
    }

    public LiveData<List<Subject>> getSubjects(Day day) {
        if (subjectsFromDay.get(day) == null) {
            LiveData<List<Subject>> temporaryList = dao.getSubjectsFromDay(day.toString());
            subjectsFromDay.put(day, temporaryList);
        }
        return subjectsFromDay.get(day);
    }

    public void updateSubject(Subject subject) {
        AsyncTask.execute(() -> dao.updateSubject(subject));
    }

    public void insertSubject(Subject subject) {
        AsyncTask.execute(() -> dao.insertSubject(subject));
    }

    public void deleteSubject(Subject subject) {
        AsyncTask.execute(() -> dao.deleteSubject(subject));
    }
}
