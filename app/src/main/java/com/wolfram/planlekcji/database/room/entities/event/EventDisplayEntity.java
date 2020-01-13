package com.wolfram.planlekcji.database.room.entities.event;

/**
 * @author Wolfram
 * @date 2019-09-12
 */
public class EventDisplayEntity extends EventEntity {
    private String name;

    public EventDisplayEntity() {
    }

    @Override
    public String toString() {
        return "EventDisplayEntity{" +
                "id=" + id +
                ", subject_id=" + subject_id +
                ", name='" + name + '\'' +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", localization='" + localization + '\'' +
                ", day='" + day + '\'' +
                '}';
    }

    public EventDisplayEntity(EventDisplayEntity event) {
        super(event);
        this.name = event.name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
