package com.wolfram.planlekcji.ui.adapters.tree;

import androidx.annotation.NonNull;

import com.wolfram.planlekcji.database.room.entities.notes.TextNoteEntity;

import java.util.Date;

public class TextNoteNode extends TreeNode {
    private Integer id;
    private int subject_id;
    private String message;
    private String title;
    private Date date;
    private static final int VIEW_TYPE = 1;
    private static final int GRID_SPAN_COUNT = 1;

    public TextNoteNode(@NonNull TextNoteEntity textNote) {
        this.id = textNote.getId();
        this.subject_id = textNote.getSubject_id();
        this.message = textNote.getMessage();
        this.title = textNote.getTitle();
        this.date = textNote.getDate();
    }

    @Override
    public int getViewType() {
        return VIEW_TYPE;
    }

    @Override
    public int getGridSpanCount() {
        return GRID_SPAN_COUNT;
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
}
