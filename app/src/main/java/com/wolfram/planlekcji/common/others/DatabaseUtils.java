package com.wolfram.planlekcji.common.others;

import com.wolfram.planlekcji.common.data.Event;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;

import java.util.List;

public final class DatabaseUtils {
    private DatabaseUtils(){}

    public static boolean checkIfDatabaseHasThisSubject(SubjectEntity subject, List<SubjectEntity> subjects) {
        String localName = subject.getName();
        for (SubjectEntity savedSubject : subjects) {
            String savedName = savedSubject.getName();
            if (savedName.equals(localName)) return true;
        }
        return false;
    }

    public static Event<SubjectEntity> getSubjectFromDatabase(SubjectEntity subject, List<SubjectEntity> subjects) {
        Event<SubjectEntity> subjectFromDatabase = new Event<>();
        subjectFromDatabase.setUsed(false);
        String localName = subject.getName();
        for (SubjectEntity savedSubject : subjects) {
            String savedName = savedSubject.getName();
            if (savedName.equals(localName)) {
                subjectFromDatabase.setValue(savedSubject);
                subjectFromDatabase.setUsed(true);
                break;
            }
        }
        return subjectFromDatabase;
    }
}
