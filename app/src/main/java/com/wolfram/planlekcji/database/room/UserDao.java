package com.wolfram.planlekcji.database.room;

import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.event.Event;
import com.wolfram.planlekcji.database.room.entities.event.EventDisplay;
import com.wolfram.planlekcji.database.room.entities.grade.Grade;
import com.wolfram.planlekcji.database.room.entities.grade.GradeDisplay;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

/**
 * @author Wolfram
 * @date 2019-07-31
 */
@Dao
public interface UserDao {
    @Query("SELECT events.id, subjects.id as subject_id, name, start_hour, start_minute, end_hour, end_minute, localization, day " +
            "FROM events JOIN subjects ON events.subject_id=subjects.id " +
            "WHERE day = (:day) ORDER BY start_hour ASC")
    LiveData<List<EventDisplay>> getEventsFromDay(String day);

    @Query("SELECT subjects.id FROM subjects WHERE (:name)=name")
    Long getSubject(String name);

    @Delete
    void deleteEvent(Event e);

    @Insert
    long insertNewSubject(Subject subject);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEvent(Event e);

    @Query("SELECT * FROM subjects ORDER BY id ASC")
    LiveData<List<Subject>> getSubjects();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGrade(Grade grade);

    @Query("SELECT grades.id, subjects.id as subject_id, subjects.name, grades.description FROM grades JOIN subjects ON grades.subject_id=subjects.id WHERE subject_id=(:subjectId) ORDER BY subject_id ASC")
    LiveData<List<GradeDisplay>> getGradesFromSubject(int subjectId);

    @Delete
    void deleteGrade(Grade grade);

    /*@Query("SELECT * FROM")
    LiveData<GradeDisplay> getGradeDisplay(int grade_id);*/
}
