package org.mini.frame.view;

import android.app.TimePickerDialog;
import android.content.Context;

/**
 * Created by hucheng on 2017/3/28.
 */

public class MiniUIPickTimeDialog extends TimePickerDialog
{


    public MiniUIPickTimeDialog(Context context, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, listener, hourOfDay, minute, is24HourView);
    }

    public MiniUIPickTimeDialog(Context context, int themeResId, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, themeResId, listener, hourOfDay, minute, is24HourView);
    }

}
