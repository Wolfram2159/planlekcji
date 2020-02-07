package com.wolfram.planlekcji.database.room.entities.notes;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.wolfram.planlekcji.common.data.DoubleGroup;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;

import java.util.List;

public class SubjectWithNotes implements DoubleGroup<TextNoteEntity, ImageNoteEntity> {

    @Embedded public SubjectEntity subject;
    @Relation(parentColumn = "id", entityColumn = "subject_id") public List<TextNoteEntity> textNotes;
    @Relation(parentColumn = "id", entityColumn = "subject_id") public List<ImageNoteEntity> imageNotes;

    @Override
    public String getTitle() {
        return subject.getName();
    }

    @Override
    public List<TextNoteEntity> getFirstList() {
        return textNotes;
    }

    @Override
    public List<ImageNoteEntity> getSecondList() {
        return imageNotes;
    }
}
