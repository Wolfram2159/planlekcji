package com.wolfram.planlekcji.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.wolfram.planlekcji.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * @author Wolfram
 * @date 2019-08-02
 */
public class SimpleDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.subject_dialog, null));
        builder.setMessage("test ?")
                .setPositiveButton("Ok", (d, id) -> {

                })
                .setNegativeButton("Cancel", (d, id) -> {

                });
        return builder.create();
    }
}
