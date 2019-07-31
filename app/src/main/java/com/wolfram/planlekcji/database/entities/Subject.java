package com.wolfram.planlekcji.database.entities;

import java.sql.Time;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author Wolfram
 * @date 2019-07-31
 */
@Entity
public class Subject {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String subject;

    private String teacher;

    private Time start_time;

    private Time end_time;

    private String localization; //make second table in relation with this

    private String additional_info;

    private String type;

    public Subject() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public Time getStart_time() {
        return start_time;
    }

    public void setStart_time(Time start_time) {
        this.start_time = start_time;
    }

    public Time getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Time end_time) {
        this.end_time = end_time;
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
