package com.wolfram.planlekcji.ui.fragments.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.common.utility.UiUtils;
import com.wolfram.planlekcji.database.room.entities.event.EventDisplayEntity;
import com.wolfram.planlekcji.ui.adapters.EventsRecyclerViewAdapter;
import com.wolfram.planlekcji.ui.bottomSheets.ActionBottomSheet;
import com.wolfram.planlekcji.common.enums.Day;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.bottomSheets.events.ModifyEventBottomSheet;

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
    private EventViewModel viewModel;
    @BindView(R.id.events_recycler)
    RecyclerView recycler;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pager_adapter_fragment, container, false);

        Bundle args = getArguments();
        int pos = Objects.requireNonNull(args).getInt(POSITION);

        ButterKnife.bind(this, view);
        viewModel = ViewModelProviders.of(getActivity()).get(EventViewModel.class);

        viewModel.getResultState().observe(this, result -> {
            if (!result.isUsed()){
                UiUtils.showSnackBar(getActivity(), result.getValue());
                viewModel.callMessageReceived();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        Day day = Day.values()[pos];

        EventsRecyclerViewAdapter adapter = new EventsRecyclerViewAdapter();
        adapter.setOnItemClickListener(this::showActionBottomSheet);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);

        LiveData<List<EventDisplayEntity>> eventsFromDay = viewModel.getEventsFromDay(day);
        eventsFromDay.observe(this, adapter::setEventsList);
        return view;
    }

    private void showActionBottomSheet(EventDisplayEntity eventClicked) {
        ActionBottomSheet actionBottomSheet = new ActionBottomSheet();
        actionBottomSheet.setOnActionListener(new ActionBottomSheet.OnActionListener() {
            @Override
            public void onObjectModify() {
                viewModel.setModifyingEvent(eventClicked);
                ModifyEventBottomSheet modifyEventBottomSheet = new ModifyEventBottomSheet();
                modifyEventBottomSheet.show(getFragmentManager(), CustomBottomSheet.MODIFY);
            }

            @Override
            public void onObjectDelete() {
                viewModel.deleteEvent(eventClicked);
            }
        });
        actionBottomSheet.show(getFragmentManager(), CustomBottomSheet.ACTION);
    }
}
