package com.wolfram.planlekcji.ui.fragments.notes;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.os.Environment;

import com.wolfram.planlekcji.common.mapper.RoomMapper;
import com.wolfram.planlekcji.database.room.AppDatabase;
import com.wolfram.planlekcji.database.room.dao.NotesDao;
import com.wolfram.planlekcji.database.room.dao.SubjectDao;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.database.room.entities.notes.ImageNoteEntity;
import com.wolfram.planlekcji.database.room.entities.notes.SubjectWithNotesEntity;
import com.wolfram.planlekcji.database.room.entities.notes.TextNoteEntity;
import com.wolfram.planlekcji.common.others.DateUtils;
import com.wolfram.planlekcji.ui.adapters.tree.DirectoryNode;
import com.wolfram.planlekcji.ui.adapters.tree.ImageNoteNode;
import com.wolfram.planlekcji.ui.adapters.tree.RootNode;
import com.wolfram.planlekcji.ui.adapters.tree.SubjectNode;
import com.wolfram.planlekcji.ui.adapters.tree.SubjectWithNoteNodes;
import com.wolfram.planlekcji.ui.adapters.tree.TextNoteNode;
import com.wolfram.planlekcji.ui.adapters.tree.TreeNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class NotesFragmentViewModel extends AndroidViewModel {

    public interface ParentSetter {

        void setParent(TreeNode parent);
    }
    private ParentSetter parentSetter;

    private NotesDao notesDao;

    private SubjectDao subjectDao;
    private LiveData<List<SubjectWithNotesEntity>> subjectWithNotesList;

    private List<SubjectWithNoteNodes> subjectsWithNotes;
    private List<SubjectEntity> subjects;
    private String currentPhotoPath;

    private ImageNoteEntity imageNote;
    private TreeNode actualParent;

    public NotesFragmentViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application.getApplicationContext());
        notesDao = appDatabase.getNotesDao();
        subjectDao = appDatabase.getSubjectDao();
        subjectWithNotesList = notesDao.getSubjectsWithNotes();
        actualParent = new RootNode();
        subjects = new ArrayList<>();
        //todo: check if parentSetter is null or create mock to avoid this situation?
    }

    public void setParentSetter(@NonNull ParentSetter parentSetter) {
        this.parentSetter = parentSetter;
    }

    public String getCurrentPhotoPath() {
        return currentPhotoPath;
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
        createSubjects(subjectsWithNotes);
    }

    private void createSubjects(List<SubjectWithNotesEntity> subjectsWithNotes) {
        subjects.clear();
        for (SubjectWithNotesEntity subjectsWithNote : subjectsWithNotes) {
            SubjectEntity subject = subjectsWithNote.getSubject();
            subjects.add(subject);
        }
    }

    public List<SubjectEntity> getSubjects() {
        return subjects;
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

    public void setActualParent(TreeNode actualParent) {
        this.actualParent = actualParent;
    }

    public TreeNode getActualParent() {
        return this.actualParent;
    }

    @SuppressLint("SimpleDateFormat")
    public File createImageFile() throws IOException {
        // Create an image file name
        Date currentDate = new Date();
        String timeStamp = DateUtils.getTimeStringForSaveFile(currentDate);
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getApplication().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix, png files saving much more, saving time there */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void insertImageNote(ImageNoteEntity imageNote) {
        AsyncTask.execute(() -> notesDao.insertImageNote(imageNote));
    }

    public void insertTextNote(TextNoteEntity textNote) {
        AsyncTask.execute(() -> notesDao.insertTextNote(textNote));
    }

    public void deleteImage() {
        // TODO: 2019-10-24 check if the image is deleted
        File imageToDelete = new File(currentPhotoPath);
        imageToDelete.delete();
    }

    public void deleteTextNote(TextNoteEntity note) {
        AsyncTask.execute(() -> notesDao.deleteTextNote(note));
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

    public SubjectEntity getSubjectById(int id) {
        for (SubjectEntity subject : subjects) {
            int subjectId = subject.getId();
            if (id == subjectId) return subject;
        }
        return null;
    }
}
