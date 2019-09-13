package com.wolfram.planlekcji.database.room.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
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
    private Integer id;

    private int subject_id;

    private String description;

    public Grade() {
    }

    public Grade(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", description='" + description + '\'' +
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
}
