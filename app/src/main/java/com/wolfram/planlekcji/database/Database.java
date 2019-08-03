package com.wolfram.planlekcji.database;

import com.wolfram.planlekcji.database.room.entities.Grade;
import com.wolfram.planlekcji.database.room.entities.Subject;

import java.util.List;

/**
 * @author Wolfram
 * @date 2019-08-03
 */
public interface Database {
    List<Subject> getSubjectList();
    List<Grade> getSubjectGrade(Subject s);
}
