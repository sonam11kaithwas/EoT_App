package com.eot_app.nav_menu.client.clientlist.client_detail.work_history.history_presenter;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.eot_app.nav_menu.appointment.dbappointment.Appointment;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
import com.eot_app.nav_menu.client.clientlist.client_detail.work_history.ClientWorkHistoryList;
import com.eot_app.nav_menu.client.clientlist.client_detail.work_history.model.AduitAppointmentHistoryReq;
import com.eot_app.nav_menu.client.clientlist.client_detail.work_history.model.JobHistoryReq;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Mahendra Dabi on 12/3/21.
 */
public class WorkHistoryPC implements WorkHistoryPI {
    private final int updatelimit;
    private final WorkHistoryView workHistoryView;
    AuditList_Res auditList_res;
    Job job;
    private int job_count;
    private int audit_count;
    private int appointment_count;
    private int updateJobindex;
    private int updateAuditindex;
    private int updateAppointmentindex;
    private Appointment app_details;


    public WorkHistoryPC(WorkHistoryView workHistoryView) {
        this.workHistoryView = workHistoryView;
        this.updatelimit = AppConstant.LIMIT_HIGH;
        this.updateJobindex = 0;
        this.updateAuditindex = 0;
        this.updateAppointmentindex = 0;

    }


    @Override
    public void getJobList(final String cltId) {
        if (AppUtility.isInternetConnected()) {
            if (updateJobindex > 0)
                workHistoryView.showMoreLoadingProgress(true);
            JobHistoryReq req = new JobHistoryReq(cltId, updateJobindex, updatelimit);
            String data = new Gson().toJson(req);
            ApiClient.getservices().eotServiceCall(Service_apis.getAdminJobList, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                try {
                                    job_count = jsonObject.get("count").getAsInt();
                                    String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                    Type listType = new TypeToken<List<Job>>() {
                                    }.getType();
                                    List<Job> data = new Gson().fromJson(convert, listType);
                                    workHistoryView.setJobList(data);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                workHistoryView.sessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }


                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG", e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            workHistoryView.showMoreLoadingProgress(false);

                        }
                    });
        } else {
            networkError();
        }
    }

    @Override
    public void getAuditList(String cltId) {
        if (AppUtility.isInternetConnected()) {
            if (updateAuditindex > 0)
                workHistoryView.showMoreLoadingProgress(true);
            AduitAppointmentHistoryReq req = new AduitAppointmentHistoryReq(cltId, updateAuditindex, updatelimit);
            String data = new Gson().toJson(req);
            ApiClient.getservices().eotServiceCall(Service_apis.getAuditAdminList, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                try {
                                    audit_count = jsonObject.get("count").getAsInt();
                                    String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                    Type listType = new TypeToken<List<AuditList_Res>>() {
                                    }.getType();
                                    List<AuditList_Res> data = new Gson().fromJson(convert, listType);
                                    workHistoryView.setAuditList(data);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                workHistoryView.sessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }


                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG", e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            workHistoryView.showMoreLoadingProgress(false);
                        }
                    });
        } else {
            networkError();
        }
    }

    @Override
    public void getAppointmentList(String cltId) {
        if (AppUtility.isInternetConnected()) {
            if (updateAppointmentindex > 0)
                workHistoryView.showMoreLoadingProgress(true);
            AduitAppointmentHistoryReq req = new AduitAppointmentHistoryReq(cltId, updateAppointmentindex, updatelimit);
            String data = new Gson().toJson(req);
            ApiClient.getservices().eotServiceCall(Service_apis.getAppointmentAdminList, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                try {

                                    appointment_count = jsonObject.get("count").getAsInt();
                                    String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                    Type listType = new TypeToken<List<Appointment>>() {
                                    }.getType();
                                    List<Appointment> data = new Gson().fromJson(convert, listType);
                                    workHistoryView.setAppointmentList(data);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                workHistoryView.sessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }


                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG", e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            workHistoryView.showMoreLoadingProgress(false);
                        }
                    });
        } else {
            networkError();
        }
    }


    @Override
    public void getAppointmentDetails(String appId) {
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow(((Fragment) workHistoryView).getActivity());

            Map<String, String> model = new HashMap<>();
            model.put("appId", appId);
            String data = new Gson().toJson(model);
            ApiClient.getservices().eotServiceCall(Service_apis.getAppointmentDetail, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                try {
                                    String convert = new Gson().toJson(jsonObject.get("data").getAsJsonObject());
                                    app_details = new Gson().fromJson(convert, Appointment.class);
                                    if (app_details != null)
                                        workHistoryView.setAppointmentDetails(app_details);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                workHistoryView.sessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }


                        }

                        @Override
                        public void onError(Throwable e) {
                            AppUtility.progressBarDissMiss();
                            Log.e("TAG", e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();


                        }
                    });
        } else {
            networkError();
        }
    }

    @Override
    public void loadMoreItem(ClientWorkHistoryList.FragmentTypes types, String cltId) {
        switch (types) {
            case JOB:
                if ((updateJobindex + updatelimit) <= job_count) {
                    updateJobindex += updatelimit;
                    getJobList(cltId);
                } else workHistoryView.showMoreLoadingProgress(false);
                break;

            case AUDIT:
                if ((updateAuditindex + updatelimit) <= audit_count) {
                    updateAuditindex += updatelimit;
                    getAuditList(cltId);
                } else workHistoryView.showMoreLoadingProgress(false);
                break;


            case APPOINTMENT:
                if ((updateAppointmentindex + updatelimit) <= appointment_count) {
                    updateAppointmentindex += updatelimit;
                    getAppointmentList(cltId);
                } else workHistoryView.showMoreLoadingProgress(false);
                break;
        }
    }


    @Override
    public void getAduitDetails(String audId) {
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow(((Fragment) workHistoryView).getActivity());
            Map<String, String> model = new HashMap<>();
            model.put("audId", audId);
            String data = new Gson().toJson(model);

            ApiClient.getservices().eotServiceCall(Service_apis.getAuditDetail,
                    AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                try {
                                    String convert = new Gson().toJson(jsonObject.get("data").getAsJsonObject());
                                    auditList_res = new Gson().fromJson(convert, AuditList_Res.class);
                                    if (auditList_res != null)
                                        workHistoryView.setAduditDetails(auditList_res);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                workHistoryView.sessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }


                        }

                        @Override
                        public void onError(Throwable e) {
                            AppUtility.progressBarDissMiss();
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();

                        }
                    });
        } else {
            networkError();

        }
    }


    @Override
    public void getJobDetails(String jobId) {
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow(((Fragment) workHistoryView).getActivity());
            Map<String, String> model = new HashMap<>();
            model.put("jobId", jobId);
            String data = new Gson().toJson(model);

            ApiClient.getservices().eotServiceCall(Service_apis.getJobDetail,
                    AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                try {
                                    String convert = new Gson().toJson(jsonObject.get("data").getAsJsonObject());
                                    job = new Gson().fromJson(convert, Job.class);
                                    if (job != null)
                                        workHistoryView.setJobDetails(job);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                workHistoryView.sessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            AppUtility.progressBarDissMiss();
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();

                        }
                    });
        } else {
            networkError();
        }
    }


    private void networkError() {
        AppUtility.alertDialog(((Fragment) workHistoryView).getActivity(), LanguageController.getInstance().
                        getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey
                        (AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok),
                "", new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }
}
