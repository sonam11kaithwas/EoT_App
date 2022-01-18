package com.eot_app.nav_menu.jobs.job_detail.detail.job_detail_presenter;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.activitylog.LogModel;
import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFiledReqModel;
import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFiledResModel;
import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFormQuestionsReq;
import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFormQuestionsRes;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.DeleteReCur;
import com.eot_app.nav_menu.jobs.job_controller.ChatController;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_detail.JobDetailActivity;
import com.eot_app.nav_menu.jobs.job_detail.detail.job_detail_view.JobDetail_view;
import com.eot_app.nav_menu.jobs.job_detail.detail.jobdetial_model.JobStatusModel;
import com.eot_app.nav_menu.jobs.job_detail.detail.jobdetial_model.Jobdetail_status_res;
import com.eot_app.nav_menu.jobs.job_detail.documents.doc_model.GetFileList_Res;
import com.eot_app.nav_menu.jobs.job_detail.documents.doc_model.GetFileList_req_Model;
import com.eot_app.nav_menu.jobs.job_detail.job_status_pkg.JobStatus_Controller;
import com.eot_app.nav_menu.jobs.joboffline_db.JobOfflineDataModel;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.db.OfflineDataController;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hypertrack.hyperlog.HyperLog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by aplite_pc302 on 6/25/18.
 */

public class JobDetail_pc implements JobDetail_pi {
    private final int updatelimit;
    JobDetail_view view;
    private int count;
    private int updateindex;
    private JobStatusModel jobstatus;
    private String jobId;
    private String img = "";

    public JobDetail_pc(JobDetail_view view) {
        this.view = view;
        LogModel logModel = ActivityLogController
                .getObj(ActivityLogController.JOB_MODULE, ActivityLogController.JOB_DETAILS, ActivityLogController.JOB_MODULE);
        ActivityLogController.saveOfflineTable(logModel);
        this.updateindex = 0;
        this.updatelimit = AppConstant.LIMIT_MID;
    }


    @Override
    public void getAttachFileList(final String jobId, final String usrId, final String type) {
        try {
            GetFileList_req_Model getFileList_model = new GetFileList_req_Model(updateindex, updatelimit, jobId, usrId, type);
            JsonObject jsonObject = AppUtility.getJsonObject(new Gson().toJson(getFileList_model));

            if (AppUtility.isInternetConnected()) {
                ActivityLogController.saveActivity(ActivityLogController.JOB_MODULE, ActivityLogController.JOB_DOCUMENT_LIST, ActivityLogController.JOB_MODULE);
                ApiClient.getservices().eotServiceCall(Service_apis.getJobAttachments, AppUtility.getApiHeaders(), jsonObject)

                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<JsonObject>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(JsonObject jsonObject) {
                                Log.e("FileList", "" + jsonObject.toString());
                                if (jsonObject.get("success").getAsBoolean()) {
                                    if (jsonObject.get("data").getAsJsonArray().size() > 0) {
                                        try {
                                            count = jsonObject.get("count").getAsInt();
                                            String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                            Type listType = new TypeToken<List<GetFileList_Res>>() {
                                            }.getType();
                                            ArrayList<GetFileList_Res> getFileList_res = new Gson().fromJson(convert, listType);
                                            view.setList(getFileList_res, "");
                                        } catch (Exception exception) {
                                            exception.getMessage();
                                        }
                                    } else {
                                        view.setList(new ArrayList<GetFileList_Res>(), "");
                                    }
                                } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                    //  view.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("", e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                Log.e("onComplete", "onComplete");
                                if ((updateindex + updatelimit) <= count) {
                                    updateindex = updatelimit;
                                    getAttachFileList(jobId, usrId, type);
                                }
                            }
                        });
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void stopRecurpattern(String jobId) {


        ApiClient.getservices().eotServiceCall(Service_apis.deleteRecur, AppUtility.getApiHeaders(),
                AppUtility.getJsonObject(new Gson().toJson(new DeleteReCur(jobId))))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        Log.e("", "");
                        if (jsonObject.get("success").getAsBoolean()) {
                            EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getServerMsgByKey("recur_deleted"));
                            view.StopRecurPatternHide();
                            Log.e("", "");
                        } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                            view.sessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                        } else {

                        }
                    }


                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG", " e.getMessage()");
                    }
                });

    }

    private void showErrorDialog(String msg) {
        AppUtility.error_Alert_Dialog(((Fragment) view).getActivity(), msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok)
                , new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }

    @Override
    synchronized public void changeJobStatusAlertInvisible(String jobid, String type, JobStatusModel jobStatus, String lat, String lng
            , String isMailSentToClt
    ) {
        HyperLog.i("JobDetail_pc", "changeJobStatusAlertInvisible(M) start");
        this.jobstatus = jobStatus;
        this.jobId = jobid;

        String dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);
        Gson gson = new Gson();
        Jobdetail_status_res request = new Jobdetail_status_res(jobId,
                App_preference.getSharedprefInstance().getLoginRes().getUsrId(), type, jobstatus.getStatus_no(),
                dateTime, lat, lng, isMailSentToClt);
        String data = gson.toJson(request);
        Job jobData = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(jobid);
        /***JOB status change Before JOB sync**/
        if (jobData != null && jobData.getJobId() != null && jobData.getTempId() != null && jobData.getJobId().equals(jobData.getTempId())) {

            JobOfflineDataModel jobOfflineDataModel = new JobOfflineDataModel(Service_apis.changeJobStatus, data, dateTime, jobData.getTempId());
            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobOfflineDao().insertJobOfflineData(jobOfflineDataModel);
            // HyperLog.i("", "Job Not Sync -----" + "Add in Job table");
            HyperLog.i("JobDetail_pc", "Job status saved IN LOCAL TABLE with temp id");
            HyperLog.i("JobDetail_pc", "ob status saved In Local Table");
        } else {
            OfflineDataController.getInstance().addInOfflineDB(Service_apis.changeJobStatus, data, dateTime);
            HyperLog.i("JobDetail_pc", "Job status saved on with job id");
            HyperLog.i("JobDetail_pc", "Job status saved In Offline Table");
        }


        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().updateJobByStatus(jobId, jobstatus.getStatus_no(),
                AppUtility.getDateByMiliseconds());
        HyperLog.i("JobDetail_pc", "Local DB updated with selected job status");


        if (jobStatus.getStatus_no().equals(AppConstant.In_Progress)) {
            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().setOtherToPending(jobId, AppConstant.In_Progress, AppConstant.New_On_Hold);
            HyperLog.i("JobDetail_pc", "Local DB refresh for other In progress job");
        }
        view.setResultForChangeInJob("Update", jobid);
        HyperLog.i("JobDetail_pc", "callback completion of local DB update the view on list");

        //data and UI refl\ect after offline save
        if (jobStatus.getStatus_no().equals(AppConstant.Cancel) || jobStatus.getStatus_no().equals(AppConstant.Reject)) {
            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().deleteJobById(jobId);
            ChatController.getInstance().removeListnerByJobID(jobid);
            ((JobDetailActivity) ((Fragment) view).getActivity()).finishActivityWithSetResult();
            HyperLog.i("JobDetail_pc", "delete job with status cancel or reject");

        } else {
            HyperLog.i("JobDetail_pc", "Local DB updated with selected job status");
            view.setButtonsUI(jobstatus);
        }

        LogModel logModel = ActivityLogController
                .getObj(ActivityLogController.JOB_MODULE, ActivityLogController.JOB_STATUS, ActivityLogController.JOB_MODULE);
        ActivityLogController.saveOfflineTable(logModel);

        HyperLog.i("JobDetail_pc", "changeJobStatusAlertInvisible(M) Completed");
    }


    @Override
    public String getStatusName(String status) {
        JobStatusModel jobStatusModel = JobStatus_Controller.getInstance().getStatusObjectById(status);
        if (jobStatusModel != null) {
            if (jobStatusModel.getImg() != null)
                img = jobStatusModel.getImg();
            if (jobStatusModel.getStatus_name() != null && jobStatusModel.getStatus_name().equals("New")) {
                return "Not Started";
            } else {
                return jobStatusModel.getStatus_name();
            }
        }
        return "";
    }

    @Override
    public String getImg() {
        return img;
    }

    @Override
    public JobStatusModel getJobStatusObject(String statusId) {
        JobStatusModel jobStatusModel = JobStatus_Controller.getInstance().getStatusObjectById(statusId);
        if (jobStatusModel != null)
            return jobStatusModel;
        else return new JobStatusModel("", "", "");
    }

    @Override
    public boolean isOldStaus(String status_no, String jobId) {
        if (jobId != null && !jobId.equals("")) {
            Job job = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(jobId);
            if (job != null && job.getStatus() != null && status_no != null && !status_no.equals("")) {
                return AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(jobId).getStatus().equals(status_no);
            }
        }
        return true;
    }

    @Override
    public void setJobCurrentStatus(String jobid) {
        Job item_job = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(jobid);
        view.resetstatus(item_job.getStatus());
    }

    @Override
    public boolean checkContactHideOrNot() {
        try {
            if (App_preference.getSharedprefInstance().getLoginRes().getIsHideContact().equals("1")) {
                return true;
            }

        } catch (Exception e) {

        }
        return false;
    }


    @Override
    public void getCustomFieldQues(final String jobId) {
        if (AppUtility.isInternetConnected()) {
            ApiClient.getservices().eotServiceCall(Service_apis.getFormDetail, AppUtility.getApiHeaders(),
                    AppUtility.getJsonObject(new Gson().toJson(new CustOmFiledReqModel("1"))))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                String convert = jsonObject.get("data").getAsJsonObject().toString();
                                CustOmFiledResModel resModel = new Gson().fromJson(convert, CustOmFiledResModel.class);
                                getQuestByParntId(resModel.getFrmId(), jobId);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                view.sessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {

                            }
                        }


                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG", e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        } else {
            // joblist_view.setRefereshPullOff();
        }
    }

    @Override
    public void getQuestByParntId(String formId, String jobId) {
        CustOmFormQuestionsReq model = new CustOmFormQuestionsReq(formId, jobId);
        if (AppUtility.isInternetConnected()) {
            ApiClient.getservices().eotServiceCall(Service_apis.getQuestionsByParentId, AppUtility.getApiHeaders(),
                    AppUtility.getJsonObject(new Gson().toJson(model)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                String convert = jsonObject.get("data").getAsJsonArray().toString();
                                Type listType = new TypeToken<List<CustOmFormQuestionsRes>>() {
                                }.getType();
                                ArrayList<CustOmFormQuestionsRes> data = new Gson().fromJson(convert, listType);
                                view.setCustomFiledList(data);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                view.sessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {

                            }
                        }


                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG", e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        } else {
            // joblist_view.setRefereshPullOff();
        }
    }
}
