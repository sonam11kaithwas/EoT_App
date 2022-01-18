package com.eot_app.nav_menu.jobs.job_detail.job_audit_remark_attchment_pkg.mvp;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.eot_app.nav_menu.audit.audit_list.documents.doc_model.GetFileList_Res;
import com.eot_app.nav_menu.jobs.job_detail.job_audit_remark_attchment_pkg.model_pkg.JobAuditSingleAttchReqModel;
import com.eot_app.services.ApiClient;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hypertrack.hyperlog.HyperLog;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Sona-11 on 1/11/21.
 */
public class JobAudit_Pc implements JobAudit_PI {
    private final JobAudit_View jobAuditView;

    public JobAudit_Pc(JobAudit_View jobAuditView) {
        this.jobAuditView = jobAuditView;
    }

    @Override
    public void uploadAttchmentOnserverForJobAudit(JobAuditSingleAttchReqModel model) {
        if (AppUtility.isInternetConnected()) {
            HyperLog.i("", "JobEquRemark_PC: " + "addNewRemark:::: Start");
            String mimeType = "";
            MultipartBody.Part body = null;
            if (!TextUtils.isEmpty(model.getPath())) {
                File file1 = new File(model.getPath());
                if (file1 != null) {
                    mimeType = URLConnection.guessContentTypeFromName(file1.getName());
                    if (mimeType == null) {
                        mimeType = file1.getName();
                    }
                    RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file1);
                    // MultipartBody.Part is used to send also the actual file name
                    body = MultipartBody.Part.createFormData("ja", file1.getName()
                            , requestFile);
                }
            }
            RequestBody audId = RequestBody.create(MultipartBody.FORM, model.getAudId());

            RequestBody equId = RequestBody.create(MultipartBody.FORM, model.getEquId());
            RequestBody isJob = RequestBody.create(MultipartBody.FORM, model.getIsJob());
            RequestBody usrId = RequestBody.create(MultipartBody.FORM, App_preference.getSharedprefInstance().getLoginRes().getUsrId());

            AppUtility.progressBarShow((Context) jobAuditView);


            ApiClient.getservices().uploadSingleAttchmentForJobAudit(AppUtility.getApiHeaders(),
                    audId,
                    equId,
                    usrId,
                    isJob,
                    body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            try {
                                HyperLog.i("", "JobEquRemark_PC: " + "JsonObject:::: " + jsonObject.toString());

                                Log.d("mahi", jsonObject.toString());
                                if (jsonObject.get("success").getAsBoolean()) {

                                    String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                    Type listType = new TypeToken<List<GetFileList_Res>>() {
                                    }.getType();
                                    /*imp this coversionsion*/
                                    ArrayList<GetFileList_Res> getFileList_res = new Gson().fromJson(convert, listType);
                                    String str = new Gson().toJson(getFileList_res);

                                    EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                    jobAuditView.attchmentUpload(str);
                                } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                    jobAuditView.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                } else {
                                    String message = LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString());
                                    jobAuditView.onErrorMsg(message);
                                }
                            } catch (Exception e) {
                                HyperLog.i("", "JobEquRemark_PC: " + "Exception:::: " + e.getMessage());
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("mahi", e.toString());
                            //  remark_view.onSessionExpire("");
                            HyperLog.i("", "JobEquRemark_PC: " + "onError:::: " + e.getMessage());

                            AppUtility.progressBarDissMiss();
                        }

                        @Override
                        public void onComplete() {
                            Log.d("mahi", "Completed");
                            AppUtility.progressBarDissMiss();
                        }
                    });
        } else {
            networkError();
        }
    }

    private void networkError() {
        AppUtility.alertDialog(((Context) jobAuditView), LanguageController.getInstance().
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
