package com.wolfram.planlekcji.ui.fragments.subjects;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.ui.adapters.SubjectsRecyclerViewAdapter;
import com.wolfram.planlekcji.database.room.entities.Subject;

import java.util.List;

import androidx.fragment.app.Fragment;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subjects, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);

        SubjectsRecyclerViewAdapter adapter = new SubjectsRecyclerViewAdapter(getLayoutInflater());
        recycler.setAdapter(adapter);

        SubjectsFragmentViewModel viewModel = ViewModelProviders.of(getActivity()).get(SubjectsFragmentViewModel.class);
        LiveData<List<Subject>> subjectsList = viewModel.getSubjects();
        subjectsList.observe(this, adapter::setSubjectList);

        recycler.setItemAnimator(new DefaultItemAnimator());

        return view;
    }
}
