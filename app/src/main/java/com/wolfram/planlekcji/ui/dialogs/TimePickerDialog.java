package com.wolfram.planlekcji.ui.dialogs;

import android.os.Build;
import android.util.Log;
import android.widget.Button;
import android.widget.TimePicker;

import com.wolfram.planlekcji.R;
import com.wolfram.planlekcji.database.room.entities.Time;

import androidx.annotation.RequiresApi;

/**
 * @author Wolfram
 * @date 2019-08-05
 */
public class TimePickerDialog extends DialogCreator {

    public interface TimeCallback {
        void setTime(Time time);
    }

    private TimeCallback timeCallback;

    public TimePickerDialog(TimeCallback timeCallback) {
        this.timeCallback = timeCallback;
    }

    @Override
    protected void setResource() {
        resource = R.layout.time_picker_dialog;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void customizeDialog() {
        TimePicker timePicker = root.findViewById(R.id.time_picker);

        timePicker.setOnTimeChangedListener((picker, hour, minute)->{
            Log.w("ChangeTime", "" + hour + ":" + minute);
        });

        Button btn = root.findViewById(R.id.button2);

        btn.setOnClickListener((v)->{
            Log.e("Click","Click");
        });

        builder.setPositiveButton("Save", (dialog, id) -> {
            //Log.w("Time", "" + hour + ":" + minute);
            Time t = new Time(timePicker.getHour(), timePicker.getMinute());
            timeCallback.setTime(t);
        })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    //Cancel
                });
    }
}
