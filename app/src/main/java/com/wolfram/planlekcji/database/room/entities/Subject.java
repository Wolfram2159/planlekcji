package com.wolfram.planlekcji.database.room.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author Wolfram
 * @date 2019-09-09
 */
@Entity(tableName = "subjects")
public class Subject {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private String name;

    public Subject() {
    }

    public Subject(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Subject(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
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
