package com.wolfram.planlekcji.ui.fragments.grades;


import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.ui.adapters.expanded.ParentGradeRecyclerViewAdapter;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.grade.GradeDisplay;
import com.wolfram.planlekcji.database.room.entities.grade.GradeGroup;
import com.wolfram.planlekcji.ui.bottomSheets.grades.ActionGradeBottomSheet;
import com.wolfram.planlekcji.ui.bottomSheets.grades.ModifyGradeBottomSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grades, container, false);
        ButterKnife.bind(this, view);

        GradesFragmentViewModel viewModel = ViewModelProviders.of(getActivity()).get(GradesFragmentViewModel.class);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);


        ParentGradeRecyclerViewAdapter adapter = new ParentGradeRecyclerViewAdapter();

        adapter.setOnChildItemClickListener(gradeDisplay -> {
            viewModel.setModifiedGrade(gradeDisplay);

            ActionGradeBottomSheet actionGradeBottomSheet = new ActionGradeBottomSheet();
            actionGradeBottomSheet.show(getFragmentManager(), "ActionGradeBottomSheet");
        });

        ((SimpleItemAnimator) recycler.getItemAnimator()).setSupportsChangeAnimations(false);
        LiveData<List<Subject>> subjectList = viewModel.getSubjects();
        subjectList.observe(this, subjects -> {
            List<GradeGroup> gradeGroups = new ArrayList<>();
            @SuppressLint("UseSparseArrays") HashMap<Integer, Integer> hashMap = new HashMap<>();
            for (int i = 0; i < subjects.size(); i++) {
                int j = i;
                Subject s = subjects.get(i);
                LiveData<List<GradeDisplay>> gradesList = viewModel.getGradesFromSubject(s.getId());
                gradesList.observe(this, grades -> {
                    if (gradeGroups.size() < subjects.size()) {
                        gradeGroups.add(new GradeGroup(s.getName(), grades));
                        hashMap.put(j, (gradeGroups.size() - 1));
                    } else {
                        int index = hashMap.get(j);
                        gradeGroups.set(index, new GradeGroup(s.getName(), grades));
                    }
                    List<GradeGroup> listForAdapter = new ArrayList<>(gradeGroups);
                    listForAdapter.sort((g1, g2) -> g1.getTitle().compareTo(g2.getTitle()));

                    adapter.setGradeGroups(listForAdapter);
                });
            }
        });
        recycler.setAdapter(adapter);
        fab.setOnClickListener(v -> {
            ModifyGradeBottomSheet bottomSheet = new ModifyGradeBottomSheet();
            bottomSheet.show(getFragmentManager(), "ModifyGrade");
        });

        return view;
    }
}