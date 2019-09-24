package com.wolfram.planlekcji.ui.bottomSheets.events;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.event.EventSingleton;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.utils.interfaces.OnDeleteListener;

/**
 * @author Wolfram
 * @date 2019-08-28
 */
public class ActionEventBottomSheet extends CustomBottomSheet {

    private boolean ifSetNull = true;
    private OnDeleteListener deleteListener;

    public void setDeleteListener(OnDeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    @Override
    protected int getResource() {
        return R.layout.action_bottom_sheet;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ifSetNull) {
            EventSingleton.setNull();
        }
    }

    @Override
    protected void customizeDialog() {
        MaterialButton edit = root.findViewById(R.id.action_edit_btn);
        MaterialButton delete = root.findViewById(R.id.action_delete_btn);

        edit.setOnClickListener(view -> {
            ModifyEventBottomSheet modifyEventBottomSheet = new ModifyEventBottomSheet();

            modifyEventBottomSheet.show(getFragmentManager(), "EditBottomSheet");
            ifSetNull = false;
            dismiss();
        });

        delete.setOnClickListener(view -> {
            if (deleteListener != null) {
                deleteListener.onDelete();
            }
            dismiss();
        });
    }
}
