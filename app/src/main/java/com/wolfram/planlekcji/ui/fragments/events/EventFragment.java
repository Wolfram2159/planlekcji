package com.wolfram.planlekcji.ui.fragments.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.ui.adapters.EventsViewPagerAdapter;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.bottomSheets.events.ModifyEventBottomSheet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Wolfram
 * @date 2019-08-30
 */
public class EventFragment extends Fragment {

    @BindView(R.id.events_fab)
    FloatingActionButton fab;
    @BindView(R.id.events_view_pager)
    ViewPager viewPager;
    @BindView(R.id.events_tab_layout)
    TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, null);
        ButterKnife.bind(this, view);

        EventViewModel viewModel = ViewModelProviders.of(getActivity()).get(EventViewModel.class);
        LiveData<List<SubjectEntity>> subjects = viewModel.getObservableSubjects();
        subjects.observe(this, viewModel::setSubjects);

        fab.setOnClickListener(v -> {
            ModifyEventBottomSheet bottomSheet = new ModifyEventBottomSheet();
            bottomSheet.show(getFragmentManager(), CustomBottomSheet.CREATE);
        });

        EventsViewPagerAdapter pagerAdapter = new EventsViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}
