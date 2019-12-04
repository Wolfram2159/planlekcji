package com.wolfram.planlekcji.ui.bottomSheets.notes;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.fragments.notes.NotesFragmentViewModel;

public class ModifyImageNoteBottomSheet extends CustomBottomSheet {
    @Override
    protected int getResource() {
        return R.layout.action_bottom_sheet;
    }

    @Override
    protected void customizeDialog() {
        MaterialButton edit = root.findViewById(R.id.action_edit_btn);
        MaterialButton delete = root.findViewById(R.id.action_delete_btn);

        NotesFragmentViewModel viewModel = ViewModelProviders.of(getActivity()).get(NotesFragmentViewModel.class);

        edit.setOnClickListener(view -> {
            EditImageNoteBottomSheet addImageNoteBottomSheet = new EditImageNoteBottomSheet();
            addImageNoteBottomSheet.show(getFragmentManager(), "modify");
            dismiss();
        });

        delete.setOnClickListener(view -> {
            viewModel.deleteImageNote();
            dismiss();
        });
    }
}
