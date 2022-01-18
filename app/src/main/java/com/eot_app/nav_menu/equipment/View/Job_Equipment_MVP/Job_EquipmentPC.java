package com.eot_app.nav_menu.equipment.View.Job_Equipment_MVP;

import android.content.Context;
import android.util.Log;

import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.InvoiceItemDataModel;
import com.eot_app.nav_menu.jobs.job_detail.detail.job_detail_presenter.JobDetail_pc;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.language_support.LanguageController;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Job_EquipmentPC extends JobDetail_pc implements Job_Equipment_PI {
    Job_Equipment_View equipment_view;
    Job job;

    public Job_EquipmentPC(Job_Equipment_View view) {
        super(view);
        this.equipment_view = view;
    }


    @Override
    public void getJobCompletionDetails(String jobId) {
        if (AppUtility.isInternetConnected()) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("jobId", jobId);
            hashMap.put("type", "1");
            ApiClient.getservices().eotServiceCall(Service_apis.getJobCompletionNOte, AppUtility.getApiHeaders(),
                    AppUtility.getJsonObject(new Gson().toJson(hashMap)))
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
                                    String completionNote = jsonObject.getAsJsonArray("data").get(0).getAsJsonObject().get("complNote").getAsString();
                                    equipment_view.setCompletionNote(completionNote);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                equipment_view.sessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }


                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG", e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        }
    }

    @Override
    public void getJobItemList(String jobId) {
        if (AppUtility.isInternetConnected()) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("jobId", jobId);
            String data = new Gson().toJson(hashMap);
            ApiClient.getservices().eotServiceCall(Service_apis.getItemFromJob, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
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
                                Type listType = new TypeToken<List<InvoiceItemDataModel>>() {
                                }.getType();
                                List<InvoiceItemDataModel> data = new Gson().fromJson(convert, listType);
                                if (data != null && data.size() > 0) {
                                    equipment_view.setJobItemList(data);
                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                equipment_view.sessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        }
    }


    private void netWork_erroR() {
        AppUtility.alertDialog(((Context) equipment_view), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }
}
