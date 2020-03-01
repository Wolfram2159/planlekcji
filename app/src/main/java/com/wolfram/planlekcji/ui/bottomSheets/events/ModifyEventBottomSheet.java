package com.wolfram.planlekcji.ui.bottomSheets.events;

import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.common.data.Event;
import com.wolfram.planlekcji.common.enums.TextViewType;
import com.wolfram.planlekcji.common.utility.UiUtils;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.database.room.entities.event.EventDisplayEntity;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.fragments.events.EventViewModel;
import com.wolfram.planlekcji.common.enums.Day;
import com.wolfram.planlekcji.common.utility.DateUtils;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.ViewModelProviders;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Wolfram
 * @date 2019-08-28
 */
public class ModifyEventBottomSheet extends CustomBottomSheet implements View.OnClickListener {

    private EventViewModel viewModel;
    private EventDisplayEntity localEvent = new EventDisplayEntity();
    private Event<EventDisplayEntity> eventToSave = new Event<>();
    private List<SubjectEntity> subjects;
    private List<Day> days;
    @BindView(R.id.event_day)
    AutoCompleteTextView dayPicker;
    @BindView(R.id.event_name)
    AutoCompleteTextView subjectPicker;
    @BindView(R.id.event_time_start)
    EditText editTimeStart;
    @BindView(R.id.event_time_end)
    EditText editTimeEnd;
    @BindView(R.id.event_localization)
    EditText subjectLocalization;
    @BindView(R.id.event_save)
    MaterialButton saveButton;
    @BindView(R.id.event_cancel)
    MaterialButton cancelButton;

    @Override
    protected int getResource() {
        return R.layout.events_bottom_sheet;
    }

    @Override
    protected void customizeDialog() {
        viewModel = ViewModelProviders.of(activity).get(EventViewModel.class);
        ButterKnife.bind(this, root);
        subjects = viewModel.getSubjects();
        days = Day.getDays();

        setupAdapters();
        setupOnClickListeners();

        if (tag.equals(CustomBottomSheet.MODIFY)) {
            EventDisplayEntity modifyingEvent = viewModel.getModifyingEvent();
            setValuesToViews(modifyingEvent);
            setValuesToLocalEvent(modifyingEvent);
        } else {
            setInitialValuesToLocalEvent();
        }
    }

    private void setupAdapters() {
        UiUtils.setAdapterToTextView(dayPicker, days, TextViewType.DayPicker, tag, this::onDayItemClick);
        UiUtils.setAdapterToTextView(subjectPicker, subjects, TextViewType.SubjectPicker, tag);
    }

    private void onDayItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Adapter adapter = adapterView.getAdapter();
        Object clickedItem = adapter.getItem(i);
        if (clickedItem instanceof Day) {
            localEvent.setDay((Day) clickedItem);
        }
    }

    private void setupOnClickListeners() {
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        editTimeStart.setOnClickListener(this);
        editTimeEnd.setOnClickListener(this);
    }

    private void setValuesToViews(EventDisplayEntity modifyingEvent) {
        subjectPicker.setText(modifyingEvent.getName(), false);

        String startTime = DateUtils.getTimeString(modifyingEvent.getStart_time());
        editTimeStart.setText(startTime);

        String endTime = DateUtils.getTimeString(modifyingEvent.getEnd_time());
        editTimeEnd.setText(endTime);

        subjectLocalization.setText(modifyingEvent.getLocalization());

        int position = Day.valueOf(modifyingEvent.getDay()).ordinal();
        dayPicker.setText(dayPicker.getAdapter().getItem(position).toString(), false);
    }

    private void setValuesToLocalEvent(EventDisplayEntity modifyingEvent) {
        localEvent.setId(modifyingEvent.getId());
        localEvent.setStart_time(modifyingEvent.getStart_time());
        localEvent.setEnd_time(modifyingEvent.getEnd_time());
        localEvent.setSubject(modifyingEvent.getSubject());
        eventToSave.setUsed(true);
    }

    private void setInitialValuesToLocalEvent() {
        Date now = new Date();
        localEvent.setStart_time(now);
        localEvent.setEnd_time(now);
        localEvent.setDay(Day.Monday);
        if (subjects.size() > 0) {
            SubjectEntity firstSubject = subjects.get(0);
            localEvent.setSubject(firstSubject);
            eventToSave.setUsed(true);
        } else {
            eventToSave.setUsed(false);
        }
        String timeString = DateUtils.getTimeString(now);
        editTimeEnd.setText(timeString);
        editTimeStart.setText(timeString);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.event_time_start:
                createTimePicker(timeStart -> {
                    String tStart = DateUtils.getTimeString(timeStart);
                    editTimeStart.setText(tStart);
                    localEvent.setStart_time(timeStart);
                });
                break;
            case R.id.event_time_end:
                createTimePicker(timeEnd -> {
                    String tEnd = DateUtils.getTimeString(timeEnd);
                    editTimeEnd.setText(tEnd);
                    localEvent.setEnd_time(timeEnd);
                });
                break;
            case R.id.event_save:
                setDataToLocalEvent();
                viewModel.modifyEvent(eventToSave, tag);
                dismiss();
                break;
            case R.id.event_cancel:
                dismiss();
                break;
        }
    }

    private void setDataToLocalEvent() {
        String subjectName = subjectPicker.getText().toString();
        Day day = Day.valueOf(dayPicker.getText().toString());
        String localization = subjectLocalization.getText().toString();

        localEvent.setName(subjectName);
        localEvent.setDay(day);
        localEvent.setLocalization(localization);

        eventToSave.setValue(localEvent);
    }
}
