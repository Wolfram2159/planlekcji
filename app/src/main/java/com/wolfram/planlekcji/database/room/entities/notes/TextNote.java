package com.wolfram.planlekcji.database.room.entities.notes;

import com.wolfram.planlekcji.adapters.tree.TreeNode;
import com.wolfram.planlekcji.database.room.entities.Subject;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * @author Wolfram
 * @date 2019-10-25
 */
@Entity(tableName = "textNotes",
        foreignKeys = @ForeignKey(
                entity = Subject.class,
                parentColumns = "id",
                childColumns = "subject_id"))
public class TextNote extends TreeNode {
    @PrimaryKey(autoGenerate = true)
    protected Integer id;

    protected int subject_id;

    protected String message;

    protected String title;

    protected Date date;

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
    public int getViewType() {
        return 1;
    }

    @Override
    public int getGridSpanCount() {
        return 1;
    }

    @Override
    public String toString() {
        return "TextNote{" +
                "id=" + id +
                ", subject_id=" + subject_id +
                ", message='" + message + '\'' +
                ", title='" + title + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public String getPath() {
        if (getParent() == null){
            return "";
        }else {
            return getParent().getPath();
        }
    }
}
