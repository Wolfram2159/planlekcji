package com.wolfram.planlekcji.common.utility;

import com.wolfram.planlekcji.database.room.entities.SubjectEntity;

import java.util.List;
import java.util.NoSuchElementException;

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

    public static SubjectEntity getSubjectFromDatabase(SubjectEntity subject, List<SubjectEntity> subjects) throws NoSuchElementException{
        String localName = subject.getName();
        for (SubjectEntity savedSubject : subjects) {
            String savedName = savedSubject.getName();
            if (savedName.equals(localName)) {
                return savedSubject;
            }
        }
        throw new NoSuchElementException();
    }
}
