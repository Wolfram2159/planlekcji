package com.wolfram.planlekcji.ui.bottomSheets;

import android.app.Application;
import android.os.AsyncTask;

import com.wolfram.planlekcji.database.room.AppDatabase;
import com.wolfram.planlekcji.database.room.UserDao;
import com.wolfram.planlekcji.database.room.entities.Event;
import com.wolfram.planlekcji.database.room.entities.Subject;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

/**
 * @author Wolfram
 * @date 2019-09-13
 */
public class ModifySubjectViewModel extends AndroidViewModel {

    private UserDao dao;

    public ModifySubjectViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application.getApplicationContext()).getUserDao();
    }

    public Long getSubject(String name) {
        Callable<Long> insertCallable = () -> dao.getSubject(name);
        Long subject_id = null;
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Long> future = executorService.submit(insertCallable);
        try {
            subject_id = future.get();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return subject_id;
    }

    public Long insertNewSubject(Subject subject) {
        Callable<Long> insertCallable = () -> dao.insertNewSubject(subject);
        Long rowId = null;

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Long> future = executorService.submit(insertCallable);
        try {
            rowId = future.get();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rowId;
    }

    public void insertEvent(Event event) {
        AsyncTask.execute(() -> dao.insertEvent(event));
    }

    public void deleteEvent(Event event) {
        AsyncTask.execute(() -> dao.deleteEvent(event));
    }
}
