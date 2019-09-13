package com.wolfram.planlekcji.database.room.entities;

/**
 * @author Wolfram
 * @date 2019-09-12
 */
public class EventDisplay extends Event{
    private String name;

    public EventDisplay() {
    }

    @Override
    public String toString() {
        return "EventDisplay{" +
                "id=" + id +
                ", subject_id=" + subject_id +
                ", name='" + name + '\'' +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", localization='" + localization + '\'' +
                ", day='" + day + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
