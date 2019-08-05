package com.wolfram.planlekcji.ui.dialogs;

import android.widget.EditText;

import com.reginald.editspinner.EditSpinner;
import com.wolfram.planlekcji.R;

import androidx.fragment.app.DialogFragment;

/**
 * @author Wolfram
 * @date 2019-08-02
 */
public class AddingSubjectDialog extends DialogCreator {

    @Override
    protected void setResource() {
        resource = R.layout.subject_dialog;
    }

    @Override
    protected void customizeDialog() {
        builder
                .setPositiveButton("Ok", (d, id) -> {

        })
                .setNegativeButton("Cancel", (d, id) -> {

                });
        EditSpinner es = root.findViewById(R.id.subject_name);

        EditText editTimeStart = root.findViewById(R.id.subject_time_start);

        EditText editTimeEnd = root.findViewById(R.id.subject_time_end);


        editTimeStart.setOnClickListener((v) -> {
            DialogFragment timePickerDialog = new TimePickerDialog((time) -> {
                editTimeStart.setText(time.toString());
            });

            timePickerDialog.show(getFragmentManager(), "TimePickerDialog");
            //Log.e("return callback", ""+ test.add(15));
        });
        editTimeEnd.setOnClickListener((v) -> {
            DialogFragment timePickerDialog = new TimePickerDialog((time) -> {
                editTimeEnd.setText(time.toString());
            });
            timePickerDialog.show(getFragmentManager(), "TimePickerDialog");
        });
    }
}
