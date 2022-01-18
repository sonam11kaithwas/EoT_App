package com.eot_app.nav_menu.equipment.popupSaveClient.equipmentClinetsave;

import android.content.Context;

import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.model.EquipmentSaveClientRes;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Equipment_Client_PC implements Equipment_Client_PI {
    private final Equipment_Client_view equipmentSaveClient_view;
    private EquipmentSaveClientRes equipmentSaveClientRes;

    public Equipment_Client_PC(Equipment_Client_view equipmentSaveClient_view) {
        this.equipmentSaveClient_view = equipmentSaveClient_view;
    }

    @Override
    public void addClientForSaveUse(final String jobId, final String audId, final String appId) {
        if (AppUtility.isInternetConnected()) {
            if (!appId.isEmpty())
                AppUtility.progressBarShow((Context) equipmentSaveClient_view);
            Map<String, String> request = new HashMap<>();
            if (!jobId.isEmpty())
                request.put("jobId", jobId);
            else if (!audId.isEmpty())
                request.put("jobId", audId);
            String data = new Gson().toJson(request);
            ApiClient.getservices().eotServiceCall(Service_apis.addClientForLinkEquipment, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
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
                                    String convert = new Gson().toJson(jsonObject.get("data").getAsJsonObject());
                                    equipmentSaveClientRes = new Gson().fromJson(convert, EquipmentSaveClientRes.class);
                                    if (equipmentSaveClientRes != null) {

                                        if (!jobId.isEmpty() && !appId.isEmpty()) {
                                            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().updateJobCltID(jobId, equipmentSaveClientRes.getCltId());
                                            getClientSyncService(appId, equipmentSaveClientRes.getCltId());

                                        } else if (!audId.isEmpty())
                                            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().updateAuditCltID(audId, equipmentSaveClientRes.getCltId());
                                        else if (!jobId.isEmpty() && appId.isEmpty())
                                            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().updateJobCltID(jobId, equipmentSaveClientRes.getCltId());

                                        equipmentSaveClient_view.setClientForSaveUse(equipmentSaveClientRes);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (!appId.isEmpty())
                                AppUtility.progressBarDissMiss();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            networkError();
        }
    }


    private void getClientSyncService(final String appId, final String cltId) {
        if (AppUtility.isInternetConnected()) {


            Map<String, String> request = new HashMap<>();
            request.put("cltId", cltId);

            String data = new Gson().toJson(request);

            ApiClient.getservices().eotServiceCall(Service_apis.getClientDetail, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                //count = jsonObject.get("count").getAsInt();
                                String convert = new Gson().toJson(jsonObject.get("data").getAsJsonObject());

                                Client data = new Gson().fromJson(convert, Client.class);
                                if (data != null) {
                                    AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel().updateAppointmentCltID(appId, cltId, data.getSiteId(), data.getConId());
                                    Site_model site_model = new Gson().fromJson(convert, Site_model.class);
                                    site_model.setDef("1");

                                    ContactData contactData = new ContactData();
                                    contactData.setCltId(data.getCltId());
                                    contactData.setConId(data.getConId());
                                    contactData.setCnm(data.getCnm());
                                    contactData.setEmail(data.getEmail());
                                    contactData.setMob1(data.getMob1());
                                    contactData.setMob2(data.getMob2());
                                    contactData.setDef("1");
                                    contactData.setExtra(data.getExtra());
                                    contactData.setIsdelete(data.getIsdelete());


                                    addClientDataToDB(data);
                                    addContactDataToDB(contactData);
                                    addSiteDataToDB(site_model);

                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            AppUtility.progressBarDissMiss();

                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                        }

                    });
        }
    }

    private void addClientDataToDB(Client data) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientModel().inserSingleClient(data);
    }


    private void addSiteDataToDB(Site_model data) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().insertSingleSiteList(data);
    }

    private void addContactDataToDB(ContactData data) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().insertSingleContactList(data);
    }


    private void networkError() {
        AppUtility.alertDialog(((Context) equipmentSaveClient_view), LanguageController.getInstance().
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
