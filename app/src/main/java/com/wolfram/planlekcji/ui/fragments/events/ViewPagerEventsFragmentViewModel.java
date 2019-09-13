package com.wolfram.planlekcji.ui.fragments.events;

import android.app.Application;
import android.os.AsyncTask;

import com.wolfram.planlekcji.database.room.AppDatabase;
import com.wolfram.planlekcji.database.room.UserDao;
import com.wolfram.planlekcji.database.room.entities.Event;
import com.wolfram.planlekcji.database.room.entities.EventDisplay;
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
public class ViewPagerEventsFragmentViewModel extends AndroidViewModel {

    private UserDao dao;
    private EnumMap<Day, LiveData<List<EventDisplay>>> eventsFromDay;

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

    public void deleteEvent(Event event) {
        AsyncTask.execute(() -> dao.deleteEvent(event));
    }
}
