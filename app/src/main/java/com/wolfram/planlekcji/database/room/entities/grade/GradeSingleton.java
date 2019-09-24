package com.wolfram.planlekcji.database.room.entities.grade;

import androidx.annotation.Nullable;

/**
 * @author Wolfram
 * @date 2019-09-20
 */
public class GradeSingleton extends GradeDisplay{
    private static GradeSingleton singleton;

    public static synchronized GradeSingleton getInstance(@Nullable GradeDisplay grade){
        if (singleton == null){
            singleton = new GradeSingleton(grade);
        }
        return singleton;
    }

    private GradeSingleton(GradeDisplay grade) {
        super(grade);
    }

    public static boolean isNull(){
        return (singleton == null);
    }

    public static void setNull(){
        singleton = null;
    }
}
