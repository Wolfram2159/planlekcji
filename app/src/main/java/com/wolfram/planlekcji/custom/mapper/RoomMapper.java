package com.wolfram.planlekcji.custom.mapper;

import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.notes.ImageNote;
import com.wolfram.planlekcji.database.room.entities.notes.TextNote;
import com.wolfram.planlekcji.ui.adapters.tree.ImageNoteNode;
import com.wolfram.planlekcji.ui.adapters.tree.SubjectNode;
import com.wolfram.planlekcji.ui.adapters.tree.TextNoteNode;

public class RoomMapper {
    private RoomMapper() {
    }

    public static SubjectNode convertSubject(Subject sourceSubject) {
        return new SubjectNode(sourceSubject);
    }

    public static Subject convertSubject(SubjectNode sourceSubjectNode) {
        return new Subject(sourceSubjectNode);
    }

    public static ImageNoteNode convertImageNote(ImageNote sourceImageNote) {
        return new ImageNoteNode(sourceImageNote);
    }

    public static ImageNote convertImageNote(ImageNoteNode sourceImageNoteNode) {
        return new ImageNote(sourceImageNoteNode);
    }

    public static TextNoteNode convertTextNote(TextNote sourceTextNote) {
        return new TextNoteNode(sourceTextNote);
    }

    public static TextNote convertTextNote(TextNoteNode sourceTextNoteNode) {
        return new TextNote(sourceTextNoteNode);
    }
}
