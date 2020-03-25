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
    Monday("Mon", true),
    Tuesday("Tue", true),
    Wednesday("Wed", true),
    Thursday("Thu", true),
    Friday("Fri", true),
    Saturday("Sat", false),
    Sunday("Sun", false);

    private String shortName;
    private boolean used;

    Day(String shortName, boolean used) {
        this.shortName = shortName;
        this.used = used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public static int getDaysCount() {
        int count = 0;
        for (int i = 0; i < values().length; i++) {
            if (values()[i].used) count++;
        }
        return count;
    }

    public static CharSequence getShortNameOfDay(int position) {
        return values()[position].shortName;
    }

    public static List<Day> getDays() {
        List<Day> usedDays = new ArrayList<>();
        for (int i = 0; i < values().length; i++) {
            if (values()[i].used) usedDays.add(values()[i]);
        }
        return usedDays;
    }
}