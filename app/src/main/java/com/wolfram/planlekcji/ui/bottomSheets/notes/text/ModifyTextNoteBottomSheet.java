package com.wolfram.planlekcji.ui.bottomSheets.notes.text;

import android.view.View;
import android.widget.Adapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.common.enums.ViewType;
import com.wolfram.planlekcji.common.utility.UiUtils;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.database.room.entities.notes.text.TextNoteDisplayEntity;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.fragments.notes.NotesFragmentViewModel;
import com.wolfram.planlekcji.common.utility.DateUtils;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.ViewModelProviders;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Wolfram
 * @date 2019-10-24
 */
public class ModifyTextNoteBottomSheet extends CustomBottomSheet {

    @BindView(R.id.notes_text_save)
    MaterialButton save;
    @BindView(R.id.notes_text_cancel)
    MaterialButton cancel;
    @BindView(R.id.notes_text_name)
    AutoCompleteTextView subjectPicker;
    @BindView(R.id.notes_text_date)
    EditText date;
    @BindView(R.id.notes_text_title)
    EditText title;
    @BindView(R.id.notes_text_note)
    EditText message;

    private NotesFragmentViewModel viewModel;
    private TextNoteDisplayEntity modifyingTextNote;
    private List<SubjectEntity> subjects;

    public ModifyTextNoteBottomSheet() {
        this.modifyingTextNote = new TextNoteDisplayEntity();
    }

    public ModifyTextNoteBottomSheet(TextNoteDisplayEntity modifyingTextNote) {
        this.modifyingTextNote = modifyingTextNote;
    }

    @Override
    protected int getResource() {
        return R.layout.notes_text_bottom_sheet;
    }

    @Override
    protected void customizeDialog() {
        viewModel = ViewModelProviders.of(activity).get(NotesFragmentViewModel.class);
        ButterKnife.bind(this, root);

        subjects = viewModel.getSubjects();

        setupSubjectAdapter();
        if (tag.equals(CustomBottomSheet.MODIFY)) setValuesToViews();
        else if (tag.equals(CustomBottomSheet.CREATE)) setInitialValues();
        setupViewsListeners();
    }

    private void setupSubjectAdapter() {
        UiUtils.setAdapterToTextView(subjectPicker, subjects, ViewType.NoEditableSubjectPicker, tag, (adapterView, view, pos, arg) -> {
            Adapter adapter = adapterView.getAdapter();
            Object clickedItem = adapter.getItem(pos);
            if (clickedItem instanceof SubjectEntity) {
                modifyingTextNote.setSubject((SubjectEntity) clickedItem);
            }
        });
    }

    private void setValuesToViews() {
        Date textNoteDate = modifyingTextNote.getDate();
        String textNoteDateString = DateUtils.getDateString(textNoteDate);
        date.setText(textNoteDateString);
        String textNoteTitle = modifyingTextNote.getTitle();
        title.setText(textNoteTitle);
        String textNoteMessage = modifyingTextNote.getMessage();
        message.setText(textNoteMessage);
    }

    private void setInitialValues() {
        Date currentDate = new Date();
        String createTime = DateUtils.getDateString(currentDate);
        date.setText(createTime);
        modifyingTextNote.setDate(currentDate);
        SubjectEntity firstSubject = subjects.get(0);
        int firstSubjectId = firstSubject.getId();
        modifyingTextNote.setSubject_id(firstSubjectId);
    }

    private void setupViewsListeners() {
        cancel.setOnClickListener(this);
        save.setOnClickListener(this);
        UiUtils.setupDatePicker(date, pickedDate -> {
            String textDate = DateUtils.getDateString(pickedDate);
            date.setText(textDate);
            modifyingTextNote.setDate(pickedDate);
        }, ViewType.DatePicker);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.notes_text_save:
                getValuesFromViews();
                viewModel.modifyTextNote(modifyingTextNote, tag);
                dismiss();
                break;
            case R.id.notes_text_cancel:
                dismiss();
                break;
        }
    }

    private void getValuesFromViews() {
        String textNoteTitle = title.getText().toString();
        modifyingTextNote.setTitle(textNoteTitle);
        String noteMessage = message.getText().toString();
        modifyingTextNote.setMessage(noteMessage);
    }
}
