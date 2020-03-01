package com.wolfram.planlekcji.ui.fragments.subjects;

import android.app.Application;
import android.os.AsyncTask;

import com.wolfram.planlekcji.common.data.Event;
import com.wolfram.planlekcji.common.utility.DatabaseUtils;
import com.wolfram.planlekcji.database.room.AppDatabase;
import com.wolfram.planlekcji.database.room.dao.SubjectDao;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

/**
 * @author Wolfram
 * @date 2019-09-14
 */
public class SubjectsFragmentViewModel extends AndroidViewModel {

    private SubjectDao dao;
    private List<SubjectEntity> subjectList;
    private LiveData<List<SubjectEntity>> subjects;
    private MediatorLiveData<Event<String>> privateResultState;
    private LiveData<Event<String>> resultState;
    private SubjectEntity modifyingSubject;
    private Event<String> event;

    private final String DELETED = "Subject deleted";
    private final String CREATED = "Subject created";
    private final String EXIST = "Subject already exist";
    private final String UPDATED = "Subject updated";
    private final String MERGED = "Subjects merged";
    public static final String UNNAMED = "unnamed";
    private final int CORRECT_ROW_COUNT = 1;

    // TODO: 2020-01-03 LiveData for state
    public SubjectsFragmentViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application.getApplicationContext()).getSubjectDao();
        subjects = dao.getSubjects();
        privateResultState = new MediatorLiveData<>();
        resultState = privateResultState;
        event = new Event<>();
    }

    public LiveData<Event<String>> getResultState() {
        return resultState;
    }

    public void setSubjectList(List<SubjectEntity> subjectList) {
        this.subjectList = subjectList;
    }

    public SubjectEntity getModifyingSubject() {
        return modifyingSubject;
    }

    public void setModifyingSubject(SubjectEntity modifyingSubject) {
        this.modifyingSubject = modifyingSubject;
    }

    public LiveData<List<SubjectEntity>> getSubjects() {
        return subjects;
    }

    public void modifySubject(SubjectEntity subject, String tag){
        event.setUsed(false);
        if (subject.getName().equals("")) subject.setName(UNNAMED);
        if (tag.equals(CustomBottomSheet.MODIFY)) {
            updateSubject(subject);
        } else {
            insertSubject(subject);
        }
    }

    private void insertSubject(SubjectEntity subject) {
        if (DatabaseUtils.checkIfDatabaseHasThisSubject(subject, subjectList)) {
            event.setValue(EXIST);
            setState(event);
        } else {
            AsyncTask.execute(() -> {
                dao.insertSubject(subject);
                event.setValue(CREATED);
                setState(event);
            });
        }
    }

    private void setState(Event<String> event) {
        privateResultState.postValue(event);
    }

    private void updateSubject(SubjectEntity subject) {
        // TODO: 2020-01-01 merging others tables with merging subjects
        if (DatabaseUtils.checkIfDatabaseHasThisSubject(subject, subjectList)) {
            deleteSubjectWithoutChangingState(modifyingSubject);
            event.setValue(MERGED);
            setState(event);
        } else {
            AsyncTask.execute(() -> {
                int editedRows = dao.updateSubject(subject);
                if (editedRows == CORRECT_ROW_COUNT) {
                    event.setValue(UPDATED);
                    setState(event);
                }
            });
        }
    }

    private void deleteSubjectWithoutChangingState(SubjectEntity subject){
        AsyncTask.execute(() -> dao.deleteSubject(subject));
    }

    public void deleteSubject(SubjectEntity subject) {
        AsyncTask.execute(() -> {
            int editedRows = dao.deleteSubject(subject);
            if (editedRows == CORRECT_ROW_COUNT) {
                event.setValue(DELETED);
                setState(event);
            }
        });
    }

    public void callMessageReceived() {
        event.setUsed(true);
        setState(event);
    }
}
