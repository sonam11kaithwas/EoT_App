package com.eot_app.nav_menu.jobs.add_job.add_job_recr.weekly_recr_pkg.weekly_recr_mvp;

import com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.daily_recur_model.DailyMsgResModel;

/**
 * Created by Sona-11 on 22/3/21.
 */
public interface WeeklyRecr_View {
    void onSessionExpired(String message);

    void showMsgOnView(DailyMsgResModel dailyMsgResModel);
}
