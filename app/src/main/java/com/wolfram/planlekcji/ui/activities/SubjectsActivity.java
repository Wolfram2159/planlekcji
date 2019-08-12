package com.wolfram.planlekcji.ui.activities;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.adapters.SubjectsPagerAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subjects_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.subjects_view_pager);

        SubjectsPagerAdapter subjectsPagerAdapter = new SubjectsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(subjectsPagerAdapter);

        tabLayout = findViewById(R.id.subjects_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }
}