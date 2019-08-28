package com.wolfram.planlekcji.ui.bottomSheets;

import android.app.TimePickerDialog;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.Time;
import com.wolfram.planlekcji.utils.enums.Day;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Wolfram
 * @date 2019-08-28
 */
public class ModifySubjectBottomSheet extends CustomBottomSheet {

    private ModifyInterface modify;
    private Subject editSubject;
    @Override
    protected int getResource() {
        return R.layout.bottom_sheet;
    }
    public interface ModifyInterface{
        void onModify(Subject subject);
    }

    public ModifySubjectBottomSheet() {
    }

    public ModifySubjectBottomSheet(Subject editSubject) {
        this.editSubject = editSubject;
    }

    public void setModify(ModifyInterface modify) {
        this.modify = modify;
    }

    @Override
    protected void customizeDialog() {
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

        Subject thisSubject = new Subject();

        editTimeStart.setOnClickListener((v) -> {
            Date dateNow = Calendar.getInstance().getTime();
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (timePicker, hour, minute) -> {
                Time timeStart = new Time(hour, minute);
                editTimeStart.setText(timeStart.toString());
                thisSubject.setStart_time(timeStart);
            }, dateNow.getHours(), dateNow.getMinutes(), true);
            timePickerDialog.show();
        });
        editTimeEnd.setOnClickListener((v) -> {
            Date dateNow = Calendar.getInstance().getTime();
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (timePicker, hour, minute) -> {
                Time timeEnd = new Time(hour, minute);
                editTimeEnd.setText(timeEnd.toString());
                thisSubject.setEnd_time(timeEnd);
            }, dateNow.getHours(), dateNow.getMinutes(), true);
            timePickerDialog.show();
        });

        MaterialButton saveButton = root.findViewById(R.id.subject_save);
        MaterialButton cancelButton = root.findViewById(R.id.subject_cancel);
        saveButton.setOnClickListener(v -> {
            String name = subjectName.getText().toString();
            thisSubject.setSubject(name);

            Day day = Day.valueOf(dayPicker.getText().toString());
            thisSubject.setDay(day);

            String localization = subjectLocalization.getText().toString();
            thisSubject.setLocalization(localization);

            modify.onModify(thisSubject);
            dismiss();
        });
        cancelButton.setOnClickListener(v -> {
            dismiss();
        });
        if (editSubject != null) {
            thisSubject.setStart_time(editSubject.getStart_time());
            thisSubject.setEnd_time(editSubject.getEnd_time());

            subjectName.setText(editSubject.getSubject());
            editTimeStart.setText(editSubject.getStart_time().toString());
            editTimeEnd.setText(editSubject.getEnd_time().toString());
            subjectLocalization.setText(editSubject.getLocalization());
            thisSubject.setStart_time(editSubject.getStart_time());
            thisSubject.setEnd_time(editSubject.getEnd_time());

            int position = Day.valueOf(editSubject.getDay()).ordinal();
            dayPicker.setText(dayPicker.getAdapter().getItem(position).toString(), false);
        }
    }
}
