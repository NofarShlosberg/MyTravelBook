package com.example.finalproject.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class SelectDateDialog extends DatePickerDialog {
    interface ISelectDate {
        void onSelectDate(long date);
    }

    private ISelectDate iSelectDate;
    protected SelectDateDialog(Context context, ISelectDate iSelectDate) {
        super(context);
        this.iSelectDate = iSelectDate;

        setOnDateSetListener((datePicker, day, month, year) -> {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH,day);
            c.set(Calendar.MONTH,month);
            c.set(Calendar.YEAR,year);
            iSelectDate.onSelectDate(c.getTimeInMillis());
        });
    }

}
