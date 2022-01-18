package com.eot_app.nav_menu.jobs.add_job.job_weekly_pkg;

import com.eot_app.utility.AppUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sona-11 on 24/3/21.
 */
public class JOB_weekly_recur_PC implements JOB_weekly_recur_PI {
    private final JOB_weekly_recur_View jobWeeklyRecurView;

    public JOB_weekly_recur_PC(JOB_weekly_recur_View jobWeeklyRecurView) {
        this.jobWeeklyRecurView = jobWeeklyRecurView;
    }


    @Override
    public void getTillDateForRecur(String startDate) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

        Date date = null;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        startDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(date);
        startDate = AppUtility.getDate(startDate);

        try {
            calendar.setTime(simpleDateFormat.parse(startDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.MONTH, 1);

        jobWeeklyRecurView.setTillDateForRecur(simpleDateFormat.format(calendar.getTime()));
    }
}
