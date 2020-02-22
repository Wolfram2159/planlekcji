package com.wolfram.planlekcji.ui.fragments.grades;

import android.app.Application;
import android.os.AsyncTask;

import com.wolfram.planlekcji.common.data.Event;
import com.wolfram.planlekcji.database.room.AppDatabase;
import com.wolfram.planlekcji.database.room.dao.GradeDao;
import com.wolfram.planlekcji.database.room.dao.SubjectDao;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.database.room.entities.grade.GradeDisplayEntity;
import com.wolfram.planlekcji.database.room.entities.grade.GradeEntity;
import com.wolfram.planlekcji.database.room.entities.grade.SubjectWithGrades;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

/**
 * @author Wolfram
 * @date 2019-09-21
 */
public class GradesFragmentViewModel extends AndroidViewModel {
    private GradeDao gradeDao;
    private GradeDisplayEntity modifyingGrade;

    private LiveData<List<SubjectWithGrades>> subjectsWithGrades;
    private List<SubjectWithGrades> subjectsWithGradesList;
    private List<SubjectEntity> subjects;

    private LiveData<Event<String>> resultState;
    private MediatorLiveData<Event<String>> privateResultState;
    private Event<String> event;

    private final String CREATED = "Grade created";
    private final String UPDATED = "Grade updated";
    private final String DELETED = "Grade deleted";

    public GradesFragmentViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application.getApplicationContext());
        gradeDao = appDatabase.getGradeDao();
        subjectsWithGrades = gradeDao.getSubjectsWithGrades();
        privateResultState = new MediatorLiveData<>();
        resultState = privateResultState;
        event = new Event<>();
        subjects = new ArrayList<>();
    }

    public void setModifyingGrade(@NonNull GradeEntity modifyingGrade) {
        int subjectId = modifyingGrade.getSubject_id();
        GradeDisplayEntity gradeDisplay = new GradeDisplayEntity(modifyingGrade);
        for (SubjectEntity subject : subjects) {
            if (subject.getId() == subjectId) gradeDisplay.setName(subject.getName());
        }
        this.modifyingGrade = gradeDisplay;
    }

    public GradeDisplayEntity getModifyingGrade() {
        return modifyingGrade;
    }

    public LiveData<List<SubjectWithGrades>> getSubjectsWithGrades() {
        return subjectsWithGrades;
    }

    public void setSubjectsWithGradesList(List<SubjectWithGrades> subjectsWithGradesList) {
        this.subjectsWithGradesList = subjectsWithGradesList;
        makeSubjects();
    }

    private void makeSubjects() {
        subjects.clear();
        for (SubjectWithGrades subjectWithGrades : subjectsWithGradesList) {
            SubjectEntity subject = subjectWithGrades.getSubject();
            subjects.add(subject);
        }
    }

    public List<SubjectEntity> getSubjects() {
        return subjects;
    }

    public LiveData<Event<String>> getResultState() {
        return resultState;
    }

    public void callMessageReceived() {
        event.setUsed(true);
        setState(event);
    }

    public void setState(Event<String> event) {
        privateResultState.postValue(event);
    }

    public void deleteGrade() {
        AsyncTask.execute(() -> {
            gradeDao.deleteGrade(modifyingGrade);
            event.setUsed(false);
            event.setValue(DELETED);
            setState(event);
        });
    }

    public void modifyGrade(GradeEntity grade, String tag) {
        event.setUsed(false);
        if (tag.equals(CustomBottomSheet.CREATE)) {
            AsyncTask.execute(() -> {
                gradeDao.insertGrade(grade);
                event.setValue(CREATED);
                setState(event);
            });
        } else if (tag.equals(CustomBottomSheet.MODIFY)) {
            AsyncTask.execute(() -> {
                gradeDao.updateGrade(grade);
                event.setValue(UPDATED);
                setState(event);
            });
        }
    }
}
