package com.wolfram.planlekcji.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * @author Wolfram
 * @date 2019-08-05
 */
public abstract class CustomDialog extends DialogFragment {

    //Wzorzec - Metoda Szablonowa ? :)

    protected int resource;
    protected AlertDialog.Builder builder;
    protected View root;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        setResource();

        builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        root = inflater.inflate(resource, null);

        builder.setView(root);

        customizeDialog();

        return builder.create();
    }

    protected abstract void setResource();

    protected abstract void customizeDialog();
}
