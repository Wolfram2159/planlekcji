package com.wolfram.planlekcji.ui.bottomSheets;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.wolfram.planlekcji.database.room.entities.SubjectEntity;
import com.wolfram.planlekcji.ui.fragments.events.EventFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Wolfram
 * @date 2019-08-28
 */
public abstract class CustomBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    protected View root;
    protected String tag;
    protected Context context;
    protected FragmentActivity activity;

    public final static String CREATE = "CreateBottomSheet";
    public final static String MODIFY = "ModifyBottomSheet";
    public final static String ACTION = "ActionBottomSheet";

    protected interface TimeSetter {
        void onTimeSet(Date time);
    }

    protected interface AutocompleteAdapterSetter {
        AutoCompleteTextView getAdapterView();

        List<String> getList();
    }

    public static class AutoCompleteTextViewData {
        private List<String> items;
        private AutoCompleteTextView textView;
        private boolean isAccessible;
        private AdapterView.OnItemClickListener onItemClickListener;

        public AutoCompleteTextViewData(@NonNull List<String> items, @NonNull AutoCompleteTextView textView, boolean isAccessible) {
            this.items = items;
            this.textView = textView;
            this.isAccessible = isAccessible;
        }

        public AutoCompleteTextViewData(@NonNull List<String> items, @NonNull AutoCompleteTextView textView, @Nullable AdapterView.OnItemClickListener onItemClickListener, boolean isAccessible) {
            this.items = items;
            this.textView = textView;
            this.isAccessible = isAccessible;
            this.onItemClickListener = onItemClickListener;
        }

        public List<String> getItems() {
            return items;
        }

        public AutoCompleteTextView getTextView() {
            return textView;
        }

        public boolean isAccessible() {
            return isAccessible;
        }

        public AdapterView.OnItemClickListener getOnItemClickListener() {
            return onItemClickListener;
        }
    }

    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        tag = getTag();
        context = getContext();
        activity = getActivity();
        root = LayoutInflater.from(getContext()).inflate(getResource(), null);
        dialog.setContentView(root);
        customizeDialog();
    }

    protected abstract int getResource();

    protected abstract void customizeDialog();

    protected void setAdapterToAutoCompleteTextView(AutoCompleteTextViewData data) {
        AutoCompleteTextView textView = data.getTextView();
        List<String> items = data.getItems();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_list_item_1,
                items
        );
        textView.setShowSoftInputOnFocus(data.isAccessible());
        if (!(data.isAccessible() || tag.equals(CustomBottomSheet.MODIFY) || items.size() == 0)) textView.setText(items.get(0));
        textView.setAdapter(arrayAdapter);
        textView.setOnItemClickListener(data.getOnItemClickListener());
    }

    protected void setAdapterToView(AutocompleteAdapterSetter adapterSetter) {
        setAdapterToView(adapterSetter, null);
    }

    protected void setAdapterToView(AutocompleteAdapterSetter adapterSetter, AdapterView.OnItemClickListener onItemClickListener) {
        AutoCompleteTextView textView = adapterSetter.getAdapterView();
        List<String> list = adapterSetter.getList();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_list_item_1,
                list
        );
        //if (list.size() > 1 && !tag.equals(CustomBottomSheet.MODIFY)) textView.setText(list.get(0));
        textView.setAdapter(arrayAdapter);
        //textView.setShowSoftInputOnFocus(false);
        //textView.setKeyListener(null);
        textView.setOnItemClickListener((adapterView, view, i, l) -> {
            Object item = adapterView.getAdapter().getItem(i);
            String selected = (String) item;
        });
    }

    protected void setAdapterToView(AutoCompleteTextView textView, List<SubjectEntity> list) {
        ArrayAdapter<SubjectEntity> arrayAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_list_item_1,
                list
        );
        //if (list.size() > 1 && !tag.equals(CustomBottomSheet.MODIFY)) textView.setText(list.get(0));
        textView.setAdapter(arrayAdapter);
        //textView.setShowSoftInputOnFocus(false);
        //textView.setKeyListener(null);
        textView.setOnItemClickListener((adapterView, view, i, l) -> {
            Object item = adapterView.getAdapter().getItem(i);
            SubjectEntity selected = (SubjectEntity) item;
        });
    }

    protected void createTimePicker(TimeSetter timeSetter) {
        Date dateNow = new Date();
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, (timePicker, hour, minute) -> {
            Date time = new Date(2019, 10, 10, hour, minute);
            timeSetter.onTimeSet(time);
        }, dateNow.getHours(), dateNow.getMinutes(), true);
        timePickerDialog.show();
    }

    protected void createDatePicker(TimeSetter timeSetter) {
        Date dateNow = new Date();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                activity,
                (datePicker, year, month, day) -> {
                    Date date = new Date((year-1900), month, day);
                    timeSetter.onTimeSet(date);
                },
                (1900 + dateNow.getYear()), dateNow.getMonth(), dateNow.getDate());
        datePickerDialog.show();
    }

    protected List<String> getSubjectsNames(List<SubjectEntity> subjects) {
        List<String> subjectsNames = new ArrayList<>();
        for (SubjectEntity subject : subjects) {
            String subjectName = subject.getName();
            subjectsNames.add(subjectName);
        }
        return subjectsNames;
    }
}
