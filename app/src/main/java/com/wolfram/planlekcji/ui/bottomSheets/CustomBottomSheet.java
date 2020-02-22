package com.wolfram.planlekcji.ui.bottomSheets;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.wolfram.planlekcji.ui.fragments.events.EventFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

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
        if (list.size() > 1 && !tag.equals(CustomBottomSheet.MODIFY)) textView.setText(list.get(0));
        textView.setAdapter(arrayAdapter);
        textView.setShowSoftInputOnFocus(false);
        textView.setOnItemClickListener(onItemClickListener);
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
}
