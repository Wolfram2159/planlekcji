package com.wolfram.planlekcji.ui.bottomSheets;

import android.app.TimePickerDialog;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.EventDisplay;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.Time;
import com.wolfram.planlekcji.utils.enums.Day;

import java.util.Calendar;
import java.util.Date;

import androidx.lifecycle.ViewModelProviders;

/**
 * @author Wolfram
 * @date 2019-08-28
 */
public class ModifySubjectBottomSheet extends CustomBottomSheet {

    private EventDisplay eventDisplay;

    @Override
    protected int getResource() {
        return R.layout.bottom_sheet;
    }

    public void setEditEvent(EventDisplay eventDisplay) {
        this.eventDisplay = eventDisplay;
    }


    public ModifySubjectBottomSheet() {
    }


    @Override
    protected void customizeDialog() {
        ModifySubjectViewModel viewModel = ViewModelProviders.of(this).get(ModifySubjectViewModel.class);

        ArrayAdapter<Day> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        android.R.layout.simple_list_item_1,
                        Day.values());

        AutoCompleteTextView dayPicker =
                root.findViewById(R.id.subject_day);
        dayPicker.setShowSoftInputOnFocus(false);//Doesnt show keyboard :)
        dayPicker.setAdapter(adapter);

        EditText subjectName = root.findViewById(R.id.subject_name);

        EditText editTimeStart = root.findViewById(R.id.subject_time_start);

        EditText editTimeEnd = root.findViewById(R.id.subject_time_end);

        EditText subjectLocalization = root.findViewById(R.id.subject_localization);

        EventDisplay thisEvent = new EventDisplay();

        editTimeStart.setOnClickListener((v) -> {
            Date dateNow = Calendar.getInstance().getTime();
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (timePicker, hour, minute) -> {
                Time timeStart = new Time(hour, minute);
                editTimeStart.setText(timeStart.toString());
                thisEvent.setStart_time(timeStart);
            }, dateNow.getHours(), dateNow.getMinutes(), true);
            timePickerDialog.show();
        });
        editTimeEnd.setOnClickListener((v) -> {
            Date dateNow = Calendar.getInstance().getTime();
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (timePicker, hour, minute) -> {
                Time timeEnd = new Time(hour, minute);
                editTimeEnd.setText(timeEnd.toString());
                thisEvent.setEnd_time(timeEnd);
            }, dateNow.getHours(), dateNow.getMinutes(), true);
            timePickerDialog.show();
        });

        MaterialButton saveButton = root.findViewById(R.id.subject_save);
        MaterialButton cancelButton = root.findViewById(R.id.subject_cancel);
        saveButton.setOnClickListener(v -> {
            viewModel.deleteEvent(eventDisplay);

            Log.e("eventdisplay", eventDisplay.toString());
            String name = subjectName.getText().toString();

            thisEvent.setName(name);

            Long id = viewModel.getSubject(name);

            if (id == null) {
                Subject s = new Subject(name);
                long newId = viewModel.insertNewSubject(s);
                thisEvent.setSubject_id(newId);
            }else {
                thisEvent.setSubject_id(id);
            }

            Day day = Day.valueOf(dayPicker.getText().toString());
            thisEvent.setDay(day);

            String localization = subjectLocalization.getText().toString();
            thisEvent.setLocalization(localization);

            viewModel.insertEvent(thisEvent);
            dismiss();
        });
        cancelButton.setOnClickListener(v -> {
            dismiss();
        });
        if (eventDisplay != null) {
            thisEvent.setStart_time(eventDisplay.getStart_time());
            thisEvent.setEnd_time(eventDisplay.getEnd_time());

            subjectName.setText(eventDisplay.getName());
            editTimeStart.setText(eventDisplay.getStart_time().toString());
            editTimeEnd.setText(eventDisplay.getEnd_time().toString());
            subjectLocalization.setText(eventDisplay.getLocalization());
            thisEvent.setStart_time(eventDisplay.getStart_time());
            thisEvent.setEnd_time(eventDisplay.getEnd_time());

            int position = Day.valueOf(eventDisplay.getDay()).ordinal();
            dayPicker.setText(dayPicker.getAdapter().getItem(position).toString(), false);
        }
    }
}
