package com.wolfram.planlekcji.ui.dialogs;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.ui.dialogs.addingSubject.AddingSubjectDialog;

import androidx.fragment.app.DialogFragment;

/**
 * @author Wolfram
 * @date 2019-08-12
 */
public class ActionDialog extends CustomDialog {

    public interface ActionDialogCallback{
        void onDelete();
        void onUpdate(Subject subject, String textValue);
    }

    private ActionDialogCallback actionDialogCallback;

    public ActionDialog(ActionDialogCallback actionDialogCallback) {
        this.actionDialogCallback = actionDialogCallback;
    }

    @Override
    protected void setResource() {
        resource = R.layout.action_dialog;
    }

    @Override
    protected void customizeDialog() {
        MaterialButton editButton = root.findViewById(R.id.action_edit_btn);
        editButton.setOnClickListener(view -> {
            DialogFragment dialogFragment = new AddingSubjectDialog((subject, textValue) -> {
                actionDialogCallback.onUpdate(subject, textValue);
                this.dismiss();
            });
            dialogFragment.show(getFragmentManager(), "EditDialog");
        });
        MaterialButton deleteButton = root.findViewById(R.id.action_delete_btn);
        deleteButton.setOnClickListener(view -> {
            actionDialogCallback.onDelete();
            dismiss();
        });
    }
}