package com.wolfram.planlekcji.database.room.entities.notes;

/**
 * @author Wolfram
 * @date 2019-10-12
 */
public class NoteDisplay extends Note {
    private String name;

    public NoteDisplay() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
