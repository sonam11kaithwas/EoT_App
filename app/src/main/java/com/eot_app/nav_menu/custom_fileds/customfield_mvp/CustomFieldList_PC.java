package com.eot_app.nav_menu.custom_fileds.customfield_mvp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.ans_model.Ans_Req;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Sonam-11 on 14/9/20.
 */
public class CustomFieldList_PC implements CustomFieldList_Pi {
    private final CustomFieldList_View customFieldListView;

    public CustomFieldList_PC(CustomFieldList_View customFieldListView) {
        this.customFieldListView = customFieldListView;
    }

    /***Give anser by Fw*/
    @Override
    public void giveAns(Ans_Req ans_req) {
        if (AppUtility.isInternetConnected()) {
            ActivityLogController.saveActivity(
                    ActivityLogController.JOB_MODULE,
                    ActivityLogController.JOB_SUBMIT_ANSER,
                    ActivityLogController.JOB_MODULE
            );
            AppUtility.progressBarShow((Context) customFieldListView);
            ApiClient.getservices().eotServiceCall(Service_apis.addAnswer, AppUtility.getApiHeaders(),
                    AppUtility.getJsonObject(new Gson().toJson(ans_req)))
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                    subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            Log.e("Responce", jsonObject.toString());
                            if (jsonObject.get("success").getAsBoolean()) {
                                Toast.makeText((Context) customFieldListView, LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()), Toast.LENGTH_SHORT).show();
                                customFieldListView.onSubmitSuccess();
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                customFieldListView.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("Error", e.getMessage());
                            AppUtility.progressBarDissMiss();
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                        }
                    });
        } else {
            // customFieldListView.showOfflineAlert(LanguageController.getInstance().getMobileMsgByKey(AppConstant.offline_feature_alert));
        }

    }
}
