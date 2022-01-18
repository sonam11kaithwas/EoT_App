package com.eot_app.common_api_contr;

import static com.eot_app.utility.AppUtility.getJsonObject;

import com.eot_app.services.ApiClient;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hypertrack.hyperlog.HyperLog;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Sona-11 on 23/8/21.
 */
public class ApiRequestresponce {
    private final ApiCalServerReqRes apiCalServerReqRes;
    private final int requestCode;

    public ApiRequestresponce(ApiCalServerReqRes apiCalServerReqRes, int requestCode) {
        this.apiCalServerReqRes = apiCalServerReqRes;
        this.requestCode = requestCode;
    }

    public void sendReqOnServerGetRes(String url, Object object) {
        if (AppUtility.isInternetConnected()) {
            HyperLog.i("ApiRequestresponce", "sendReqOnServerGetRes(M) started");
            String body = new Gson().toJson(object);
            ApiClient.getservices().eotServiceCall(
                    url, AppUtility.getApiHeaders(),
                    getJsonObject(body)
            ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            HyperLog.i("ApiRequestresponce", "onNext");
                            if (jsonObject != null)
                                apiCalServerReqRes.onSuccess(jsonObject, requestCode);
                        }

                        @Override
                        public void onError(Throwable e) {
                            HyperLog.i("ApiRequestresponce", "onError");
                            apiCalServerReqRes.onError(e, requestCode);

                        }

                        @Override
                        public void onComplete() {
                            //    apiCalServerReqRes.onComplete();

                        }
                    });

        } else {
            EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.network_error));
        }

    }
}
