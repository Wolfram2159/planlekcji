package com.wolfram.planlekcji.ui.bottomSheets.notes.image;

import android.net.Uri;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.database.room.entities.notes.image.ImageNoteDisplayEntity;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.fragments.notes.NotesFragmentViewModel;
import com.wolfram.planlekcji.common.utility.DateUtils;

import java.io.File;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Wolfram
 * @date 2019-10-11
 */
public class ModifyImageNoteBottomSheet extends CustomBottomSheet {

    @BindView(R.id.notes_image_photo)
    ImageView photoView;
    @BindView(R.id.notes_image_edit)
    ImageView waterMarkView;
    @BindView(R.id.notes_image_name)
    AutoCompleteTextView subjectNameView;
    @BindView(R.id.notes_image_date)
    EditText dateView;
    @BindView(R.id.notes_image_save)
    MaterialButton saveButton;
    @BindView(R.id.notes_image_cancel)
    MaterialButton cancelButton;

    private List<SubjectEntity> subjects;
    private ImageNoteDisplayEntity modifyingImageNote;
    private NotesFragmentViewModel viewModel;

    public ModifyImageNoteBottomSheet(String photoPath) {
        this.modifyingImageNote = new ImageNoteDisplayEntity();
        this.modifyingImageNote.setPhotoPath(photoPath);
    }

    public ModifyImageNoteBottomSheet(@NonNull ImageNoteDisplayEntity modifyingImageNote) {
        this.modifyingImageNote = modifyingImageNote;
    }

    @Override
    protected int getResource() {
        return R.layout.notes_image_bottom_sheet;
    }

    @Override
    protected void customizeDialog() {
        viewModel = ViewModelProviders.of(getActivity()).get(NotesFragmentViewModel.class);
        ButterKnife.bind(this, root);

        subjects = viewModel.getSubjects();

        setupSubjectAdapter();
        if (tag.equals(CustomBottomSheet.CREATE)) setInitialValues();
        else if (tag.equals(CustomBottomSheet.MODIFY)) setValuesToViews();

        setupButtons();
    }

    private void setupSubjectAdapter() {
        setAdapterToView(new AutocompleteAdapterSetter() {
            @Override
            public AutoCompleteTextView getAdapterView() {
                return subjectNameView;
            }

            @Override
            public List<String> getList() {
                return getSubjectsNames(subjects);
            }
        }, (parent, view, pos, arg) -> {
            SubjectEntity clickedSubject = subjects.get(pos);
            int subjectId = clickedSubject.getId();
            modifyingImageNote.setSubject_id(subjectId);
        });
    }

    private void setInitialValues() {
        waterMarkView.setVisibility(View.INVISIBLE);
        Date dateNow = new Date();
        String dateString = DateUtils.getDateString(dateNow);
        modifyingImageNote.setDate(dateNow);
        dateView.setText(dateString);
        String photoPath = modifyingImageNote.getPhotoPath();
        setImageToView(photoPath);
    }

    private void setValuesToViews() {
        String subjectName = modifyingImageNote.getName();
        subjectNameView.setText(subjectName);
        Date noteDate = modifyingImageNote.getDate();
        String dateString = DateUtils.getDateString(noteDate);
        dateView.setText(dateString);
        String photoPath = modifyingImageNote.getPhotoPath();
        setImageToView(photoPath);
    }

    private void setImageToView(String photoPath) {
        File photoFile = new File(photoPath);
        Uri photoUri = Uri.fromFile(photoFile);
        photoView.setImageURI(photoUri);
    }

    private void setupButtons() {
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        dateView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.notes_image_save:
                viewModel.modifyImageNote(modifyingImageNote, tag);
                dismiss();
                break;
            case R.id.notes_image_cancel:
                dismiss();
                break;
            case R.id.notes_image_date:
                createDatePicker(pickedDate -> {
                    String textDate = DateUtils.getDateString(pickedDate);
                    dateView.setText(textDate);
                    modifyingImageNote.setDate(pickedDate);
                });
                break;
        }
    }
}
