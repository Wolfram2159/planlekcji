package com.wolfram.planlekcji.database.room.entities.notes;

import com.wolfram.planlekcji.adapters.tree.TreeNode;
import com.wolfram.planlekcji.database.room.entities.Subject;

import java.util.Date;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * @author Wolfram
 * @date 2019-10-12
 */
@Entity(tableName = "notes",
        foreignKeys = @ForeignKey(
                entity = Subject.class,
                parentColumns = "id",
                childColumns = "subject_id"))
public class Note extends TreeNode {
    @PrimaryKey(autoGenerate = true)
    protected Integer id;

    protected int subject_id;

    @Nullable
    protected String photoPath;

    @Nullable
    protected String filePath;

    protected Date date;

    public Note(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Nullable
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(@Nullable String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getPath() {
        if (getParent() == null){
            return "";
        }else {
            return getParent().getPath();
        }
    }
}
