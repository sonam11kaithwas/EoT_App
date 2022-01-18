package com.eot_app.home_screens;

import android.content.Context;
import android.util.Log;

import com.eot_app.lat_lng_sync_pck.LatLngSycn_Controller;
import com.eot_app.login_next.login_next_model.ResLoginData;
import com.eot_app.nav_menu.jobs.job_controller.ChatController;
import com.eot_app.nav_menu.jobs.job_db.Job;
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

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by aplite_pc302 on 8/9/18.
 */

public class MainActivity_pc implements MainActivity_pi {
    private final String CHECK_IN_TYPE = "1";
    private final String CHECK_OUT_TYPE = "2";
    private final MainActivityView mainActivityView;


    public MainActivity_pc(MainActivityView mainActivityView) {
        this.mainActivityView = mainActivityView;

    }


    private void addRecordsToDB(List<Job> data) {
        try {
            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().inserJob(data);
//        for add/remove listener.
            for (Job item : data) {
                if (item.getIsdelete().equals("0")
                        || item.getStatus().equals(AppConstant.Cancel)
                        || item.getStatus().equals(AppConstant.Closed)
                        || item.getStatus().equals(AppConstant.Reject)) {
                    ChatController.getInstance().removeListnerByJobID(item.getJobId());
                } else {
                    ChatController.getInstance().registerChatListner(item);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onLogoutServicecall() {
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow((Context) mainActivityView);
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("udId", App_preference.getSharedprefInstance().getLoginRes().getUdId());
            Gson gson = new Gson();
            String data = gson.toJson(hm);

            if (AppUtility.isInternetConnected())
                ApiClient.getservices().eotServiceCall(Service_apis.logout, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<JsonObject>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onNext(JsonObject jsonObject) {
                                if (jsonObject.get("success").getAsBoolean()) {
                                    mainActivityView.onClearCache();
//                                    add logout call
                                    ResLoginData data = App_preference.getSharedprefInstance().getLoginRes();
                                    data.setToken("");
                                    App_preference.getSharedprefInstance().setLoginResponse(new Gson().toJson(data));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("", e.getMessage());
                                AppUtility.progressBarDissMiss();
                            }

                            @Override
                            public void onComplete() {
                                mainActivityView.onLogout("");
                                AppUtility.progressBarDissMiss();
                                ChatController.getInstance().setAppUserOnline(0);
                            }
                        });
        } else {
            networkDialog();
        }
    }

    @Override
    public void addCheckInOutIime() {
        String usrId = App_preference.getSharedprefInstance().getLoginRes().getUsrId();
        String time = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT_new);
        String checkId = App_preference.getSharedprefInstance().getcheckId();
        String checkType = App_preference.getSharedprefInstance().getcheckId().isEmpty() ? CHECK_IN_TYPE : CHECK_OUT_TYPE;
        if (AppUtility.isInternetConnected()) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("usrId", usrId);
            hm.put("time", time);
            hm.put("checkId", checkId);
            hm.put("checkType", checkType);

            if (AppUtility.isGPSEnabled()) {
                hm.put("lat", LatLngSycn_Controller.getInstance().getLat());
                hm.put("lng", LatLngSycn_Controller.getInstance().getLng());
            } else {
                hm.put("lat", "0.0");
                hm.put("lng", "0.0");
            }

            Gson gson = new Gson();
            String data = gson.toJson(hm);

            if (AppUtility.isInternetConnected())
                ApiClient.getservices().eotServiceCall(Service_apis.addCheckInOutIime, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<JsonObject>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onNext(JsonObject jsonObject) {
                                if (jsonObject.get("success").getAsBoolean()) {
                                    Log.e("TAG: ", "TAG");
                                    JsonObject dataobj = jsonObject.getAsJsonObject("data");
                                    if (dataobj != null) {
                                        String checkId = dataobj.get("checkId").getAsString();
                                        App_preference.getSharedprefInstance().setcheckId(checkId);
                                        mainActivityView.checkIdUpdateUI(checkId, jsonObject.get("message").getAsString());
                                    } else {
                                        App_preference.getSharedprefInstance().setcheckId("");
                                        mainActivityView.checkIdUpdateUI("", jsonObject.get("message").getAsString());
                                    }
                                } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {

                                    EotApp.getAppinstance().sessionExpired();
                                } else {
                                    Log.e("TAG: ", "TAG");
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("", e.getMessage());
                                mainActivityView.check_In_Out_Fail();
                            }

                            @Override
                            public void onComplete() {
                            }
                        });
        } else {
            networkDialog();
        }
    }

    private void networkDialog() {
        AppUtility.alertDialog(((Context) mainActivityView), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }
}
