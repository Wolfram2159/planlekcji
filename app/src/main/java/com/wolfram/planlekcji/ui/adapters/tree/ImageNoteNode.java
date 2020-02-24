package com.wolfram.planlekcji.ui.adapters.tree;

import androidx.annotation.NonNull;

import com.wolfram.planlekcji.database.room.entities.notes.image.ImageNoteEntity;

import java.util.Date;

public class ImageNoteNode extends TreeNode {
    private Integer id;
    private int subject_id;
    private String photoPath;
    private Date date;
    private static final int VIEW_TYPE = 3;
    private static final int GRID_SPAN_COUNT = 2;

    public ImageNoteNode(@NonNull ImageNoteEntity sourceImageNote) {
        this.id = sourceImageNote.getId();
        this.subject_id = sourceImageNote.getSubject_id();
        this.photoPath = sourceImageNote.getPhotoPath();
        this.date = sourceImageNote.getDate();
    }

    public String getSubjectName() {
        TreeNode parent = getParent();
        while(!parent.isSubjectNode()) {
            parent = parent.getParent();
        }
        return parent.getNodeName();
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
