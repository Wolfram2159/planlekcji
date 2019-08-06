package com.wolfram.planlekcji.ui.dialogs;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.reginald.editspinner.EditSpinner;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.Time;
import com.wolfram.planlekcji.ui.activities.SubjectsViewModel;
import com.wolfram.planlekcji.utils.enums.Day;

import androidx.fragment.app.DialogFragment;

/**
 * @author Wolfram
 * @date 2019-08-02
 */
public class AddingSubjectDialog extends CustomDialog {

    public interface SnackbarMaker{
        void makeSnackbar(String value);
    }

    private SubjectsViewModel viewModel;
    private SnackbarMaker snackbarMaker;

    public AddingSubjectDialog(SubjectsViewModel viewModel, SnackbarMaker snackbarMaker) {
        this.viewModel = viewModel;
        this.snackbarMaker = snackbarMaker;
    }


    @Override
    protected void setResource() {
        resource = R.layout.subject_dialog;
    }

    @Override
    protected void customizeDialog() {

        EditSpinner subjectName = root.findViewById(R.id.subject_name);

        EditText editTimeStart = root.findViewById(R.id.subject_time_start);
        final Time[] timeStart = new Time[1];

        EditText editTimeEnd = root.findViewById(R.id.subject_time_end);
        final Time[] timeEnd = new Time[1];

        Spinner daySpinner = root.findViewById(R.id.subject_day);

        daySpinner.setAdapter(new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, Day.values()));


        editTimeStart.setOnClickListener((v) -> {
            DialogFragment timePickerDialog = new TimePickerDialog((time) -> {
                editTimeStart.setText(time.toString());
                timeStart[0] = time;
            });

            timePickerDialog.show(getFragmentManager(), "TimePickerDialog");
        });
        editTimeEnd.setOnClickListener((v) -> {
            DialogFragment timePickerDialog = new TimePickerDialog((time) -> {
                editTimeEnd.setText(time.toString());
                timeEnd[0] = time;
            });

            timePickerDialog.show(getFragmentManager(), "TimePickerDialog");
        });

        EditSpinner subjectLocalization = root.findViewById(R.id.subject_localization);

        builder
                .setPositiveButton("Save", (d, id) -> {
                    String name = subjectName.getText().toString();
                    Day day = (Day) daySpinner.getSelectedItem();
                    String localization = subjectLocalization.getText().toString();

                    Subject subject = new Subject(
                            name,
                            timeStart[0],
                            timeEnd[0],
                            localization,
                            day
                    );
                    viewModel.insertSubject(subject);
                    snackbarMaker.makeSnackbar("Record save in database");
                })
                .setNegativeButton("Cancel", (d, id) -> {
                    snackbarMaker.makeSnackbar("Operation canceled");
                });
    }
}
