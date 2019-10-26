package com.wolfram.planlekcji.ui.bottomSheets.notes;

import android.app.DatePickerDialog;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.Subject;
import com.wolfram.planlekcji.database.room.entities.notes.TextNote;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;
import com.wolfram.planlekcji.ui.fragments.notes.NotesFragmentViewModel;
import com.wolfram.planlekcji.utils.others.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import androidx.lifecycle.ViewModelProviders;

/**
 * @author Wolfram
 * @date 2019-10-24
 */
public class AddTextNoteBottomSheet extends CustomBottomSheet {
    @Override
    protected int getResource() {
        return R.layout.notes_text_bottom_sheet;
    }

    @Override
    protected void customizeDialog() {
        NotesFragmentViewModel viewModel = ViewModelProviders.of(getActivity()).get(NotesFragmentViewModel.class);

        MaterialButton save = root.findViewById(R.id.notes_text_save);
        MaterialButton cancel = root.findViewById(R.id.notes_text_cancel);

        AutoCompleteTextView subjectName = root.findViewById(R.id.notes_text_name);
        EditText title = root.findViewById(R.id.notes_text_title);
        EditText date = root.findViewById(R.id.notes_text_date);

        TextNote newTextNote = new TextNote();

        viewModel.getSubjects().observe(this, subjects -> {
            ArrayAdapter<Subject> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, subjects);
            subjectName.setAdapter(adapter);
            subjectName.setShowSoftInputOnFocus(false);
        });

        subjectName.setOnItemClickListener((parent, view, pos, arg) -> {
            Object item = parent.getItemAtPosition(pos);
            if (item instanceof Subject){
                newTextNote.setSubject_id(((Subject) item).getId());
            }
        });

        Date currentDate = viewModel.getCurrentDate();
        String createTime = Utils.getDateString(currentDate);
        date.setText(createTime);
        newTextNote.setDate(currentDate);

        date.setOnClickListener(view -> {
            DatePickerDialog pickerDialog = new DatePickerDialog(getContext(), (picker, year, month, day) -> {
                Date pickedDate = new Date((year-1900), month, day);
                String myDate = Utils.getDateString(pickedDate);
                date.setText(myDate);
                newTextNote.setDate(pickedDate);
            }, (1900 + currentDate.getYear()), currentDate.getMonth(), currentDate.getDate());
            pickerDialog.show();
        });

        save.setOnClickListener(view -> {
            newTextNote.setTitle(title.getText().toString());
            File file = null;
            try {
                file = viewModel.createFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (file != null) {
                newTextNote.setFilePath(file.getAbsolutePath());
                viewModel.insertTextNote(newTextNote);
            }
            dismiss();
        });
        cancel.setOnClickListener(view -> {
            viewModel.deleteFile();
            dismiss();
        });
    }
}
