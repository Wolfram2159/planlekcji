package com.wolfram.planlekcji.ui.fragments.events;

import android.app.Application;
import android.os.AsyncTask;

import com.wolfram.planlekcji.database.room.AppDatabase;
import com.wolfram.planlekcji.database.room.UserDao;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.database.room.entities.event.EventDisplayEntity;
import com.wolfram.planlekcji.common.enums.Day;

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
public class EventViewModel extends AndroidViewModel {

    private UserDao dao;
    private EnumMap<Day, LiveData<List<EventDisplayEntity>>> eventsFromDays;
    private EnumMap<Day, List<EventDisplayEntity>> events;
    private EventDisplayEntity modifyingEvent;
    private LiveData<List<SubjectEntity>> subjectList;
    private List<SubjectEntity> subjects;

    public EventViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application.getApplicationContext());
        dao = appDatabase.getUserDao();
        eventsFromDays = new EnumMap<>(Day.class);
        for (Day day : Day.values()) {
            LiveData<List<EventDisplayEntity>> eventsFromDay = dao.getEventsFromDay(day.toString());
            eventsFromDays.put(day, eventsFromDay);
        }
        subjectList = dao.getSubjects();
    }

    public LiveData<List<EventDisplayEntity>> getEventsFromDay(Day day) {
        return eventsFromDays.get(day);
    }

    public void setSubjects(List<SubjectEntity> subjects) {
        this.subjects = subjects;
    }

    public LiveData<List<SubjectEntity>> getObservableSubjects() {
        return this.subjectList;
    }

    public List<SubjectEntity> getSubjects() {
        return this.subjects;
    }

    public void setEvents(EnumMap<Day, List<EventDisplayEntity>> events) {
        this.events = events;
    }

    public void deleteEvent(EventDisplayEntity event) {
        AsyncTask.execute(() -> dao.deleteEvent(event));
    }

    public void setModifyingEvent(EventDisplayEntity modifyingEvent) {
        this.modifyingEvent = modifyingEvent;
    }

    public EventDisplayEntity getModifyingEvent() {
        return modifyingEvent;
    }

    public Long insertNewSubject(SubjectEntity subject) {
        Callable<Long> insertCallable = () -> dao.insertSubject(subject);
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

    public void insertEvent(EventDisplayEntity event) {
        // TODO: 2020-01-03 check if subject exist, if -> set it id to event, else create new SubjectEntity and set it id
        AsyncTask.execute(() -> dao.insertEvent(event));
    }
}
