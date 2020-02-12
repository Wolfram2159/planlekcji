package com.wolfram.planlekcji.ui.adapters.tree;

import androidx.annotation.NonNull;

import com.wolfram.planlekcji.database.room.entities.SubjectEntity;

public class SubjectNode extends TreeNode {
    private Integer id;
    private String name;
    private static final int VIEW_TYPE = 4;
    private static final int GRID_SPAN_COUNT = 2;

    public SubjectNode(@NonNull SubjectEntity subject) {
        this.id = subject.getId();
        this.name = subject.getName();
        this.nodeName = subject.getName();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
