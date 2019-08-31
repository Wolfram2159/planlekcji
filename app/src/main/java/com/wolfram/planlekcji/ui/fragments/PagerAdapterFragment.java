package com.wolfram.planlekcji.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.adapters.SubjectsRecyclerViewAdapter;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.ui.bottomSheets.ActionBottomSheet;
import com.wolfram.planlekcji.utils.enums.Day;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class PagerAdapterFragment extends Fragment {

    public static final String POSITION = "POSITION";

    @BindView(R.id.subjects_recycler)
    RecyclerView recycler;
    @BindView(R.id.root_subjects)
    ConstraintLayout root;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pager_adapter_fragment, container, false);

        Bundle args = getArguments();
        int pos = args.getInt(POSITION);

        ButterKnife.bind(this, view);

        SubjectsFragmentViewModel viewModel = ViewModelProviders.of(this).get(SubjectsFragmentViewModel.class);

        LayoutInflater layoutInflater = getLayoutInflater();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);

        Day day = Day.values()[pos];
        LiveData<List<Subject>> subjectList = viewModel.getSubjects(day);

        SubjectsRecyclerViewAdapter adapter = new SubjectsRecyclerViewAdapter(layoutInflater, new SubjectsRecyclerViewAdapter.RecyclerViewAdapterCallback() {
            @Override
            public void onDelete(Subject subject, int position) {
                viewModel.deleteSubject(subject);
                Snackbar snackbar = Snackbar.make(root, "You have deleted " + subject.getSubject(), Snackbar.LENGTH_LONG);

                snackbar.setAction(R.string.undo, v -> viewModel.insertSubject(subject));
                snackbar.show();
            }

            @Override
            public void onItemClick(Subject subject, int position) {
                ActionBottomSheet actionBottomSheet = new ActionBottomSheet();
                actionBottomSheet.setOnDeleteListener(() ->{
                    viewModel.deleteSubject(subject);
                });
                actionBottomSheet.setOnModifyListener((editedSubject) -> {
                    editedSubject.setSubject_id(subject.getSubject_id());
                    viewModel.updateSubject(editedSubject);
                }, subject);
                actionBottomSheet.show(getFragmentManager(), "ActionBottomSheet");
            }
        });

        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);

        subjectList.observe(this, (adapter::setSubjectsList));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

}
