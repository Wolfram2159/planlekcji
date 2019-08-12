package com.wolfram.planlekcji.ui.activities;

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
 * @date 2019-08-04
 */
public class SubjectsViewModel extends AndroidViewModel {

    //With android view model i can get Application in constructor and then get context for getting appDatabase instance

    private UserDao dao;
    private LiveData<List<Subject>> subjects;
    //private HashMap<Day, LiveData<List<Subject>>> subjectsFromDay;
    private EnumMap<Day, LiveData<List<Subject>>> subjectsFromDay;

    public SubjectsViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application.getApplicationContext());
        dao = appDatabase.getUserDao();
        subjectsFromDay = new EnumMap<>(Day.class);
    }

    public LiveData<List<Subject>> getSubjects() {
        if (subjects == null) {
            subjects = dao.getSubjects();
        }
        return subjects;
    }

    public LiveData<List<Subject>> getSubjects(Day day) {
        if (subjectsFromDay.get(day) == null){
            LiveData<List<Subject>> temporaryList = dao.getSubjectsFromDay(day.toString());
            subjectsFromDay.put(day, temporaryList);
        }
        return subjectsFromDay.get(day);
    }


    public void insertSubject(Subject subject) {
        AsyncTask.execute(() -> dao.insertSubject(subject));
    }

    public void deleteSubject(Subject subject) {
        AsyncTask.execute(() -> dao.deleteSubject(subject));
    }
}
