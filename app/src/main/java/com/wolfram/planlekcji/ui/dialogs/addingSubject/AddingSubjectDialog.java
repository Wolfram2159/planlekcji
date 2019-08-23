package com.wolfram.planlekcji.ui.dialogs.addingSubject;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.Time;
import com.wolfram.planlekcji.ui.dialogs.CustomDialog;
import com.wolfram.planlekcji.utils.enums.Day;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * @author Wolfram
 * @date 2019-08-02
 */
public class AddingSubjectDialog extends CustomDialog {

    public interface SubjectDialogCallback {
        void onSubjectAction(Subject subject, String textValue);
    }

    private SubjectDialogCallback subjectDialogCallback;
    private Subject editSubject;

    public AddingSubjectDialog(SubjectDialogCallback subjectDialogCallback) {
        this.subjectDialogCallback = subjectDialogCallback;
        editSubject = null;
    }

    public AddingSubjectDialog(SubjectDialogCallback subjectDialogCallback, Subject editSubject) {
        this.subjectDialogCallback = subjectDialogCallback;
        this.editSubject = editSubject;
    }

    @Override
    protected void setResource() {
        resource = R.layout.subject_dialog;
    }

    @Override
    protected void customizeDialog() {
        ArrayAdapter<Day> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.support_simple_spinner_dropdown_item,
                        Day.values());

        AutoCompleteTextView dayPicker =
                root.findViewById(R.id.subject_day);
        dayPicker.setOnClickListener((v) -> hideKeyboard());
        dayPicker.setAdapter(adapter);

        EditText subjectName = root.findViewById(R.id.subject_name);

        EditText editTimeStart = root.findViewById(R.id.subject_time_start);

        EditText editTimeEnd = root.findViewById(R.id.subject_time_end);

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

        EditText subjectLocalization = root.findViewById(R.id.subject_localization);

        builder
                .setPositiveButton("Save", (d, id) -> {
                    String name = subjectName.getText().toString();
                    thisSubject.setSubject(name);

                    Day day = Day.valueOf(dayPicker.getText().toString());
                    thisSubject.setDay(day);

                    String localization = subjectLocalization.getText().toString();
                    thisSubject.setLocalization(localization);

                    subjectDialogCallback.onSubjectAction(thisSubject, "Record save in database");
                })
                .setNegativeButton("Cancel", (d, id) -> {
                });

        if (getTag().equals("EditDialog")) {
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

    private void hideKeyboard() {
        if (getDialog().getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getDialog().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
