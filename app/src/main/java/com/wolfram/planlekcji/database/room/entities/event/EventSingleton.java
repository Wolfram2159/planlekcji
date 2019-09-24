package com.wolfram.planlekcji.database.room.entities.event;

import androidx.annotation.Nullable;

/**
 * @author Wolfram
 * @date 2019-09-19
 */
public class EventSingleton extends EventDisplay {
    private static EventSingleton singleton;

    public static synchronized EventSingleton getInstance(@Nullable EventDisplay event) {
        if (singleton == null) {
            singleton = new EventSingleton(event);
        }
        return singleton;
    }

    public static void setNull() {
        singleton = null;
    }

    public static boolean isNull() {
        return (singleton == null);
    }

    private EventSingleton(EventDisplay event) {
        super(event);
    }
}
