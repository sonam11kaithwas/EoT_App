package com.eot_app.nav_menu.jobs.add_job.add_job_recr;

import com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.daily_recur_model.JobRecurModel;

/**
 * Created by Mahendra Dabi on 9/4/21.
 */
public class RecurReqResModel {
    private JobRecurModel transVar;

    public RecurReqResModel(JobRecurModel jobRecurModel) {
        this.transVar = jobRecurModel;
    }

    public JobRecurModel getJobRecurModel() {
        return transVar;
    }

    public void setJobRecurModel(JobRecurModel jobRecurModel) {
        this.transVar = jobRecurModel;
    }
}
