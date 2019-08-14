package com.wolfram.planlekcji.database.room;

import com.wolfram.planlekcji.database.room.entities.Subject;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
 * @author Wolfram
 * @date 2019-07-31
 */
@Dao
public interface UserDao {
    @Query("SELECT * FROM subjects")
    LiveData<List<Subject>> getSubjects();

    /*@Query("SELECT * FROM grades")
    LiveData<List<Grade>> getGrades();*/

    @Query("SELECT * FROM SUBJECTS WHERE day = (:day) ORDER BY start_hour ASC")
    LiveData<List<Subject>> getSubjectsFromDay(String day);

    @Update
    int updateSubject(Subject... subject);

    @Insert
    void insertSubject(Subject s);

    @Delete
    void deleteSubject(Subject s);

    /*@Insert
    void setGrade(Grade g);*/
}
