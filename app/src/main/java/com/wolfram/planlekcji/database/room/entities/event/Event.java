package com.wolfram.planlekcji.database.room.entities.event;

import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.Time;
import com.wolfram.planlekcji.utils.enums.Day;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
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

    @Embedded(prefix = "start_")
    protected Time start_time;

    @Embedded(prefix = "end_")
    protected Time end_time;

    protected String localization;

    protected String day;

    public Event() {

    }

    //copying constructor
    public Event(Event event){
        this.id = event.id;
        this.subject_id = event.subject_id;
        this.start_time = event.start_time;
        this.end_time = event.end_time;
        this.localization = event.localization;
        this.day = event.day;
    }

    public Event(Integer id, Long subject_id, Time start_time, Time end_time, String localization, String day) {
        this.id = id;
        this.subject_id = subject_id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.localization = localization;
        this.day = day;
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
        return start_time.toString() + " - " + end_time.toString();
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
