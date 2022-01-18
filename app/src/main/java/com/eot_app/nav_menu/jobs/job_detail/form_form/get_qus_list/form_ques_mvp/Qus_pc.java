package com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.form_ques_mvp;

import android.content.Context;
import android.util.Log;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.ans_model.Ans_Req;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model.QuesGetModel;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model.QuesRspncModel;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by ubuntu on 18/9/18.
 */

public class Qus_pc implements Que_pi {
    Que_View queAns_view;
    String ansId;

    public Qus_pc(Que_View queAns_view) {
        this.queAns_view = queAns_view;
    }

    /***get Question's**/
    @Override
    public void getQuestions(QuesGetModel quesGetModel) {
        if (AppUtility.isInternetConnected()) {
            ActivityLogController.saveActivity(
                    ActivityLogController.JOB_MODULE,
                    ActivityLogController.JOB_GET_QUESTON,
                    ActivityLogController.JOB_MODULE
            );
            ansId = quesGetModel.getAnsId();
            AppUtility.progressBarShow((Context) queAns_view);
            ApiClient.getservices().eotServiceCall(Service_apis.getQuestionsByParentId, AppUtility.getApiHeaders(), AppUtility.jsonToStingConvrt(quesGetModel))

                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                    subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            Log.e("Responce", jsonObject.toString());
                            if (jsonObject.get("success").getAsBoolean()) {
                                String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                Type listType = new TypeToken<List<QuesRspncModel>>() {
                                }.getType();
                                List<QuesRspncModel> respnc = new Gson().fromJson(convert, listType);
                                if (ansId.equals("-1")) {
                                    queAns_view.questionlist(respnc);// update list by initial encounter id
                                } else {
                                    queAns_view.addfragmentDynamically(respnc);// update list by previous encounter id
                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                queAns_view.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("Error", e.getMessage());
                            AppUtility.progressBarDissMiss();
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                        }
                    });
        } else {
            queAns_view.showOfflineAlert(LanguageController.getInstance().getMobileMsgByKey(AppConstant.offline_feature_alert));
        }
    }


    @Override
    public void addAnswerWithAttachments(Ans_Req ans_req, List<MultipartBody.Part> signAns, List<MultipartBody.Part> docAns
            , ArrayList<String> signQueIdArrays, ArrayList<String> docQueIdArrays) {
        String str1 = new Gson().toJson(ans_req.getAnswer());

        RequestBody usrId = RequestBody.create(MultipartBody.FORM, ans_req.getUsrId());
        RequestBody answer = RequestBody.create(MultipartBody.FORM, str1);
        RequestBody frmId = RequestBody.create(MultipartBody.FORM, ans_req.getFrmId());
        RequestBody jobId = RequestBody.create(MultipartBody.FORM, ans_req.getJobId());
        RequestBody isdelete = RequestBody.create(MultipartBody.FORM, ans_req.getIsdelete());
        RequestBody type = RequestBody.create(MultipartBody.FORM, ans_req.getType());
        String signIdArrayStr = new Gson().toJson(signQueIdArrays);
        RequestBody signQueIdArray = RequestBody.create(MultipartBody.FORM, signIdArrayStr);
        String docIdArrayStr = new Gson().toJson(docQueIdArrays);
        RequestBody docQueIdArray = RequestBody.create(MultipartBody.FORM, docIdArrayStr);

        //   RequestBody signQueIdArrayStr = RequestBody.create(MultipartBody.FORM, new Gson().toJson(signQueIdArray));
        // RequestBody docQueIdArrayStr = RequestBody.create(MultipartBody.FORM, new Gson().toJson(docQueIdArray));

        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow(((Context) queAns_view));
            ApiClient.getservices().submitCustomFormAns(AppUtility.getApiHeaders(),
                    signAns, docAns, signQueIdArray, docQueIdArray
                    , answer, usrId, frmId, jobId, isdelete, type)
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
                                queAns_view.onSubmitSuccess(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                queAns_view.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {
                                queAns_view.onSubmitSuccess(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG : error----", e.getMessage());
                            AppUtility.progressBarDissMiss();
                            queAns_view.finishMuAvtivity();
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                            Log.e("TAG onComplete------", "onComplete");
                        }
                    });
        } else {
            queAns_view.onSubmitSuccess(LanguageController.getInstance().getMobileMsgByKey(AppConstant.network_error));
        }
    }

}
