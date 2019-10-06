package com.wolfram.planlekcji.ui.fragments.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.adapters.EventsRecyclerViewAdapter;
import com.wolfram.planlekcji.database.room.entities.event.EventDisplay;
import com.wolfram.planlekcji.ui.bottomSheets.events.ActionEventBottomSheet;
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

        ViewPagerEventsFragmentViewModel viewModel = ViewModelProviders.of(getActivity()).get(ViewPagerEventsFragmentViewModel.class);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);

        Day day = Day.values()[pos];
        LiveData<List<EventDisplay>> subjectList = viewModel.getEvents(day);

        EventsRecyclerViewAdapter adapter = new EventsRecyclerViewAdapter();
        adapter.setOnItemClickListener(event -> {
            viewModel.setModifiedEvent(event);

            ActionEventBottomSheet actionEventBottomSheet = new ActionEventBottomSheet();
            actionEventBottomSheet.show(getFragmentManager(), "ActionEventBottomSheet");
        });
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);

        subjectList.observe(this, (adapter::setEventsList));
        return view;
    }
}
