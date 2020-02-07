package com.wolfram.planlekcji.database.room.entities.grade;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.wolfram.planlekcji.common.data.Group;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;

import java.util.List;

public class SubjectWithGrades implements Group<GradeEntity> {
    @Embedded public SubjectEntity subject;
    @Relation(parentColumn = "id", entityColumn = "subject_id") public List<GradeEntity> gradesList;

    @Override
    public String getTitle() {
        return subject.getName();
    }

    @Override
    public List<GradeEntity> getList() {
        return gradesList;
    }

    public SubjectEntity getSubject() {
        return subject;
    }
}