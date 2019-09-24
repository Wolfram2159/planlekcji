package com.wolfram.planlekcji.ui.bottomSheets.grades;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.grade.GradeSingleton;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.utils.interfaces.OnDeleteListener;

/**
 * @author Wolfram
 * @date 2019-09-20
 */
public class ActionGradeBottomSheet extends CustomBottomSheet {

    private boolean ifSetNull = true;
    private OnDeleteListener onDeleteListener;

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    @Override
    protected int getResource() {
        return R.layout.action_bottom_sheet;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ifSetNull) {
            GradeSingleton.setNull();
        }
    }

    @Override
    protected void customizeDialog() {
        MaterialButton edit = root.findViewById(R.id.action_edit_btn);
        MaterialButton delete = root.findViewById(R.id.action_delete_btn);

        edit.setOnClickListener(view -> {
            ModifyGradeBottomSheet modifyGradeBottomSheet = new ModifyGradeBottomSheet();

            modifyGradeBottomSheet.show(getFragmentManager(), "EditBottomSheet");
            ifSetNull = false;
            dismiss();
        });

        delete.setOnClickListener(view -> {
            onDeleteListener.onDelete();
            dismiss();
        });
    }
}
