package com.wolfram.planlekcji.ui.bottomSheets.notes;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.notes.ImageNote;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.fragments.notes.NotesFragmentViewModel;
import com.wolfram.planlekcji.custom.others.Utils;

import java.io.File;
import java.util.Date;

import androidx.lifecycle.ViewModelProviders;

/**
 * @author Wolfram
 * @date 2019-10-11
 */
public class AddImageNoteBottomSheet extends CustomBottomSheet {

    @Override
    protected int getResource() {
        return R.layout.notes_image_bottom_sheet;
    }

    @Override
    protected void customizeDialog() {
        NotesFragmentViewModel viewModel = ViewModelProviders.of(getActivity()).get(NotesFragmentViewModel.class);

        ImageNote newImageNote = new ImageNote();

        ImageView photo = root.findViewById(R.id.notes_image_photo);
        ImageView invisibleImageView = root.findViewById(R.id.notes_image_edit);
        invisibleImageView.setVisibility(View.INVISIBLE);
        AutoCompleteTextView subjectName = root.findViewById(R.id.notes_image_name);
        EditText date = root.findViewById(R.id.notes_image_date);
        MaterialButton save = root.findViewById(R.id.notes_image_save);
        MaterialButton cancel = root.findViewById(R.id.notes_image_cancel);

        viewModel.getSubjects().observe(this, subjects -> {
            ArrayAdapter<Subject> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, subjects);
            subjectName.setAdapter(adapter);
            subjectName.setShowSoftInputOnFocus(false);
        });

        subjectName.setOnItemClickListener((parent, view, pos, arg) -> {
            Object item = parent.getItemAtPosition(pos);
            if (item instanceof Subject){
                newImageNote.setSubject_id(((Subject) item).getId());
            }
        });

        Date currentDate = viewModel.getCurrentDate();
        String createTime = Utils.getDateString(currentDate);
        date.setText(createTime);
        newImageNote.setDate(currentDate);

        date.setOnClickListener(view -> {
            DatePickerDialog pickerDialog = new DatePickerDialog(getContext(), (picker, year, month, day) -> {
                Date pickedDate = new Date((year-1900), month, day);
                String myDate = Utils.getDateString(pickedDate);
                date.setText(myDate);
                newImageNote.setDate(pickedDate);
            }, (1900 + currentDate.getYear()), currentDate.getMonth(), currentDate.getDate());
            pickerDialog.show();
        });

        File f = new File(viewModel.getCurrentPhotoPath());
        Uri contentUri = Uri.fromFile(f);
        photo.setImageURI(contentUri);
        newImageNote.setPhotoPath(viewModel.getCurrentPhotoPath());

        save.setOnClickListener(view -> {
            viewModel.insertImageNote(newImageNote);
            dismiss();
        });

        cancel.setOnClickListener(view -> {
            viewModel.deleteImage();
            dismiss();
        });
    }
}
