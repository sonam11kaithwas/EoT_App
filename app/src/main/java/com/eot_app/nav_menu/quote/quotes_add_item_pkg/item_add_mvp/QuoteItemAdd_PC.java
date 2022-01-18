package com.eot_app.nav_menu.quote.quotes_add_item_pkg.item_add_mvp;

import static com.eot_app.utility.AppUtility.getJsonObject;

import android.content.Context;
import android.util.Log;

import com.eot_app.nav_menu.jobs.job_detail.invoice.inventry_pkg.Inventry_ReQ_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.inventry_pkg.Inventry_ReS_Model;
import com.eot_app.nav_menu.quote.add_quotes_pkg.model_pkg.Update_Quote_ReQ;
import com.eot_app.nav_menu.quote.quotes_add_item_pkg.item_model_pkg.AddItem_Model;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.setting_db.FieldWorker;
import com.eot_app.utility.settings.setting_db.JobTitle;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class QuoteItemAdd_PC implements QuoteItemAdd_PI {
    private final QuoteItemAdd_View quoteItemAdd_view;
    private final int updatelimit;
    private final String search_txt = "";
    List<Inventry_ReS_Model> finalList = new ArrayList<>();
    private int updateindex;
    private int count;
    private List<Inventry_ReS_Model> inveteryItemDataList = new ArrayList<>();
    private List<FieldWorker> fwDataList = new ArrayList<>();
    private List<JobTitle> jobtitleDataList = new ArrayList<>();

    public QuoteItemAdd_PC(QuoteItemAdd_View quoteItemAdd_view) {
        this.quoteItemAdd_view = quoteItemAdd_view;
        this.updateindex = 0;
        this.updatelimit = AppConstant.LIMIT_MID;
    }


    @Override
    public void getTaxList() {
        quoteItemAdd_view.setTaxList(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).invoiceTaxDao().getTaxList());
    }


    @Override
    public void initialize_FwList_ServiceTittle_inventoryList() {
        //  getInventryItemFromServer();
        inveteryItemDataList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).invoiceItemDao().getInventryItemList();
        fwDataList = AppDataBase.getInMemoryDatabase((Context) quoteItemAdd_view).fieldWorkerModel().getFieldWorkerlist();
        jobtitleDataList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobTitleModel().getJobTitlelist();
    }


    @Override
    public void getInventryItemFromServer() {

        Log.e("data--->>>", "data--->>>");
        if (AppUtility.isInternetConnected()) {
            //  AppUtility.progressBarShow((Context) quoteItemAdd_view);
            Inventry_ReQ_Model inventry_model = new Inventry_ReQ_Model(Integer.parseInt(App_preference.getSharedprefInstance()
                    .getLoginRes().getCompId()),
                    search_txt,
                    updatelimit,
                    updateindex, App_preference.getSharedprefInstance().getInventryItemSyncTime());

            String data = new Gson().toJson(inventry_model);

            ApiClient.getservices().eotServiceCall(Service_apis.getItemList, AppUtility.getApiHeaders(), getJsonObject(data))
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
                                Type listType = new TypeToken<List<Inventry_ReS_Model>>() {
                                }.getType();
                                List<Inventry_ReS_Model> inventryitemlist = new Gson().fromJson(convert, listType);
                                inveteryItemDataList.addAll(inventryitemlist);
                                Log.e("data----", "" + inventryitemlist);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                EotApp.getAppinstance().sessionExpired();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG : error----", e.getMessage());
                            //   AppUtility.progressBarDissMiss();
                        }

                        @Override
                        public void onComplete() {
                            //  AppUtility.progressBarDissMiss();
                            Log.e("TAG onComplete------", "onComplete");
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                getInventryItemFromServer();
                            } else {
                                updateindex = 0;
                                count = 0;
                                quoteItemAdd_view.updateItemServiceListner();
                            }
                        }
                    });
        } else {
            netWork_erroR();
        }
    }


    @Override
    public void getFwList() {
        if (fwDataList.isEmpty()) {
            fwDataList = AppDataBase.getInMemoryDatabase((Context) quoteItemAdd_view).fieldWorkerModel().getFieldWorkerlist();
        }
        quoteItemAdd_view.setFwList(fwDataList);
    }

    private void netWork_erroR() {
        AppUtility.alertDialog(((Context) quoteItemAdd_view), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }

    @Override
    public void getInventryItemList() {
//        if (inveteryItemDataList != null && inveteryItemDataList.size() > 0) {
        quoteItemAdd_view.setInventryItem(inveteryItemDataList);
        //       }
    }

    @Override
    synchronized public void loadFromServer(final String search) {
        Log.e("data--->>>", "data--->>>");
        if (AppUtility.isInternetConnected()) {
            Inventry_ReQ_Model inventry_model =
                    new Inventry_ReQ_Model(Integer.parseInt(App_preference.getSharedprefInstance().getLoginRes().getCompId()),
                            search,
                            updatelimit, updateindex, "");//

            String data = new Gson().toJson(inventry_model);

            ApiClient.getservices().eotServiceCall(Service_apis.getItemList, AppUtility.getApiHeaders(), getJsonObject(data))
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
                                Type listType = new TypeToken<List<Inventry_ReS_Model>>() {
                                }.getType();
                                List<Inventry_ReS_Model> inventryitemlist = new Gson().fromJson(convert, listType);
                                finalList.addAll(inventryitemlist);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG : error----", e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            Log.e("TAG onComplete------", "onComplete");
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                loadFromServer(search);
                            } else {
                                if (count != 0) {
                                    App_preference.getSharedprefInstance()
                                            .setInventryItemSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                                }
                                updateindex = 0;
                                count = 0;
                                quoteItemAdd_view.setInventryItem(finalList);
                            }
                        }
                    });
        }
    }

    @Override
    public void getJobServiceTittle() {
        if (jobtitleDataList.isEmpty()) {
            jobtitleDataList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobTitleModel().getJobTitlelist();
        }
        quoteItemAdd_view.setJobServices(jobtitleDataList);
    }


    @Override
    public void apiCallAddQuotesItem(AddItem_Model reqModel) {
        JsonObject jsonObject = AppUtility.getJsonObject(new Gson().toJson(reqModel));
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow((Context) quoteItemAdd_view);
            ApiClient.getservices().eotServiceCall(Service_apis.addQuotItemForMobile, AppUtility.getApiHeaders(), jsonObject)

                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            Log.e("Responce--->>>", "" + jsonObject.toString());
                            if (jsonObject.get("success").getAsBoolean()) {
                                String convert = new Gson().toJson(jsonObject.get("data"));
                                EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                quoteItemAdd_view.setItemAdded();
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                quoteItemAdd_view.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            AppUtility.progressBarDissMiss();
                            Log.e("", e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                        }
                    });

        } else {
            netWork_erroR();
        }
    }


    @Override
    public void callApiUpdateQuotesItem(Update_Quote_ReQ updateItemmodel) {
        JsonObject jsonObject = AppUtility.getJsonObject(new Gson().toJson(updateItemmodel));
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow((Context) quoteItemAdd_view);
            ApiClient.getservices().eotServiceCall(Service_apis.updateQuotItemForMobile, AppUtility.getApiHeaders(), jsonObject)

                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            Log.e("Responce--->>>", "" + jsonObject.toString());
                            if (jsonObject.get("success").getAsBoolean()) {
                                String convert = new Gson().toJson(jsonObject.get("data"));
                                EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                quoteItemAdd_view.setItemAdded();
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                quoteItemAdd_view.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            AppUtility.progressBarDissMiss();
                            Log.e("", e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                        }
                    });

        } else {
            netWork_erroR();
        }
    }
}
