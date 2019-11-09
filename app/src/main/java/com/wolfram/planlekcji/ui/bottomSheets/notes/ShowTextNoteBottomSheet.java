package com.wolfram.planlekcji.ui.bottomSheets.notes;

import android.util.Log;
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
        EditText editText = root.findViewById(R.id.show_text_note_edit_text);
        MaterialButton editButton = root.findViewById(R.id.show_text_note_btn_edit);
        MaterialButton okButton = root.findViewById(R.id.show_text_note_btn_ok);

        NotesFragmentViewModel viewModel = ViewModelProviders.of(getActivity()).get(NotesFragmentViewModel.class);

        TextNote textNote = viewModel.getTextNote();

        editText.setText(textNote.getMessage());

        editButton.setOnClickListener(view -> {
            String newMessage = editText.getText().toString();
            textNote.setMessage(newMessage);
            viewModel.insertTextNote(textNote);
            dismiss();
        });

        okButton.setOnClickListener(view -> {
            dismiss();
        });
    }
}
