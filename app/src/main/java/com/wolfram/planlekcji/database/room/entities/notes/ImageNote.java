package com.wolfram.planlekcji.database.room.entities.notes;

import com.wolfram.planlekcji.ui.adapters.tree.ImageNoteNode;
import com.wolfram.planlekcji.database.room.entities.Subject;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * @author Wolfram
 * @date 2019-10-12
 */
@Entity(tableName = "imageNotes",
        foreignKeys = @ForeignKey(
                entity = Subject.class,
                parentColumns = "id",
                childColumns = "subject_id"))
public class ImageNote {
    @PrimaryKey(autoGenerate = true)
    protected Integer id;

    protected int subject_id;

    protected String photoPath;

    protected Date date;

    public ImageNote() {
    }

    public ImageNote(@NonNull ImageNoteNode subjectNode) {
        this.id = subjectNode.getId();
        this.subject_id = subjectNode.getSubject_id();
        this.photoPath = subjectNode.getPhotoPath();
        this.date = subjectNode.getDate();
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

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
