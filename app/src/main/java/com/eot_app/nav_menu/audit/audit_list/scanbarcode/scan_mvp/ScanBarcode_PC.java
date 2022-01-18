package com.eot_app.nav_menu.audit.audit_list.scanbarcode.scan_mvp;

import android.content.Context;
import android.util.Log;

import com.eot_app.nav_menu.audit.audit_list.FragmentAuditList;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
import com.eot_app.nav_menu.audit.audit_list.equipment.model.Equipment_Res;
import com.eot_app.nav_menu.audit.audit_list.scanbarcode.AuditScanbarcodeFragment;
import com.eot_app.nav_menu.audit.audit_list.scanbarcode.model.ScanBarcodeRequest;
import com.eot_app.nav_menu.audit.nav_scan.BarcodeScanActivity;
import com.eot_app.nav_menu.audit.nav_scan.ScanEquipmentsInBack;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.settings.equipmentdb.Equipment;
import com.eot_app.utility.settings.equipmentdb.EquipmentListReq;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Mahendra Dabi on 13/11/19.
 */
public class ScanBarcode_PC implements ScanBarcode_PI {

    private final ScanBarcode_View scanBarcode_view;
    private boolean isFromNavMenu;
    private Context mContext;
    private int count;
    private int updateindex;
    private int updatelimit;
    private boolean isEquipmentSyncInProgress;

    public ScanBarcode_PC(ScanBarcode_View scanBarcode_view, boolean isFromNavMenu) {
        this.scanBarcode_view = scanBarcode_view;
        this.mContext = ((BarcodeScanActivity) scanBarcode_view);
        this.isFromNavMenu = isFromNavMenu;
        this.updatelimit = AppConstant.LIMIT_HIGH;
    }

    public ScanBarcode_PC(ScanBarcode_View scanBarcode_view) {
        this.scanBarcode_view = scanBarcode_view;
        if (scanBarcode_view instanceof AuditScanbarcodeFragment)
            this.mContext = ((AuditScanbarcodeFragment) scanBarcode_view).getActivity();
        else if (scanBarcode_view instanceof FragmentAuditList)
            this.mContext = ((FragmentAuditList) scanBarcode_view).getActivity();

    }

    @Override
    public void searchEquipmentinAudit(ScanBarcodeRequest request) {
        new ScanEquipmentsInBack(new ScanEquipmentsInBack.OnSearchedEquipment() {
            @Override
            public void OnRecordFound(List<Job> jobList, List<AuditList_Res> auditList) {
                Log.d("", "");
                scanBarcode_view.onRecordFound(jobList, auditList);
            }

            @Override
            public void onEquipmentFound(Equipment equipment) {
                scanBarcode_view.onEquipmentFoundButNotLinked(equipment);
            }

            @Override
            public void showProgress(boolean b) {
                if (b)
                    AppUtility.progressBarShow(mContext);
                else AppUtility.progressBarDissMiss();
            }

        }, request.getBarCode()).filterEquipments();//.execute(request.getBarCode());
        // }).execute(request.getBarCode());
    }


    @Override
    public void searchAuditWithBarcode(ScanBarcodeRequest request) {
        AuditList_Res auditsEquipmentList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                .auditDao().getAuditsEquipmentList(request.getAudId());
        if (auditsEquipmentList != null) {
            boolean isEquipmentFound = false;
            List<Equipment_Res> equArray = auditsEquipmentList.getEquArray();
            if (equArray != null) {
                for (Equipment_Res equipment : equArray) {
                    if (equipment.getSno() != null && equipment.getSno().equals(request.getBarCode()) ||
                            equipment.getBarcode() != null && equipment.getBarcode().equals(request.getBarCode())) {
                        isEquipmentFound = true;
                        scanBarcode_view.onEquipmentFound(equipment);
                        break;
                    }
                }
            }
            if (!isEquipmentFound) scanBarcode_view.onEquipmentFound(null);
        } else {
            scanBarcode_view.onEquipmentFound(null);
        }

    }

    @Override
    public void syncEquipments() {
        if (AppUtility.isInternetConnected() && !isEquipmentSyncInProgress) {
            AppUtility.progressBarShow(mContext);

            isEquipmentSyncInProgress = true;
            updateindex = 0;
            count = 0;
            EquipmentListReq equipmentListReq = new EquipmentListReq(
                    updatelimit, updateindex, "", App_preference.getSharedprefInstance().getAllEquipmentSyncTime());
            String data = new Gson().toJson(equipmentListReq);
            ApiClient.getservices().eotServiceCall(Service_apis.getAllEquipments, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            Log.d("equipmentlist", jsonObject.toString());
                            if (jsonObject.get("success").getAsBoolean()) {
                                count = jsonObject.get("count").getAsInt();
                                String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                Type listType = new TypeToken<List<Equipment>>() {
                                }.getType();
                                List<Equipment> equipmentList = new Gson().fromJson(convert, listType);
                                if (equipmentList != null)
                                    AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).equipmentDao().insertEquipmentList(equipmentList);

                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("", "");
                        }

                        @Override
                        public void onComplete() {
                            isEquipmentSyncInProgress = false;
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                syncEquipments();
                            } else {
                                AppUtility.progressBarDissMiss();
                                if (count != 0) {
                                    App_preference.getSharedprefInstance().setAllEquipmentSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                                }
                                updateindex = 0;
                                count = 0;
                                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).equipmentDao().deleteEquipmentByIsDelete();
                            }
                        }
                    });
        }
    }


}
