package com.wolfram.planlekcji.ui.bottomSheets.subjects;

import android.widget.EditText;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.fragments.subjects.SubjectsFragment;
import com.wolfram.planlekcji.ui.fragments.subjects.SubjectsFragmentViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ModifySubjectBottomSheet extends CustomBottomSheet {

    private SubjectEntity modifyingSubject = new SubjectEntity();
    private SubjectsFragmentViewModel viewModel;
    private String tag;
    @BindView(R.id.subjects_name)
    EditText subjectName;
    @BindView(R.id.subjects_save)
    MaterialButton saveButton;
    @BindView(R.id.subjects_cancel)
    MaterialButton cancelButton;

    @Override
    protected int getResource() {
        return R.layout.subjects_bottom_sheet;
    }

    @Override
    protected void customizeDialog() {
        ButterKnife.bind(this, root);
        tag = getTag();

        viewModel = ViewModelProviders.of(getActivity()).get(SubjectsFragmentViewModel.class);

        if (tag.equals(SubjectsFragment.MODIFY)) setValuesToViews();

        saveButton.setOnClickListener(v -> {
            String name = subjectName.getText().toString();
            modifyingSubject.setName(name);
            viewModel.modifySubject(modifyingSubject, tag);
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());
    }

    private void setValuesToViews() {
        SubjectEntity modifyingSubject = viewModel.getModifyingSubject();
        Integer modSubjectId = modifyingSubject.getId();
        this.modifyingSubject.setId(modSubjectId);
        String modSubjectName = modifyingSubject.getName();
        subjectName.setText(modSubjectName);
    }
}
