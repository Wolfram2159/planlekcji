package com.wolfram.planlekcji.ui.fragments.notes;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.os.Environment;

import com.wolfram.planlekcji.database.room.AppDatabase;
import com.wolfram.planlekcji.database.room.UserDao;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.notes.ImageNote;
import com.wolfram.planlekcji.database.room.entities.notes.TextNote;
import com.wolfram.planlekcji.utils.others.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class NotesFragmentViewModel extends AndroidViewModel {

    private UserDao dao;
    private String currentPhotoPath;
    private String currentFilePath;
    private Date currentDate;

    public NotesFragmentViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application.getApplicationContext()).getUserDao();
    }

    public String getCurrentPhotoPath() {
        return currentPhotoPath;
    }

    public Date getCurrentDate(){
        return currentDate;
    }

    public File createFile() throws IOException {
        // Create an image file name
        currentDate = new Date();
        String timeStamp = Utils.getTimeStringForSaveFile(currentDate);
        String imageFileName = "TXT_" + timeStamp + "_";
        File storageDir = getApplication().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File text = File.createTempFile(
                imageFileName,  /* prefix */
                ".txt",         /* suffix, png files saving much more, saving time there */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentFilePath = text.getAbsolutePath();
        return text;
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

    public void insertTextNote(TextNote textNote){
        AsyncTask.execute(() -> dao.insertTextNote(textNote));
    }

    public LiveData<List<Subject>> getSubjects() {
        return dao.getSubjects();
    }

    public LiveData<List<ImageNote>> getImageNotesFromSubject(int subject_id){
        return dao.getImageNotesFromSubject(subject_id);
    }

    public LiveData<List<TextNote>> getTextNotesFromSubject(int subject_id){
        return dao.getTextNotesFromSubject(subject_id);
    }

    public LiveData<List<ImageNote>> getNotes() {
        return dao.getImageNotes();
    }

    public void deleteImage() {
        // TODO: 2019-10-24 check if the image is deleted
        File imageToDelete = new File(currentPhotoPath);
        imageToDelete.delete();
    }
    public void deleteFile(){
        File fileToDelete = new File(currentFilePath);
        fileToDelete.delete();
    }
}
