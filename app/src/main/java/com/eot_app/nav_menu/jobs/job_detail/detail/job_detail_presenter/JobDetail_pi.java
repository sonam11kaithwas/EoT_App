package com.eot_app.nav_menu.jobs.job_detail.detail.job_detail_presenter;

import com.eot_app.nav_menu.jobs.job_detail.detail.jobdetial_model.JobStatusModel;

/**
 * Created by aplite_pc302 on 6/25/18.
 */

public interface JobDetail_pi {
    void changeJobStatusAlertInvisible(String jobId, String type, JobStatusModel status, String lat, String lng, String isMailSentToClt);//,String cltMailConfirmEnable);

    String getStatusName(String status);

    boolean isOldStaus(String status_no, String jobId);

    void setJobCurrentStatus(String jobid);

    boolean checkContactHideOrNot();

    JobStatusModel getJobStatusObject(String statusId);

    void getCustomFieldQues(String jobId);

    void getQuestByParntId(String formId, String jobId);

    void stopRecurpattern(String jobId);

    void getAttachFileList(String jobId, String usrId, String type);

    String getImg();
}
