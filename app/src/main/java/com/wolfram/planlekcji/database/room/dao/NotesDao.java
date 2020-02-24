package com.wolfram.planlekcji.database.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.wolfram.planlekcji.database.room.entities.notes.image.ImageNoteEntity;
import com.wolfram.planlekcji.database.room.entities.notes.SubjectWithNotesEntity;
import com.wolfram.planlekcji.database.room.entities.notes.text.TextNoteEntity;

import java.util.List;

@Dao
public interface NotesDao {
    @Query("SELECT * FROM subjects")
    LiveData<List<SubjectWithNotesEntity>> getSubjectsWithNotes();

    @Query("SELECT * FROM imageNotes WHERE subject_id=(:subject_id)")
    LiveData<List<ImageNoteEntity>> getImageNotesFromSubject(int subject_id);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateImageNote(ImageNoteEntity imageNote);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertImageNote(ImageNoteEntity imageNote);

    @Delete
    void deleteImageNote(ImageNoteEntity note);

    @Query("SELECT * FROM textNotes WHERE subject_id=(:subject_id)")
    LiveData<List<TextNoteEntity>> getTextNotesFromSubject(int subject_id);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTextNote(TextNoteEntity texteNote);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTextNote(TextNoteEntity textNote);

    @Delete
    void deleteTextNote(TextNoteEntity note);
}
