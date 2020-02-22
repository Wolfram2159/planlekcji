package com.wolfram.planlekcji.ui.bottomSheets.notes;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.database.room.entities.notes.TextNoteEntity;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.fragments.notes.NotesFragmentViewModel;
import com.wolfram.planlekcji.common.others.DateUtils;

import java.util.ArrayList;
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
    AutoCompleteTextView subjectName;
    @BindView(R.id.notes_text_date)
    EditText date;
    @BindView(R.id.notes_text_title)
    EditText title;
    @BindView(R.id.notes_text_note)
    EditText message;

    private NotesFragmentViewModel viewModel;
    private TextNoteEntity modifyingTextNote;
    private List<SubjectEntity> subjects;

    public ModifyTextNoteBottomSheet() {
        this.modifyingTextNote = new TextNoteEntity();
    }

    public ModifyTextNoteBottomSheet(TextNoteEntity modifyingTextNote) {
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
        else setInitialValues();
        setupButtons();
    }

    private void setupSubjectAdapter() {
        setAdapterToView(new AutocompleteAdapterSetter() {
            @Override
            public AutoCompleteTextView getAdapterView() {
                return subjectName;
            }

            @Override
            public List<String> getList() {
                return getSubjectsNames(subjects);
            }
        }, (parent, view, pos, arg) -> {
            SubjectEntity clickedSubject = subjects.get(pos);
            int subjectId = clickedSubject.getId();
            modifyingTextNote.setSubject_id(subjectId);
        });
    }

    private List<String> getSubjectsNames(List<SubjectEntity> subjects) {
        List<String> subjectsNames = new ArrayList<>();
        for (SubjectEntity subject : subjects) {
            String subjectName = subject.getName();
            subjectsNames.add(subjectName);
        }
        return subjectsNames;
    }

    private void setValuesToViews() {
        Date textNoteDate = modifyingTextNote.getDate();
        String textNoteDateString = DateUtils.getDateString(textNoteDate);
        date.setText(textNoteDateString);
        String textNoteTitle = modifyingTextNote.getTitle();
        title.setText(textNoteTitle);
        String textNoteMessage = modifyingTextNote.getMessage();
        message.setText(textNoteMessage);
        int textNoteSubjectId = modifyingTextNote.getSubject_id();
        // TODO: 2020-02-21 how to get subject name to display it
    }

    private void setInitialValues() {
        Date currentDate = new Date();
        String createTime = DateUtils.getDateString(currentDate);
        date.setText(createTime);
        modifyingTextNote.setDate(currentDate);
        SubjectEntity firstSubject = subjects.get(0);
        String firstSubjectName = firstSubject.getName();
        subjectName.setText(firstSubjectName);
        int firstSubjectId = firstSubject.getId();
        modifyingTextNote.setSubject_id(firstSubjectId);
    }

    private void setupButtons() {
        cancel.setOnClickListener(this);
        save.setOnClickListener(this);
        date.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.notes_text_save:
                String textNoteTitle = title.getText().toString();
                modifyingTextNote.setTitle(textNoteTitle);
                String message = this.message.getText().toString();
                modifyingTextNote.setMessage(message);
                viewModel.insertTextNote(modifyingTextNote);
                dismiss();
                break;
            case R.id.notes_text_date:
                createDatePicker(pickedDate -> {
                    String textDate = DateUtils.getDateString(pickedDate);
                    date.setText(textDate);
                    modifyingTextNote.setDate(pickedDate);
                });
                break;
            case R.id.notes_text_cancel:
                dismiss();
                break;
        }
    }
}
