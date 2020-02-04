package com.wolfram.planlekcji.ui.fragments.grades;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.common.others.SnackbarUtils;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.ui.adapters.expanded.ParentGradeRecyclerViewAdapter;
import com.wolfram.planlekcji.database.room.entities.grade.GradeDisplayEntity;
import com.wolfram.planlekcji.ui.bottomSheets.ActionBottomSheet;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.bottomSheets.grades.ModifyGradeBottomSheet;
import com.wolfram.planlekcji.ui.bottomSheets.subjects.ModifySubjectBottomSheet;
import com.wolfram.planlekcji.ui.fragments.subjects.SubjectsFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GradesFragment extends Fragment {

    @BindView(R.id.grades_fab)
    FloatingActionButton fab;
    @BindView(R.id.grades_recycler)
    RecyclerView recycler;
    private GradesFragmentViewModel viewModel;
    private FragmentManager fragmentManager;

    public final static String CREATE = "CreateGrade";
    public final static String MODIFY = "ModifyGrade";

    private final String NO_SUBJECTS = "Create subject first";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grades, container, false);
        ButterKnife.bind(this, view);
        fragmentManager = getFragmentManager();
        viewModel = ViewModelProviders.of(getActivity()).get(GradesFragmentViewModel.class);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);

        ParentGradeRecyclerViewAdapter adapter = new ParentGradeRecyclerViewAdapter();

        adapter.setOnChildItemClickListener(this::showActionBottomSheet);

        ((SimpleItemAnimator) recycler.getItemAnimator()).setSupportsChangeAnimations(false);

        LiveData<List<SubjectEntity>> subjectList = viewModel.getSubjectsList();
        subjectList.observe(this, viewModel::setSubjects);
        LiveData<List<GradeDisplayEntity>> gradesList = viewModel.getGradesList();
        gradesList.observe(this, grades -> viewModel.setGrades(grades, adapter::setGradeGroups));
        recycler.setAdapter(adapter);

        viewModel.getResultState().observe(this, result -> {
            if (!result.isUsed()){
                SnackbarUtils.showSnackBar(getActivity(), result.getValue());
                viewModel.callMessageReceived();
            }
        });

        fab.setOnClickListener(v -> {
            if (viewModel.getSubjects().size() > 0) {
                ModifyGradeBottomSheet bottomSheet = new ModifyGradeBottomSheet();
                bottomSheet.show(fragmentManager, CREATE);
            } else {
                SnackbarUtils.showSnackBar(getActivity(), NO_SUBJECTS);
            }
        });
        return view;
    }

    private void showActionBottomSheet(GradeDisplayEntity clickedGrade){
        ActionBottomSheet actionBottomSheet = new ActionBottomSheet();
        actionBottomSheet.setOnActionListener(new ActionBottomSheet.OnActionListener() {
            @Override
            public void onObjectModify() {
                viewModel.setModifyingGrade(clickedGrade);
                ModifyGradeBottomSheet modifyGradeBottomSheet = new ModifyGradeBottomSheet();
                modifyGradeBottomSheet.show(fragmentManager, MODIFY);
            }

            @Override
            public void onObjectDelete() {
                viewModel.deleteGrade();
            }
        });
        actionBottomSheet.show(fragmentManager, ActionBottomSheet.TAG);
    }
}