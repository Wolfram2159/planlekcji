package com.wolfram.planlekcji.database.room.entities.grade;

/**
 * @author Wolfram
 * @date 2019-09-20
 */
public class GradeDisplay extends Grade {
    private String name;

    public GradeDisplay(){

    }

    @Override
    public String toString() {
        return "GradeDisplay{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", subject_id=" + subject_id +
                ", description='" + description + '\'' +
                '}';
    }

    public GradeDisplay(GradeDisplay grade){
        super(grade);
        this.name = grade.name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}