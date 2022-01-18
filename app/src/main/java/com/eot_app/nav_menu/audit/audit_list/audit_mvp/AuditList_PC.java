package com.eot_app.nav_menu.audit.audit_list.audit_mvp;

import android.util.Log;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditListRequestModel;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
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

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Mahendra Dabi on 9/11/19.
 */
public class AuditList_PC implements AuditList_PI {

    private final AuditList_View auditListView;
    private final int updatelimit;
    private int count;
    private int updateindex;


    public AuditList_PC(AuditList_View auditListView) {
        this.auditListView = auditListView;
        this.updatelimit = AppConstant.LIMIT_MID;
        this.updateindex = 0;
    }

    @Override
    public void updatedAuditAtRefresh() {
        if (AppUtility.isInternetConnected()) {
            ActivityLogController.saveActivity(
                    ActivityLogController.AUDIT_MODULE,
                    ActivityLogController.AUDIT_LIST,
                    ActivityLogController.AUDIT_MODULE);
            AuditListRequestModel auditListRequestModel = new AuditListRequestModel(Integer.parseInt(App_preference.getSharedprefInstance().getLoginRes().getUsrId()),
                    updatelimit, updateindex, App_preference.getSharedprefInstance().getAuditSyncTime());
            String data = new Gson().toJson(auditListRequestModel);
            ApiClient.getservices().eotServiceCall(Service_apis.getAuditList, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            try {
                                Log.d("mahi", jsonObject.toString());
                                if (jsonObject.get("success").getAsBoolean()) {
                                    count = jsonObject.get("count").getAsInt();
                                    String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                    Type listType = new TypeToken<List<AuditList_Res>>() {
                                    }.getType();
                                    List<AuditList_Res> data = new Gson().fromJson(convert, listType);
                                    addAuditListInDB(data);
                                } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                    auditListView.onSessionExpired(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                } else {
                                    auditListView.showErrorAlertDialog(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            // HyperLog.i("", "RefreshAuditList-----" + "Error");
                        }

                        @Override
                        public void onComplete() {
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                updatedAuditAtRefresh();
                            } else {
                                App_preference.getSharedprefInstance().setAuditSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                                updateindex = 0;
                                count = 0;
                                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().deleteJobByIsDelete();
                                auditListView.onNotificationRedirect();
                                getAuditList();
                            }
                        }
                    });
        } else {

            auditListView.setRefereshPullOff();
        }
    }


    private void addAuditListInDB(List<AuditList_Res> auditList) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().inserAuditList(auditList);
    }

    @Override
    public void getAuditListBySearch(String text) {
        auditListView.setAuditList(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().
                getAuditListBySearch("%" + text + "%"));
    }

    @Override
    public void getAuditList() {
        auditListView.setAuditList(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().getAuditsList());
    }

    @Override
    public void getAudit() {
        List<AuditList_Res> data = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().getAuditsList();
        auditListView.setAuditList(data);
        updatedAuditAtRefresh();
    }


}
