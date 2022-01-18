package com.eot_app.nav_menu.jobs.job_detail.detail.job_detail_view;

import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFormQuestionsRes;
import com.eot_app.nav_menu.jobs.job_detail.detail.jobdetial_model.JobStatusModel;
import com.eot_app.nav_menu.jobs.job_detail.documents.doc_model.GetFileList_Res;

import java.util.ArrayList;

/**
 * Created by aplite_pc302 on 6/25/18.
 */

public interface JobDetail_view {

    void setButtonsUI(JobStatusModel jobstatus);

    void setResultForChangeInJob(String update, String jobid);

    void resetstatus(String status_no);

    void setCustomFiledList(ArrayList<CustOmFormQuestionsRes> dataList);

    void sessionExpire(String msg);

    void StopRecurPatternHide();

    void setList(ArrayList<GetFileList_Res> getFileList_res, String isAttachCompletionNotes);

}
