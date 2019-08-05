package com.wolfram.planlekcji.ui.activities;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.adapters.SubjectAdapter;
import com.wolfram.planlekcji.database.Database;
import com.wolfram.planlekcji.database.mock.MockDatabase;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.ui.dialogs.AddingSubjectDialog;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SubjectsActivity extends AppCompatActivity {


    private RecyclerView recycler;
    private LayoutInflater layoutInflater;
    private SubjectAdapter adapter;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subjects_activity);

        recycler = findViewById(R.id.subjects_recycler);
        layoutInflater = getLayoutInflater();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recycler.setLayoutManager(layoutManager);

        Database db = new MockDatabase();

        List<Subject> data = db.getSubjectList();

        adapter = new SubjectAdapter(layoutInflater, data);

        recycler.setAdapter(adapter);

        fab = findViewById(R.id.subjects_fab);

        DialogFragment dialog = new AddingSubjectDialog();

        fab.setOnClickListener((v)->{
            dialog.show(getSupportFragmentManager(), "Dialog");
        });

    }
}