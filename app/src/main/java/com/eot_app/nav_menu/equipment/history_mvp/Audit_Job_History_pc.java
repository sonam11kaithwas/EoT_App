package com.eot_app.nav_menu.equipment.history_mvp;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
import com.eot_app.nav_menu.equipment.model.aduit_job_history.Aduit_Job_History_Req;
import com.eot_app.nav_menu.equipment.model.aduit_job_history.Aduit_Job_History_Res;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Audit_Job_History_pc implements Audit_Job_History_pi {
    private final Audit_Job_History_View audit_history_view;
    private final List<Aduit_Job_History_Res> aduit_res;
    private final List<Aduit_Job_History_Res> job_res;
    private AuditList_Res auditList_res;
    private Job job;

    public Audit_Job_History_pc(Audit_Job_History_View audit_history_view) {
        this.audit_history_view = audit_history_view;
        aduit_res = new ArrayList<>();
        job_res = new ArrayList<>();
    }

    @Override
    public void getApiForUploadAttchment(String equ_Id, String usr_Manual_Doc) {
        if (AppUtility.isInternetConnected()) {
            String mimeType = "";
            MultipartBody.Part body = null;
            List<MultipartBody.Part> filesList = new ArrayList<>();
            if (!TextUtils.isEmpty(usr_Manual_Doc)) {
                File file1 = new File(usr_Manual_Doc);
                if (file1 != null) {
                    mimeType = URLConnection.guessContentTypeFromName(file1.getName());
                    if (mimeType == null) {
                        mimeType = file1.getName();
                    }
                    RequestBody usrManualDoc = RequestBody.create(MediaType.parse(mimeType), file1);
                    // MultipartBody.Part is used to send also the actual file name
                    body = MultipartBody.Part.createFormData("usrManualDoc",
                            file1.getName()
                            , usrManualDoc);
                    filesList.add(body);
                }
            }
            RequestBody equId = RequestBody.create(MultipartBody.FORM, equ_Id);

            AppUtility.progressBarShow((Context) audit_history_view);

            ApiClient.getservices().uploadEquDetailAttchment(AppUtility.getApiHeaders(),
                    equId,
                    body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            try {
                                Log.d("mahi", jsonObject.toString());
                                if (jsonObject.get("success").getAsBoolean()) {
                                    String message = LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString());
                                    EotApp.getAppinstance().showToastmsg(message);

                                    audit_history_view.uploadAttchView(jsonObject.get("data").getAsString());
                                } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                    audit_history_view.sessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                } else {
                                    String message = LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString());
                                    audit_history_view.finishErroroccur(message);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("mahi", e.toString());
                            AppUtility.progressBarDissMiss();
                            audit_history_view.finishErroroccur(e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            Log.d("mahi", "Completed");
                            AppUtility.progressBarDissMiss();
                        }
                    });

        } else {
            audit_history_view.finishErroroccur(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network));
        }

    }

    @Override
    public void getEquipmentAduitHistory(String equId) {
        if (AppUtility.isInternetConnected()) {
            Aduit_Job_History_Req model = new Aduit_Job_History_Req(equId, "1");
            String data = new Gson().toJson(model);
            ApiClient.getservices().eotServiceCall(Service_apis.getEquAuditSchedule,
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
                                String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                Type listType = new TypeToken<List<Aduit_Job_History_Res>>() {
                                }.getType();
                                List<Aduit_Job_History_Res> list = new Gson().fromJson(convert, listType);
                                audit_history_view.setEquipmentAduitList(list);
                                if (list != null)
                                    audit_history_view.getAduitSize(list.size());

                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            if (audit_history_view != null)
                audit_history_view.setNetworkError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network));


        }
    }

    @Override
    public void getEquipmentJobHistory(String equId) {
        if (AppUtility.isInternetConnected()) {
            Aduit_Job_History_Req model = new Aduit_Job_History_Req(equId, "0");
            String data = new Gson().toJson(model);
            ApiClient.getservices().eotServiceCall(Service_apis.getEquAuditSchedule,
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
                                String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                Type listType = new TypeToken<List<Aduit_Job_History_Res>>() {
                                }.getType();
                                List<Aduit_Job_History_Res> list = new Gson().fromJson(convert, listType);
                                audit_history_view.setEquipmentJobList(list);
                                if (list != null)
                                    audit_history_view.getJobSize(list.size());

                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {


                        }
                    });
        } else {
            if (audit_history_view != null)
                audit_history_view.setNetworkError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network));


        }
    }


    @Override
    public void getEquipmentAduitDetails(String audId) {
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow((Context) audit_history_view);
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
                                        audit_history_view.setAduditDetails(auditList_res);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                audit_history_view.sessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
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
            netWork_erroR();

        }
    }


    @Override
    public void getEquipmentJobDetails(String jobId) {
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow((Context) audit_history_view);
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
                                        audit_history_view.setJobDetails(job);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                audit_history_view.sessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
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
            netWork_erroR();

        }
    }


    private void netWork_erroR() {
        AppUtility.alertDialog(((Context) audit_history_view), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }

}
