package com.wolfram.planlekcji.database.room.entities.grade;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wolfram
 * @date 2019-09-21
 */
public class GradeGroup {
    public String title;
    public List<GradeDisplayEntity> gradeList;

    public GradeGroup(String title, List<GradeDisplayEntity> gradeList) {
        this.title = title;
        this.gradeList = gradeList;
    }

    public GradeGroup(String title, GradeDisplayEntity grade) {
        this.title = title;
        this.gradeList = new ArrayList<>();
        addGrade(grade);
    }

    public GradeGroup(String title){
        this.title = title;
        this.gradeList = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<GradeDisplayEntity> getGradeList() {
        return gradeList;
    }

    public void setGradeList(List<GradeDisplayEntity> gradeList) {
        this.gradeList = gradeList;
    }

    public void addGrade(@NonNull GradeDisplayEntity grade) {
        gradeList.add(grade);
    }

    public void clearGroup() {
        gradeList.clear();
    }
}
