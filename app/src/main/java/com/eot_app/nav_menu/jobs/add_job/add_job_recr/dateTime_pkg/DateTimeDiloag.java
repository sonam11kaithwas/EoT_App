package com.eot_app.nav_menu.jobs.add_job.add_job_recr.dateTime_pkg;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeDiloag extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private final DateTimeCallBack dateTimeCallBack;
    private final boolean StartRecrCheck;

    public DateTimeDiloag(DateTimeCallBack dateTimeCallBack, boolean StartRecrCheck) {
        this.dateTimeCallBack = dateTimeCallBack;
        /***@StartRecrCheck use for Prevoius Date Disable for Start Date Filed*******/
        this.StartRecrCheck = StartRecrCheck;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        if (StartRecrCheck)
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        return datePickerDialog;
    }


    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String currentDateString = "";
        try {
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

            Date startDate = formatter.parse(dayOfMonth + "-" + (month + 1) + "-" + year);
            currentDateString = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateTimeCallBack.getDateTimeFromPicker(currentDateString);
    }

}
