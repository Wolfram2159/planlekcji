package com.wolfram.planlekcji.database.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.wolfram.planlekcji.database.room.entities.event.EventDisplayEntity;
import com.wolfram.planlekcji.database.room.entities.event.EventEntity;

import java.util.List;

@Dao
public interface EventDao {
    @Query("SELECT events.event_id, subjects.id as subject_id, name, start_time, end_time, localization, day " +
            "FROM events JOIN subjects ON events.subject_id=subjects.id " +
            "WHERE day = (:day) ORDER BY start_time ASC")
    LiveData<List<EventDisplayEntity>> getEventsFromDay(String day);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertEvent(EventEntity e);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updateEvent(EventEntity event);

    @Delete
    void deleteEvent(EventEntity e);
}
