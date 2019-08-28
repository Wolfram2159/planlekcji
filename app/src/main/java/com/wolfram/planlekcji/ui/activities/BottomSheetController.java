package com.wolfram.planlekcji.ui.activities;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
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
public class BottomSheetController{
    private View root;
    private Context context;
    private BottomSheetBehavior bottomSheetBehavior;

    public BottomSheetController(View root, Context context, BottomSheetBehavior bottomSheetBehavior) {
        this.root = root;
        this.context = context;
        this.bottomSheetBehavior = bottomSheetBehavior;
        setupBottomSheet();
    }
    protected void setupBottomSheet() {
        ArrayAdapter<Day> adapter =
                new ArrayAdapter<>(
                        context,
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
            TimePickerDialog timePickerDialog = new TimePickerDialog(context, (timePicker, hour, minute) -> {
                Time timeStart = new Time(hour, minute);
                editTimeStart.setText(timeStart.toString());
                thisSubject.setStart_time(timeStart);
            }, dateNow.getHours(), dateNow.getMinutes(), true);
            timePickerDialog.show();
        });
        editTimeEnd.setOnClickListener((v) -> {
            Date dateNow = Calendar.getInstance().getTime();
            TimePickerDialog timePickerDialog = new TimePickerDialog(context, (timePicker, hour, minute) -> {
                Time timeEnd = new Time(hour, minute);
                editTimeEnd.setText(timeEnd.toString());
                thisSubject.setEnd_time(timeEnd);
            }, dateNow.getHours(), dateNow.getMinutes(), true);
            timePickerDialog.show();
        });

        MaterialButton saveButton = root.findViewById(R.id.subject_save);
        MaterialButton cancelButton = root.findViewById(R.id.subject_cancel);
        saveButton.setOnClickListener(v -> {

            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });
        cancelButton.setOnClickListener(v -> {
            clearBottomSheet();
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });
    }
    public void clearBottomSheet(){

    }
}
