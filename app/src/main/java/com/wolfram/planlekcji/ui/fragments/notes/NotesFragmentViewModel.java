package com.wolfram.planlekcji.ui.fragments.notes;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Environment;

import com.wolfram.planlekcji.common.data.Event;
import com.wolfram.planlekcji.common.mapper.RoomMapper;
import com.wolfram.planlekcji.database.room.AppDatabase;
import com.wolfram.planlekcji.database.room.dao.NotesDao;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.database.room.entities.notes.image.ImageNoteEntity;
import com.wolfram.planlekcji.database.room.entities.notes.SubjectWithNotesEntity;
import com.wolfram.planlekcji.database.room.entities.notes.text.TextNoteEntity;
import com.wolfram.planlekcji.common.utility.DateUtils;
import com.wolfram.planlekcji.ui.adapters.tree.DirectoryNode;
import com.wolfram.planlekcji.ui.adapters.tree.ImageNoteNode;
import com.wolfram.planlekcji.ui.adapters.tree.RootNode;
import com.wolfram.planlekcji.ui.adapters.tree.SubjectNode;
import com.wolfram.planlekcji.ui.adapters.tree.SubjectWithNoteNodes;
import com.wolfram.planlekcji.ui.adapters.tree.TextNoteNode;
import com.wolfram.planlekcji.ui.adapters.tree.TreeNode;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class NotesFragmentViewModel extends AndroidViewModel {

    public interface ParentSetter {
        void setParent(TreeNode parent);
    }

    private ParentSetter parentSetter;
    private NotesDao notesDao;
    private List<SubjectEntity> subjects;

    private LiveData<List<SubjectWithNotesEntity>> subjectWithNotesList;

    private List<SubjectWithNoteNodes> subjectsWithNotes;
    private Event<String> textNoteEvent;
    private LiveData<Event<String>> textNoteObservableEvent;
    private MediatorLiveData<Event<String>> privateTextNoteEvent;
    private String photoPath;
    private ImageNoteEntity imageNote;

    private TreeNode actualParent;
    public static final String TEXT_NOTE_CREATED = "Text note created";

    public static final String TEXT_NOTE_UPDATED = "Text note updated";
    public static final String TEXT_NOTE_DELETED = "Text note deleted";

    public NotesFragmentViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application.getApplicationContext());
        notesDao = appDatabase.getNotesDao();
        subjectWithNotesList = notesDao.getSubjectsWithNotes();
        actualParent = new RootNode();
        subjects = new ArrayList<>();
        textNoteEvent = new Event<>();
        privateTextNoteEvent = new MediatorLiveData<>();
        textNoteObservableEvent = privateTextNoteEvent;
        //todo: check if parentSetter is null or create mock to avoid this situation?
    }

    public LiveData<Event<String>> getTextNoteEvent() {
        return textNoteObservableEvent;
    }

    public void callMessageReceived() {
        textNoteEvent.setUsed(true);
        setTextNoteState(textNoteEvent);
    }

    private void setTextNoteState(String value, boolean used) {
        textNoteEvent.setValue(value);
        textNoteEvent.setUsed(used);
        privateTextNoteEvent.postValue(textNoteEvent);
    }

    private void setTextNoteState(Event<String> event) {
        privateTextNoteEvent.postValue(event);
    }

    public void setParentSetter(@NonNull ParentSetter parentSetter) {
        this.parentSetter = parentSetter;
    }

    public LiveData<List<SubjectWithNotesEntity>> getSubjectWithNotesList() {
        return subjectWithNotesList;
    }

    public void setSubjectsWithNotes(List<SubjectWithNotesEntity> subjectsWithNotes) {
        this.subjectsWithNotes = RoomMapper.convertSubjectWithNotesList(subjectsWithNotes);
        TreeNode root = createTree();
        if (actualParent != null) {
            actualParent = root.getNodeFromTree(actualParent);
            parentSetter.setParent(actualParent);
        } else {
            parentSetter.setParent(root);
        }
        setSubjects(subjectsWithNotes);
    }

    private TreeNode createTree() {
        TreeNode root = new RootNode();
        for (SubjectWithNoteNodes subjectWithNoteNodes : subjectsWithNotes) {
            SubjectNode subjectNode = subjectWithNoteNodes.getSubject();
            List<TextNoteNode> textNotes = subjectWithNoteNodes.getTextNodes();
            DirectoryNode documents = createDocuments(textNotes);
            subjectNode.addChildren(documents);
            List<ImageNoteNode> imageNotes = subjectWithNoteNodes.getImageNodes();
            DirectoryNode pictures = createImages(imageNotes);
            subjectNode.addChildren(pictures);
            root.addChildren(subjectNode);
        }
        return root;
    }

    private DirectoryNode createDocuments(List<TextNoteNode> textNotes) {
        DirectoryNode documents = new DirectoryNode("Documents");
        for (TextNoteNode textNote : textNotes) {
            documents.addChildren(textNote);
        }
        return documents;
    }

    private DirectoryNode createImages(List<ImageNoteNode> imageNotes) {
        DirectoryNode pictures = new DirectoryNode("Pictures");
        for (ImageNoteNode imageNote : imageNotes) {
            pictures.addChildren(imageNote);
        }
        return pictures;
    }

    private void setSubjects(List<SubjectWithNotesEntity> subjectsWithNotes) {
        subjects.clear();
        for (SubjectWithNotesEntity subjectsWithNote : subjectsWithNotes) {
            SubjectEntity subject = subjectsWithNote.getSubject();
            subjects.add(subject);
        }
    }

    public List<SubjectEntity> getSubjects() {
        return subjects;
    }

    public void setActualParent(TreeNode actualParent) {
        this.actualParent = actualParent;
    }

    public TreeNode getActualParent() {
        return this.actualParent;
    }

    public String createNewPhotoString() {
        Date currentDate = new Date();
        String timeStamp = DateUtils.getTimeStringForSaveFile(currentDate);
        String photoName = "JPEG_" + timeStamp + ".jpg";
        setPhotoPath(photoName);
        return photoName;
    }

    private void setPhotoPath(String path) {
        this.photoPath = getApplication().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + path;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void modifyTextNote(TextNoteEntity textNote, String tag) {
        switch (tag) {
            case CustomBottomSheet.CREATE:
                insertTextNote(textNote);
                break;
            case CustomBottomSheet.MODIFY:
                updateTextNote(textNote);
                break;
        }
    }

    private void insertTextNote(TextNoteEntity textNote) {
        AsyncTask.execute(() -> {
            notesDao.insertTextNote(textNote);
            setTextNoteState(TEXT_NOTE_CREATED, false);
        });
    }

    private void updateTextNote(TextNoteEntity textNote) {
        AsyncTask.execute(() -> {
            notesDao.updateTextNote(textNote);
            setTextNoteState(TEXT_NOTE_UPDATED, false);
        });
    }

    public void modifyImageNote(ImageNoteEntity imageNote, String tag) {
        switch (tag) {
            case CustomBottomSheet.CREATE:
                insertImageNote(imageNote);
                break;
            case CustomBottomSheet.MODIFY:
                updateImageNote(imageNote);
                break;
        }
    }

    private void insertImageNote(ImageNoteEntity imageNote) {
        AsyncTask.execute(() -> notesDao.insertImageNote(imageNote));
    }

    private void updateImageNote(ImageNoteEntity imageNote) {
        AsyncTask.execute(() -> notesDao.updateImageNote(imageNote));
    }

    public void deleteImage(String path) {
        File imageToDelete = new File(path);
        if (imageToDelete.exists()) {
            imageToDelete.delete();
        }
    }

    public void deleteTextNote(TextNoteEntity note) {
        AsyncTask.execute(() -> {
            notesDao.deleteTextNote(note);
            setTextNoteState(TEXT_NOTE_DELETED, false);
        });
    }

    public void deleteImageNote() {
        AsyncTask.execute(() -> notesDao.deleteImageNote(imageNote));
        File imageToDelete = new File(imageNote.getPhotoPath());
        imageToDelete.delete();
    }

    public ImageNoteEntity getImageNote() {
        return imageNote;
    }

    public void setImageNote(ImageNoteEntity imageNote) {
        this.imageNote = imageNote;
    }
}
