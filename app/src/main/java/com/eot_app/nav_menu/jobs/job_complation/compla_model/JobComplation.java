package com.eot_app.nav_menu.jobs.job_complation.compla_model;

import com.eot_app.utility.App_preference;

/**
 * Created by Sonam-11 on 2020-02-04.
 */
public class JobComplation {
    String jobId;
    String usrId;
    String complNote;

    public JobComplation(String jobId, String complNote) {
        this.jobId = jobId;
        this.usrId = App_preference.getSharedprefInstance().
                getLoginRes().getUsrId();
        this.complNote = complNote;
    }
}
