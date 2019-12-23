package com.wolfram.planlekcji.custom.mapper;

import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.notes.ImageNote;
import com.wolfram.planlekcji.database.room.entities.notes.TextNote;
import com.wolfram.planlekcji.ui.adapters.tree.ImageNoteNode;
import com.wolfram.planlekcji.ui.adapters.tree.SubjectNode;
import com.wolfram.planlekcji.ui.adapters.tree.TextNoteNode;

public class RoomMapper {
    public SubjectNode convertSubject(Subject sourceSubject) {
        return new SubjectNode(sourceSubject);
    }

    public Subject convertSubject(SubjectNode sourceSubjectNode) {
        return new Subject(sourceSubjectNode);
    }

    public ImageNoteNode convertImageNote(ImageNote sourceImageNote) {
        return new ImageNoteNode(sourceImageNote);
    }

    public ImageNote convertImageNote(ImageNoteNode sourceImageNoteNode) {
        return new ImageNote(sourceImageNoteNode);
    }

    public TextNoteNode convertTextNote(TextNote sourceTextNote) {
        return new TextNoteNode(sourceTextNote);
    }

    public TextNote convertTextNote(TextNoteNode sourceTextNoteNode) {
        return new TextNote(sourceTextNoteNode);
    }
}
