package com.wolfram.planlekcji.database.room.entities.notes.text;

import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.ui.adapters.tree.TextNoteNode;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

/**
 * @author Wolfram
 * @date 2019-10-25
 */
@Entity(tableName = "textNotes",
        foreignKeys = @ForeignKey(
                entity = SubjectEntity.class,
                parentColumns = "id",
                childColumns = "subject_id",
                onDelete = CASCADE
        )
)
public class TextNoteEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "text_note_id")
    protected Integer id;

    protected int subject_id;

    protected String message;

    protected String title;

    protected Date date;

    public TextNoteEntity() {
    }

    public TextNoteEntity(@NonNull TextNoteNode textNoteNode) {
        this.id = textNoteNode.getId();
        this.subject_id = textNoteNode.getSubject_id();
        this.message = textNoteNode.getMessage();
        this.title = textNoteNode.getTitle();
        this.date = textNoteNode.getDate();
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "TextNoteEntity{" +
                "id=" + id +
                ", subject_id=" + subject_id +
                ", message='" + message + '\'' +
                ", title='" + title + '\'' +
                ", date=" + date +
                '}';
    }
}
