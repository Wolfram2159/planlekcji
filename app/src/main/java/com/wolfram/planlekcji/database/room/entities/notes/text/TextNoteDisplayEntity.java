package com.wolfram.planlekcji.database.room.entities.notes.text;

import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
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

    public void setSubject(SubjectEntity subject) {
        this.subject_id = subject.getId();
        this.name = subject.getName();
    }

    public SubjectEntity getSubject() {
        return new SubjectEntity(this.subject_id, this.name);
    }
}
