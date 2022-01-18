package com.eot_app.nav_menu.audit.audit_list.equipment.equipment_mvp;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
import com.eot_app.nav_menu.audit.audit_list.equipment.AuditEquipmentFragment;
import com.eot_app.nav_menu.audit.audit_list.equipment.model.AuditEquipmentRequestModel;
import com.eot_app.nav_menu.audit.audit_list.equipment.model.Equipment_Res;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Mahendra Dabi on 11/11/19.
 */
public class Equipment_PC implements Equipment_PI {

    private final Equipment_View equipment_view;
    private final int updatelimit;
    private final Context mContext;
    private int count;
    private int updateindex;

    public Equipment_PC(Equipment_View view) {
        this.equipment_view = view;
        this.updatelimit = AppConstant.LIMIT_HIGH;
        this.updateindex = 0;
        mContext = ((AuditEquipmentFragment) view).getActivity();
    }

    @Override
    public void getEquipmentBySiteName(String auditID, String snm) {
        try {
            AuditList_Res auditEquipmentList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().getAuditsEquipmentList(auditID);
            ArrayList<Equipment_Res> equipmentResArrayList = new ArrayList<>();
            if (auditEquipmentList != null && auditEquipmentList.getEquArray() != null) {
                for (Equipment_Res model : auditEquipmentList.getEquArray()) {
                    if (model.getSnm().toLowerCase().startsWith(snm.toLowerCase())
//                            || (model.getSno() != null && model.getSno().equalsIgnoreCase(snm)) ||
//                            (model.getMno() != null && model.getMno().equalsIgnoreCase(snm))
                    ) {
                        equipmentResArrayList.add(model);
                    }
                }
            }
            equipment_view.setEuqipmentList(equipmentResArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getEquipmentList(String auditId) {
        try {
            AuditList_Res auditEquipmentList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().getAuditsEquipmentList(auditId);
            if (auditEquipmentList != null && auditEquipmentList.getEquArray() != null)
                equipment_view.setEuqipmentList(auditEquipmentList.getEquArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void networkError() {
        AppUtility.alertDialog(((Fragment) equipment_view).getActivity(), LanguageController.getInstance().
                        getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey
                        (AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok),
                "", new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return null;
                    }
                });
    }


    @Override
    public void refreshList(final String auditID) {
        if (AppUtility.isInternetConnected()) {
            AuditEquipmentRequestModel auditListRequestModel = new AuditEquipmentRequestModel(auditID,
                    updatelimit, updateindex, "");//App_preference.getSharedprefInstance().getEquipmentSyncTime()

            String data = new Gson().toJson(auditListRequestModel);
            ApiClient.getservices().eotServiceCall(Service_apis.getEquipmentList, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
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
                                    Type listType = new TypeToken<List<Equipment_Res>>() {
                                    }.getType();
                                    List<Equipment_Res> data = new Gson().fromJson(convert, listType);
//                                    if (data.size() > 0)
                                    updateEuipmentInDB(data, auditID);
                                    equipment_view.setEuqipmentList(data);
                                } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                    equipment_view.onSessionExpired(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                } else {
                                    equipment_view.showErrorAlertDialog(LanguageController.getInstance().
                                            getServerMsgByKey(jsonObject.get("message").getAsString()));
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            equipment_view.swipeRefresh();
                            App_preference.getSharedprefInstance().setEquipmentSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                            } else {
                                updateindex = 0;
                                count = 0;
                            }
                        }
                    });
        } else {
            equipment_view.swipeRefresh();
            networkError();
        }
    }

    private void updateEuipmentInDB(List<Equipment_Res> equpDataList, String auiId) {

        try {
            AuditList_Res job = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().getAuditById(auiId);
            /******Notify JOB overView for Equipmetn Added first time ****/
            if (job.getEquArray() != null && job.getEquArray().size() == 0) {
                job.setEquArray(equpDataList);
                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().updateEquipmentList(equpDataList, auiId);
                EotApp.getAppinstance().getJobFlagOverView();
            } else {
                /***Refresh job Table in Exiting Equ. lisy***/
                job.setEquArray(equpDataList);
                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().updateEquipmentList(equpDataList, auiId);
                if (job.getEquArray() != null && job.getEquArray().size() == 0)
                    EotApp.getAppinstance().getJobFlagOverView();
            }

            getEquipmentList(auiId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

//        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().updateEquipmentList(equpDataList, auiId);
//        getEquipmentList(auiId);
    }
}
