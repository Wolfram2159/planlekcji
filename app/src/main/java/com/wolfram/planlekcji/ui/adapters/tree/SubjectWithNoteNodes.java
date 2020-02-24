package com.wolfram.planlekcji.ui.adapters.tree;

import androidx.annotation.NonNull;

import com.wolfram.planlekcji.common.mapper.RoomMapper;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.database.room.entities.notes.image.ImageNoteEntity;
import com.wolfram.planlekcji.database.room.entities.notes.SubjectWithNotesEntity;
import com.wolfram.planlekcji.database.room.entities.notes.text.TextNoteEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubjectWithNoteNodes {
    private SubjectNode subject;
    private List<TextNoteNode> textNodes;
    private List<ImageNoteNode> imageNodes;

    public SubjectWithNoteNodes(@NonNull SubjectWithNotesEntity subjectWithNotes) {
        SubjectEntity subjectEntity = subjectWithNotes.getSubject();
        assignSubject(subjectEntity);
        List<TextNoteEntity> textNotes = subjectWithNotes.getTextNotes();
        assignTextNotes(textNotes);
        List<ImageNoteEntity> imageNotes = subjectWithNotes.getImageNotes();
        assignImageNotes(imageNotes);
    }

    private void assignSubject(SubjectEntity subjectEntity) {
        this.subject = RoomMapper.convertSubject(subjectEntity);
    }

    private void assignTextNotes(List<TextNoteEntity> textNotesEntity) {
        this.textNodes = new ArrayList<>();
        for (TextNoteEntity textNote : textNotesEntity) {
            TextNoteNode textNoteNode = RoomMapper.convertTextNote(textNote);
            this.textNodes.add(textNoteNode);
        }
        sortTextNotes();
    }

    private void sortTextNotes() {
        Collections.sort(textNodes, (firstNode, secondNode) -> firstNode.getDate().compareTo(secondNode.getDate()));
    }

    private void assignImageNotes(List<ImageNoteEntity> imageNotesEntity) {
        this.imageNodes = new ArrayList<>();
        for (ImageNoteEntity imageNoteEntity : imageNotesEntity) {
            ImageNoteNode imageNoteNode = RoomMapper.convertImageNote(imageNoteEntity);
            this.imageNodes.add(imageNoteNode);
        }
        sortImageNotes();
    }

    private void sortImageNotes() {
        Collections.sort(imageNodes, (firstNode, secondNode) -> firstNode.getDate().compareTo(secondNode.getDate()));
    }

    public SubjectNode getSubject() {
        return subject;
    }

    public List<TextNoteNode> getTextNodes() {
        return textNodes;
    }

    public List<ImageNoteNode> getImageNodes() {
        return imageNodes;
    }
}
