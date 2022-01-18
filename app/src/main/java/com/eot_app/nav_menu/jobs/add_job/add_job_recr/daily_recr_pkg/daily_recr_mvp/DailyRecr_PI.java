package com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.daily_recr_mvp;

import com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.daily_recur_model.DailyMsgReqModel;

/**
 * Created by Sona-11 on 15/3/21.
 */
public interface DailyRecr_PI {

    void getDailyRecurMsg(DailyMsgReqModel dailyMsgReqModel);

//    void getendDateForoccurance(String date, int occuranceDays, String defaultDateTime, boolean check);
//
//    void getendDateForEveryRecr(String strDate, String endDate, int days);
//
//    void calculateDaysFromStrToEnd(String strDate, String endDate, String defaultJobDate);
//
//    void getEndDateForWeekRAdio(String date, int occuranceDays, String defaultDateTime);
//
//    int calCulateWeekDay(String strDate, String endDate, String defaultJobDateTime);

}
