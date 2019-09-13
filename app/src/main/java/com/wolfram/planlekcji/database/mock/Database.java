package com.wolfram.planlekcji.database.mock;

import com.wolfram.planlekcji.database.room.entities.Event;
import com.wolfram.planlekcji.database.room.entities.Grade;

import java.util.List;

/**
 * @author Wolfram
 * @date 2019-08-03
 */
public interface Database {
    List<Event> getSubjectList();
    List<Grade> getSubjectGrade(Event s);
}
