package com.wolfram.planlekcji.common.utility;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wolfram.planlekcji.common.enums.TextViewType;
import com.wolfram.planlekcji.ui.bottomSheets.CustomBottomSheet;

import java.util.List;

public final class UiUtils {
    private UiUtils() {
    }

    public static void setAdapterToTextView(@NonNull AutoCompleteTextView textView,
                                            @NonNull List<?> items,
                                            @NonNull TextViewType viewType,
                                            @NonNull String tag) {
        setAdapterToTextView(textView, items, viewType, tag, null);
    }

    public static void setAdapterToTextView(@NonNull AutoCompleteTextView textView,
                                            @NonNull List<?> items,
                                            @NonNull TextViewType viewType,
                                            @NonNull String tag,
                                            @Nullable AdapterView.OnItemClickListener itemClickListener) {
        Context context = textView.getContext();
        ArrayAdapter<?> arrayAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_list_item_1,
                items
        );
        textView.setShowSoftInputOnFocus(viewType.isEditable());
        if (viewType.isSelectedFirstValue() && tag.equals(CustomBottomSheet.CREATE) && items.size() > 0) textView.setText(items.get(0).toString());
        textView.setAdapter(arrayAdapter);
        textView.setOnItemClickListener(itemClickListener);
        if (!viewType.isEditable()) textView.setKeyListener(null);
    }
}
