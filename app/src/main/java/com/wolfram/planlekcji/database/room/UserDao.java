package com.wolfram.planlekcji.database.room;

import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.event.Event;
import com.wolfram.planlekcji.database.room.entities.event.EventDisplay;
import com.wolfram.planlekcji.database.room.entities.grade.Grade;
import com.wolfram.planlekcji.database.room.entities.grade.GradeDisplay;
import com.wolfram.planlekcji.database.room.entities.notes.ImageNote;
import com.wolfram.planlekcji.database.room.entities.notes.TextNote;

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
    @Query("SELECT events.id, subjects.id as subject_id, name, start_time, end_time, localization, day " +
            "FROM events JOIN subjects ON events.subject_id=subjects.id " +
            "WHERE day = (:day) ORDER BY start_time ASC")
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

    @Query("SELECT grades.id, subjects.id as subject_id, date, subjects.name, grades.description " +
            "FROM grades JOIN subjects " +
            "ON grades.subject_id=subjects.id WHERE subject_id=(:subjectId) ORDER BY date ASC")
    LiveData<List<GradeDisplay>> getGradesFromSubject(int subjectId);

    @Delete
    void deleteGrade(Grade grade);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertImageNote(ImageNote imageNote);

    @Query("SELECT * FROM imageNotes WHERE subject_id=(:subject_id)")
    LiveData<List<ImageNote>> getImageNotesFromSubject(int subject_id);

    @Query("SELECT * FROM imageNotes")
    LiveData<List<ImageNote>> getImageNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTextNote(TextNote textNote);

    @Query("SELECT * FROM textNotes WHERE subject_id=(:subject_id)")
    LiveData<List<TextNote>> getTextNotesFromSubject(int subject_id);

    @Query("SELECT * FROM textNotes")
    LiveData<List<TextNote>> getTextNotes();

}
