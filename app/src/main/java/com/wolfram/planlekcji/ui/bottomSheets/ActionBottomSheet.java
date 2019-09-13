package com.wolfram.planlekcji.ui.bottomSheets;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.EventDisplay;

/**
 * @author Wolfram
 * @date 2019-08-28
 */
public class ActionBottomSheet extends CustomBottomSheet {
    @Override
    protected int getResource() {
        return R.layout.action_bottom_sheet;
    }

    private OnDeleteListener onDeleteListener;
    private EventDisplay editEvent;

    public interface OnDeleteListener {
        void onDelete();
    }

    public void setOnModifyListener(EventDisplay editEvent) {
        this.editEvent = editEvent;
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
            modifySubjectBottomSheet.setEditEvent(editEvent);
            modifySubjectBottomSheet.show(getFragmentManager(), "EditBottomSheet");
            dismiss();
        });

        delete.setOnClickListener(view -> {
            onDeleteListener.onDelete();
            dismiss();
        });
    }
}
