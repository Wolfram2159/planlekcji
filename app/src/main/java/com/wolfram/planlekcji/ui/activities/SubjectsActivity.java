package com.wolfram.planlekcji.ui.activities;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.adapters.SubjectsPagerAdapter;
import com.wolfram.planlekcji.ui.bottomSheets.ModifySubjectBottomSheet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SubjectsActivity extends AppCompatActivity {

    @BindView(R.id.subjects_toolbar)
    Toolbar toolbar;
    @BindView(R.id.subjects_view_pager)
    ViewPager viewPager;
    @BindView(R.id.subjects_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.subjects_fab)
    FloatingActionButton fab;
    @BindView(R.id.subject_bottom_sheet)
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subjects_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        SubjectsViewModel viewModel = ViewModelProviders.of(this).get(SubjectsViewModel.class);

        ModifySubjectBottomSheet bottomSheetDialog = new ModifySubjectBottomSheet();

        bottomSheetDialog.setModify((viewModel::insertSubject));

        fab.setOnClickListener(w -> bottomSheetDialog.show(getSupportFragmentManager(), "AddBottomSheet"));//bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));

        viewPager = findViewById(R.id.subjects_view_pager);

        SubjectsPagerAdapter subjectsPagerAdapter = new SubjectsPagerAdapter(getSupportFragmentManager(), viewModel);

        viewPager.setAdapter(subjectsPagerAdapter);

        tabLayout = findViewById(R.id.subjects_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }
}