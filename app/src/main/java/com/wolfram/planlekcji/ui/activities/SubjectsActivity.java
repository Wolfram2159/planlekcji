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

    /*@BindView(R.id.subjects_fab)
    FloatingActionButton fab;
    @BindView(R.id.subjects_recycler)
    RecyclerView recycler;
    @BindView(R.id.root_subjects)
    ConstraintLayout root;

    @OnClick(R.id.subjects_fab)
    void onClick(View view) {
        DialogFragment dialog = new AddingSubjectDialog(viewModel, value -> {
            Snackbar sn = Snackbar.make(root, value, Snackbar.LENGTH_LONG);
            sn.show();
        });
        dialog.show(getSupportFragmentManager(), "Dialog");
    }

    private SubjectsViewModel viewModel;*/

    @BindView(R.id.subjects_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subjects_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);


        ViewPager viewPager = findViewById(R.id.subjects_view_pager);

        SubjectsPagerAdapter subjectsPagerAdapter = new SubjectsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(subjectsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.subjects_tab_layout);
        tabLayout.setupWithViewPager(viewPager);


        /*ButterKnife.bind(this);


        viewModel = ViewModelProviders.of(this).get(SubjectsViewModel.class);

        LayoutInflater layoutInflater = getLayoutInflater();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);

        LiveData<List<Subject>> subjectList = viewModel.getSubjects();

        SubjectAdapter adapter = new SubjectAdapter(layoutInflater, viewModel, (subject, position)->{
            Snackbar snackbar = Snackbar.make(root, "You have deleted " + subject.getSubject(), Snackbar.LENGTH_LONG);

            snackbar.setAction(R.string.undo, view -> viewModel.insertSubject(subject));
            snackbar.show();
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter, this));
        itemTouchHelper.attachToRecyclerView(recycler);

        recycler.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);

        subjectList.observe(this, (adapter::setSubjectsList));
    }

    /*private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }*/
    }
}