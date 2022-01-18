package com.eot_app.nav_menu.equipment.View.Job_Equipment_MVP;

import com.eot_app.nav_menu.jobs.job_detail.detail.job_detail_presenter.JobDetail_pi;

public interface Job_Equipment_PI extends JobDetail_pi {


    void getJobCompletionDetails(String jobId);

    void getJobItemList(String jobId);
}
