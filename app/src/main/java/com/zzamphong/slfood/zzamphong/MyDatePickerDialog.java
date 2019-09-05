package com.zzamphong.slfood.zzamphong;

/**
 * Created by wonbokyu on 2017-09-13.
 */


import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;

import java.lang.reflect.Field;

class MyDatePickerDialog extends DatePickerDialog {



    public MyDatePickerDialog(Context context, OnDateSetListener callBack, int year,
                              int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);

        try {
            Field[] f = DatePickerDialog.class.getDeclaredFields();
            for (Field dateField : f) {
                if(dateField.getName().equals("mDatePicker")) {
                    dateField.setAccessible(true);

                    DatePicker datePicker = (DatePicker)dateField.get(this);

                    Field datePickerFields[] = dateField.getType().getDeclaredFields();

                    for(Field datePickerField : datePickerFields) {
                        if("mDayPicker".equals(datePickerField.getName())  ||
                                "mDaySpinner".equals(datePickerField.getName() )) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = new Object();
                            dayPicker = datePickerField.get(datePicker);
                            ((View)dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
            setTitle(year+"년 "+monthOfYear+"월");
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        super.onDateChanged(view, year, month, day);
        setTitle(year+"년 "+(month+1)+"월");
    }
}