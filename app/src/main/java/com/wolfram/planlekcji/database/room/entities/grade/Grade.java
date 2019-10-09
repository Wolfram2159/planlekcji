package com.wolfram.planlekcji.database.room.entities.grade;

import com.wolfram.planlekcji.database.room.entities.Subject;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * @author Wolfram
 * @date 2019-09-11
 */
@Entity(tableName = "grades",
        foreignKeys = @ForeignKey(
                entity = Subject.class,
                parentColumns = "id",
                childColumns = "subject_id"))
public class Grade {
    @PrimaryKey(autoGenerate = true)
    protected Integer id;

    protected int subject_id;

    protected String description;

    protected Date date;

    public Grade() {
    }

    @Ignore
    public Grade(int subject_id, String description) {
        this.subject_id = subject_id;
        this.description = description;
    }
    @Ignore
    public Grade(Grade grade){
        this.id = grade.id;
        this.subject_id = grade.subject_id;
        this.description = grade.description;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", subject_id=" + subject_id +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Integer getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(Integer subject_id) {
        this.subject_id = subject_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
