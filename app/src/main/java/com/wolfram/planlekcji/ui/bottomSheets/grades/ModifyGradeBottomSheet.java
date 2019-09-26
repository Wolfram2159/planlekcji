package com.wolfram.planlekcji.ui.bottomSheets.grades;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.grade.Grade;
import com.wolfram.planlekcji.database.room.entities.grade.GradeDisplay;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.fragments.grades.GradesFragmentViewModel;

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

        viewModel = ViewModelProviders.of(getActivity()).get(GradesFragmentViewModel.class);

        Grade newGrade = new Grade();

        GradeDisplay modifiedGrade;

        if ((modifiedGrade = viewModel.getModifiedGrade()) != null){
            newGrade.setId(modifiedGrade.getId());
            newGrade.setSubject_id(modifiedGrade.getSubject_id());
            newGrade.setDescription(modifiedGrade.getDescription());

            subjectPicker.setText(modifiedGrade.getName());
            desc.setText(modifiedGrade.getDescription());
        }

        viewModel.getSubjects().observe(this, subjects -> {
            ArrayAdapter<Subject> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, subjects);

            subjectPicker.setShowSoftInputOnFocus(false);
            subjectPicker.setAdapter(adapter);
        });



        subjectPicker.setOnItemClickListener((parent, view, pos, arg) -> {
            Object item = parent.getItemAtPosition(pos);
            if (item instanceof Subject){
                newGrade.setSubject_id(((Subject) item).getId());
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
