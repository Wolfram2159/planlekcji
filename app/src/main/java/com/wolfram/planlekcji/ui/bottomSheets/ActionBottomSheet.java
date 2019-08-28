package com.wolfram.planlekcji.ui.bottomSheets;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.Subject;

/**
 * @author Wolfram
 * @date 2019-08-28
 */
public class ActionBottomSheet extends CustomBottomSheet {
    @Override
    protected int getResource() {
        return R.layout.action_bottom_sheet;
    }

    private ModifySubjectBottomSheet.OnModifyListener modifyListener;
    private OnDeleteListener onDeleteListener;
    private Subject editSubject;

    public interface OnDeleteListener {
        void onDelete();
    }

    public void setOnModifyListener(ModifySubjectBottomSheet.OnModifyListener modifyListener, Subject subject) {
        this.modifyListener = modifyListener;
        this.editSubject = subject;
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    @Override
    protected void customizeDialog() {
        MaterialButton edit = root.findViewById(R.id.action_edit_btn);
        MaterialButton delete = root.findViewById(R.id.action_delete_btn);

        edit.setOnClickListener(view -> {
            ModifySubjectBottomSheet modifySubjectBottomSheet = new ModifySubjectBottomSheet();
            modifySubjectBottomSheet.setModify(modifyListener);
            modifySubjectBottomSheet.setEditSubject(editSubject);
            modifySubjectBottomSheet.show(getFragmentManager(), "EditBottomSheet");
            dismiss();
        });

        delete.setOnClickListener(view -> {
            onDeleteListener.onDelete();
            dismiss();
        });
    }
}
