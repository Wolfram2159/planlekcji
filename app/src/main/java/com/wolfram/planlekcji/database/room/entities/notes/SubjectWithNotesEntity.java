package com.wolfram.planlekcji.database.room.entities.notes;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import java.util.List;

public class SubjectWithNotesEntity {

    @Embedded private SubjectEntity subject;
    @Relation(parentColumn = "id", entityColumn = "subject_id") private List<TextNoteEntity> textNotes;
    @Relation(parentColumn = "id", entityColumn = "subject_id") private List<ImageNoteEntity> imageNotes;

    public SubjectEntity getSubject() {
        return subject;
    }

    public List<TextNoteEntity> getTextNotes() {
        return textNotes;
    }

    public List<ImageNoteEntity> getImageNotes() {
        return imageNotes;
    }

    public void setSubject(SubjectEntity subject) {
        this.subject = subject;
    }

    public void setTextNotes(List<TextNoteEntity> textNotes) {
        this.textNotes = textNotes;
    }

    public void setImageNotes(List<ImageNoteEntity> imageNotes) {
        this.imageNotes = imageNotes;
    }
}
