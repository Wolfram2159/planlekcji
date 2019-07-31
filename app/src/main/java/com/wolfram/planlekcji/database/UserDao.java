package com.wolfram.planlekcji.database;

import com.wolfram.planlekcji.database.entities.Subject;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

/**
 * @author Wolfram
 * @date 2019-07-31
 */
@Dao
public interface UserDao {
    @Query("SELECT * FROM subject")
    List<Subject> getSubjects();
}
