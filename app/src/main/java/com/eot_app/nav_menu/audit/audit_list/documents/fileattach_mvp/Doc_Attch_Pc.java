package com.eot_app.nav_menu.audit.audit_list.documents.fileattach_mvp;


import android.text.TextUtils;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
import com.eot_app.nav_menu.audit.audit_list.documents.doc_model.GetFileList_Res;
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
import com.google.gson.reflect.TypeToken;

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
 * Created by ubuntu on 8/10/18.
 */

public class Doc_Attch_Pc implements Doc_Attch_Pi {
    private final int updatelimit;
    Doc_Attch_View doc_attch_view;
    private int count;
    private int updateindex;

    public Doc_Attch_Pc(Doc_Attch_View doc_attch_view) {
        this.doc_attch_view = doc_attch_view;
        this.updateindex = 0;
        this.updatelimit = AppConstant.LIMIT_MID;
    }


    @Override
    public void getAttachFileList(final String audId) {
        AuditDocRequest getFileList_model = new AuditDocRequest(updateindex, updatelimit, audId,
                App_preference.getSharedprefInstance().getLoginRes().getUsrId());

        JsonObject jsonObject = AppUtility.getJsonObject(new Gson().toJson(getFileList_model));
//        if (AppUtility.isInternetConnected()) {
        ActivityLogController.saveActivity(
                ActivityLogController.AUDIT_MODULE,
                ActivityLogController.AUDIT_DOC_LIST,
                ActivityLogController.AUDIT_MODULE);
        ApiClient.getservices().eotServiceCall(Service_apis.getAuditAttachments, AppUtility.getApiHeaders(), jsonObject)

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        try {
                            Log.e("FileList", "" + jsonObject.toString());
                            if (jsonObject.get("success").getAsBoolean()) {
                                if (jsonObject.get("data").getAsJsonArray().size() > 0) {
                                    count = jsonObject.get("count").getAsInt();
                                    String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                    Type listType = new TypeToken<List<GetFileList_Res>>() {
                                    }.getType();
                                    ArrayList<GetFileList_Res> getFileList_res = new Gson().fromJson(convert, listType);
                                    doc_attch_view.setList(getFileList_res);
                                } else {
                                    doc_attch_view.addView();
                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                doc_attch_view.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }


                    @Override
                    public void onError(Throwable e) {
                        Log.e("", e.getMessage());
                        // AppUtility.progressBarDissMiss();
                    }

                    @Override
                    public void onComplete() {
                        Log.e("onComplete", "onComplete");
                        //  AppUtility.progressBarDissMiss();
                        if ((updateindex + updatelimit) <= count) {
                            updateindex = updatelimit;
                            getAttachFileList(audId);
                        }
                    }
                });
//        } else {
//            networkDialog();
//        }

    }


    /*
     * filter list and add duplicate entry to separte
     * auditor signature and adding to another index
     * */
    private void filterList(ArrayList<GetFileList_Res> getFileList_res) {

        ArrayList<GetFileList_Res> filterData = new ArrayList<>();
        for (GetFileList_Res response : getFileList_res) {
            if (!TextUtils.isEmpty(response.getAttachAuditSign())) {
                GetFileList_Res auditSign = new GetFileList_Res();

                auditSign.setImage_name(response.getAudImg_name());
                auditSign.setAttachFileActualName(response.getAttachAuditActualSign());
                auditSign.setAttachFileName(response.getAttachAuditSign());
                auditSign.setAttachThumnailFileName(response.getAttachThumnailAuditSign());
                filterData.add(auditSign);

            }

            if (!TextUtils.isEmpty(response.getAttachThumnailFileName()))
                filterData.add(response);
        }

        doc_attch_view.setList(filterData);

    }

    @Override
    public void uploadDocuments(final String job_Id, String file, String finalFname) {
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow((((Fragment) doc_attch_view).getActivity()));
            String mimeType = "";
            MultipartBody.Part body = null;
            File file1 = new File(file);
            if (file1 != null) {
                mimeType = URLConnection.guessContentTypeFromName(file1.getName());
                if (mimeType == null) {
                    mimeType = finalFname;
                }
                RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file1);
                // MultipartBody.Part is used to send also the actual file name
                body = MultipartBody.Part.createFormData("ja", finalFname + file.substring(file.lastIndexOf(".")), requestFile);
            }
            RequestBody jobId = RequestBody.create(MultipartBody.FORM, job_Id);
            RequestBody userId = RequestBody.create(MultipartBody.FORM, App_preference.getSharedprefInstance().getLoginRes().getUsrId());
            RequestBody type = RequestBody.create(MultipartBody.FORM, "2");
            RequestBody docNm = RequestBody.create(MultipartBody.FORM, finalFname);

            ActivityLogController.saveActivity(
                    ActivityLogController.AUDIT_MODULE,
                    ActivityLogController.AUDIT_UPLOAD_DOC,
                    ActivityLogController.AUDIT_MODULE
            );
            ApiClient.getservices().uploadAuditDocements(AppUtility.getApiHeaders(), jobId, userId, docNm, type, body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            Log.e("Responce", jsonObject.toString());
                            if (jsonObject.get("success").getAsBoolean()) {
                                String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                Type listType = new TypeToken<List<GetFileList_Res>>() {
                                }.getType();
                                ArrayList<GetFileList_Res> docList = new Gson().fromJson(convert, listType);
                                doc_attch_view.setList(docList);
                                EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                updateJobData(job_Id);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                doc_attch_view.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {
                                doc_attch_view.fileExtensionNotSupport(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            AppUtility.progressBarDissMiss();
                            Log.e("Error", e.getMessage() + "error");
                            EotApp.getAppinstance().showToastmsg(e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                        }
                    });
        } else {
            networkDialog();
            AppUtility.progressBarDissMiss();
        }

    }

    private void updateJobData(String job_id) {
        try {
            AuditList_Res job = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().getAuditById(job_id);
            /****Notify JOB overView for Attachment Upload first time ****/
            if (job != null && Integer.parseInt(job.getAttachCount()) == 0) {
                job.setAttachCount("1");
                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().updateAuditList_Res(job);
                EotApp.getAppinstance().getJobFlagOverView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void networkDialog() {
        AppUtility.alertDialog((((Fragment) doc_attch_view).getActivity()), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }
}
