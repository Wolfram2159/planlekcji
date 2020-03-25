package com.wolfram.planlekcji.database.room.converter;

import androidx.room.TypeConverter;

import com.wolfram.planlekcji.common.enums.Day;

public final class DayConverter {
    @TypeConverter
    public static String fromDay(Day day) {
        return day.toString();
    }

    @TypeConverter
    public static Day toDay(String name) {
        return Day.valueOf(name);
    }
}
