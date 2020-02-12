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
import com.wolfram.planlekcji.ui.adapters.tree.SubjectWithNotes;
import com.wolfram.planlekcji.ui.adapters.tree.TextNoteNode;
import com.wolfram.planlekcji.ui.adapters.tree.TreeNode;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

public class NotesFragmentViewModel extends AndroidViewModel {

    public interface ParentSetter {
        void setParent(TreeNode parent);
    }

    private NotesDao notesDao;
    private SubjectDao subjectDao;

    private LiveData<List<SubjectWithNotesEntity>> subjectWithNotesList;
    private List<SubjectWithNotes> subjectsWithNotes;

    private String currentPhotoPath;
    private Date currentDate;
    private TextNoteEntity textNote;
    private ImageNoteEntity imageNote;
    private TreeNode parentOfTree = null;
    private TreeNode actualParent;

    public NotesFragmentViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application.getApplicationContext());
        notesDao = appDatabase.getNotesDao();
        subjectDao = appDatabase.getSubjectDao();
        subjectWithNotesList = notesDao.getSubjectsWithNotes();
        actualParent = new RootNode();
    }

    public String getCurrentPhotoPath() {
        return currentPhotoPath;
    }

    public Date getCurrentDate() {
        return new Date();
    }

    public LiveData<List<SubjectWithNotesEntity>> getSubjectWithNotesList() {
        return subjectWithNotesList;
    }

    public void setSubjectsWithNotes(List<SubjectWithNotesEntity> subjectsWithNotes, ParentSetter setter) {
        this.subjectsWithNotes = RoomMapper.convertSubjectWithNotesList(subjectsWithNotes);
        TreeNode root = createTree();
        if (actualParent != null) {
            TreeNode newParent = root.getNodeFromTree(actualParent);
            actualParent = newParent;
            setter.setParent(newParent);
        } else {
            setter.setParent(root);
        }
    }

    private TreeNode createTree() {
        //create Tree always but implement searching actual parent or implement detecting changes
        TreeNode root = new RootNode();
        for (SubjectWithNotes subjectWithNotes : subjectsWithNotes) {
            SubjectNode subjectNode = subjectWithNotes.getSubject();
            List<TextNoteNode> textNotes = subjectWithNotes.getTextNodes();
            DirectoryNode documents = createDocuments(textNotes);
            subjectNode.addChildren(documents);
            List<ImageNoteNode> imageNotes = subjectWithNotes.getImageNodes();
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

    /*public interface TreeObserver {
        TreeNode getParent();

        void setParent(TreeNode parent);
    }

    public TreeNode getParentOfTree(@NonNull LifecycleOwner lifecycleOwner, TreeObserver treeObserver) {
        if (parentOfTree == null) {
            return createTree(lifecycleOwner, treeObserver);
        } else {
            return parentOfTree;
        }
    }

    private TreeNode createTree(@NonNull LifecycleOwner lifecycleOwner, TreeObserver treeObserver) {
        parentOfTree = new RootNode();
        getSubjects().observe(lifecycleOwner, subjects -> {
            for (SubjectEntity subject : subjects) {
                SubjectNode subjectNode = RoomMapper.convertSubject(subject);
                TreeNode pictures = new DirectoryNode();
                TreeNode documents = new DirectoryNode();
                subjectNode.addChildren(pictures, "Pictures");
                subjectNode.addChildren(documents, "Documents");
                parentOfTree.addChildren(subjectNode, subjectNode.getName());

                getImageNotesFromSubject(subjectNode.getId()).observe(lifecycleOwner, imageNotes -> {
                    TreeNode actualRoot = treeObserver.getParent();
                    pictures.clearChildrens();
                    for (ImageNoteEntity imageNote : imageNotes) {
                        ImageNoteNode imageNoteNode = RoomMapper.convertImageNote(imageNote);
                        pictures.addChildren(imageNoteNode, "Photo");
                    }
                    treeObserver.setParent(actualRoot);
                });

                getTextNotesFromSubject(subjectNode.getId()).observe(lifecycleOwner, textNotes -> {
                    TreeNode actualRoot = treeObserver.getParent();
                    documents.clearChildrens();
                    for (TextNoteEntity textNote : textNotes) {
                        TextNoteNode textNoteNode = RoomMapper.convertTextNote(textNote);
                        documents.addChildren(textNoteNode, "File");
                    }
                    treeObserver.setParent(actualRoot);
                });
            }
            treeObserver.setParent(parentOfTree);
        });
        return parentOfTree;
    }*/

    @SuppressLint("SimpleDateFormat")
    public File createImageFile() throws IOException {
        // Create an image file name
        currentDate = new Date();
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

    public LiveData<List<SubjectEntity>> getSubjects() {
        return subjectDao.getSubjects();
    }

    public LiveData<List<ImageNoteEntity>> getImageNotesFromSubject(int subject_id) {
        return notesDao.getImageNotesFromSubject(subject_id);
    }

    public LiveData<List<TextNoteEntity>> getTextNotesFromSubject(int subject_id) {
        return notesDao.getTextNotesFromSubject(subject_id);
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

    public void setTextNote(TextNoteEntity note) {
        this.textNote = note;
    }

    public TextNoteEntity getTextNote() {
        return this.textNote;
    }

    public ImageNoteEntity getImageNote() {
        return imageNote;
    }

    public void setImageNote(ImageNoteEntity imageNote) {
        this.imageNote = imageNote;
    }
}
