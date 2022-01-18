package com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist;

import android.util.Log;

import com.eot_app.nav_menu.client.client_db.Client_Request_model;
import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFiledReqModel;
import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFiledResModel;
import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFormQuestionsReq;
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
import com.hypertrack.hyperlog.HyperLog;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ubuntu on 8/6/18.
 */

public class Site_pc implements Site_pi {
    private final int updatelimit;
    Site_View site_view;
    String key;
    private int count;
    private int updateindex;

    public Site_pc(Site_View site_view, String key) {
        this.site_view = site_view;
        this.key = key;
        this.updatelimit = AppConstant.LIMIT_MID;
    }

    @Override
    public void siteResponce() {
        HyperLog.i("Site_pc", "siteResponce(M) called Start");
        if (AppUtility.isInternetConnected()) {
            Client_Request_model client_request_model = new Client_Request_model(Integer.parseInt(App_preference.getSharedprefInstance().getLoginRes().getCompId()),
                    updatelimit, updateindex, App_preference.getSharedprefInstance().getSiteSyncTime());

            String data = new Gson().toJson(client_request_model);

            ApiClient.getservices().eotServiceCall(Service_apis.getClientSiteSink, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
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
                                if (data.size() > 0) {
                                    getCustomFieldForSite();
                                    HyperLog.i("Site_pc", "siteResponce(M) Update data Get");
                                    addSiteListToDB(data);
                                }
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
                                siteResponce();
                            } else {

                                if (count != 0) {
                                    App_preference.getSharedprefInstance().setSiteSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                                }
                                updateindex = 0;
                                count = 0;

                                /****very very importanmt sonam**/
                                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().deleteSiteByIsDelete();

                                List<Site_model> data = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getSitesFromCltId(Integer.parseInt(key));
                                site_view.setSiteData(data);
                            }
                        }
                    });
        }
        HyperLog.i("Site_pc", "siteResponce(M) called End");
    }

    @Override
    public void addSiteListToDB(List<Site_model> siteList) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().insertSiteList(siteList);
        for (Site_model item : siteList) {
            if (item.getDef().equals("1")) {
                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().updateDefault(item.getCltId(), item.getSiteId());
            }
        }
    }

    /**
     * get Custom filed For Client Site
     ****/
    private void getCustomFieldForSite() {
        ApiClient.getservices().eotServiceCall(Service_apis.getFormDetail, AppUtility.getApiHeaders(),
                AppUtility.getJsonObject(new Gson().toJson(new CustOmFiledReqModel("2"))))
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
                            getQuestByParntId(resModel.getFrmId());
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

    }


    private void getQuestByParntId(String frmid) {
        CustOmFormQuestionsReq model = new CustOmFormQuestionsReq(frmid, "");
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
                            App_preference.getSharedprefInstance().setSiteCustomFiled(convert);
                         /*   Type listType = new TypeToken<List<CustOmFormQuestionsRes>>() {
                            }.getType();
                            ArrayList<CustOmFormQuestionsRes> data = new Gson().fromJson(convert, listType);*/

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
    }

    @Override
    public void GetSiteDetails() {
        List<Site_model> data = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getSitesFromCltId(Integer.parseInt(key));//.getSitelist();
        site_view.setSiteData(data);
        siteResponce();
    }

    @Override
    public void getsiteList(String query, String cltId) {
        List<Site_model> sitesData = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getSitelist(query, cltId);
        site_view.setSearchData(sitesData);
    }

    @Override
    public void GetSiteDetailsFromDB() {
        List<Site_model> data = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getSitesFromCltId(Integer.parseInt(key));//.getSitelist();
        site_view.setSiteData(data);
    }
}