package com.eot_app.api_call_controller;

import android.content.Context;
import android.util.Log;

import com.eot_app.services.ApiClient;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.JsonObject;

import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class ApiCallController {
    private static ApiCallController apiCallController;
    private Context context;
    private ApiCallCallBack apiCallCallBack;

    public static ApiCallController getApiCallController() {
        if (apiCallController == null) {
            apiCallController = new ApiCallController();
        }
        return apiCallController;
    }

    public void setApiCallCallBack(ApiCallCallBack apiCallCallBack, Context context) {
        this.apiCallCallBack = apiCallCallBack;
        this.context = context;
    }


    public void getLoadDataFromSerVer(JsonObject reqData, String ApiName) {
        if (AppUtility.isInternetConnected()) {

            ApiClient.getservices().eotServiceCall(ApiName, AppUtility.getApiHeaders(), reqData)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                apiCallCallBack.getSuccessResponce(jsonObject);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                sessionExpireDialog(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG", e.getMessage());
                            EotApp.getAppinstance().showToastmsg(e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            apiCallCallBack.getApiCallComplete();
                        }
                    });
        } else {
//            apiCallCallBack.disableSwipeRefresh();
            networkerror();
        }
    }

    private void sessionExpireDialog(String msg) {
        AppUtility.alertDialog(context, LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_error_title)
                , msg, LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        EotApp.getAppinstance().sessionExpired();
                        return null;
                    }
                });
    }


    private void networkerror() {

    }
}
