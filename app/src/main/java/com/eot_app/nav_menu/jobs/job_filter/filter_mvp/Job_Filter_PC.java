package com.eot_app.nav_menu.jobs.job_filter.filter_mvp;

import android.content.Context;

import com.eot_app.nav_menu.jobs.job_detail.detail.jobdetial_model.JobStatusModel;
import com.eot_app.nav_menu.jobs.job_filter.filter_model.JobPrioty;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.setting_db.TagData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class Job_Filter_PC implements Job_Filter_Pi {
    Job_Filter_View job_filter_view;


    public Job_Filter_PC(Job_Filter_View job_filter_view) {
        this.job_filter_view = job_filter_view;
    }

    @Override
    public void getTagList() {
        List<TagData> tagData = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).tagmodel().getTagslist();
        job_filter_view.setTagsList(tagData);
    }


    @Override
    public void setStatusList() {
        final ArrayList<JobStatusModel> jobStatusList = new ArrayList<>();
        jobStatusList.add(new JobStatusModel(AppConstant.Not_Started, LanguageController.getInstance().getMobileMsgByKey(AppConstant.status_new)));
        jobStatusList.add(new JobStatusModel((AppConstant.Accepted), LanguageController.getInstance().getMobileMsgByKey(AppConstant.accepted)));
        jobStatusList.add(new JobStatusModel(AppConstant.Travelling, LanguageController.getInstance().getMobileMsgByKey(AppConstant.travelling)));
        jobStatusList.add(new JobStatusModel(AppConstant.Break, LanguageController.getInstance().getMobileMsgByKey(AppConstant.break_key)));
        jobStatusList.add(new JobStatusModel(AppConstant.In_Progress, LanguageController.getInstance().getMobileMsgByKey(AppConstant.In_progress)));
        jobStatusList.add(new JobStatusModel(AppConstant.Pending, LanguageController.getInstance().getMobileMsgByKey(AppConstant.on_hold)));
        jobStatusList.add(new JobStatusModel(AppConstant.Completed, LanguageController.getInstance().getMobileMsgByKey(AppConstant.completed)));
        job_filter_view.setstatus(jobStatusList);
    }


    @Override
    public void setJobPrityList() {
        ArrayList<JobPrioty> jobPrioties = new ArrayList<>();
        jobPrioties.add(new JobPrioty(AppConstant.Low_id, AppConstant.Low));
        jobPrioties.add(new JobPrioty(AppConstant.Medium_id, AppConstant.medium));
        jobPrioties.add(new JobPrioty(AppConstant.High_id, AppConstant.High));
        job_filter_view.setJobPriopity(jobPrioties);
    }

    @Override
    public void emptyFilterListDialog() {
        AppUtility.alertDialog(((Context) job_filter_view), "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_filter), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }
}
