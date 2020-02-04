package com.wolfram.planlekcji.ui.bottomSheets.subjects;

import android.view.View;
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

        viewModel = ViewModelProviders.of(activity).get(SubjectsFragmentViewModel.class);

        if (tag.equals(SubjectsFragment.MODIFY)) setValuesToViews();

        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    private void setValuesToViews() {
        SubjectEntity modifyingSubject = viewModel.getModifyingSubject();
        Integer modSubjectId = modifyingSubject.getId();
        this.modifyingSubject.setId(modSubjectId);
        String modSubjectName = modifyingSubject.getName();
        subjectName.setText(modSubjectName);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.subjects_save:
                modifySubject();
                break;
            case R.id.subjects_cancel:
                dismiss();
                break;
        }
    }

    private void modifySubject(){
        String name = subjectName.getText().toString();
        modifyingSubject.setName(name);
        viewModel.modifySubject(modifyingSubject, tag);
        dismiss();
    }
}
