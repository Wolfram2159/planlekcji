package com.wolfram.planlekcji.common.utility;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wolfram.planlekcji.common.enums.ViewType;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;

import java.util.Date;
import java.util.List;

public final class UiUtils {
    private UiUtils() {
    }

    public interface DateSetter {
        void setDate(Date date);
    }

    public static void setAdapterToTextView(@NonNull AutoCompleteTextView textView,
                                            @NonNull List<?> items,
                                            @NonNull ViewType viewType,
                                            @NonNull String tag) {
        setAdapterToTextView(textView, items, viewType, tag, null);
    }

    public static void setAdapterToTextView(@NonNull AutoCompleteTextView textView,
                                            @NonNull List<?> items,
                                            @NonNull ViewType viewType,
                                            @NonNull String tag,
                                            @Nullable AdapterView.OnItemClickListener itemClickListener) {
        Context context = textView.getContext();
        ArrayAdapter<?> arrayAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_list_item_1,
                items
        );
        textView.setShowSoftInputOnFocus(viewType.isEditable());
        if (viewType.isSelectedFirstValue() && tag.equals(CustomBottomSheet.CREATE) && items.size() > 0)
            textView.setText(items.get(0).toString());
        textView.setAdapter(arrayAdapter);
        textView.setOnItemClickListener(itemClickListener);
        if (!viewType.isEditable()) textView.setKeyListener(null);
    }

    public static void setupDatePicker(EditText datePicker, DateSetter dateSetter, ViewType viewType) {
        if (!viewType.isEditable()) {
            datePicker.setShowSoftInputOnFocus(false);
            datePicker.setKeyListener(null);
        }
        Context context = datePicker.getContext();
        datePicker.setOnFocusChangeListener((view, isFocused) -> {
            if (isFocused) {
                switch (viewType) {
                    case DatePicker:
                        createDatePicker(dateSetter, context);
                        break;
                    case TimePicker:
                        createTimePicker(dateSetter, context);
                        break;
                }
            }
        });
    }

    private static void createTimePicker(DateSetter dateSetter, Context context) {
        Date dateNow = new Date();
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, (timePicker, hour, minute) -> {
            Date time = new Date(2019, 10, 10, hour, minute);
            dateSetter.setDate(time);
        }, dateNow.getHours(), dateNow.getMinutes(), true);
        timePickerDialog.show();
    }

    private static void createDatePicker(DateSetter dateSetter, Context context) {
        Date dateNow = new Date();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (datePicker, year, month, day) -> {
            Date date = new Date((year - 1900), month, day);
            dateSetter.setDate(date);
        }, (1900 + dateNow.getYear()), dateNow.getMonth(), dateNow.getDate());
        datePickerDialog.show();
    }
}
