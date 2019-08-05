package com.wolfram.planlekcji.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.wolfram.planlekcji.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * @author Wolfram
 * @date 2019-08-05
 */
//todo: delete this class
public class SecondDialog extends DialogFragment {

    private Integer value = 100;
    private SimpleCallback callback;

    public interface SimpleCallback {
        void transferValue(Integer value);
    }

    public SecondDialog(SimpleCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View root = inflater.inflate(R.layout.second_dialog, null);

        builder.setView(root);

        TimePicker tp = root.findViewById(R.id.time_picker);

        tp.setOnTimeChangedListener((picker, hour, minute) -> {
            Log.w("TimePicker time = ","Hour" + hour + " minute " + minute); //Work properly
        });

        /*Button btn = root.findViewById(R.id.button3);

        btn.setOnClickListener((v)->{
            Log.w("Click", "btn3");
            callback.transferValue(value);
        });*/

        builder.setPositiveButton("OK", (dialog, id) -> {
            Log.w("", "second Dialog");
        });

        return builder.create();
    }
}
