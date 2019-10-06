package com.wolfram.planlekcji.database.room.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * @author Wolfram
 * @date 2019-09-09
 */
@Entity(indices = {@Index(value = "id", unique = true)}, tableName = "subjects")
public class Subject {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private String name;

    public Subject() {
    }
    @Ignore
    public Subject(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    @Ignore
    public Subject(String name) {
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
