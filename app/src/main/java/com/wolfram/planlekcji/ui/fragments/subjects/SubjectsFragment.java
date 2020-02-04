package com.wolfram.planlekcji.ui.fragments.subjects;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.common.data.Event;
import com.wolfram.planlekcji.common.others.SnackbarUtils;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.ui.adapters.SubjectsRecyclerViewAdapter;
import com.wolfram.planlekcji.ui.bottomSheets.ActionBottomSheet;
import com.wolfram.planlekcji.ui.bottomSheets.subjects.ModifySubjectBottomSheet;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubjectsFragment extends Fragment {

    @BindView(R.id.subjects_recycler)
    RecyclerView recycler;
    @BindView(R.id.subjects_fab)
    FloatingActionButton fab;
    private SubjectsFragmentViewModel viewModel;
    private FragmentManager fragmentManager;
    public final static String CREATE = "CreateSubject";
    public final static String MODIFY = "ModifySubject";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subjects, container, false);
        ButterKnife.bind(this, view);

        fragmentManager = Objects.requireNonNull(getFragmentManager());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);

        SubjectsRecyclerViewAdapter adapter = new SubjectsRecyclerViewAdapter(getLayoutInflater());
        recycler.setAdapter(adapter);

        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SubjectsFragmentViewModel.class);
        LiveData<List<SubjectEntity>> subjects = viewModel.getSubjects();
        subjects.observe(this, subjectList -> {
            adapter.setSubjectList(subjectList);
            viewModel.setSubjectList(subjectList);
        });
        adapter.setOnItemClickListener(this::showActionBottomSheet);

        recycler.setItemAnimator(new DefaultItemAnimator());

        fab.setOnClickListener(v -> {
            ModifySubjectBottomSheet modifySubject = new ModifySubjectBottomSheet();
            modifySubject.show(getFragmentManager(), CREATE);
        });

        LiveData<Event<String>> resultState = viewModel.getResultState();
        resultState.observe(this, event -> {
            if (!event.isUsed()) {
                SnackbarUtils.showSnackBar(getActivity(), event.getValue());
                viewModel.callMessageReceived();
            }
        });
        return view;
    }

    private void showActionBottomSheet(SubjectEntity clickedSubject) {
        ActionBottomSheet actionBottomSheet = new ActionBottomSheet();
        actionBottomSheet.setOnActionListener(new ActionBottomSheet.OnActionListener() {
            @Override
            public void onObjectModify() {
                viewModel.setModifyingSubject(clickedSubject);
                ModifySubjectBottomSheet modifySubjectBottomSheet = new ModifySubjectBottomSheet();
                modifySubjectBottomSheet.show(fragmentManager, MODIFY);
            }

            @Override
            public void onObjectDelete() {
                viewModel.deleteSubject(clickedSubject);
            }
        });
        actionBottomSheet.show(fragmentManager, ActionBottomSheet.TAG);
    }
}
