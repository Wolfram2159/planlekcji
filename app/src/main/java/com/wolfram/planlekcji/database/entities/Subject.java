package com.wolfram.planlekcji.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author Wolfram
 * @date 2019-07-31
 */
@Entity(tableName = "subjects")
public class Subject {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private Integer subject_id;

    private String subject;

    private String teacher;

    @Embedded(prefix = "start_")
    private Time start_time;

    @Embedded(prefix = "end_")
    private Time end_time;

    private String localization; //make second table in relation with this

    private String additional_info;

    private String type;

    public Subject(){

    }

    public Subject(Integer s) {
        this.subject = "Matematyka";
        this.teacher = "cwel";
        this.start_time = new Time(13,15);
        this.end_time = new Time(14,0);
        this.localization = "B4";
        this.additional_info = "cos";
        this.type = "audytoria";
    }

    @Override
    public String toString() {
        return "Subject{" +
                "subject_id=" + subject_id +
                ", subject='" + subject + '\'' +
                ", teacher='" + teacher + '\'' +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", localization='" + localization + '\'' +
                ", additional_info='" + additional_info + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public Integer getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(Integer subject_id) {
        this.subject_id = subject_id;
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
