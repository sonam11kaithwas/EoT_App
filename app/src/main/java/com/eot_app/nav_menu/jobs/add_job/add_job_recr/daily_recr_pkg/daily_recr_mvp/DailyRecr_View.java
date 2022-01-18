package com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.daily_recr_mvp;

import com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.daily_recur_model.DailyMsgResModel;

/**
 * Created by Sona-11 on 15/3/21.
 */
public interface DailyRecr_View {
    //    void setEndAfterDate(String endAfterDate);
//
//    void setOccuranceDays(String eccurDays);
    void onSessionExpired(String message);

    void showMsgOnView(DailyMsgResModel dailyMsgResModel);

}
