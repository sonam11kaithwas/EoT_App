package com.eot_app.nav_menu.quote.quote_invoice_pkg.quote_mvp_pkg;

import android.content.Context;
import android.util.Log;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.nav_menu.quote.add_quotes_pkg.model_pkg.Remove_ItemData;
import com.eot_app.nav_menu.quote.quote_invoice_pkg.quote_model_pkg.Quote_InvoiceDetails_ReQ;
import com.eot_app.nav_menu.quote.quote_invoice_pkg.quote_model_pkg.QuotesDetails;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Quo_Inv_Pc implements Quo_Invo_Pi {
    private final Quo_Invo_View quotesList_view;

    public Quo_Inv_Pc(Quo_Invo_View quotesList_view) {
        this.quotesList_view = quotesList_view;
    }

    @Override
    public void getQuotesInvoiceDetails(String quotId) {

        Quote_InvoiceDetails_ReQ model = new Quote_InvoiceDetails_ReQ(quotId);
        JsonObject jsonObject = AppUtility.getJsonObject(new Gson().toJson(model));

        if (AppUtility.isInternetConnected()) {
            ActivityLogController.saveActivity(
                    ActivityLogController.QUOTE_MODULE,
                    ActivityLogController.QUOTE_INVOICE_DETAILS,
                    ActivityLogController.QUOTE_MODULE
            );
            ApiClient.getservices().eotServiceCall(Service_apis.getQuotationInvoiceDetail, AppUtility.getApiHeaders(), jsonObject)

                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            Log.e("", "" + jsonObject.toString());
                            if (jsonObject.get("success").getAsBoolean()) {
                                Gson gson = new Gson();
                                QuotesDetails quotesDetails = gson.fromJson(jsonObject.get("data"), QuotesDetails.class);
                                quotesList_view.setQuotesDetails(quotesDetails);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                quotesList_view.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {

                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            quotesList_view.dismissPullTorefresh();
                        }

                        @Override
                        public void onComplete() {
                            quotesList_view.dismissPullTorefresh();
                        }
                    });


        } else {
            networkError();
            quotesList_view.dismissPullTorefresh();
        }


    }


    @Override
    public void removeQuotesItem(ArrayList<String> itemId, String invId) {
        Remove_ItemData rmObject = new Remove_ItemData(itemId, invId);


        JsonObject jsonObject = AppUtility.getJsonObject(new Gson().toJson(rmObject));

        if (AppUtility.isInternetConnected()) {
            ActivityLogController.saveActivity(
                    ActivityLogController.QUOTE_MODULE,
                    ActivityLogController.QUOTE_REMOVE_ITEM,
                    ActivityLogController.QUOTE_MODULE
            );
            AppUtility.progressBarShow((Context) quotesList_view);
            ApiClient.getservices().eotServiceCall(Service_apis.deleteQuotItemForMobile, AppUtility.getApiHeaders(), jsonObject)

                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            Log.e("", "" + jsonObject.toString());
                            if (jsonObject.get("success").getAsBoolean()) {
                                EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                AppUtility.progressBarDissMiss();
                                quotesList_view.itemdeletedSuccefully();
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                quotesList_view.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("", e.getMessage());
                            AppUtility.progressBarDissMiss();
                            quotesList_view.dismissPullTorefresh();


                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                            quotesList_view.dismissPullTorefresh();
                        }
                    });


        } else {
            networkError();
            quotesList_view.dismissPullTorefresh();
        }


    }

    @Override
    public void generateQuotPDF(String quotId) {
        Map<String, String> param = new HashMap<>();
        param.put("quotId", quotId);
        JsonObject jsonObject = AppUtility.getJsonObject(new Gson().toJson(param));

        if (AppUtility.isInternetConnected()) {
            ActivityLogController.saveActivity(
                    ActivityLogController.QUOTE_MODULE,
                    ActivityLogController.QUOTE_GENERATE_PDF,
                    ActivityLogController.QUOTE_MODULE
            );
            AppUtility.progressBarShow((Context) quotesList_view);
            ApiClient.getservices().eotServiceCall(Service_apis.generateQuotPDF, AppUtility.getApiHeaders(), jsonObject)

                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            Log.e("Responce--->>>", "" + jsonObject.toString());
                            if (jsonObject.get("success").getAsBoolean()) {
                                quotesList_view.onGetPdfPath(jsonObject.getAsJsonObject("data").get("path").getAsString());
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                quotesList_view.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            AppUtility.progressBarDissMiss();
                            Log.e("", e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                        }
                    });


        } else {
            networkError();
            quotesList_view.dismissPullTorefresh();
        }
    }

    @Override
    public void convertQuotationToJob(String quotId) {
        Map<String, String> param = new HashMap<>();
        param.put("quotId", quotId);
        param.put("compId", App_preference.getSharedprefInstance().getLoginRes().getCompId());
        JsonObject jsonObject = AppUtility.getJsonObject(new Gson().toJson(param));

        if (AppUtility.isInternetConnected()) {
            ActivityLogController.saveActivity(
                    ActivityLogController.QUOTE_MODULE,
                    ActivityLogController.QUOUTE_CONVERT_JOB,
                    ActivityLogController.QUOTE_MODULE
            );
            AppUtility.progressBarShow((Context) quotesList_view);
            ApiClient.getservices().eotServiceCall(Service_apis.convertQuotationToJob, AppUtility.getApiHeaders(), jsonObject)

                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            Log.e("Responce--->>>", "" + jsonObject.toString());
                            if (jsonObject.get("success").getAsBoolean()) {
                                quotesList_view.onConvertQuotationToJob(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                quotesList_view.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            AppUtility.progressBarDissMiss();
                            Log.e("", e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                        }
                    });


        } else {
            networkError();
            quotesList_view.dismissPullTorefresh();
        }
    }

    private void networkError() {
        AppUtility.alertDialog(((Context) quotesList_view), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }
}
