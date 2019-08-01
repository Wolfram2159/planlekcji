package com.wolfram.planlekcji.database;

import com.wolfram.planlekcji.database.entities.Grade;
import com.wolfram.planlekcji.database.entities.Subject;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * @author Wolfram
 * @date 2019-07-31
 */
@Dao
public interface UserDao {
    @Query("SELECT * FROM subjects")
    LiveData<List<Subject>> getSubjects();

    @Query("SELECT * FROM grades")
    LiveData<List<Grade>> getGrades();

    @Insert
    void setSubject(Subject s);

    @Insert
    void setGrade(Grade g);
}
