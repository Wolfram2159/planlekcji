package com.wolfram.planlekcji.ui.bottomSheets;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

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
}
