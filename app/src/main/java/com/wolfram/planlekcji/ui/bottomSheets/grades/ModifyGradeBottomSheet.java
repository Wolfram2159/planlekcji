package com.wolfram.planlekcji.ui.bottomSheets.grades;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.grade.Grade;
import com.wolfram.planlekcji.database.room.entities.grade.GradeSingleton;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;

import androidx.lifecycle.ViewModelProviders;

/**
 * @author Wolfram
 * @date 2019-09-18
 */
public class ModifyGradeBottomSheet extends CustomBottomSheet {
    @Override
    protected int getResource() {
        return R.layout.grades_bottom_sheet;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GradeSingleton.setNull();
    }

    @Override
    protected void customizeDialog() {
        MaterialButton cancelBtn = root.findViewById(R.id.grade_cancel);
        MaterialButton saveBtn = root.findViewById(R.id.grade_save);
        EditText desc = root.findViewById(R.id.grade_desc);
        AutoCompleteTextView subjectPicker = root.findViewById(R.id.grade_name);

        GradeBottomSheetViewModel viewModel = ViewModelProviders.of(this).get(GradeBottomSheetViewModel.class);

        viewModel.getSubjectsNames().observe(this, subjects -> {
            ArrayAdapter<Subject> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, subjects);

            subjectPicker.setShowSoftInputOnFocus(false);
            subjectPicker.setAdapter(adapter);
        });

        Grade newGrade = new Grade();

        subjectPicker.setOnItemClickListener((parent, view, pos, arg) -> {
            Object item = parent.getItemAtPosition(pos);
            if (item instanceof Subject){
                newGrade.setSubject_id(((Subject) item).getId());
            }
        });


        /*todo: this

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            DatePickerDialog picker = new DatePickerDialog(getContext());
            picker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker picker, int year, int month, int day) {
                    Log.e("czas", "" + year + "/" + month + "/" + day + "");
                }
            });
            picker.show();
        }
*/
        saveBtn.setOnClickListener(view -> {
            newGrade.setDescription(desc.getText().toString());
            viewModel.insertGrade(newGrade);
            dismiss();
        });

        cancelBtn.setOnClickListener(view -> {
            dismiss();
        });
        if (!GradeSingleton.isNull()){
            GradeSingleton gradeSingleton = GradeSingleton.getInstance(null);
            int id = gradeSingleton.getId();
            newGrade.setId(id);

            subjectPicker.setText(gradeSingleton.getName());
            desc.setText(gradeSingleton.getDescription());
        }
    }
}
