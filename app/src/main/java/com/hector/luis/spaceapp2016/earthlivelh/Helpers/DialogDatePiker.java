package com.hector.luis.spaceapp2016.earthlivelh.Helpers;

import android.app.Dialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Hector Arredondo on 23/04/2016.
 */
public class DialogDatePiker {
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    Context context;

    public DialogDatePiker(Context context) {
        this.context = context;
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
    }

    public Dialog createDialog() {
        return new android.app.DatePickerDialog(context, myDateListener, year, month, day);
    }

    private android.app.TimePickerDialog.OnTimeSetListener myTimerListener
            = new android.app.TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            final Calendar c = Calendar.getInstance();
            int mHour = c.get(Calendar.HOUR_OF_DAY);
//		        int mMin = c.get(Calendar.MINUTE);
            String mMin = minute+"";
            String am_pm;
            mHour=hourOfDay;

            if(hourOfDay < 12) {
                am_pm = "AM";

            } else {
                am_pm = "PM";
                mHour=mHour-12;
            }

            if(minute<10)
                mMin="0"+minute;
            if(mHour == 0)
                mHour = 12;

            showTime(mHour, mMin, am_pm);
        }

    };

    private android.app.DatePickerDialog.OnDateSetListener myDateListener
            = new android.app.DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker dataPicker, int year, int month, int day) {
            showDate(year, month+1, day);
        }
    };

    /**
     * Metodo que muestra la fecha (sobrecargar al generar la instancia en otra clase)
     * @param year
     * @param month
     * @param day
     */
    public void showDate(int year, int month, int day) {}

    public void showTime(int hourOfDay, String minute, String am_pm){}
}

