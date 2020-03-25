package com.wolfram.planlekcji.database.room.converter;

import java.util.Date;

import androidx.room.TypeConverter;

/**
 * @author Wolfram
 * @date 2019-10-07
 */
public class DateConverter {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
