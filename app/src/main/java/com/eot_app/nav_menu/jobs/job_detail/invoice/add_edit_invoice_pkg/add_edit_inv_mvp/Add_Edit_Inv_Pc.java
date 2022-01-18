package com.eot_app.nav_menu.jobs.job_detail.invoice.add_edit_invoice_pkg.add_edit_inv_mvp;

import android.content.Context;
import android.util.Log;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.activitylog.LogModel;
import com.eot_app.nav_menu.jobs.job_detail.invoice.inventry_pkg.Inventry_ReS_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Inv_Res_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.ItemData;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Tax;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_model_pkg.Invoice_Update_Request_Model;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.setting_db.FieldWorker;
import com.eot_app.utility.settings.setting_db.JobTitle;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Add_Edit_Inv_Pc implements Add_Edit_Inv_Pi {
    private final List<Tax> taxList = new ArrayList<>();
    private final Add_Edit_Inv_View add_edit_Inv_view;
    private final int updateindex;
    private final int updatelimit;
    private final List<Inventry_ReS_Model> inveteryItemDataList = new ArrayList<>();
    private int count;
    private String search_txt = "";
    private List<FieldWorker> fwDataList = new ArrayList<>();
    private List<JobTitle> jobtitleDataList = new ArrayList<>();


    public Add_Edit_Inv_Pc(Add_Edit_Inv_View add_edit_Inv_view) {
        this.add_edit_Inv_view = add_edit_Inv_view;
        this.updateindex = 0;
        this.updatelimit = AppConstant.LIMIT_MID;
    }

    @Override
    public void getFwList() {
        if (fwDataList.isEmpty()) {
            fwDataList = AppDataBase.getInMemoryDatabase((Context) add_edit_Inv_view).fieldWorkerModel().getFieldWorkerlist();
        }
        add_edit_Inv_view.setFieldWorKerList(fwDataList);
    }

    @Override
    public void getJobServiceTittle() {
        if (jobtitleDataList.isEmpty()) {
            jobtitleDataList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobTitleModel().getJobTitlelist();
        }
        add_edit_Inv_view.setJobtitleList(jobtitleDataList);
    }

    @Override
    public void initialize_FwList_ServiceTittle_inventoryList() {
        fwDataList = AppDataBase.getInMemoryDatabase((Context) add_edit_Inv_view).fieldWorkerModel().getFieldWorkerlist();
        jobtitleDataList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobTitleModel().getJobTitlelist();
    }

    @Override
    public void updateSearchText(String searchText) {
        this.search_txt = searchText;
        inveteryItemDataList.clear();
        getInventryItemListFromServer();
    }

    @Override
    public void getInventryItemListFromServer() {
        Log.e("data--->>>", "data--->>>");
        add_edit_Inv_view.setItemdata(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).invoiceItemDao().getInventryItemList());
    }

    @Override
    public void getTaxList() {
        add_edit_Inv_view.setTaxList(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).invoiceTaxDao().getTaxList());
    }

    @Override
    public void getInventryItemList() {
        add_edit_Inv_view.setItemdata(inveteryItemDataList);
    }

    @Override
    public void addInvoiceItem(final Invoice_Update_Request_Model inv_res_model) {
        try {
            for (ItemData itemdata : inv_res_model.getItemData()) {
                for (Tax tax : itemdata.getTax()) {
//                    if (tax.getTxRate() == null) {
//                        tax.setTxRate(tax.getRate());
//                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObject jsonObject = AppUtility.getJsonObject(new Gson().toJson(inv_res_model));

        if (AppUtility.isInternetConnected()) {
            LogModel logModel = ActivityLogController
                    .getObj(ActivityLogController.JOB_MODULE, ActivityLogController.JOB_ADD_INVOICE_ITEM, ActivityLogController.JOB_MODULE);
            ActivityLogController.saveOfflineTable(logModel);
            AppUtility.progressBarShow((Context) add_edit_Inv_view);
            ApiClient.getservices().eotServiceCall(Service_apis.updateInvoice, AppUtility.getApiHeaders(), jsonObject)

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
                                Inv_Res_Model invResModel = new Gson().fromJson(convert, Inv_Res_Model.class);
                                List<ItemData> itemDataList = new ArrayList<>();
                                for (ItemData itemData : invResModel.getItemData()) {
                                    itemData.setAmount(AppUtility.getCalculatedAmount(itemData.getQty(), itemData.getRate(), itemData.getDiscount(),
                                            itemData.getTax(), invResModel.getTaxCalculationType()));
                                    itemDataList.add(itemData);
                                }
                                invResModel.getItemData().clear();
                                invResModel.setItemData(itemDataList);
                                add_edit_Inv_view.setInvoiceData(invResModel);
                                EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.add_edit_item));
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                EotApp.getAppinstance().sessionExpired();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            add_edit_Inv_view.errorOccured();
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

    private void addtempItemInDb(ItemData itemData) {
    }


    private void netWork_erroR() {
        AppUtility.alertDialog(((Context) add_edit_Inv_view), LanguageController.getInstance().
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
