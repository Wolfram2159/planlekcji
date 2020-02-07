package com.wolfram.planlekcji.database.room.dao;

import com.wolfram.planlekcji.database.room.entities.SubjectEntity;

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
public interface SubjectDao {
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


}
