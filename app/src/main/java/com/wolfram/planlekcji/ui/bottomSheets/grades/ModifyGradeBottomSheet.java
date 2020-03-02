package com.wolfram.planlekcji.ui.bottomSheets.grades;

import android.view.View;
import android.widget.Adapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.common.enums.ViewType;
import com.wolfram.planlekcji.common.utility.UiUtils;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.database.room.entities.grade.GradeDisplayEntity;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.fragments.grades.GradesFragmentViewModel;
import com.wolfram.planlekcji.common.utility.DateUtils;

import androidx.lifecycle.ViewModelProviders;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Wolfram
 * @date 2019-09-18
 */
public class ModifyGradeBottomSheet extends CustomBottomSheet implements View.OnClickListener {

    @BindView(R.id.grade_cancel)
    MaterialButton cancelButton;
    @BindView(R.id.grade_save)
    MaterialButton saveButton;
    @BindView(R.id.grade_desc)
    EditText description;
    @BindView(R.id.grade_name)
    AutoCompleteTextView subjectPicker;
    @BindView(R.id.grade_date)
    EditText date;

    private GradesFragmentViewModel viewModel;
    private GradeDisplayEntity modifyingGrade = new GradeDisplayEntity();
    private List<SubjectEntity> subjects;

    @Override
    protected int getResource() {
        return R.layout.grades_bottom_sheet;
    }

    @Override
    protected void customizeDialog() {
        ButterKnife.bind(this, root);

        date.setShowSoftInputOnFocus(false);

        viewModel = ViewModelProviders.of(activity).get(GradesFragmentViewModel.class);
        subjects = viewModel.getSubjects();

        setupViewsListeners();
        setupAdapters();

        if (tag.equals(CustomBottomSheet.MODIFY)) setValuesToViews();
        else setInitialValuesToLocalGrade();
    }

    private void setInitialValuesToLocalGrade() {
        Date now = new Date();
        modifyingGrade.setDate(now);
        String dateNow = DateUtils.getDateString(now);
        date.setText(dateNow);
        SubjectEntity firstSubject = subjects.get(0);
        modifyingGrade.setSubject_id(firstSubject.getId());
    }

    private void setupViewsListeners() {
        UiUtils.setupDatePicker(date, pickedDate -> {
            String dateString = DateUtils.getDateString(pickedDate);
            date.setText(dateString);
            modifyingGrade.setDate(pickedDate);
        }, ViewType.DatePicker);
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    private void setupAdapters() {
        UiUtils.setAdapterToTextView(subjectPicker, subjects, ViewType.NoEditableSubjectPicker, tag, (adapterView, view, pos, arg) -> {
            Adapter adapter = adapterView.getAdapter();
            Object item = adapter.getItem(pos);
            if (item instanceof SubjectEntity) {
                modifyingGrade.setSubject((SubjectEntity) item);
            }
        });
    }

    private void setValuesToViews() {
        modifyingGrade = viewModel.getModifyingGrade();
        String gradeDescription = modifyingGrade.getDescription();
        description.setText(gradeDescription);
        Date gradeDate = modifyingGrade.getDate();
        String stringDate = DateUtils.getDateString(gradeDate);
        date.setText(stringDate);
        String gradeName = modifyingGrade.getName();
        subjectPicker.setText(gradeName);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.grade_save:
                String desc = description.getText().toString();
                modifyingGrade.setDescription(desc);
                viewModel.modifyGrade(modifyingGrade, tag);
                dismiss();
                break;
            case R.id.grade_cancel:
                dismiss();
                break;
        }
    }
}
