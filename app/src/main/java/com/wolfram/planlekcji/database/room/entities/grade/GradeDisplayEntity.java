package com.wolfram.planlekcji.database.room.entities.grade;

/**
 * @author Wolfram
 * @date 2019-09-20
 */
public class GradeDisplayEntity extends GradeEntity {
    private String name;

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
        this.name = name;
    }
}
