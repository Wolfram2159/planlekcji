package com.wolfram.planlekcji.database.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.wolfram.planlekcji.database.room.entities.grade.GradeEntity;
import com.wolfram.planlekcji.database.room.entities.grade.SubjectWithGrades;

import java.util.List;

@Dao
public interface GradeDao {
    @Query("SELECT * FROM subjects")
    LiveData<List<SubjectWithGrades>> getSubjectsWithGrades();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGrade(GradeEntity grade);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateGrade(GradeEntity grade);

    @Delete
    void deleteGrade(GradeEntity grade);
}
