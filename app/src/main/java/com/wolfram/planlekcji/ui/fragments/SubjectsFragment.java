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
import com.wolfram.planlekcji.ui.dialogs.ActionDialog;
import com.wolfram.planlekcji.ui.dialogs.addingSubject.AddingSubjectDialog;
import com.wolfram.planlekcji.utils.enums.Day;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class SubjectsFragment extends Fragment {

    public static final String POSITION = "POSITION";
    private SubjectsViewModel viewModel;

    public SubjectsFragment(SubjectsViewModel viewModel) {
        this.viewModel = viewModel;
    }

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

        fab.setOnClickListener(w -> {
            DialogFragment dialog = new AddingSubjectDialog((subject, textValue) -> {
                viewModel.insertSubject(subject);
                Snackbar sn = Snackbar.make(root, textValue, Snackbar.LENGTH_LONG);
                sn.show();
            });
            dialog.show(getFragmentManager(), "AddingDialog");
        });

        LayoutInflater layoutInflater = getLayoutInflater();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);

        Day day = Day.values()[pos];
        LiveData<List<Subject>> subjectList = viewModel.getSubjects(day);

        SubjectAdapter adapter = new SubjectAdapter(layoutInflater, new SubjectAdapter.RecyclerViewAdapterCallback() {
            @Override
            public void onDelete(Subject subject, int position) {
                viewModel.deleteSubject(subject);
                Snackbar snackbar = Snackbar.make(root, "You have deleted " + subject.getSubject(), Snackbar.LENGTH_LONG);

                snackbar.setAction(R.string.undo, v -> viewModel.insertSubject(subject));
                snackbar.show();
            }

            @Override
            public void onItemClick(Subject subject, int position) {
                DialogFragment dialogFragment = new ActionDialog(new ActionDialog.ActionDialogCallback() {
                    @Override
                    public void onDelete() {
                        viewModel.deleteSubject(subject);
                    }

                    @Override
                    public void onUpdate(Subject s, String textValue) {
                        s.setSubject_id(subject.getSubject_id());
                        viewModel.updateSubject(s);

                        Snackbar snackbar = Snackbar.make(root, "You have updated " + s.getSubject(), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
                dialogFragment.show(getFragmentManager(), "ActionDialog");
            }
        });

        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);

        subjectList.observe(this, (adapter::setSubjectsList));
    }

}
