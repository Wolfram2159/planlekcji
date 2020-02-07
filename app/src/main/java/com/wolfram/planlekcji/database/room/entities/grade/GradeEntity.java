package com.wolfram.planlekcji.database.room.entities.grade;

import com.wolfram.planlekcji.database.room.entities.SubjectEntity;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

/**
 * @author Wolfram
 * @date 2019-09-11
 */
@Entity(tableName = "grades",
        foreignKeys = @ForeignKey(
                entity = SubjectEntity.class,
                parentColumns = "id",
                childColumns = "subject_id",
                onDelete = CASCADE
        )
)
public class GradeEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "grade_id")
    protected Integer id;

    protected int subject_id;

    protected String description;

    protected Date date;

    public GradeEntity() {}

    @Ignore
    public GradeEntity(int subject_id, String description) {
        this.subject_id = subject_id;
        this.description = description;
    }

    @Ignore
    public GradeEntity(GradeEntity grade){
        this.id = grade.id;
        this.date = grade.date;
        this.subject_id = grade.subject_id;
        this.description = grade.description;
    }

    @Override
    public String toString() {
        return "GradeEntity{" +
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
