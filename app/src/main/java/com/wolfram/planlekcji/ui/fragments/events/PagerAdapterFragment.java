package com.wolfram.planlekcji.ui.fragments.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.adapters.EventsRecyclerViewAdapter;
import com.wolfram.planlekcji.database.room.entities.EventDisplay;
import com.wolfram.planlekcji.ui.bottomSheets.ActionBottomSheet;
import com.wolfram.planlekcji.utils.enums.Day;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
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

    @BindView(R.id.events_recycler)
    RecyclerView recycler;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pager_adapter_fragment, container, false);

        Bundle args = getArguments();
        int pos = Objects.requireNonNull(args).getInt(POSITION);

        ButterKnife.bind(this, view);

        ViewPagerEventsFragmentViewModel viewModel = ViewModelProviders.of(this).get(ViewPagerEventsFragmentViewModel.class);

        LayoutInflater layoutInflater = getLayoutInflater();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);

        Day day = Day.values()[pos];
        LiveData<List<EventDisplay>> subjectList = viewModel.getEvents(day);

        EventsRecyclerViewAdapter adapter = new EventsRecyclerViewAdapter(layoutInflater, (event, position) -> {
            ActionBottomSheet actionBottomSheet = new ActionBottomSheet();
            actionBottomSheet.setOnDeleteListener(() -> {
                viewModel.deleteEvent(event);
            });
            actionBottomSheet.setOnModifyListener(event);
            actionBottomSheet.show(getFragmentManager(), "ActionBottomSheet");
        });

        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);

        subjectList.observe(this, (adapter::setEventsList));
        return view;
    }
}
