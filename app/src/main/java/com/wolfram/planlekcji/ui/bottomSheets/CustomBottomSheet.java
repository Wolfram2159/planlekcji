package com.wolfram.planlekcji.ui.bottomSheets;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;

/**
 * @author Wolfram
 * @date 2019-08-28
 */
public abstract class CustomBottomSheet extends BottomSheetDialogFragment {
    protected View root;

    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View v = LayoutInflater.from(getContext()).inflate(getResource(), null);
        root = v;
        dialog.setContentView(v);
        customizeDialog();
    }
    protected abstract int getResource();
    protected abstract void customizeDialog();
}
