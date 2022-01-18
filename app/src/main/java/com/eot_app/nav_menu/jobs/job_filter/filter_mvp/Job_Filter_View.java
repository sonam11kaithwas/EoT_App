package com.eot_app.nav_menu.jobs.job_filter.filter_mvp;

import com.eot_app.nav_menu.jobs.job_detail.detail.jobdetial_model.JobStatusModel;
import com.eot_app.nav_menu.jobs.job_filter.filter_model.JobPrioty;
import com.eot_app.utility.settings.setting_db.TagData;

import java.util.ArrayList;
import java.util.List;

public interface Job_Filter_View {

    void setJobPriopity(ArrayList<JobPrioty> jobPrioties);

    void setstatus(ArrayList<JobStatusModel> jobStatusModels);

    void setTagsList(List<TagData> tagList);

}
