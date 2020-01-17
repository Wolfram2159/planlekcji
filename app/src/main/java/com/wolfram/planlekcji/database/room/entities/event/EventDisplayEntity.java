package com.wolfram.planlekcji.database.room.entities.event;

import com.wolfram.planlekcji.database.room.entities.SubjectEntity;

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

    public void setSubject(SubjectEntity subject){
        this.subject_id = subject.getId();
        this.name = subject.getName();
    }

    public SubjectEntity getSubject(){
        return new SubjectEntity(this.subject_id, this.name);
    }
}
