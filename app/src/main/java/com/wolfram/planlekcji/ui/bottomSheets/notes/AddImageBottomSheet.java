package com.wolfram.planlekcji.ui.bottomSheets.notes;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.notes.Note;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.fragments.notes.NotesFragmentViewModel;
import com.wolfram.planlekcji.utils.others.Utils;

import java.io.File;
import java.util.Date;

import androidx.lifecycle.ViewModelProviders;

/**
 * @author Wolfram
 * @date 2019-10-11
 */
public class AddImageBottomSheet extends CustomBottomSheet {

    private NotesFragmentViewModel viewModel;

    @Override
    protected int getResource() {
        return R.layout.notes_image_bottom_sheet;
    }

    @Override
    protected void customizeDialog() {
        viewModel = ViewModelProviders.of(getActivity()).get(NotesFragmentViewModel.class);

        Note newNote = new Note();

        ImageView photo = root.findViewById(R.id.notes_image_photo);
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
                newNote.setSubject_id(((Subject) item).getId());
            }
        });

        Date photoDate = viewModel.getCurrentDate();
        String takingPhotoTime = Utils.getDateString(photoDate);
        date.setText(takingPhotoTime);
        newNote.setDate(photoDate);

        date.setOnClickListener(view -> {
            DatePickerDialog pickerDialog = new DatePickerDialog(getContext(), (picker, year, month, day) -> {
                Date pickedDate = new Date((year-1900), month, day);
                String myDate = Utils.getDateString(pickedDate);
                date.setText(myDate);
                newNote.setDate(pickedDate);
            }, (1900 + photoDate.getYear()), photoDate.getMonth(), photoDate.getDate());
            pickerDialog.show();
        });

        File f = new File(viewModel.getCurrentPhotoPath());
        Uri contentUri = Uri.fromFile(f);
        photo.setImageURI(contentUri);
        newNote.setPhotoPath(viewModel.getCurrentPhotoPath());


        save.setOnClickListener(view -> {
            viewModel.insertCurrentImage(newNote);
            dismiss();
        });

        cancel.setOnClickListener(view -> {
            dismiss();
        });
    }
}
