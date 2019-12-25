package com.wolfram.planlekcji.ui.fragments.notes;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.os.Environment;

import com.wolfram.planlekcji.custom.mapper.RoomMapper;
import com.wolfram.planlekcji.database.room.AppDatabase;
import com.wolfram.planlekcji.database.room.UserDao;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.notes.ImageNote;
import com.wolfram.planlekcji.database.room.entities.notes.TextNote;
import com.wolfram.planlekcji.custom.others.Utils;
import com.wolfram.planlekcji.ui.adapters.tree.DirectoryNode;
import com.wolfram.planlekcji.ui.adapters.tree.ImageNoteNode;
import com.wolfram.planlekcji.ui.adapters.tree.RootNode;
import com.wolfram.planlekcji.ui.adapters.tree.SubjectNode;
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

    private UserDao dao;
    private String currentPhotoPath;
    private Date currentDate;
    private TextNote textNote;
    private ImageNote imageNote;
    private TreeNode parentOfTree = null;

    public NotesFragmentViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application.getApplicationContext()).getUserDao();
    }

    public String getCurrentPhotoPath() {
        return currentPhotoPath;
    }

    public Date getCurrentDate() {
        return new Date();
    }

    public interface TreeObserver {
        TreeNode getParent();

        void setParent(TreeNode parent);
    }

    private TreeNode createTree(@NonNull LifecycleOwner lifecycleOwner, TreeObserver treeObserver) {
        parentOfTree = new RootNode();
        getSubjects().observe(lifecycleOwner, subjects -> {
            for (Subject subject : subjects) {
                SubjectNode subjectNode = RoomMapper.convertSubject(subject);
                TreeNode pictures = new DirectoryNode();
                TreeNode documents = new DirectoryNode();
                subjectNode.addChildren(pictures, "Pictures");
                subjectNode.addChildren(documents, "Documents");
                parentOfTree.addChildren(subjectNode, subjectNode.getName());

                getImageNotesFromSubject(subjectNode.getId()).observe(lifecycleOwner, imageNotes -> {
                    TreeNode actualRoot = treeObserver.getParent();
                    pictures.clearChildrens();
                    for (ImageNote imageNote : imageNotes) {
                        ImageNoteNode imageNoteNode = RoomMapper.convertImageNote(imageNote);
                        pictures.addChildren(imageNoteNode, "Photo");
                    }
                    treeObserver.setParent(actualRoot);
                });

                getTextNotesFromSubject(subjectNode.getId()).observe(lifecycleOwner, textNotes -> {
                    TreeNode actualRoot = treeObserver.getParent();
                    documents.clearChildrens();
                    for (TextNote textNote : textNotes) {
                        TextNoteNode textNoteNode = RoomMapper.convertTextNote(textNote);
                        documents.addChildren(textNoteNode, "File");
                    }
                    treeObserver.setParent(actualRoot);
                });
            }
            treeObserver.setParent(parentOfTree);
        });
        return parentOfTree;
    }

    public TreeNode getParentOfTree(@NonNull LifecycleOwner lifecycleOwner, TreeObserver treeObserver) {
        if (parentOfTree == null) {
            return createTree(lifecycleOwner, treeObserver);
        } else {
            return parentOfTree;
        }
    }

    @SuppressLint("SimpleDateFormat")
    public File createImageFile() throws IOException {
        // Create an image file name
        currentDate = new Date();
        String timeStamp = Utils.getTimeStringForSaveFile(currentDate);
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

    public void insertImageNote(ImageNote imageNote) {
        AsyncTask.execute(() -> dao.insertImageNote(imageNote));
    }

    public void insertTextNote(TextNote textNote) {
        AsyncTask.execute(() -> dao.insertTextNote(textNote));
    }

    public LiveData<List<Subject>> getSubjects() {
        return dao.getSubjects();
    }

    public LiveData<List<ImageNote>> getImageNotesFromSubject(int subject_id) {
        return dao.getImageNotesFromSubject(subject_id);
    }

    public LiveData<List<TextNote>> getTextNotesFromSubject(int subject_id) {
        return dao.getTextNotesFromSubject(subject_id);
    }

    public void deleteImage() {
        // TODO: 2019-10-24 check if the image is deleted
        File imageToDelete = new File(currentPhotoPath);
        imageToDelete.delete();
    }

    public void deleteTextNote(TextNote note) {
        AsyncTask.execute(() -> dao.deleteTextNote(note));
    }

    public void deleteImageNote() {
        AsyncTask.execute(() -> dao.deleteImageNote(imageNote));
        File imageToDelete = new File(imageNote.getPhotoPath());
        imageToDelete.delete();
    }

    public void setTextNote(TextNote note) {
        this.textNote = note;
    }

    public TextNote getTextNote() {
        return this.textNote;
    }

    public ImageNote getImageNote() {
        return imageNote;
    }

    public void setImageNote(ImageNote imageNote) {
        this.imageNote = imageNote;
    }
}
