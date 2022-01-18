package com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.mvp;

import static com.eot_app.utility.AppUtility.getJsonObject;

import android.content.Context;
import android.util.Log;

import com.eot_app.nav_menu.jobs.job_detail.invoice.inventry_pkg.Inventry_ReQ_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.inventry_pkg.Inventry_ReS_Model;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.settings.setting_db.FieldWorker;
import com.eot_app.utility.settings.setting_db.JobTitle;
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
 * Created by Sonam-11 on 10/6/20.
 */
public class AddEditInvoiceItem_PC implements AddEditInvoiceItem_PI {
    private final AddEditInvoiceItem_View invoiceItem_view;
    private final int updatelimit;
    List<Inventry_ReS_Model> finalList = new ArrayList<>();
    private List<FieldWorker> fwDataList = new ArrayList<>();
    private List<JobTitle> jobtitleDataList = new ArrayList<>();
    private int count;
    private int updateindex;

    public AddEditInvoiceItem_PC(AddEditInvoiceItem_View invoiceItem_view) {
        this.invoiceItem_view = invoiceItem_view;
        this.updatelimit = AppConstant.LIMIT_HIGH;
    }


    @Override
    public void getFwList() {
        if (fwDataList.isEmpty()) {
            fwDataList = AppDataBase.getInMemoryDatabase((Context) invoiceItem_view).fieldWorkerModel().getFieldWorkerlist();
        }
        invoiceItem_view.setFieldWorKerList(fwDataList);
    }

    @Override
    public void getTaxList() {
        invoiceItem_view.setTaxList(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).invoiceTaxDao().getTaxList());
    }

    @Override
    public void getInventryItemList() {
        // if (AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).invoiceItemDao().getInventryItemList().size() > 0) {
        invoiceItem_view.setItemdata(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).invoiceItemDao().getInventryItemList());
        // }
    }

    @Override
    synchronized public void getDataFromServer(final String search) {
        Log.e("data--->>>", "data--->>>");
        if (AppUtility.isInternetConnected()) {
            //   AppUtility.progressBarShow((Context) invoiceItem_view);
            Inventry_ReQ_Model inventry_model = new Inventry_ReQ_Model(
                    Integer.parseInt(App_preference.getSharedprefInstance().getLoginRes().getCompId()),
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
                            AppUtility.progressBarDissMiss();
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                getDataFromServer(search);
                            } else {
                                if (count != 0) {
                                    App_preference.getSharedprefInstance()
                                            .setInventryItemSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                                }
                                updateindex = 0;
                                count = 0;
                                invoiceItem_view.setItemdata(finalList);
                            }
                        }
                    });
        }
    }

    private void addInvoiceItemInDB(List<Inventry_ReS_Model> inventryitemlist) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).invoiceItemDao().insertInvebtryItems(inventryitemlist);
        getInventryItemList();
    }

    @Override
    public void getJobServiceTittle() {
        if (jobtitleDataList.isEmpty()) {
            jobtitleDataList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobTitleModel().getJobTitlelist();
        }
        invoiceItem_view.setJobtitleList(jobtitleDataList);
    }
}
