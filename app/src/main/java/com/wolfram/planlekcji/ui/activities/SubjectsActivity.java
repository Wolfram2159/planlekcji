package com.wolfram.planlekcji.ui.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.adapters.SubjectAdapter;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.ui.dialogs.AddingSubjectDialog;
import com.wolfram.planlekcji.ui.recyclerView.GridSpacingItemDecoration;
import com.wolfram.planlekcji.ui.recyclerView.SwipeToDeleteCallback;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
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
    void onClick(View view) {
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

        SubjectAdapter adapter = new SubjectAdapter(layoutInflater, viewModel, (subject, position)->{
            Snackbar snackbar = Snackbar.make(root, "You have deleted " + subject.getSubject(), Snackbar.LENGTH_LONG);

            snackbar.setAction(R.string.undo, view -> viewModel.insertSubject(subject));
            snackbar.show();
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter, this));
        itemTouchHelper.attachToRecyclerView(recycler);

        recycler.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);

        subjectList.observe(this, (adapter::setSubjectsList)); //todo: what is this ?
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}