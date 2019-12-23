package com.wolfram.planlekcji.database.room.entities.event;

import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.custom.enums.Day;
import com.wolfram.planlekcji.custom.others.Utils;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * @author Wolfram
 * @date 2019-09-11
 */
@Entity(tableName = "events",
        foreignKeys = @ForeignKey(
                entity = Subject.class,
                parentColumns = "id",
                childColumns = "subject_id"
        ))
public class Event {
    @PrimaryKey(autoGenerate = true)
    protected Integer id;

    protected Long subject_id;

    protected Date start_time;

    protected Date end_time;

    protected String localization;

    protected String day;

    public Event() {

    }

    @Ignore
    public Event(Event event){
        this.id = event.id;
        this.subject_id = event.subject_id;
        this.start_time = event.start_time;
        this.end_time = event.end_time;
        this.localization = event.localization;
        this.day = event.day;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", subject_id=" + subject_id +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", localization='" + localization + '\'' +
                ", day='" + day + '\'' +
                '}';
    }

    public String getTimeString(){
        String startTime = Utils.getTimeString(getStart_time());
        String endTime = Utils.getTimeString(getEnd_time());
        return startTime + " - " + endTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(Long subject_id) {
        this.subject_id = subject_id;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
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
