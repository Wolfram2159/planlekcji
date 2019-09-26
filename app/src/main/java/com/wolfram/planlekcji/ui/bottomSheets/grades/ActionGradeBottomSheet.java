package com.wolfram.planlekcji.ui.bottomSheets.grades;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.fragments.grades.GradesFragmentViewModel;

import androidx.lifecycle.ViewModelProviders;

/**
 * @author Wolfram
 * @date 2019-09-20
 */
public class ActionGradeBottomSheet extends CustomBottomSheet {

    private GradesFragmentViewModel viewModel;
    private boolean isEditedBtnClicked = false;

    @Override
    protected int getResource() {
        return R.layout.action_bottom_sheet;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!isEditedBtnClicked) {
            viewModel.setModifiedGrade(null);
        }
    }

    @Override
    protected void customizeDialog() {
        MaterialButton edit = root.findViewById(R.id.action_edit_btn);
        MaterialButton delete = root.findViewById(R.id.action_delete_btn);

        viewModel = ViewModelProviders.of(getActivity()).get(GradesFragmentViewModel.class);

        edit.setOnClickListener(view -> {
            isEditedBtnClicked = true;
            ModifyGradeBottomSheet modifyGradeBottomSheet = new ModifyGradeBottomSheet();
            modifyGradeBottomSheet.show(getFragmentManager(), "EditBottomSheet");
            dismiss();
        });

        delete.setOnClickListener(view -> {
            viewModel.deleteGrade();
            dismiss();
        });
    }
}
