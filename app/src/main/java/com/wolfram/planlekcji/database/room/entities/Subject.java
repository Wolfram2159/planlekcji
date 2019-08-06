package com.wolfram.planlekcji.database.room.entities;

import com.wolfram.planlekcji.utils.enums.Day;

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
    private Integer subject_id;

    private String subject;

    @Embedded(prefix = "start_")
    private Time start_time;

    @Embedded(prefix = "end_")
    private Time end_time;

    private String localization; //make second table in relation with this or not ?

    //private String additional_info; //consider this field

    private String day;

    //private String type;

    public Subject(){

    }

    public Subject(String subject, Time start_time, Time end_time, String localization, Day day) {
        this.subject = subject;
        this.start_time = start_time;
        this.end_time = end_time;
        this.localization = localization;
        this.day = day.toString();
    }

    @Override
    public String toString() {
        return "Subject{" +
                "subject_id=" + subject_id +
                ", subject='" + subject + '\'' +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", localization='" + localization + '\'' +
                ", day='" + day + '\'' +
                '}';
    }

    public String getTimeString(){
        return start_time.toString() + " - " + end_time.toString();
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setDay(Day day) {
        this.day = day.toString();
    }
}
