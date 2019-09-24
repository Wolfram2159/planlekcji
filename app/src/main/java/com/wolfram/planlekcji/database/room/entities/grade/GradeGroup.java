package com.wolfram.planlekcji.database.room.entities.grade;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * @author Wolfram
 * @date 2019-09-21
 */
public class GradeGroup extends ExpandableGroup<Grade> {
    public GradeGroup(String title, List<Grade> items) {
        super(title, items);
    }
}
