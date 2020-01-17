package com.wolfram.planlekcji.ui.bottomSheets.grades;

import android.app.DatePickerDialog;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.database.room.entities.grade.GradeDisplayEntity;
import com.wolfram.planlekcji.database.room.entities.grade.GradeEntity;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.fragments.grades.GradesFragmentViewModel;
import com.wolfram.planlekcji.common.others.DateUtils;

import java.util.Date;

import androidx.lifecycle.ViewModelProviders;

/**
 * @author Wolfram
 * @date 2019-09-18
 */
public class ModifyGradeBottomSheet extends CustomBottomSheet {

    private GradesFragmentViewModel viewModel;

    @Override
    protected int getResource() {
        return R.layout.grades_bottom_sheet;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.setModifiedGrade(null);
    }

    @Override
    protected void customizeDialog() {
        MaterialButton cancelBtn = root.findViewById(R.id.grade_cancel);
        MaterialButton saveBtn = root.findViewById(R.id.grade_save);
        EditText desc = root.findViewById(R.id.grade_desc);
        AutoCompleteTextView subjectPicker = root.findViewById(R.id.grade_name);
        EditText date = root.findViewById(R.id.grade_date);

        GradeEntity newGrade = new GradeEntity();

        date.setOnClickListener(v -> {
            Date dateNow = new Date();
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (picker, year, month, day) -> {
                        Date pickedDate = new Date((year-1900), month, day);
                        String myDate = DateUtils.getDateString(pickedDate);
                        date.setText(myDate);
                        newGrade.setDate(pickedDate);
                    }, (1900 + dateNow.getYear()), dateNow.getMonth(), dateNow.getDate());
            datePickerDialog.show();
        });
        date.setShowSoftInputOnFocus(false);

        viewModel = ViewModelProviders.of(getActivity()).get(GradesFragmentViewModel.class);

        GradeDisplayEntity modifiedGrade;

        if ((modifiedGrade = viewModel.getModifiedGrade()) != null){
            newGrade.setId(modifiedGrade.getId());
            newGrade.setSubject_id(modifiedGrade.getSubject_id());
            newGrade.setDescription(modifiedGrade.getDescription());
            newGrade.setDate(modifiedGrade.getDate());

            subjectPicker.setText(modifiedGrade.getName());
            desc.setText(modifiedGrade.getDescription());
            String myDate = DateUtils.getDateString(modifiedGrade.getDate());
            date.setText(myDate);
        }

        viewModel.getSubjects().observe(this, subjects -> {
            ArrayAdapter<SubjectEntity> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, subjects);

            subjectPicker.setShowSoftInputOnFocus(false);
            subjectPicker.setAdapter(adapter);
        });



        subjectPicker.setOnItemClickListener((parent, view, pos, arg) -> {
            Object item = parent.getItemAtPosition(pos);
            if (item instanceof SubjectEntity){
                newGrade.setSubject_id(((SubjectEntity) item).getId());
            }
        });

        saveBtn.setOnClickListener(view -> {
            newGrade.setDescription(desc.getText().toString());
            viewModel.insertGrade(newGrade);
            dismiss();
        });

        cancelBtn.setOnClickListener(view -> {
            dismiss();
        });
    }
}
