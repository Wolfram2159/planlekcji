package com.wolfram.planlekcji.database.room.entities;

import com.wolfram.planlekcji.ui.adapters.tree.SubjectNode;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * @author Wolfram
 * @date 2019-09-09
 */
@Entity(indices = {@Index(value = "name", unique = true)}, tableName = "subjects")
public class SubjectEntity {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private String name;

    public SubjectEntity() {
    }

    public SubjectEntity(@NonNull SubjectNode subjectNode) {
        this.id = subjectNode.getId();
        this.name = subjectNode.getName();
    }

    @Ignore
    public SubjectEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Ignore
    public SubjectEntity(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
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
