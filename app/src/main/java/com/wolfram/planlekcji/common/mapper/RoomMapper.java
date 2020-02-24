package com.wolfram.planlekcji.common.mapper;

import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.database.room.entities.notes.image.ImageNoteDisplayEntity;
import com.wolfram.planlekcji.database.room.entities.notes.image.ImageNoteEntity;
import com.wolfram.planlekcji.database.room.entities.notes.SubjectWithNotesEntity;
import com.wolfram.planlekcji.database.room.entities.notes.text.TextNoteDisplayEntity;
import com.wolfram.planlekcji.database.room.entities.notes.text.TextNoteEntity;
import com.wolfram.planlekcji.ui.adapters.tree.ImageNoteNode;
import com.wolfram.planlekcji.ui.adapters.tree.SubjectNode;
import com.wolfram.planlekcji.ui.adapters.tree.SubjectWithNoteNodes;
import com.wolfram.planlekcji.ui.adapters.tree.TextNoteNode;

import java.util.ArrayList;
import java.util.List;

public class RoomMapper {
    private RoomMapper() {
    }

    public static SubjectNode convertSubject(SubjectEntity sourceSubject) {
        return new SubjectNode(sourceSubject);
    }

    public static SubjectEntity convertSubject(SubjectNode sourceSubjectNode) {
        return new SubjectEntity(sourceSubjectNode);
    }

    public static ImageNoteNode convertImageNote(ImageNoteEntity sourceImageNote) {
        return new ImageNoteNode(sourceImageNote);
    }

    public static ImageNoteDisplayEntity convertImageNote(ImageNoteNode sourceImageNoteNode) {
        return new ImageNoteDisplayEntity(sourceImageNoteNode);
    }

    public static TextNoteNode convertTextNote(TextNoteEntity sourceTextNote) {
        return new TextNoteNode(sourceTextNote);
    }

    public static TextNoteDisplayEntity convertTextNote(TextNoteNode sourceTextNoteNode) {
        return new TextNoteDisplayEntity(sourceTextNoteNode);
    }

    public static List<SubjectWithNoteNodes> convertSubjectWithNotesList(List<SubjectWithNotesEntity> subjectWithNotesList) {
        List<SubjectWithNoteNodes> subjectWithNotes = new ArrayList<>();
        for (SubjectWithNotesEntity subjectWithNotesEntity : subjectWithNotesList) {
            SubjectWithNoteNodes subject = convertSubjectWithNotes(subjectWithNotesEntity);
            subjectWithNotes.add(subject);
        }
        return subjectWithNotes;
    }

    private static SubjectWithNoteNodes convertSubjectWithNotes(SubjectWithNotesEntity subjectWithNotes) {
        return new SubjectWithNoteNodes(subjectWithNotes);
    }
}
