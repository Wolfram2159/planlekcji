package com.wolfram.planlekcji.database.room;

import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.database.room.entities.event.EventDisplayEntity;
import com.wolfram.planlekcji.database.room.entities.event.EventEntity;
import com.wolfram.planlekcji.database.room.entities.grade.GradeDisplayEntity;
import com.wolfram.planlekcji.database.room.entities.grade.GradeEntity;
import com.wolfram.planlekcji.database.room.entities.notes.ImageNoteEntity;
import com.wolfram.planlekcji.database.room.entities.notes.TextNoteEntity;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

/**
 * @author Wolfram
 * @date 2019-07-31
 */
@Dao
public interface UserDao {
    @Query("SELECT * FROM subjects ORDER BY id ASC")
    LiveData<List<SubjectEntity>> getSubjects();

    @Query("SELECT subjects.id FROM subjects WHERE (:name)=name")
    Long getSubject(String name);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertSubject(SubjectEntity subject);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    int updateSubject(SubjectEntity subject);

    @Delete
    int deleteSubject(SubjectEntity subject);

    @Query("SELECT events.id, subjects.id as subject_id, name, start_time, end_time, localization, day " +
            "FROM events JOIN subjects ON events.subject_id=subjects.id " +
            "WHERE day = (:day) ORDER BY start_time ASC")
    LiveData<List<EventDisplayEntity>> getEventsFromDay(String day);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEvent(EventEntity e);

    @Delete
    void deleteEvent(EventEntity e);

    @Query("SELECT grades.id, subjects.id as subject_id, date, subjects.name, grades.description " +
            "FROM grades JOIN subjects " +
            "ON grades.subject_id=subjects.id WHERE subject_id=(:subjectId) ORDER BY date ASC")
    LiveData<List<GradeDisplayEntity>> getGradesFromSubject(int subjectId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGrade(GradeEntity grade);

    @Delete
    void deleteGrade(GradeEntity grade);

    @Query("SELECT * FROM imageNotes WHERE subject_id=(:subject_id)")
    LiveData<List<ImageNoteEntity>> getImageNotesFromSubject(int subject_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertImageNote(ImageNoteEntity imageNote);

    @Delete
    void deleteImageNote(ImageNoteEntity note);

    @Query("SELECT * FROM textNotes WHERE subject_id=(:subject_id)")
    LiveData<List<TextNoteEntity>> getTextNotesFromSubject(int subject_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTextNote(TextNoteEntity textNote);

    @Delete
    void deleteTextNote(TextNoteEntity note);
}
