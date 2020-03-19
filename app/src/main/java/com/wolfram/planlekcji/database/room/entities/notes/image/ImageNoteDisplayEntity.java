package com.wolfram.planlekcji.database.room.entities.notes.image;

import androidx.annotation.NonNull;

import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.ui.adapters.tree.ImageNoteNode;

/**
 * @author Wolfram
 * @date 2019-10-12
 */
public class ImageNoteDisplayEntity extends ImageNoteEntity {
    private String name;

    public ImageNoteDisplayEntity() {
    }

    public ImageNoteDisplayEntity(@NonNull ImageNoteNode imageNoteNode) {
        super(imageNoteNode);
        this.name = imageNoteNode.getSubjectName();
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
