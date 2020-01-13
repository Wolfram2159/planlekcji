package com.wolfram.planlekcji.ui.bottomSheets.events;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.database.room.entities.event.EventDisplayEntity;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.fragments.events.EventFragment;
import com.wolfram.planlekcji.ui.fragments.events.EventViewModel;
import com.wolfram.planlekcji.common.enums.Day;
import com.wolfram.planlekcji.common.others.Utils;

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
public class ModifyEventBottomSheet extends CustomBottomSheet {

    private interface TimeSetter {
        void onTimeSet(Date time);
    }

    private interface AdapterSetter {
        AutoCompleteTextView getAdapterView();

        List<String> getList();
    }

    private EventViewModel viewModel;
    private EventDisplayEntity localEvent = new EventDisplayEntity();
    private FragmentActivity activity;
    @BindView(R.id.subject_day)
    AutoCompleteTextView dayPicker;
    @BindView(R.id.subject_name)
    AutoCompleteTextView subjectPicker;
    @BindView(R.id.subject_time_start)
    EditText editTimeStart;
    @BindView(R.id.subject_time_end)
    EditText editTimeEnd;
    @BindView(R.id.subject_localization)
    EditText subjectLocalization;
    @BindView(R.id.subject_save)
    MaterialButton saveButton;
    @BindView(R.id.subject_cancel)
    MaterialButton cancelButton;

    @Override
    protected int getResource() {
        return R.layout.events_bottom_sheet;
    }

    @Override
    protected void customizeDialog() {
        activity = Objects.requireNonNull(getActivity());
        viewModel = ViewModelProviders.of(activity).get(EventViewModel.class);
        ButterKnife.bind(this, root);

        setupAdapters();

        String bottomSheetTag = Objects.requireNonNull(getTag());
        if (bottomSheetTag.equals(EventFragment.MODIFY)) {
            EventDisplayEntity modifyingEvent = viewModel.getModifyingEvent();
            setValuesToViews(modifyingEvent);
            setValuesToLocalEvent(modifyingEvent);
        }

        editTimeStart.setOnClickListener(v -> createTimePicker(timeStart -> {
            String tStart = Utils.getTimeString(timeStart);
            editTimeStart.setText(tStart);
            localEvent.setStart_time(timeStart);
        }));

        editTimeEnd.setOnClickListener(v -> createTimePicker(timeEnd -> {
            String tEnd = Utils.getTimeString(timeEnd);
            editTimeEnd.setText(tEnd);
            localEvent.setEnd_time(timeEnd);
        }));

        saveButton.setOnClickListener(v -> {
            setDataToLocalEvent();
            viewModel.insertEvent(localEvent);
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());
    }

    private void setDataToLocalEvent() {
        String subjectName = subjectPicker.getText().toString();
        Day day = Day.valueOf(dayPicker.getText().toString());
        String localization = subjectLocalization.getText().toString();

        localEvent.setName(subjectName);
        localEvent.setDay(day);
        localEvent.setLocalization(localization);
    }

    private void setupAdapters() {
        setAdapterToView(new AdapterSetter() {
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
        setAdapterToView(new AdapterSetter() {
            @Override
            public AutoCompleteTextView getAdapterView() {
                return subjectPicker;
            }

            @Override
            public List<String> getList() {
                return subjectNames;
            }
        }, (adapterView, view, i, l) -> {
            if (i == (subjectNames.size() - 1)) {
                subjectPicker.setShowSoftInputOnFocus(true);
                subjectPicker.setText("");
                showKeyboard();
            }
        });
    }

    private void setAdapterToView(AdapterSetter adapterSetter) {
        setAdapterToView(adapterSetter, null);
    }

    private void setAdapterToView(AdapterSetter adapterSetter, AdapterView.OnItemClickListener onItemClickListener) {
        Context context = Objects.requireNonNull(getContext());
        AutoCompleteTextView textView = adapterSetter.getAdapterView();
        List<String> list = adapterSetter.getList();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_list_item_1,
                list
        );
        textView.setAdapter(arrayAdapter);
        textView.setShowSoftInputOnFocus(false);
        textView.setOnItemClickListener(onItemClickListener);
    }

    private List<String> getSubjectsNames(List<SubjectEntity> subjects) {
        final String NEW_SUBJECT = "Add new subject...";
        List<String> names = new ArrayList<>();
        for (SubjectEntity subject : subjects) {
            names.add(subject.toString());
        }
        names.add(NEW_SUBJECT);
        return names;
    }

    private void setValuesToViews(EventDisplayEntity modifyingEvent) {
        subjectPicker.setText(modifyingEvent.getName());
        String startTime = Utils.getTimeString(modifyingEvent.getStart_time());
        editTimeStart.setText(startTime);
        String endTime = Utils.getTimeString(modifyingEvent.getEnd_time());
        editTimeEnd.setText(endTime);
        subjectLocalization.setText(modifyingEvent.getLocalization());
        int position = Day.valueOf(modifyingEvent.getDay()).ordinal();
        dayPicker.setText(dayPicker.getAdapter().getItem(position).toString(), false);
    }

    private void setValuesToLocalEvent(EventDisplayEntity modifyingEvent) {
        localEvent.setId(modifyingEvent.getId());
        localEvent.setStart_time(modifyingEvent.getStart_time());
        localEvent.setEnd_time(modifyingEvent.getEnd_time());
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void createTimePicker(TimeSetter timeSetter) {
        Date dateNow = new Date();
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (timePicker, hour, minute) -> {
            Date time = new Date(2019, 10, 10, hour, minute);
            timeSetter.onTimeSet(time);
        }, dateNow.getHours(), dateNow.getMinutes(), true);
        timePickerDialog.show();
    }
}
