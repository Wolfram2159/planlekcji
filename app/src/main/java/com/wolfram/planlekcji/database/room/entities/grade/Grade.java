package com.wolfram.planlekcji.database.room.entities.grade;

import android.os.Parcel;
import android.os.Parcelable;

import com.wolfram.planlekcji.database.room.entities.Subject;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * @author Wolfram
 * @date 2019-09-11
 */
@Entity(tableName = "grades",
        foreignKeys = @ForeignKey(
                entity = Subject.class,
                parentColumns = "id",
                childColumns = "subject_id"))
public class Grade implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    protected Integer id;

    protected int subject_id;

    protected String description;

    public Grade() {
    }

    public Grade(int subject_id, String description) {
        this.subject_id = subject_id;
        this.description = description;
    }

    public Grade(Grade grade){
        this.id = grade.id;
        this.subject_id = grade.subject_id;
        this.description = grade.description;
    }

    protected Grade(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        subject_id = in.readInt();
        description = in.readString();
    }

    public static final Creator<Grade> CREATOR = new Creator<Grade>() {
        @Override
        public Grade createFromParcel(Parcel in) {
            return new Grade(in);
        }

        @Override
        public Grade[] newArray(int size) {
            return new Grade[size];
        }
    };

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Integer getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(Integer subject_id) {
        this.subject_id = subject_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeInt(subject_id);
        parcel.writeString(description);
    }
}
