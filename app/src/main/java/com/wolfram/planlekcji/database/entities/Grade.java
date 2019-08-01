package com.wolfram.planlekcji.database.entities;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * @author Wolfram
 * @date 2019-07-31
 */
@Entity(tableName = "grades", foreignKeys = @ForeignKey(entity = Subject.class,
        parentColumns = "id",
        childColumns = "subject_id",
        onDelete = ForeignKey.NO_ACTION))
public class Grade {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "grade_id")
    private Integer id;

    private int subject_id;

    private String description; //Power of grade

    @Embedded
    private Date date;

    public Grade() {
    }

    public Grade(int s_id){
        this.subject_id = s_id;
        this.description = "blabla";
        this.date = new Date();
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

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public String getDescription() {
        return description;
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
