package com.wolfram.planlekcji.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.adapters.SubjectsViewPagerAdapter;
import com.wolfram.planlekcji.ui.bottomSheets.ModifySubjectBottomSheet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Wolfram
 * @date 2019-08-30
 */
public class SubjectsFragment extends Fragment {

    @BindView(R.id.subjects_fab)
    FloatingActionButton fab;
    @BindView(R.id.subjects_view_pager)
    ViewPager viewPager;
    @BindView(R.id.subjects_tab_layout)
    TabLayout tabLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subjects, null);
        ButterKnife.bind(this, view);

        SubjectsFragmentViewModel viewModel = ViewModelProviders.of(this).get(SubjectsFragmentViewModel.class);

        ModifySubjectBottomSheet bottomSheet = new ModifySubjectBottomSheet();

        bottomSheet.setModify(viewModel::insertSubject);

        fab.setOnClickListener(v -> bottomSheet.show(getFragmentManager(), "AddSubject"));

        SubjectsViewPagerAdapter pagerAdapter = new SubjectsViewPagerAdapter(getFragmentManager());

        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}
