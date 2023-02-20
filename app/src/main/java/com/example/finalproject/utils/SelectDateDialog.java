package com.example.finalproject.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class SelectDateDialog extends DatePickerDialog {


    public static String getDateString(long date) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        return c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);
    }

    public interface ISelectDate {
        void onSelectDate(long date);
    }

    protected SelectDateDialog(Context context, ISelectDate iSelectDate) {
        super(context);
        setOnDateSetListener((datePicker, year, month, day) -> {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH, day);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.YEAR, year);
            iSelectDate.onSelectDate(c.getTimeInMillis());
        });
    }

}
