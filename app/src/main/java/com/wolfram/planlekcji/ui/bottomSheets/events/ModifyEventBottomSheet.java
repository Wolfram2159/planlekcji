package com.wolfram.planlekcji.ui.bottomSheets.events;

import android.app.TimePickerDialog;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.event.Event;
import com.wolfram.planlekcji.database.room.entities.event.EventDisplay;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.fragments.events.ViewPagerEventsFragmentViewModel;
import com.wolfram.planlekcji.utils.enums.Day;
import com.wolfram.planlekcji.utils.others.Utils;

import java.util.Date;

import androidx.lifecycle.ViewModelProviders;

/**
 * @author Wolfram
 * @date 2019-08-28
 */
public class ModifyEventBottomSheet extends CustomBottomSheet {

    private ViewPagerEventsFragmentViewModel viewModel;

    @Override
    protected int getResource() {
        return R.layout.events_bottom_sheet;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.setModifiedEvent(null);
    }

    @Override
    protected void customizeDialog() {
        viewModel = ViewModelProviders.of(getActivity()).get(ViewPagerEventsFragmentViewModel.class);

        ArrayAdapter<Day> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        android.R.layout.simple_list_item_1,
                        Day.values());

        AutoCompleteTextView dayPicker = root.findViewById(R.id.subject_day);
        dayPicker.setShowSoftInputOnFocus(false);//Doesnt show keyboard
        dayPicker.setAdapter(adapter);

        EditText subjectName = root.findViewById(R.id.subject_name);
        EditText editTimeStart = root.findViewById(R.id.subject_time_start);
        EditText editTimeEnd = root.findViewById(R.id.subject_time_end);
        EditText subjectLocalization = root.findViewById(R.id.subject_localization);

        Event newEvent = new Event();

        EventDisplay modifiedEvent;

        if ((modifiedEvent = viewModel.getModifiedEvent()) != null) {
            newEvent.setId(modifiedEvent.getId());
            newEvent.setStart_time(modifiedEvent.getStart_time());
            newEvent.setEnd_time(modifiedEvent.getEnd_time());

            subjectName.setText(modifiedEvent.getName());

            String startTime = Utils.getTimeString(modifiedEvent.getStart_time());
            String endTime = Utils.getTimeString(modifiedEvent.getEnd_time());

            editTimeStart.setText(startTime);
            editTimeEnd.setText(endTime);
            subjectLocalization.setText(modifiedEvent.getLocalization());

            int position = Day.valueOf(modifiedEvent.getDay()).ordinal();
            dayPicker.setText(dayPicker.getAdapter().getItem(position).toString(), false);
        }

        editTimeStart.setOnClickListener(v -> {
            Date dateNow = new Date();
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (timePicker, hour, minute) -> {
                Date timeStart = new Date(2019, 10, 10, hour, minute);
                String tStart = Utils.getTimeString(timeStart);
                editTimeStart.setText(tStart);
                newEvent.setStart_time(timeStart);
            }, dateNow.getHours(), dateNow.getMinutes(), true);
            timePickerDialog.show();
        });
        editTimeEnd.setOnClickListener(v -> {
            Date dateNow = new Date();
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (timePicker, hour, minute) -> {
                Date timeEnd = new Date(2019, 10, 10 , hour, minute);
                String tEnd = Utils.getTimeString(timeEnd);
                editTimeEnd.setText(tEnd);
                newEvent.setEnd_time(timeEnd);
            }, dateNow.getHours(), dateNow.getMinutes(), true);
            timePickerDialog.show();
        });

        MaterialButton saveButton = root.findViewById(R.id.subject_save);
        MaterialButton cancelButton = root.findViewById(R.id.subject_cancel);
        saveButton.setOnClickListener(v -> {
            String name = subjectName.getText().toString();
            Day day = Day.valueOf(dayPicker.getText().toString());
            String localization = subjectLocalization.getText().toString();

            Long id = viewModel.getSubject(name);
            if (id == null) {
                Subject newSubject = new Subject(name);
                id = viewModel.insertNewSubject(newSubject);
            }

            newEvent.setSubject_id(id);
            newEvent.setDay(day);
            newEvent.setLocalization(localization);

            viewModel.insertEvent(newEvent);
            dismiss();
        });
        cancelButton.setOnClickListener(v -> {
            dismiss();
        });
    }
}
