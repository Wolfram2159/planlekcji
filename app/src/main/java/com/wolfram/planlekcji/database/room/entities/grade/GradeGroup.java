package com.wolfram.planlekcji.database.room.entities.grade;

import com.wolfram.planlekcji.common.data.Group;

import java.util.List;

/**
 * @author Wolfram
 * @date 2019-09-21
 */
public class GradeGroup implements Group<GradeEntity> {
    public String title;
    public List<GradeEntity> gradeList;

    public GradeGroup(String title, List<GradeEntity> gradeList) {
        this.title = title;
        this.gradeList = gradeList;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<GradeEntity> getList() {
        return gradeList;
    }
}
