package com.wolfram.planlekcji.common.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Wolfram
 * @date 2019-08-03
 */
public enum Day {
    // TODO: 2020-02-29 rework this, @TypeConverter for saving enum in db
    Monday(2),
    Tuesday,
    Wednesday,
    Thursday,
    Friday;

    private int a;
    private String shortName;

    Day() {
    }

    Day(int a) {
        this.a = a;
    }

    public static List<String> getNames() {
        Day[] values = Day.values();
        List<String> dayNames = new ArrayList<>();
        for (Day value : values) {
            dayNames.add(value.toString());
        }
        return dayNames;
    }

    public static List<Day> getDays() {
        Day[] values = Day.values();
        return Arrays.asList(values);
    }
}