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
