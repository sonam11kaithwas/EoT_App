package com.eot_app.nav_menu.appointment;

import static com.eot_app.utility.AppUtility.getJsonObject;

import com.eot_app.services.ApiClient;
import com.eot_app.utility.AppUtility;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Requestor {
    private final ServerResponse serverResponse;
    private final int requestCode;

    public Requestor(ServerResponse serverResponse, int requestCode) {
        this.serverResponse = serverResponse;
        this.requestCode = requestCode;

    }

    public void sendRequestToServer(String url, Object requestBody) {
        if (AppUtility.isInternetConnected()) {
            String body = new Gson().toJson(requestBody);
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
                            if (jsonObject != null)
                                serverResponse.onSuccess(jsonObject, requestCode);
                        }

                        @Override
                        public void onError(Throwable e) {
                            serverResponse.onError(e.toString(), requestCode);

                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        } /*else serverResponse.onError("No internet connection", requestCode);*/
    }
}
