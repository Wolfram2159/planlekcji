package com.wolfram.planlekcji.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.wolfram.planlekcji.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * @author Wolfram
 * @date 2019-08-05
 */
//todo: delete this class
public class FirstDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View root = inflater.inflate(R.layout.first_dialog,null);

        Button btn = root.findViewById(R.id.button2);

        btn.setOnClickListener((v)->{
            Log.w("Click","btn2");//Work

            SecondDialog secondDialog = new SecondDialog((val)->{
                Log.w("Val from second Dialog", "" + val + "");
            });

            secondDialog.show(getFragmentManager(), "Second Dialog");

        });

        builder.setView(
                root
        );

        builder.setPositiveButton("OK", (dialog, id) -> {
            Log.w("","first dialog");
        });

        return builder.create();
    }
}
