package com.wolfram.planlekcji.ui.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.adapters.SubjectAdapter;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.ui.dialogs.AddingSubjectDialog;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubjectsActivity extends AppCompatActivity {

    @BindView(R.id.subjects_fab)
    FloatingActionButton fab;
    @BindView(R.id.subjects_recycler)
    RecyclerView recycler;
    @BindView(R.id.root_subjects)
    ConstraintLayout root;
    @OnClick(R.id.subjects_fab)
    void onClick(View view){
        DialogFragment dialog = new AddingSubjectDialog(viewModel, value -> {
            Snackbar sn = Snackbar.make(root, value, Snackbar.LENGTH_LONG);
            sn.show();
        });
        dialog.show(getSupportFragmentManager(), "Dialog");
    }
    private SubjectsViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subjects_activity);
        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this).get(SubjectsViewModel.class);

        LayoutInflater layoutInflater = getLayoutInflater();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);

        LiveData<List<Subject>> subjectList = viewModel.getSubjects();

        subjectList.observe(this, (subjects -> {
            SubjectAdapter adapter = new SubjectAdapter(layoutInflater, subjects);
            recycler.setAdapter(adapter);
        }));
    }
}