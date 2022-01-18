package com.eot_app.nav_menu.jobs.job_detail.job_detail_activity_presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.InvoiceItemDataModel;
import com.eot_app.nav_menu.jobs.job_detail.invoice.inventry_pkg.Inventry_ReQ_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.inventry_pkg.Inventry_ReS_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.model_pkg.ItembyJobModel;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hypertrack.hyperlog.HyperLog;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Job_Detail_Activity_pc implements Job_Detail_Activity_pi {//ApiCallCallBack
    private final Job_Detail_Activity_View activity_view;
    private final int updatelimit;
    private int updateindex;
    private int count;
    //  private int STATE = 1;
    private String jobId;

    public Job_Detail_Activity_pc(Job_Detail_Activity_View activity_view) {
        this.activity_view = activity_view;
        this.updatelimit = AppConstant.LIMIT_HIGH;
        /****set current Activity/Fragment Context***/
        //  ApiCallController.getApiCallController().setApiCallCallBack(this, ((Context) activity_view));
    }


  /*  @Override
    public void getSuccessResponce(JsonObject jsonObject) {
        if (jsonObject.get("success").getAsBoolean()) {
            if (STATE == 1) {
                String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                HyperLog.i("", "Job_Detail_Activity_pc: " + convert);
                Type listType = new TypeToken<List<InvoiceItemDataModel>>() {
                }.getType();
                List<InvoiceItemDataModel> data = new Gson().fromJson(convert, listType);
                if (data.size() > 0) {
                    addgetItemFromJobToDB(data, jobId);
                }
                activity_view.moreInvoiceOption(data);
            } else if (STATE == 2) {
                try {
                    count = jsonObject.get("count").getAsInt();
                } catch (Exception exception) {
                    exception.getMessage();
                    count = 0;
                }
                try {
                    if (count < 2000) {
                        String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                        Type listType = new com.google.gson.reflect.TypeToken<List<Inventry_ReS_Model>>() {
                        }.getType();
                        List<Inventry_ReS_Model> inventryitemlist = new Gson().fromJson(convert, listType);
                        if (inventryitemlist.size() > 0) {
                            addInvoiceItemInDB(inventryitemlist);
                        }
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

            } else if (STATE == 3) {
                activity_view.onGetPdfPath(jsonObject.getAsJsonObject("data").get("path").getAsString());
            }
        }
    }

    @Override
    public void getApiErrorResponce(Throwable error) {
        if (STATE == 1)
            HyperLog.i("", "Job_Detail_Activity_pc: " + error.toString());
        else if (STATE == 2)
            HyperLog.i("", "Job_Detail_Activity_pc: " + "getInvoiceItemList(M) " + error.toString());
        else if (STATE == 3)
            AppUtility.progressBarDissMiss();

    }

    @Override
    public void getApiCallComplete() {
        if (STATE == 2) {
            if ((updateindex + updatelimit) <= count) {
                updateindex += updatelimit;
                getInvoiceItemList(STATE);
            } else {
                if (count != 0) {
                    App_preference.getSharedprefInstance()
                            .setInventryItemSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                }
                updateindex = 0;
                count = 0;
            }
        } else if (STATE == 3)
            AppUtility.progressBarDissMiss();
    }*/

    /***get invoice Invetrt Item list****/
    @Override
    public void getInvoiceItemList() {
        Log.e("data--->>>", "data--->>>");
        if (AppUtility.isInternetConnected()) {
            // STATE = type;
            HyperLog.i("", "Job_Detail_Activity_pc: " + "getInvoiceItemList(M) start");
            Inventry_ReQ_Model inventry_model = new
                    Inventry_ReQ_Model(Integer.parseInt(App_preference.getSharedprefInstance().getLoginRes().getCompId()),
                    "",
                    updatelimit,
                    updateindex, App_preference.getSharedprefInstance().getInventryItemSyncTime());//


            String data = new Gson().toJson(inventry_model);

            ApiClient.getservices().eotServiceCall(Service_apis.getItemList, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
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
                                    count = jsonObject.get("count").getAsInt();
                                } catch (Exception exception) {
                                    exception.getMessage();
                                    count = 0;
                                }
                                try {
                                    if (count < 2000) {
                                        String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                        Type listType = new com.google.gson.reflect.TypeToken<List<Inventry_ReS_Model>>() {
                                        }.getType();
                                        List<Inventry_ReS_Model> inventryitemlist = new Gson().fromJson(convert, listType);
                                        if (inventryitemlist.size() > 0) {
                                            addInvoiceItemInDB(inventryitemlist);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.getMessage();
                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                activity_view.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG", e.getMessage());
                            HyperLog.i("", "Job_Detail_Activity_pc: " + "getInvoiceItemList(M) " + e.getMessage());
                            EotApp.getAppinstance().showToastmsg(e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                getInvoiceItemList();
                            }
                        }
                    });


//            ApiCallController.getApiCallController().setApiCallCallBack(this, ((Context) activity_view));
//
//            ApiCallController.getApiCallController().getLoadDataFromSerVer(AppUtility.getJsonObject(data), Service_apis.getItemList);

            HyperLog.i("", "Job_Detail_Activity_pc: " + "getInvoiceItemList(M) completed");
        }
    }

    @Override
    public void printJobCard(String jobId) {
        final Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("jobId", jobId);
        jsonMap.put("techId", App_preference.getSharedprefInstance().getLoginRes().getUsrId());
        JsonObject jsonObject = AppUtility.getJsonObject(new Gson().toJson(jsonMap));
        if (AppUtility.isInternetConnected()) {

            AppUtility.progressBarShow((Context) activity_view);

            ApiClient.getservices().eotServiceCall(Service_apis.generateJobCardPDF, AppUtility.getApiHeaders(), jsonObject)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                activity_view.onGetPdfPath(jsonObject.getAsJsonObject("data").get("path").getAsString());
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                activity_view.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG", e.getMessage());
                            AppUtility.progressBarDissMiss();
                            EotApp.getAppinstance().showToastmsg(e.getMessage());
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


    /***Update Inventry Item's in Local Db**/
    private void addInvoiceItemInDB(List<Inventry_ReS_Model> inventryitemlist) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).invoiceItemDao().insertInvebtryItems(inventryitemlist);
    }

    /****Featch update Job/Invoice Item From Server***/
    @Override
    public void getItemFromServer(final String jobId) {
        if (AppUtility.isInternetConnected()) {
//            STATE = type;
            this.jobId = jobId;
            HyperLog.i("", "Job_Detail_Activity_pc: " + "getItemFromServer(M) start");
            ItembyJobModel model = new ItembyJobModel(jobId);
            String data = new Gson().toJson(model);


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
                                HyperLog.i("", "Job_Detail_Activity_pc: " + convert);
                                Type listType = new TypeToken<List<InvoiceItemDataModel>>() {
                                }.getType();
                                List<InvoiceItemDataModel> data = new Gson().fromJson(convert, listType);
                                if (data.size() > 0) {
                                    addgetItemFromJobToDB(data, jobId);
                                }
                                activity_view.moreInvoiceOption(data);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                activity_view.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG", e.getMessage());
                            HyperLog.i("", "Job_Detail_Activity_pc: " + e.toString());
                            EotApp.getAppinstance().showToastmsg(e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            //      apiCallCallBack.getApiCallComplete();
                        }
                    });


//
//            ApiCallController.getApiCallController().setApiCallCallBack(this, ((Context) activity_view));
//
//
//            ApiCallController.getApiCallController().getLoadDataFromSerVer(AppUtility.getJsonObject(data), Service_apis.getItemFromJob);

            HyperLog.i("", "Job_Detail_Activity_pc: " + "getItemFromServer(M) Complete");
        } else {
            networkError();
        }
    }

    private void addgetItemFromJobToDB(List<InvoiceItemDataModel> data, String jobId) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().updateJobitems(jobId, data);
    }

    private void networkError() {
        AppUtility.alertDialog(((Context) activity_view), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }

    @Override
    public void uploadCustomerSign(final String jobId, File file1) {
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow((Context) activity_view);
            ActivityLogController.saveActivity(ActivityLogController.JOB_MODULE, ActivityLogController.JOB_UPLOAD_DOC, ActivityLogController.JOB_MODULE);
            String mimeType = "";
            MultipartBody.Part body = null;
            if (file1 != null) {
                mimeType = URLConnection.guessContentTypeFromName(file1.getName());
                if (mimeType == null) {
                    mimeType = file1.getAbsolutePath();
                }
                RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file1);
                body = MultipartBody.Part.createFormData("signImg", file1.getAbsolutePath(), requestFile);
            }
            RequestBody requestBody_jobId = RequestBody.create(MultipartBody.FORM, jobId);

            ApiClient.getservices().uploadCustomerSignature(AppUtility.getApiHeaders(),
                    requestBody_jobId, body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                String signaturePath = jsonObject.get("data").getAsString();
                                if (!TextUtils.isEmpty(signaturePath)) {
                                    AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                                            .jobModel().updateSignaturePath(signaturePath, jobId);
                                    activity_view.onSignatureUpload(signaturePath, LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));

                                }

                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                activity_view.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {
                                AppUtility.alertDialog(((Context) activity_view), "", LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                                    @Override
                                    public Boolean call() throws Exception {
                                        return null;
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            HyperLog.i("", e.getMessage());
                            EotApp.getAppinstance().showToastmsg(e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                        }
                    });
        } else {
            AppUtility.progressBarDissMiss();
            networkError();
        }
    }

}
