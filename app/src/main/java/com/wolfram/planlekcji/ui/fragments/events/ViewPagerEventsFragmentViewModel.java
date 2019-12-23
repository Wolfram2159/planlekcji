package com.wolfram.planlekcji.ui.fragments.events;

import android.app.Application;
import android.os.AsyncTask;

import com.wolfram.planlekcji.database.room.AppDatabase;
import com.wolfram.planlekcji.database.room.UserDao;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.event.Event;
import com.wolfram.planlekcji.database.room.entities.event.EventDisplay;
import com.wolfram.planlekcji.custom.enums.Day;

import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * @author Wolfram
 * @date 2019-08-31
 */
public class ViewPagerEventsFragmentViewModel extends AndroidViewModel {

    private UserDao dao;
    private EnumMap<Day, LiveData<List<EventDisplay>>> eventsFromDay;
    private EventDisplay modifiedEvent;

    public ViewPagerEventsFragmentViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application.getApplicationContext());
        dao = appDatabase.getUserDao();
        eventsFromDay = new EnumMap<>(Day.class);
    }

    public LiveData<List<EventDisplay>> getEvents(Day day) {
        if (eventsFromDay.get(day) == null) {
            LiveData<List<EventDisplay>> temporaryList = dao.getEventsFromDay(day.toString());
            eventsFromDay.put(day, temporaryList);
        }
        return eventsFromDay.get(day);
    }

    public void deleteEvent() {
        AsyncTask.execute(() -> dao.deleteEvent(modifiedEvent));
    }

    public void setModifiedEvent(EventDisplay modifiedEvent) {
        this.modifiedEvent = modifiedEvent;
    }

    public EventDisplay getModifiedEvent() {
        return modifiedEvent;
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

}
