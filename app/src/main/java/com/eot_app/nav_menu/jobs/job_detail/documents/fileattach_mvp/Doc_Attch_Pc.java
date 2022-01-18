package com.eot_app.nav_menu.jobs.job_detail.documents.fileattach_mvp;


import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_detail.documents.doc_model.DocUpdateRequest;
import com.eot_app.nav_menu.jobs.job_detail.documents.doc_model.GetFileList_Res;
import com.eot_app.nav_menu.jobs.job_detail.documents.doc_model.GetFileList_req_Model;
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
    public void getAttachFileList(final String jobId, final String usrId, final String type) {
        GetFileList_req_Model getFileList_model = new GetFileList_req_Model(updateindex, updatelimit, jobId, usrId, type);
        JsonObject jsonObject = AppUtility.getJsonObject(new Gson().toJson(getFileList_model));

        if (AppUtility.isInternetConnected()) {
            ActivityLogController.saveActivity(ActivityLogController.JOB_MODULE, ActivityLogController.JOB_DOCUMENT_LIST, ActivityLogController.JOB_MODULE);
            ApiClient.getservices().eotServiceCall(Service_apis.getJobAttachments, AppUtility.getApiHeaders(), jsonObject)

                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            Log.e("FileList", "" + jsonObject.toString());
                            if (jsonObject.get("success").getAsBoolean()) {
                                if (jsonObject.get("data").getAsJsonArray().size() > 0) {
                                    count = jsonObject.get("count").getAsInt();
                                    String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                    Type listType = new TypeToken<List<GetFileList_Res>>() {
                                    }.getType();
                                    ArrayList<GetFileList_Res> getFileList_res = new Gson().fromJson(convert, listType);
                                    doc_attch_view.setList(getFileList_res, "");
                                } else {
                                    doc_attch_view.addView();
                                    doc_attch_view.setList(new ArrayList<GetFileList_Res>(), "");
                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                doc_attch_view.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("", e.getMessage());
                            // AppUtility.progressBarDissMiss();
                            doc_attch_view.hideProgressBar();
                        }

                        @Override
                        public void onComplete() {
                            doc_attch_view.hideProgressBar();
                            Log.e("onComplete", "onComplete");
                            //  AppUtility.progressBarDissMiss();
                            if ((updateindex + updatelimit) <= count) {
                                updateindex = updatelimit;
                                getAttachFileList(jobId, usrId, type);
                            }
                        }
                    });
        } else {
            networkDialog();
        }

    }

    @Override
    public void uploadDocuments(final String job_Id, String file, String finalFname, String des, String type, final String isAddAttachAsCompletionNote) {

        if (AppUtility.isInternetConnected()) {
            ActivityLogController.saveActivity(ActivityLogController.JOB_MODULE, ActivityLogController.JOB_UPLOAD_DOC, ActivityLogController.JOB_MODULE);
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
            final RequestBody jobId = RequestBody.create(MultipartBody.FORM, job_Id);
            RequestBody docName = RequestBody.create(MultipartBody.FORM, finalFname);
            RequestBody descBody = RequestBody.create(MultipartBody.FORM, des);
            RequestBody userId = RequestBody.create(MultipartBody.FORM, App_preference.getSharedprefInstance().getLoginRes().getUsrId());
            RequestBody typeId = RequestBody.create(MultipartBody.FORM, type);
            RequestBody isAddAttachAsCompletionNoteBody = RequestBody.create(MultipartBody.FORM, isAddAttachAsCompletionNote);

            //     AppUtility.progressBarShow(((Fragment) doc_attch_view).getActivity());
            ApiClient.getservices().uploadDocements(AppUtility.getApiHeaders(), jobId, userId, descBody, typeId, docName, isAddAttachAsCompletionNoteBody, body)
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
                                if (isAddAttachAsCompletionNote.equals("1")) {
                                    AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                                            .jobModel().updateComplitionNotes(docList.get(0).getComplNote(), job_Id);
                                    EotApp.getAppinstance().notifyObserver("removeFW", "complition", docList.get(0).getComplNote());
                                }
                                doc_attch_view.setList(docList, isAddAttachAsCompletionNote);
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
                            Log.e("Error", e.getMessage());
                            EotApp.getAppinstance().showToastmsg(e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                        }
                    });
        } else {
            AppUtility.progressBarDissMiss();
            networkDialog();
        }
    }

    private void updateJobData(String job_id) {
        try {
            Job job = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(job_id);
            /****Notify JOB overView for Attachment Upload first time ****/
            if (job != null && Integer.parseInt(job.getAttachCount()) == 0) {
                job.setAttachCount("1");
                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().updateJob(job);
                EotApp.getAppinstance().getJobFlagOverView();
            }
            EotApp.getAppinstance().getNotifyForAttchCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateDocuments(String docId, String des, final String isAddAttachAsCompletionNote, final String jobId) {
        if (AppUtility.isInternetConnected()) {

            DocUpdateRequest docUpdateRequest = new DocUpdateRequest(docId, des, isAddAttachAsCompletionNote);
            String request = new Gson().toJson(docUpdateRequest);
            ApiClient.getservices().eotServiceCall(Service_apis.updateJObDocument,
                    AppUtility.getApiHeaders(), AppUtility.getJsonObject(request))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            try {
                                if (jsonObject.get("success").getAsBoolean()) {
                                    String message = LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString());
                                    if (isAddAttachAsCompletionNote.equals("1") && jsonObject.getAsJsonArray("data").size() > 0) {
                                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                                                .jobModel().updateComplitionNotes(jsonObject.getAsJsonArray("data").get(0).getAsJsonObject().get("complNote").getAsString(), jobId);
                                        EotApp.getAppinstance().notifyObserver("removeFW", "complition", jsonObject.getAsJsonArray("data").get(0).getAsJsonObject().get("complNote").getAsString());
                                    }
                                    doc_attch_view.onDocumentUpdate(message, true);
                                } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                    doc_attch_view.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                } else {
                                    String message = LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString());
                                    doc_attch_view.onDocumentUpdate(message, false);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            //  remark_view.onSessionExpire("");
                            AppUtility.progressBarDissMiss();
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                        }
                    });

        } else {
            AppUtility.progressBarDissMiss();
            networkDialog();
        }
    }


    private void networkDialog() {
        try {
            Context mContext = null;
            if (doc_attch_view instanceof Fragment)
                mContext = (((Fragment) doc_attch_view).getActivity());
            else if (doc_attch_view instanceof Activity)
                mContext = (Activity) doc_attch_view;
            AppUtility.alertDialog(mContext, LanguageController.getInstance()
                    .getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().
                    getMobileMsgByKey(AppConstant.err_check_network), LanguageController.getInstance().
                    getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
