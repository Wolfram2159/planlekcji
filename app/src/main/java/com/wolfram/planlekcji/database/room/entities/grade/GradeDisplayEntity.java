package com.wolfram.planlekcji.database.room.entities.grade;

import com.wolfram.planlekcji.database.room.entities.SubjectEntity;

/**
 * @author Wolfram
 * @date 2019-09-20
 */
public class GradeDisplayEntity extends GradeEntity {
    private String name;

    public GradeDisplayEntity(){}

    public GradeDisplayEntity(GradeEntity grade){
        super(grade);
    }

    public GradeDisplayEntity(GradeDisplayEntity gradeDisplay){
        super(gradeDisplay);
        this.name = gradeDisplay.name;
    }

    @Override
    public String toString() {
        return "GradeDisplayEntity{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", subject_id=" + subject_id +
                ", description='" + description + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name.trim();
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
