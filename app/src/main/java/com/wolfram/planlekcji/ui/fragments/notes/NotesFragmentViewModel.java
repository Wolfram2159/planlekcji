package com.wolfram.planlekcji.ui.fragments.notes;

import android.app.Application;

import com.wolfram.planlekcji.database.room.AppDatabase;
import com.wolfram.planlekcji.database.room.UserDao;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class NotesFragmentViewModel extends AndroidViewModel {

    private UserDao dao;

    public NotesFragmentViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application.getApplicationContext()).getUserDao();
    }
}
