package com.wolfram.planlekcji.ui.bottomSheets.events;

import android.app.TimePickerDialog;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.event.EventDisplay;
import com.wolfram.planlekcji.database.room.entities.event.EventSingleton;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.Time;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.utils.enums.Day;

import java.util.Calendar;
import java.util.Date;

import androidx.lifecycle.ViewModelProviders;

/**
 * @author Wolfram
 * @date 2019-08-28
 */
public class ModifyEventBottomSheet extends CustomBottomSheet {

    @Override
    protected int getResource() {
        return R.layout.events_bottom_sheet;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventSingleton.setNull();
    }

    @Override
    protected void customizeDialog() {
        EventBottomSheetViewModel viewModel = ViewModelProviders.of(this).get(EventBottomSheetViewModel.class);

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

        EventDisplay newEvent = new EventDisplay();

        editTimeStart.setOnClickListener((v) -> {
            Date dateNow = Calendar.getInstance().getTime();
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (timePicker, hour, minute) -> {
                Time timeStart = new Time(hour, minute);
                editTimeStart.setText(timeStart.toString());
                newEvent.setStart_time(timeStart);
            }, dateNow.getHours(), dateNow.getMinutes(), true);
            timePickerDialog.show();
        });
        editTimeEnd.setOnClickListener((v) -> {
            Date dateNow = Calendar.getInstance().getTime();
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (timePicker, hour, minute) -> {
                Time timeEnd = new Time(hour, minute);
                editTimeEnd.setText(timeEnd.toString());
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
        if (!EventSingleton.isNull()) {
            EventSingleton eventDisplay = EventSingleton.getInstance(null);
            int id = eventDisplay.getId();
            newEvent.setId(id);

            newEvent.setStart_time(eventDisplay.getStart_time());
            newEvent.setEnd_time(eventDisplay.getEnd_time());

            subjectName.setText(eventDisplay.getName());
            editTimeStart.setText(eventDisplay.getStart_time().toString());
            editTimeEnd.setText(eventDisplay.getEnd_time().toString());
            subjectLocalization.setText(eventDisplay.getLocalization());
            newEvent.setStart_time(eventDisplay.getStart_time());
            newEvent.setEnd_time(eventDisplay.getEnd_time());

            int position = Day.valueOf(eventDisplay.getDay()).ordinal();
            dayPicker.setText(dayPicker.getAdapter().getItem(position).toString(), false);
        }
    }
}
