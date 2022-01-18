package com.eot_app.nav_menu.jobs.add_job.add_job_recr.weekly_recr_pkg.weekly_recr_mvp;

import com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.daily_recur_model.DailyMsgReqModel;

/**
 * Created by Sona-11 on 22/3/21.
 */
public interface WeeklyRecr_PI {
    void getDailyRecurMsg(DailyMsgReqModel dailyMsgReqModel);

//    void calcultEndDateByWeekDats(ArrayList<String> SELECTED_DAYS, String strDate, int occurance, String defaultJobDateTime);
//
//    void calcultEndDateByWeekDatsForEvery(ArrayList<String> SELECTED_DAYS, String strDate, int occurance, String defaultJobDateTime, String every);
}
