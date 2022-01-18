package com.eot_app.nav_menu.client.clientlist.client_presenter;

import android.util.Log;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.client_db.Client_Request_model;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
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

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ubuntu on 1/6/18.
 */

public class Client_pc implements Client_pi {
    private final int updatelimit;
    ClientList_View clientListView;
    private int count;
    private int updateindex;


    public Client_pc(ClientList_View clientListView) {
        this.clientListView = clientListView;
        this.updatelimit = AppConstant.LIMIT_MID;
    }


    /**
     * get update record
     */
    @Override
    public void client_Responce() {
        if (AppUtility.isInternetConnected()) {
            ActivityLogController.saveActivity(
                    ActivityLogController.CLIENT_MODULE,
                    ActivityLogController.CLIENT_SYNC,
                    ActivityLogController.CLIENT_MODULE
            );
            Client_Request_model client_request_model = new Client_Request_model(Integer.parseInt(App_preference.getSharedprefInstance().getLoginRes().getCompId()),
                    updatelimit, updateindex, App_preference.getSharedprefInstance().getClientSyncTime());
            JsonObject jsonObject = AppUtility.getJsonObject(new Gson().toJson(client_request_model));

            ApiClient.getservices().eotServiceCall(Service_apis.getClientSink, AppUtility.getApiHeaders(), jsonObject)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                count = jsonObject.get("count").getAsInt();
                                String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                Type listType = new TypeToken<List<Client>>() {
                                }.getType();
                                List<Client> data = new Gson().fromJson(convert, listType);
                                addRecordsToDB(data);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                clientListView.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG: ", "error");
                        }

                        @Override
                        public void onComplete() {
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                client_Responce();
                            } else {
                                if (count != 0) {
                                    App_preference.getSharedprefInstance().setClientSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                                }
                                updateindex = 0;
                                count = 0;
                                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientModel().deleteClientByIsDelete();

                                List<Client> data = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientModel().getClientList();
                                clientListView.setdata(data);
                            }
                        }
                    });
        }
    }

    @Override
    public void addRecordsToDB(List<Client> data) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientModel().insertUser(data);
    }

    @Override
    public void getClientList() {
        // get Records from database
        List<Client> data = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientModel().getClientList();
        clientListView.setdata(data);
    }


    /**
     * Update Ever client & contact data when Pull to refresh
     */
    @Override
    public void getSIteList() {
        if (AppUtility.isInternetConnected()) {
            ActivityLogController.saveActivity(
                    ActivityLogController.CLIENT_MODULE,
                    ActivityLogController.SITE_LIST,
                    ActivityLogController.CLIENT_MODULE
            );
            Client_Request_model client_request_model = new Client_Request_model(Integer.parseInt(App_preference.getSharedprefInstance().getLoginRes().getCompId()),
                    updatelimit, updateindex, App_preference.getSharedprefInstance().getSiteSyncTime());

            JsonObject jsonObject = AppUtility.getJsonObject(new Gson().toJson(client_request_model));

            ApiClient.getservices().eotServiceCall(Service_apis.getClientSiteSink, AppUtility.getApiHeaders(),
                    jsonObject)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                count = jsonObject.get("count").getAsInt();
                                String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                Type listType = new TypeToken<List<Site_model>>() {
                                }.getType();
                                List<Site_model> data = new Gson().fromJson(convert, listType);
                                addSiteDataToDB(data);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                //clientListView.onSessionExpire(jsonObject.get("message").getAsString());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG: ", "error");
                        }

                        @Override
                        public void onComplete() {
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                getSIteList();
                            } else {
                                updateindex = 0;
                                count = 0;
                                App_preference.getSharedprefInstance().setSiteSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                                getContactList();
                            }
                        }
                    });
        }


    }

    /**
     * get update record
     */
    private void getContactList() {
        if (AppUtility.isInternetConnected()) {
            ActivityLogController.saveActivity(
                    ActivityLogController.CLIENT_MODULE,
                    ActivityLogController.CONTACT_SYNC,
                    ActivityLogController.CLIENT_MODULE
            );
            Client_Request_model client_request_model = new Client_Request_model(Integer.parseInt(App_preference.getSharedprefInstance().getLoginRes().getCompId()),
                    updatelimit, updateindex, App_preference.getSharedprefInstance().getContactSyncTime());

            JsonObject jsonObject = AppUtility.getJsonObject(new Gson().toJson(client_request_model));

            ApiClient.getservices().eotServiceCall(Service_apis.getClientContactSink, AppUtility.getApiHeaders(),
                    jsonObject)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                count = jsonObject.get("count").getAsInt();
                                String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                Type listType = new TypeToken<List<ContactData>>() {
                                }.getType();
                                List<ContactData> data = new Gson().fromJson(convert, listType);
                                addContactDataToDB(data);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG: ", "error");
                        }

                        @Override
                        public void onComplete() {
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                getContactList();
                            } else {
                                updateindex = 0;
                                count = 0;
                                App_preference.getSharedprefInstance().setContactSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                                client_Responce();
                            }
                        }
                    });
        }

    }

    private void addContactDataToDB(List<ContactData> contactData) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().insertContactList(contactData);
        for (ContactData item : contactData) {
            if (item.getDef().equals("1")) {
                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().updateDefaultContact(item.getConId(), item.getCltId());
                return;
            }
        }
    }

    private void addSiteDataToDB(List<Site_model> sitedata) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().insertSiteList(sitedata);
        for (Site_model item : sitedata) {
            if (item.getDef().equals("1")) {
                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().updateDefault(item.getCltId(), item.getSiteId());
            }
        }
    }

    @Override
    public void getclientListfromDB(String query) {
        List<Client> clientsData = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientModel().getClientsWithMobile(query);
        clientListView.setSearchData(clientsData);
    }
}
