package com.eot_app.nav_menu.jobs.job_detail.history;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class history_pc implements history_pi {

    private final int updatelimit;
    private final List<History> historyList = new ArrayList<>();
    String id;
    history_listview history_listview;
    private int count;
    private int updateindex;

    public history_pc(history_listview history_listview) {
        this.history_listview = history_listview;
        this.updatelimit = AppConstant.LIMIT_MID;
    }

    @Override
    public void getHistoryList() {
        loadHsitoryFromServer();
    }


    @Override
    public void loadHsitoryFromServer() {
        if (AppUtility.isInternetConnected()) {
            ActivityLogController.saveActivity(ActivityLogController.JOB_MODULE, ActivityLogController.JOB_HISTORY, ActivityLogController.JOB_MODULE);


            History_Request_model history_request_model = new History_Request_model(
                    App_preference.getSharedprefInstance().getLoginRes().getCompId(),
                    App_preference.getSharedprefInstance().getLoginRes().getUsrId(),
                    updatelimit, updateindex, id);

            String data = new Gson().toJson(history_request_model);
            //AppUtility.progressBarShow(((android.support.v4.app.Fragment) history_listview).getActivity());
            ApiClient.getservices().eotServiceCall(Service_apis.getJobStatusHistoryMobile, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
//                                 first confirm field worker is remove or not
                                if (jsonObject.get("data").getAsJsonArray().size() > 0) {
                                    if (jsonObject.get("data").getAsJsonArray().get(0).getAsJsonObject().get("status_code") != null &&
                                            jsonObject.get("data").getAsJsonArray().get(0).getAsJsonObject().get("status_code").getAsString()
                                                    .equals("1")) {
                                        String jobId = jsonObject.get("data").getAsJsonArray().get(0).getAsJsonObject().get("jobid").getAsString();
                                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().deleteJobById(jobId);
//                                    for alert msg to remove job
                                        EotApp.getAppinstance().notifyObserver("removeFW", jobId, LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                    } else {
                                        count = jsonObject.get("count").getAsInt();
                                        String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                        Type listType = new TypeToken<List<History>>() {
                                        }.getType();
                                        List<History> data = new Gson().fromJson(convert, listType);
                                        historyList.addAll(data);
//                                addRecordsToDB(data);
                                    }
                                } else {
                                    history_listview.setdata(historyList);
                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                history_listview.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG: ", "error");
                            //AppUtility.progressBarDissMiss();
                        }

                        @Override
                        public void onComplete() {
                            //AppUtility.progressBarDissMiss();
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                loadHsitoryFromServer();
                            } else {
                                updateindex = 0;
                                count = 0;
                                history_listview.setdata(historyList);
                            }
                        }
                    });
        } else {
            // history_listview.netwrkDialog();
            netWrkError();
        }
    }

    @Override
    public void addRecordsToDB(List<History> data) {
//        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).historyDao().insertHistory(data);
    }

    @Override
    public void getJobId(String jobId) {
        id = jobId;
    }

    public void netWrkError() {
        AppUtility.alertDialog(((Fragment) history_listview).getActivity(), LanguageController.getInstance().
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
