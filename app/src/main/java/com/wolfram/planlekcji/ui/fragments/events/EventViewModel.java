package com.wolfram.planlekcji.ui.fragments.events;

import android.app.Application;
import android.os.AsyncTask;

import com.wolfram.planlekcji.common.data.Event;
import com.wolfram.planlekcji.common.utility.DatabaseUtils;
import com.wolfram.planlekcji.database.room.AppDatabase;
import com.wolfram.planlekcji.database.room.dao.EventDao;
import com.wolfram.planlekcji.database.room.dao.SubjectDao;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.database.room.entities.event.EventDisplayEntity;
import com.wolfram.planlekcji.common.enums.Day;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.fragments.subjects.SubjectsFragmentViewModel;

import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

/**
 * @author Wolfram
 * @date 2019-08-31
 */
public class EventViewModel extends AndroidViewModel {

    private EventDao eventDao;
    private SubjectDao subjectDao;
    private EnumMap<Day, LiveData<List<EventDisplayEntity>>> eventsFromDays;
    private EnumMap<Day, List<EventDisplayEntity>> events;
    private EventDisplayEntity modifyingEvent;
    private LiveData<List<SubjectEntity>> subjectList;
    private List<SubjectEntity> subjects;
    private LiveData<Event<String>> resultState;
    private MediatorLiveData<Event<String>> privateResultState;
    private Event<String> stateEvent;

    private final String DELETED = "Event deleted";
    private final String CREATED = "Event created";
    private final String UPDATED = "Event updated";

    public EventViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application.getApplicationContext());
        eventDao = appDatabase.getEventDao();
        subjectDao = appDatabase.getSubjectDao();
        eventsFromDays = new EnumMap<>(Day.class);
        for (Day day : Day.values()) {
            LiveData<List<EventDisplayEntity>> eventsFromDay = eventDao.getEventsFromDay(day.toString());
            eventsFromDays.put(day, eventsFromDay);
        }
        subjectList = subjectDao.getSubjects();
        privateResultState = new MediatorLiveData<>();
        resultState = privateResultState;
        stateEvent = new Event<>();
    }

    public LiveData<Event<String>> getResultState() {
        return resultState;
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
        AsyncTask.execute(() -> {
            eventDao.deleteEvent(event);
            setState(DELETED);
        });
    }

    public void setModifyingEvent(EventDisplayEntity modifyingEvent) {
        this.modifyingEvent = modifyingEvent;
    }

    public EventDisplayEntity getModifyingEvent() {
        return modifyingEvent;
    }

    private Long insertSubject(SubjectEntity subject) {
        String name = subject.getName();
        name = name.equals("") ? SubjectsFragmentViewModel.UNNAMED : name;
        SubjectEntity subjectToSave = new SubjectEntity(name);
        Callable<Long> insertCallable = () -> subjectDao.insertSubject(subjectToSave);
        Long id = null;

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Long> future = executorService.submit(insertCallable);
        try {
            id = future.get();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return id;
    }

    public void modifyEvent(Event<EventDisplayEntity> event, String tag) {
        // TODO: 2020-01-03 check if subject exist, if -> set it id to stateEvent, else create new SubjectEntity and set it id
        EventDisplayEntity eventToSave = event.getValue();
        SubjectEntity eventSubject = eventToSave.getSubject();
        Event<SubjectEntity> subjectFromDatabase = DatabaseUtils.getSubjectFromDatabase(eventSubject, subjects);
        if (!event.isUsed()) {
            if (subjectFromDatabase.isUsed()) {
                eventToSave.setSubject(subjectFromDatabase.getValue());
            } else {
                Integer subjectId = insertSubject(eventSubject).intValue();
                eventToSave.setSubject_id(subjectId);
            }
        }
        if (tag.equals(CustomBottomSheet.MODIFY)) {
            AsyncTask.execute(() -> {
                eventDao.updateEvent(eventToSave);
                setState(UPDATED);
            });
        } else {
            AsyncTask.execute(() -> {
                eventDao.insertEvent(eventToSave);
                setState(CREATED);
            });
        }
    }

    private void setState(String message){
        stateEvent.setValue(message);
        stateEvent.setUsed(false);
        privateResultState.postValue(stateEvent);
    }

    public void callMessageReceived() {
        stateEvent.setUsed(true);
        privateResultState.postValue(stateEvent);
    }
}
