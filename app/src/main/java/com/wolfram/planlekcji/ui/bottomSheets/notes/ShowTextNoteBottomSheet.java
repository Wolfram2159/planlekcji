package com.wolfram.planlekcji.ui.bottomSheets.notes;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.notes.TextNote;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.fragments.notes.NotesFragmentViewModel;

public class ShowTextNoteBottomSheet extends CustomBottomSheet {
    @Override
    protected int getResource() {
        return R.layout.show_text_note_bottom_sheet;
    }

    @Override
    protected void customizeDialog() {
        NotesFragmentViewModel viewModel = ViewModelProviders.of(getActivity()).get(NotesFragmentViewModel.class);

        MaterialButton editButton = root.findViewById(R.id.show_text_note_btn_edit);
        MaterialButton okButton = root.findViewById(R.id.show_text_note_btn_ok);

        EditText noteValue = root.findViewById(R.id.show_text_note_edit_text);

        TextNote textNote = viewModel.getTextNote();

        noteValue.setText(textNote.getMessage());

        okButton.setOnClickListener(view -> {
            if (okButton.getText().toString().equals("Save")){
                textNote.setMessage(noteValue.getText().toString());
                viewModel.insertTextNote(textNote);
            }
            dismiss();
        });
        editButton.setOnClickListener(view -> {
            if (editButton.getText().toString().equals("Cancel")){
                dismiss();
            }
            noteValue.setFocusableInTouchMode(true);
            noteValue.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(noteValue, InputMethodManager.SHOW_IMPLICIT);
            okButton.setText("Save");
            editButton.setText("Cancel");
        });
    }
}
