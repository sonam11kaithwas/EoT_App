package com.eot_app.nav_menu.equipment.linkequip.linkMVP;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.eot_app.nav_menu.audit.audit_list.equipment.model.EquipmentStatus;
import com.eot_app.nav_menu.equipment.linkequip.linkMVP.model.ContractEquipmentReq;
import com.eot_app.nav_menu.equipment.linkequip.linkMVP.model.EquipmentListReq;
import com.eot_app.nav_menu.jobs.job_db.EquArrayModel;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.model.EquipmentStatusReq;
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
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LinkEquipmentPC implements LinkEquipmentPI {
    int index;
    int limit;
    int count;

    int updatelimit;
    int updateindex;
    int countlimit;

    LinkEquipmentView view;

    List<EquArrayModel> listLinked = new ArrayList<>();
    List<EquArrayModel> list = new ArrayList<>();
    List<EquArrayModel> contractList = new ArrayList<>();

    public LinkEquipmentPC(LinkEquipmentView linkEquipmentView) {
        this.view = linkEquipmentView;
        this.limit = AppConstant.LIMIT_HIGH;
        this.updatelimit = AppConstant.LIMIT_HIGH;
        getEquipmentStatus();

    }

    @Override
    public void getEquipmentList(final String type, final String cltId, final String audId) {
        if (AppUtility.isInternetConnected()) {

            if (index == 0) list.clear();
            view.showHideProgressBar(true);
            EquipmentListReq equipmentListReq = new EquipmentListReq(
                    index,
                    limit,
                    type,
                    cltId
            );

            JsonObject jsonObject = AppUtility.jsonToStingConvrt(equipmentListReq);
            ApiClient.getservices().eotServiceCall(
                    Service_apis.getAllEquipments,
                    AppUtility.getApiHeaders(),
                    jsonObject
            ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull JsonObject jsonObject) {

                            if (jsonObject != null)
                                if (jsonObject.get("success").getAsBoolean()) {
                                    {
                                        count = jsonObject.get("count").getAsInt();
                                        String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                        Type listType = new TypeToken<List<EquArrayModel>>() {
                                        }.getType();
                                        List<EquArrayModel> sliceList = new Gson().fromJson(convert, listType);
                                        if (sliceList != null)
                                            list.addAll(sliceList);
                                        Log.d("equipmentList", jsonObject.toString());

                                    }

                                }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            view.showHideProgressBar(false);
                        }

                        @Override
                        public void onComplete() {
                            if ((index + limit) <= count) {
                                index += limit;
                                getEquipmentList(type, cltId, audId);
                            } else {
                                index = 0;
                                count = 0;
                                getAttachedEquipmentList(audId, "");
                            }
                        }
                    });
        } else networkError();

    }

    @Override
    public void getAttachedEquipmentList(String audId, final String contrId) {
        if (AppUtility.isInternetConnected()) {
            if (updateindex == 0) listLinked.clear();
            HashMap<String, String> auditListRequestModel = new HashMap<>();
            auditListRequestModel.put("index", updateindex + "");
            auditListRequestModel.put("activeRecord", "0");
            auditListRequestModel.put("audId", audId);
            auditListRequestModel.put("contrId", contrId);
            auditListRequestModel.put("isJob", "1");
            auditListRequestModel.put("isParent", "1");

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
                                if (jsonObject.get("success").getAsBoolean()) {
                                    countlimit = jsonObject.get("count").getAsInt();
                                    String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                    Type listType = new com.google.common.reflect.TypeToken<List<EquArrayModel>>() {
                                    }.getType();
                                    List<EquArrayModel> data = new Gson().fromJson(convert, listType);
                                    if (data != null)
                                        listLinked.addAll(data);

                                } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                    view.onSessionExpired(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                view.showHideProgressBar(false);

                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.showHideProgressBar(false);

                        }

                        @Override
                        public void onComplete() {
                            if ((updateindex + updatelimit) <= countlimit) {
                                updateindex += updatelimit;
                            } else {
                                updateindex = 0;
                                countlimit = 0;
                                mergeTheListForLinkedEquipment(!TextUtils.isEmpty(contrId));

                            }
                        }
                    });
        } else {
            networkError();
        }
    }

    @Override
    public void linkUnlinkEquipment(List<String> equId, String audId, String contrId) {
        if (AppUtility.isInternetConnected()) {

            HashMap hashMap = new HashMap();
            hashMap.put("equId", equId);
            hashMap.put("audId", audId);
            hashMap.put("contrId", contrId);

            String data = new Gson().toJson(hashMap);

            ApiClient.getservices().eotServiceCall(
                    Service_apis.addAuditEquipment,
                    AppUtility.getApiHeaders(),
                    AppUtility.getJsonObject(data)
            ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                view.updateLinkUnlinkEqu();
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                view.onSessionExpired(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        } else networkError();
    }

    @Override
    public void addAuditEquipment(List<String> equId, String audId, String contrId) {
        if (AppUtility.isInternetConnected()) {

            HashMap hashMap = new HashMap();
            hashMap.put("equId", equId);
            hashMap.put("audId", audId);
            hashMap.put("contrId", contrId);

            String data = new Gson().toJson(hashMap);

            ApiClient.getservices().eotServiceCall(
                    Service_apis.addAuditEquipment,
                    AppUtility.getApiHeaders(),
                    AppUtility.getJsonObject(data)
            ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                view.refreshEquipmentList();
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                view.onSessionExpired(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        } else networkError();
    }

    @Override
    public void getContractList(final ContractEquipmentReq req) {
        if (AppUtility.isInternetConnected()) {

            req.setIndex(index);
            req.setLimit(limit);

            if (index == 0) contractList.clear();
            view.showHideProgressBar(true);
            JsonObject jsonObject = AppUtility.jsonToStingConvrt(req);
            ApiClient.getservices().eotServiceCall(
                    Service_apis.getContractEquipmentList,
                    AppUtility.getApiHeaders(),
                    jsonObject
            ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull JsonObject jsonObject) {

                            if (jsonObject.get("success").getAsBoolean()) {

                                count = jsonObject.get("count").getAsInt();
                                String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                Type listType = new TypeToken<List<EquArrayModel>>() {
                                }.getType();
                                List<EquArrayModel> sliceList = new Gson().fromJson(convert, listType);
                                if (sliceList != null)
                                    contractList.addAll(sliceList);
                                Log.d("equipmentList", jsonObject.toString());
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                view.onSessionExpired(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            view.showHideProgressBar(false);
                        }

                        @Override
                        public void onComplete() {
                            if ((index + limit) <= count) {
                                index += limit;
                                getContractList(req);
                            } else {
                                index = 0;
                                count = 0;
                                getAttachedEquipmentList(req.getJobId(), req.getContrId());
                            }
                        }
                    });
        } else

            networkError();

    }

    @Override
    public void getEquipmentStatus() {
        if (AppUtility.isInternetConnected()) {
            EquipmentStatusReq equipmentListReq = new EquipmentStatusReq();
            String data = new Gson().toJson(equipmentListReq);
            ApiClient.getservices().eotServiceCall(Service_apis.getEquipmentStatus, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
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
                                Type listType = new TypeToken<List<EquipmentStatus>>() {
                                }.getType();
                                List<EquipmentStatus> equipmentStatusList = new Gson().fromJson(convert, listType);
                                view.setEquStatusList(equipmentStatusList);
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

    private void mergeTheListForLinkedEquipment(boolean mergeContract) {
        if (listLinked.size() > 0) {
            if (mergeContract) {
                for (EquArrayModel model : listLinked)
                    for (EquArrayModel inModel : contractList)
                        if (model.getEquId().equals(inModel.getEquId())) {
                            inModel.setLinkStatus(1);
                            if (!TextUtils.isEmpty(model.getStatus()))
                                inModel.setIsRemarkAdd(1);
                            if (model != null && model.getBarcode() != null)
                                inModel.setBarcode(model.getBarcode());
                        }
            } else {
                for (EquArrayModel model : listLinked)
                    for (EquArrayModel inModel : list)
                        if (model.getEquId().equals(inModel.getEquId())) {
                            inModel.setLinkStatus(1);
                            if (!TextUtils.isEmpty(model.getStatus()))
                                inModel.setIsRemarkAdd(1);
                            if (model != null && model.getBarcode() != null)
                                inModel.setBarcode(model.getBarcode());
                        }
            }

        }
        if (mergeContract)
            view.setEquipmentList(contractList);
        else
            view.setEquipmentList(list);
        view.showHideProgressBar(false);
    }

    private void networkError() {
        AppUtility.alertDialog(((Context) view), LanguageController.getInstance().
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
