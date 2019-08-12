package com.wolfram.planlekcji.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.adapters.SubjectAdapter;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.ui.activities.SubjectsViewModel;
import com.wolfram.planlekcji.ui.dialogs.AddingSubjectDialog;
import com.wolfram.planlekcji.ui.recyclerView.SwipeToDeleteCallback;
import com.wolfram.planlekcji.utils.enums.Day;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class SubjectsFragment extends Fragment {

    public static final String POSITION = "POSITION";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subjects, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        int pos = args.getInt(POSITION);

        RecyclerView recycler = view.findViewById(R.id.subjects_recycler);
        FloatingActionButton fab = view.findViewById(R.id.subjects_fab);
        ConstraintLayout root = view.findViewById(R.id.root_subjects);

        SubjectsViewModel viewModel = ViewModelProviders.of(this).get(SubjectsViewModel.class);

        fab.setOnClickListener(w -> {
            DialogFragment dialog = new AddingSubjectDialog(viewModel, value -> {
                Snackbar sn = Snackbar.make(root, value, Snackbar.LENGTH_LONG);
                sn.show();
            });
            dialog.show(getFragmentManager(), "Dialog");
        });

        LayoutInflater layoutInflater = getLayoutInflater();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);

        Day day = Day.values()[pos];
        LiveData<List<Subject>> subjectList = viewModel.getSubjects(day);


        SubjectAdapter adapter = new SubjectAdapter(layoutInflater, viewModel, (subject, position)->{
            Snackbar snackbar = Snackbar.make(root, "You have deleted " + subject.getSubject(), Snackbar.LENGTH_LONG);

            snackbar.setAction(R.string.undo, v -> viewModel.insertSubject(subject));
            snackbar.show();
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter, getContext()));
        itemTouchHelper.attachToRecyclerView(recycler);

        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);

        subjectList.observe(this, (adapter::setSubjectsList));
    }

}
