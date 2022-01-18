package com.eot_app.nav_menu.audit.audit_list.details.audit_detail_mvp;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditStatusRequest;
import com.eot_app.nav_menu.audit.audit_list.details.AuditDetailsFragment;
import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFiledReqModel;
import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFiledResModel;
import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFormQuestionsReq;
import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFormQuestionsRes;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.db.OfflineDataController;
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

/**
 * Created by Mahendra Dabi on 12/11/19.
 */
public class AuditDetail_PC implements AuditDetails_PI {
    private static final String TAG = "mahi";
    private final AuditDetails_View auditDetails_view;
    private Context mContext;

    public AuditDetail_PC(AuditDetails_View auditDetails_view) {
        this.auditDetails_view = auditDetails_view;
        try {
            Fragment fragment = (AuditDetailsFragment) auditDetails_view;
            mContext = fragment.getActivity();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updateAuditStatus(AuditStatusRequest request) {

        OfflineDataController.getInstance().addInOfflineDB(Service_apis.updateAuditStatus, new Gson().toJson(request), AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
        auditDetails_view.statusChanged(Integer.parseInt(request.getStatus()));
    }


    @Override
    public void getCustomFieldQues(final String jobId) {
        if (AppUtility.isInternetConnected()) {
            ApiClient.getservices().eotServiceCall(Service_apis.getFormDetail, AppUtility.getApiHeaders(),
                    AppUtility.getJsonObject(new Gson().toJson(new CustOmFiledReqModel("3"))))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                String convert = jsonObject.get("data").getAsJsonObject().toString();
                                CustOmFiledResModel resModel = new Gson().fromJson(convert, CustOmFiledResModel.class);
                                getQuestByParntId(resModel.getFrmId(), jobId);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                auditDetails_view.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {

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
        } else {
            // joblist_view.setRefereshPullOff();
        }
    }

    @Override
    public void getQuestByParntId(String formId, String jobId) {
        CustOmFormQuestionsReq model = new CustOmFormQuestionsReq(jobId);
        model.setFrmId(formId);
        if (AppUtility.isInternetConnected()) {
            ApiClient.getservices().eotServiceCall(Service_apis.getQuestionsByParentId, AppUtility.getApiHeaders(),
                    AppUtility.getJsonObject(new Gson().toJson(model)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                String convert = jsonObject.get("data").getAsJsonArray().toString();
                                Type listType = new TypeToken<List<CustOmFormQuestionsRes>>() {
                                }.getType();
                                ArrayList<CustOmFormQuestionsRes> data = new Gson().fromJson(convert, listType);
                                auditDetails_view.setCustomFiledList(data);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                auditDetails_view.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {

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
        } else {
            // joblist_view.setRefereshPullOff();
        }
    }

}
