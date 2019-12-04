package com.wolfram.planlekcji.ui.bottomSheets.notes;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.notes.ImageNote;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.fragments.notes.NotesFragmentViewModel;
import com.wolfram.planlekcji.utils.others.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class EditImageNoteBottomSheet extends CustomBottomSheet {

    private static final int REQUEST_TAKE_PHOTO = 1;
    private ImageView photo;
    private File photoFile;

    @Override
    protected int getResource() {
        return R.layout.notes_image_bottom_sheet;
    }

    @Override
    protected void customizeDialog() {
        NotesFragmentViewModel viewModel = ViewModelProviders.of(getActivity()).get(NotesFragmentViewModel.class);

        ImageNote imageNote = viewModel.getImageNote();

        photo = root.findViewById(R.id.notes_image_photo);
        ImageView editPhoto = root.findViewById(R.id.notes_image_edit);
        AutoCompleteTextView subjectName = root.findViewById(R.id.notes_image_name);
        EditText date = root.findViewById(R.id.notes_image_date);
        MaterialButton save = root.findViewById(R.id.notes_image_save);
        MaterialButton cancel = root.findViewById(R.id.notes_image_cancel);

        photoFile = new File(imageNote.getPhotoPath());
        Uri contentUri = Uri.fromFile(photoFile);
        photo.setImageURI(contentUri);

        Date currentDate = viewModel.getCurrentDate();
        Date createDate = imageNote.getDate();
        String createTime = Utils.getDateString(createDate);
        date.setText(createTime);

        date.setOnClickListener(view -> {
            DatePickerDialog pickerDialog = new DatePickerDialog(getContext(), (picker, year, month, day) -> {
                Date pickedDate = new Date((year - 1900), month, day);
                String myDate = Utils.getDateString(pickedDate);
                date.setText(myDate);
                imageNote.setDate(pickedDate);
            }, (1900 + currentDate.getYear()), currentDate.getMonth(), currentDate.getDate());
            pickerDialog.show();
        });

        viewModel.getSubjects().observe(this, subjects -> {
            ArrayAdapter<Subject> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, subjects);
            subjectName.setAdapter(adapter);
            subjectName.setShowSoftInputOnFocus(false);
            for (Subject subject : subjects) {
                if (subject.getId().equals(imageNote.getSubject_id()))
                    subjectName.setText(subject.getName());
            }
        });

        subjectName.setOnItemClickListener((parent, view, pos, arg) -> {
            Object item = parent.getItemAtPosition(pos);
            if (item instanceof Subject) {
                imageNote.setSubject_id(((Subject) item).getId());
            }
        });

        save.setOnClickListener(view -> {
            viewModel.insertImageNote(imageNote);
            dismiss();
        });

        cancel.setOnClickListener(view -> dismiss());

        editPhoto.setOnClickListener(view -> {
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Continue only if the File was successfully created
            Uri photoURI = FileProvider.getUriForFile(getContext(),
                    "com.example.android.fileprovider",
                    photoFile);
            camera.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(camera, REQUEST_TAKE_PHOTO);
        });
    }

    // TODO: 2019-11-22 this
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Uri conrentUri = Uri.fromFile(photoFile);
        photo.setImageURI(conrentUri);
    }
}
