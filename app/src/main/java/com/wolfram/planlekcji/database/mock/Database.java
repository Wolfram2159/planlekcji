package com.wolfram.planlekcji.database.mock;

import com.wolfram.planlekcji.database.room.entities.event.Event;
import com.wolfram.planlekcji.database.room.entities.grade.Grade;

import java.util.List;

/**
 * @author Wolfram
 * @date 2019-08-03
 */
public interface Database {
    List<Event> getSubjectList();
    List<Grade> getSubjectGrade(Event s);
}
