package com.wolfram.planlekcji.ui.fragments.grades;

import android.app.Application;
import android.os.AsyncTask;

import com.wolfram.planlekcji.common.data.Event;
import com.wolfram.planlekcji.database.room.AppDatabase;
import com.wolfram.planlekcji.database.room.UserDao;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.database.room.entities.grade.GradeDisplayEntity;
import com.wolfram.planlekcji.database.room.entities.grade.GradeEntity;
import com.wolfram.planlekcji.database.room.entities.grade.GradeGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

/**
 * @author Wolfram
 * @date 2019-09-21
 */
public class GradesFragmentViewModel extends AndroidViewModel {

    public interface GradeGroupSetter {

        void setGradeGroups(List<GradeGroup> gradeGroups);
    }

    private GradeDisplayEntity modifyingGrade;

    private UserDao dao;
    private LiveData<List<SubjectEntity>> subjectsList;

    private List<SubjectEntity> subjects;
    private LiveData<List<GradeDisplayEntity>> gradesList;

    private List<GradeDisplayEntity> grades;
    private List<GradeGroup> gradeGroups;

    private LiveData<Event<String>> resultState;
    private MediatorLiveData<Event<String>> privateResultState;
    private Event<String> event;

    private final String CREATED = "Grade created";
    private final String UPDATED = "Grade updated";
    private final String DELETED = "Grade deleted";

    public GradesFragmentViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application.getApplicationContext()).getUserDao();
        subjectsList = dao.getSubjects();
        gradesList = dao.getGrades();
        this.gradeGroups = new ArrayList<>();
        privateResultState = new MediatorLiveData<>();
        resultState = privateResultState;
        event = new Event<>();

        subjects = new ArrayList<>();
        grades = new ArrayList<>();
    }

    public void setModifyingGrade(@Nullable GradeDisplayEntity modifyingGrade) {
        this.modifyingGrade = modifyingGrade;
    }

    public GradeDisplayEntity getModifyingGrade() {
        return modifyingGrade;
    }

    public void setSubjects(List<SubjectEntity> subjects) {
        this.gradeGroups.clear();
        this.subjects = subjects;
        createGradeGroups();
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

    public List<SubjectEntity> getSubjects() {
        return subjects;
    }

    private void createGradeGroups() {
        for (SubjectEntity subject : subjects) {
            GradeGroup group = new GradeGroup(subject.getName());
            gradeGroups.add(group);
        }
    }

    public void setGrades(@NonNull List<GradeDisplayEntity> grades, GradeGroupSetter gradeGroupSetter) {
        this.grades = grades;
        clearGroups();
        addGradesToGroups();
        gradeGroupSetter.setGradeGroups(gradeGroups);
    }

    private void clearGroups() {
        for (GradeGroup gradeGroup : gradeGroups) {
            gradeGroup.clearGroup();
        }
    }

    private void addGradesToGroups() {
        for (GradeDisplayEntity grade : grades) {
            String name = grade.getName();
            for (GradeGroup gradeGroup : gradeGroups) {
                String title = gradeGroup.getTitle();
                if (name.equals(title)) gradeGroup.addGrade(grade);
            }
        }
    }

    public LiveData<List<SubjectEntity>> getSubjectsList() {
        return subjectsList;
    }

    public LiveData<List<GradeDisplayEntity>> getGradesList() {
        return gradesList;
    }

    public void deleteGrade() {
        AsyncTask.execute(() -> {
            dao.deleteGrade(modifyingGrade);
            event.setUsed(false);
            event.setValue(DELETED);
            setState(event);
        });
    }

    public void modifyGrade(GradeEntity grade, String tag) {
        event.setUsed(false);
        if (tag.equals(GradesFragment.CREATE)) {
            AsyncTask.execute(() -> {
                dao.insertGrade(grade);
                event.setValue(CREATED);
                setState(event);
            });
        } else if (tag.equals(GradesFragment.MODIFY)) {
            AsyncTask.execute(() -> {
                dao.updateGrade(grade);
                event.setValue(UPDATED);
                setState(event);
            });
        }
    }
}
