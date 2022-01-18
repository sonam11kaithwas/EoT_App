package com.eot_app.nav_menu.client.clientlist.client_detail.contact.clientcontact_mvp;

import android.util.Log;

import com.eot_app.nav_menu.client.client_db.Client_Request_model;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
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
 * Created by ubuntu on 7/6/18.
 */

public class Contact_pc implements Contact_pi {
    private final int updatelimit;
    Contact_View contact_view;
    String key;
    private int count;
    private int updateindex;

    public Contact_pc(Contact_View contact_view, String key) {
        this.contact_view = contact_view;
        this.key = key;
        this.updatelimit = AppConstant.LIMIT_MID;
    }


    /**
     * load Updated Contact list
     */
    @Override
    public void contactResponce() {
        if (AppUtility.isInternetConnected()) {
            Client_Request_model client_request_model = new Client_Request_model(Integer.parseInt(App_preference.getSharedprefInstance().getLoginRes().getCompId()),
                    updatelimit, updateindex, App_preference.getSharedprefInstance().getContactSyncTime());

            String data = new Gson().toJson(client_request_model);

            ApiClient.getservices().eotServiceCall(Service_apis.getClientContactSink, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
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
                                addDataToDB(data);
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
                                contactResponce();
                            } else {
                                if (count != 0) {
                                    App_preference.getSharedprefInstance().setContactSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                                }
                                updateindex = 0;
                                count = 0;
                                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().deleteContactByIsDelete();
                                List<ContactData> data = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().getContactFromcltId(key);//(Integer.parseInt(key)
                                contact_view.setData(data);
                            }
                        }
                    });
        }
    }

    @Override
    public void addDataToDB(List<ContactData> contactData) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().insertContactList(contactData);
        for (ContactData item : contactData) {
            if (item.getDef().equals("1")) {
                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().updateDefaultContact(item.getConId(), item.getCltId());
                return;
            }
        }
    }

    @Override
    public void contactDetails() {
        List<ContactData> data = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().getContactFromcltId((key));//Integer.parseInt
        contact_view.setData(data);
        contactResponce();
    }

    @Override
    public void getcontactList(String query, String cltId) {
        List<ContactData> contactData = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().getContactlist(query, cltId);
        contact_view.setSearchData(contactData);
    }

    @Override
    public void getContactListFromDB() {
        List<ContactData> data = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().getContactFromcltId((key));//Integer.parseInt
        contact_view.setData(data);
    }
}
