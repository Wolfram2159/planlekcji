package com.wolfram.planlekcji.database.mock;

import com.wolfram.planlekcji.database.room.entities.event.Event;
import com.wolfram.planlekcji.database.room.entities.grade.Grade;
import com.wolfram.planlekcji.database.room.entities.Time;
import com.wolfram.planlekcji.utils.enums.Day;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Wolfram
 * @date 2019-08-03
 */
public class MockDatabase implements Database {

    private Random random = new Random();

    private final String[] subjectArray = new String[]{
            "Matematyka",
            "Fizyka",
            "J. Angielski",
            "WF"
    };

    private final Time[] startTimeArray = new Time[]{
            new Time(9, 15),
            new Time(10, 10),
            new Time(11, 0),
            new Time(12, 5)
    };

    private final Time[] endTimeArray = new Time[]{
            new Time(10, 0),
            new Time(10, 55),
            new Time(11, 45),
            new Time(12, 50)
    };

    private final String[] localizationArray = new String[]{
            "B4 - 118",
            "s. 112",
            "B4/B5 - 011",
            "s. 712"
    };

    private final Day[] dayArray = new Day[]{
            Day.Monday,
            Day.Thursday,
            Day.Wednesday,
            Day.Friday
    };

    @Override
    public List<Event> getSubjectList() {
        ArrayList<Event> subjectsList = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            int randomNumber = random.nextInt(4);


        }
        return subjectsList;
    }

    @Override
    public List<Grade> getSubjectGrade(Event s) {
        return null;
    }
}
