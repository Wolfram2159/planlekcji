package com.wolfram.planlekcji.database.room.entities.notes.text;

import com.wolfram.planlekcji.database.room.entities.notes.text.TextNoteEntity;
import com.wolfram.planlekcji.ui.adapters.tree.TextNoteNode;

/**
 * @author Wolfram
 * @date 2019-10-25
 */
public class TextNoteDisplayEntity extends TextNoteEntity {
    private String name;

    public TextNoteDisplayEntity() {
    }

    public TextNoteDisplayEntity(TextNoteNode textNoteNode) {
        super(textNoteNode);
        this.name = textNoteNode.getSubjectName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
