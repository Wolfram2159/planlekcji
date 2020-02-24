package com.wolfram.planlekcji.ui.bottomSheets.notes.text;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.notes.text.TextNoteDisplayEntity;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowTextNoteBottomSheet extends CustomBottomSheet {
    @BindView(R.id.show_text_note_btn_edit)
    MaterialButton editButton;
    @BindView(R.id.show_text_note_btn_ok)
    MaterialButton okButton;
    @BindView(R.id.show_text_note_value)
    TextView noteValue;
    @BindView(R.id.show_text_note_title)
    TextView noteTitle;
    private TextNoteDisplayEntity showingTextNote;

    public ShowTextNoteBottomSheet(TextNoteDisplayEntity showingTextNote) {
        this.showingTextNote = showingTextNote;
    }

    @Override
    protected int getResource() {
        return R.layout.show_text_note_bottom_sheet;
    }

    @Override
    protected void customizeDialog() {
        ButterKnife.bind(this, root);
        noteValue.setMovementMethod(new ScrollingMovementMethod());

        enterData();
        setupButtons();
    }

    private void enterData() {
        String message = showingTextNote.getMessage();
        noteValue.setText(message);
        String title = showingTextNote.getTitle();
        noteTitle.setText(title);
    }

    private void setupButtons() {
        okButton.setOnClickListener(this);
        editButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.show_text_note_btn_ok:
                dismiss();
                break;
            case R.id.show_text_note_btn_edit:
                ModifyTextNoteBottomSheet bottomSheet = new ModifyTextNoteBottomSheet(showingTextNote);
                bottomSheet.show(getFragmentManager(), CustomBottomSheet.MODIFY);
                dismiss();
                break;
        }
    }
}
