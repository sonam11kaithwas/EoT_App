package com.eot_app.nav_menu.jobs.job_complation.complat_mvp;

import android.util.Log;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.activitylog.LogModel;
import com.eot_app.nav_menu.jobs.job_complation.compla_model.JobComplation;
import com.eot_app.nav_menu.jobs.job_complation.compla_model.RemoveAttchment;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.db.OfflineDataController;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Sonam-11 on 2020-02-04.
 */
public class Compl_PC implements Compl_PI {
    private final Compla_View complaView;

    public Compl_PC(Compla_View complaView) {
        this.complaView = complaView;
    }

    @Override
    public void removeUploadAttchment(final String jaId) {
        if (AppUtility.isInternetConnected()) {

            RemoveAttchment jobListRequestModel = new RemoveAttchment(jaId);

            String data = new Gson().toJson(jobListRequestModel);
            ApiClient.getservices().eotServiceCall(Service_apis.deleteDocument, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                Log.e("", "");
                                EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                complaView.uploadDocDelete("");
                                EotApp.getAppinstance().getNotifyForAttchCount();
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                Log.e("", "");
                                complaView.sessionexpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {
                                complaView.uploadDocDelete(jsonObject.get("message").getAsString());
                            }
                        }


                        @Override
                        public void onError(Throwable e) {
                            complaView.uploadDocDelete(e.getMessage());
                            Log.e("TAG", e.getMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
        }
    }

    @Override
    public void addEditJobComplation(String jobId, String complNote) {

        JobComplation request = new JobComplation
                (jobId, complNote);
        String dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);
        Gson gson = new Gson();
        String data = gson.toJson(request);

        //save offline data for future sync
        OfflineDataController.getInstance().addInOfflineDB(Service_apis.setCompletionNotes, data, dateTime);
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().updateJobComplation(jobId, complNote, dateTime);
        complaView.updateDetailJob(complNote);

        LogModel logModel = ActivityLogController
                .getObj(ActivityLogController.JOB_MODULE, ActivityLogController.JOB_COMPLETION_NOTE, ActivityLogController.JOB_MODULE);
        ActivityLogController.saveOfflineTable(logModel);

    }
}
