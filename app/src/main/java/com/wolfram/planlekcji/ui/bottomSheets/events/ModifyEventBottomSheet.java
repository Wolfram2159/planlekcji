package com.wolfram.planlekcji.ui.bottomSheets.events;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.common.data.Event;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.database.room.entities.event.EventDisplayEntity;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.fragments.events.EventFragment;
import com.wolfram.planlekcji.ui.fragments.events.EventViewModel;
import com.wolfram.planlekcji.common.enums.Day;
import com.wolfram.planlekcji.common.others.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.fragment.app.FragmentActivity;
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
        setAdapterToView(new AutocompleteAdapterSetter() {
            @Override
            public AutoCompleteTextView getAdapterView() {
                return dayPicker;
            }

            @Override
            public List<String> getList() {
                return Day.getNames();
            }
        });

        List<SubjectEntity> subjects = viewModel.getSubjects();
        List<String> subjectNames = getSubjectsNames(subjects);
        setAdapterToView(
                new AutocompleteAdapterSetter() {
                    @Override
                    public AutoCompleteTextView getAdapterView() {
                        return subjectPicker;
                    }

                    @Override
                    public List<String> getList() {
                        return subjectNames;
                    }
                },
                (adapterView, view, i, l) -> {
                    if (i == (subjectNames.size() - 1)) {
                        subjectPicker.setShowSoftInputOnFocus(true);
                        subjectPicker.setText("");
                        showKeyboard();
                        eventToSave.setUsed(false);
                    } else {
                        SubjectEntity pickedSubject = subjects.get(i);
                        localEvent.setSubject(pickedSubject);
                        eventToSave.setUsed(true);
                    }
                });
    }

    private void setupOnClickListeners() {
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        editTimeStart.setOnClickListener(this);
        editTimeEnd.setOnClickListener(this);
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
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
