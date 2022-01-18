package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.Inv_detail_mvp_pkg;

import android.content.Context;
import android.util.Log;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.activitylog.LogModel;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Inv_List_Req_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Inv_Res_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.ItemData;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Tax;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_model_pkg.Invoice_Update_Request_Model;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Inv_Details_Pc implements Inv_Details_Pi {
    private final Inv_Details_View inv_details_view;

    public Inv_Details_Pc(Inv_Details_View inv_details_view) {
        this.inv_details_view = inv_details_view;
    }

    @Override
    public void getinvoicedetails(final String jobId) {
        Inv_List_Req_Model inv_list_req_model = new Inv_List_Req_Model(jobId);
        JsonObject jsonObject = AppUtility.getJsonObject(new Gson().toJson(inv_list_req_model));

        if (AppUtility.isInternetConnected()) {
            LogModel logModel = ActivityLogController
                    .getObj(ActivityLogController.JOB_MODULE, ActivityLogController.JOB_INVOICE_LIST, ActivityLogController.JOB_MODULE);
            ActivityLogController.saveOfflineTable(logModel);
            AppUtility.progressBarShow((Context) inv_details_view);
            ApiClient.getservices().eotServiceCall(Service_apis.getInvoiceDetail, AppUtility.getApiHeaders(), jsonObject)

                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                Gson gson = new Gson();
                                Inv_Res_Model invResModel = gson.fromJson(jsonObject.get("data"), Inv_Res_Model.class);
                                List<ItemData> itemDataList = new ArrayList<>();
                                for (ItemData itemData : invResModel.getItemData()) {
                                    itemData.setAmount(itemData.getRate());
                                    itemDataList.add(itemData);
                                }
                                invResModel.getItemData().clear();
                                invResModel.setItemData(itemDataList);
                                inv_details_view.setInvoiceDetails(invResModel);
                                inv_details_view.dismissPullTorefresh();
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                inv_details_view.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {
                                inv_details_view.InvoiceNotFound(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("", e.getMessage());
                            AppUtility.progressBarDissMiss();
                            inv_details_view.dismissPullTorefresh();
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                            inv_details_view.dismissPullTorefresh();
                        }
                    });


        } else {
            //    networkError();
            inv_details_view.dismissPullTorefresh();
        }
    }

    private void networkError() {
        AppUtility.alertDialog(((Context) inv_details_view), LanguageController.getInstance().
                getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().
                getMobileMsgByKey(AppConstant.err_check_network), LanguageController.getInstance().
                getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }

    @Override
    public void rmInvooiceItemApiCall(final Invoice_Update_Request_Model inv_res_model) {
        try {
            for (ItemData itemdata : inv_res_model.getItemData()) {
                for (Tax tax : itemdata.getTax()) {
                    if (tax.getRate() == null) {
                        tax.setRate(tax.getRate());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        JsonObject jsonObject = AppUtility.getJsonObject(new Gson().toJson(inv_res_model));
        if (AppUtility.isInternetConnected()) {
            LogModel logModel = ActivityLogController
                    .getObj(ActivityLogController.JOB_MODULE, ActivityLogController.JOB_INVOICE_ITEM_DELETE, ActivityLogController.JOB_MODULE);
            ActivityLogController.saveOfflineTable(logModel);

            AppUtility.progressBarShow((Context) inv_details_view);
            ApiClient.getservices().eotServiceCall(Service_apis.updateInvoice, AppUtility.getApiHeaders(), jsonObject)

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
                                EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.remove_item_msg));
                                String convert = new Gson().toJson(jsonObject.get("data"));
                                Inv_Res_Model invResModel = new Gson().fromJson(convert, Inv_Res_Model.class);
                                List<ItemData> itemDataList = new ArrayList<>();
                                for (ItemData itemData : inv_res_model.getItemData()) {
                                    itemData.setAmount(AppUtility.getCalculatedAmount(itemData.getQty(), itemData.getRate(), itemData.getDiscount(),
                                            itemData.getTax(), invResModel.getTaxCalculationType()));
                                    itemDataList.add(itemData);
                                }
                                inv_res_model.getItemData().clear();
                                inv_res_model.setItemData(itemDataList);
                                inv_details_view.setItemDataList(invResModel);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                inv_details_view.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {

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
        }

    }

    @Override
    public void getGenerateInvoicePdf(String invId) {
        final Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("invId", invId);
        JsonObject jsonObject = AppUtility.getJsonObject(new Gson().toJson(jsonMap));
        if (AppUtility.isInternetConnected()) {
            ActivityLogController.saveActivity(
                    ActivityLogController.JOB_MODULE,
                    ActivityLogController.JOB_GENERATE_INVOICE_PDF,
                    ActivityLogController.JOB_MODULE
            );
            AppUtility.progressBarShow((Context) inv_details_view);
            ApiClient.getservices().eotServiceCall(Service_apis.generateInvoicePDF, AppUtility.getApiHeaders(), jsonObject)

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
                                inv_details_view.onGetPdfPath(jsonObject.getAsJsonObject("data").get("path").getAsString());
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                inv_details_view.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {

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
        }
    }
}
