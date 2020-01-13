package com.wolfram.planlekcji.common.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wolfram
 * @date 2019-08-03
 */
public enum Day {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday;

    public static List<String> getNames(){
        Day[] values = Day.values();
        List<String> dayNames = new ArrayList<>();
        for (Day value : values) {
            dayNames.add(value.toString());
        }
        return dayNames;
    }
    }