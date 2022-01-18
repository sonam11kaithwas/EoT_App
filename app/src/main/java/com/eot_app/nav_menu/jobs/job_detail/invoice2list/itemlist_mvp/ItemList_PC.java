package com.eot_app.nav_menu.jobs.job_detail.invoice2list.itemlist_mvp;

import android.content.Context;
import android.util.Log;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.activitylog.LogModel;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.AddInvoiceItemReqModel;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.InvoiceItemDataModel;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.location_tax_dao.TaxesLocation;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.model_pkg.ItembyJobModel;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Inv_List_Req_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice2list.itemlist_model.InvoiceItemDetailsModel;
import com.eot_app.nav_menu.jobs.job_detail.invoice2list.itemlist_model.RemoveItems;
import com.eot_app.nav_menu.jobs.joboffline_db.JobOfflineDataModel;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.db.OfflineDataController;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.setting_db.Offlinetable;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Sonam-11 on 11/6/20.
 */
public class ItemList_PC implements ItemList_PI {
    private final ItemList_View itemListView;
    private final int updatelimit;
    private int count;
    private int updateindex;

    public ItemList_PC(ItemList_View itemListView) {
        this.itemListView = itemListView;
        this.updatelimit = AppConstant.LIMIT_HIGH;
    }


    @Override
    public void getloctaxexList() {
        List<TaxesLocation> locationModelList = new ArrayList<>();
        locationModelList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).taxesLocationDao().getTaxLocationList();
        itemListView.setLocationTaxsList(locationModelList);
    }

    @Override
    public void getItemListByJobFromDB(String jobId) {
        Job job = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(jobId);
        try {
            if (job.getItemData() != null && job.getItemData() != null && job.getItemData().size() > 0) {
                itemListView.setItemListByJob(job.getItemData());
            } else {
                itemListView.setItemListByJob(new ArrayList<InvoiceItemDataModel>());
            }
        } catch (Exception ex) {
            ex.getMessage();
        }

    }


    /****Remove Item's***/
    @Override
    public void updareRmitemsInDB(String jobId, List<InvoiceItemDataModel> updateItemList, ArrayList<String> ijmmIdList
            , List<InvoiceItemDataModel> notSyncItemList, boolean removeItemOnInvoice) {
        /***Remove item from JOB table***/
        addgetItemFromJobToDB(updateItemList, jobId);

        Job job = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(jobId);
        if (job.getJobId().equals(job.getTempId())) {
            JobOfflineDataModel jobOfflineDataModel = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobOfflineDao().
                    getJobofflineDataForInvoice(Service_apis.addItemOnJob, job.getTempId());
            try {
                if (job.getLocId() == null) {
                    job.setLocId("0");
                }
            } catch (Exception exception) {
                exception.getMessage();
                job.setLocId("0");
            }

            AddInvoiceItemReqModel addInvoiceItem = new AddInvoiceItemReqModel(jobId, updateItemList, removeItemOnInvoice, job.getLocId());
            Gson gson = new Gson();
            jobOfflineDataModel.setParams(gson.toJson(addInvoiceItem));
            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobOfflineDao().update(jobOfflineDataModel);
        } else {
            /***remove update item from Offline Table***/
            if (ijmmIdList.size() > 0) {
                List<Offlinetable> offlinetableList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().getOfflinetablesById(Service_apis.updateItemInJobMobile);
                for (Offlinetable off : offlinetableList) {
                    AddInvoiceItemReqModel updateModelModel = new Gson().fromJson(off.getParams(), AddInvoiceItemReqModel.class);
                    for (String ijmnId : ijmmIdList) {
                        if (updateModelModel.getJobId().equals(jobId) && updateModelModel.getItemData().get(0).getIjmmId().equals(ijmnId)) {
                            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().deleteFromId(off.getId());
                            break;
                        }
                    }
                }
            }

            /***remove not synnc Item's**/
            if (notSyncItemList.size() > 0) {
                List<Offlinetable> offlinetable = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().getOfflinetablesById(Service_apis.addItemOnJob);
                for (Offlinetable offlineModel : offlinetable) {
                    AddInvoiceItemReqModel updateModelModel = new Gson().fromJson(offlineModel.getParams(), AddInvoiceItemReqModel.class);
                    for (InvoiceItemDataModel itemModel : updateModelModel.getItemData()) {
                        for (InvoiceItemDataModel notSyncItem : notSyncItemList) {

                            if (updateModelModel.getJobId().equals(jobId)) {
                                if (notSyncItem.getDataType().equals("1") || notSyncItem.getDataType().equals("2") &&
                                        notSyncItem.getItemType().equals("0") || notSyncItem.getItemType().equals("")
                                        && notSyncItem.getItemId().equals(itemModel.getItemId())) {
                                    updateModelModel.getItemData().remove(itemModel);
                                    offlineModel.setParams(new Gson().toJson(updateModelModel));
                                    AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().update(offlineModel);
                                    break;
                                } else if (notSyncItem.getDataType().equals("1") && notSyncItem.getItemType().equals("1")
                                        && notSyncItem.getInm().equals(itemModel.getInm())) {
                                    updateModelModel.getItemData().remove(itemModel);
                                    offlineModel.setParams(new Gson().toJson(updateModelModel));
                                    AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().update(offlineModel);
                                    break;
                                } else if (notSyncItem.getDataType().equals("3") &&
                                        notSyncItem.getItemId().equals(itemModel.getItemId())) {
                                    updateModelModel.getItemData().remove(itemModel);
                                    offlineModel.setParams(new Gson().toJson(updateModelModel));
                                    AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().update(offlineModel);
                                    break;
                                }
                                break;
                            }
                        }
                    }
                }
            }


        }


        Gson gson = new Gson();
        if (ijmmIdList.size() > 0) {
            RemoveItems model = new RemoveItems(jobId, ijmmIdList);
            String removeModel = gson.toJson(model);
            OfflineDataController.getInstance().addInOfflineDB(Service_apis.deleteItemFromJob, removeModel, AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
        }

        getItemListByJobFromDB(jobId);
    }


    /****Featch update Job/Invoice Item From Server***/
    @Override
    public void getItemFromServer(final String jobId) {
        if (AppUtility.isInternetConnected()) {
            ItembyJobModel model = new ItembyJobModel(jobId);//, App_preference.getSharedprefInstance().getJobSyncTime()
            String data = new Gson().toJson(model);
            ApiClient.getservices().eotServiceCall(Service_apis.getItemFromJob, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
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
                                Type listType = new TypeToken<List<InvoiceItemDataModel>>() {
                                }.getType();
                                List<InvoiceItemDataModel> data = new Gson().fromJson(convert, listType);
                                if (data.size() > 0) {
                                    addgetItemFromJobToDB(data, jobId);
                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                itemListView.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {
                                itemListView.finishActivity();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            itemListView.dismissPullTorefresh();
                            itemListView.finishActivity();
                        }

                        @Override
                        public void onComplete() {
                            getItemListByJobFromDB(jobId);
                        }
                    });
        } else {
            itemListView.dismissPullTorefresh();
            networkError();
        }
    }


    private void addgetItemFromJobToDB(List<InvoiceItemDataModel> data, String jobId) {
        if (data != null && data.size() > 0) {
            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().updateJobitems(jobId, data);
        }
    }


    /****Get Invoice Details for Generate Invoice***/
    @Override
    public void getinvoicedetails(final String jobId) {
        Inv_List_Req_Model inv_list_req_model = new Inv_List_Req_Model(jobId);
        JsonObject jsonObject = AppUtility.getJsonObject(new Gson().toJson(inv_list_req_model));

        if (AppUtility.isInternetConnected()) {
            LogModel logModel = ActivityLogController
                    .getObj(ActivityLogController.JOB_MODULE, ActivityLogController.JOB_INVOICE_LIST, ActivityLogController.JOB_MODULE);
            ActivityLogController.saveOfflineTable(logModel);
            AppUtility.progressBarShow((Context) itemListView);
            ApiClient.getservices().eotServiceCall(Service_apis.getInvoiceDetailMobile, AppUtility.getApiHeaders(), jsonObject)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                Gson gson = new Gson();
                                InvoiceItemDetailsModel invResModel = gson.fromJson(jsonObject.get("data"), InvoiceItemDetailsModel.class);
                                itemListView.setInvoiceDetails(invResModel);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                itemListView.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {
                                itemListView.InvoiceNotFound(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }


                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("", e.getMessage());
                            AppUtility.progressBarDissMiss();
                            itemListView.errorActivityFinish(e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                        }
                    });


        } else {
            networkError();
        }
    }


    /*****Generate Invoice Pdf File*****/
    @Override
    public void getGenerateInvoicePdf(String invId, String isProformaInv) {
        final Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("invId", invId);
        jsonMap.put("isProformaInv", isProformaInv);
        JsonObject jsonObject = AppUtility.getJsonObject(new Gson().toJson(jsonMap));
        if (AppUtility.isInternetConnected()) {
            ActivityLogController.saveActivity(
                    ActivityLogController.JOB_MODULE,
                    ActivityLogController.JOB_GENERATE_INVOICE_PDF,
                    ActivityLogController.JOB_MODULE
            );
            AppUtility.progressBarShow((Context) itemListView);
            ApiClient.getservices().eotServiceCall(Service_apis.generateInvoicePDF, AppUtility.getApiHeaders(), jsonObject)

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
                                itemListView.onGetPdfPath(jsonObject.getAsJsonObject("data").get("path").getAsString());
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                itemListView.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {
                                itemListView.finishActivity();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            AppUtility.progressBarDissMiss();
                            Log.e("", e.getMessage());
                            itemListView.finishActivity();
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                        }
                    });

        } else {
            networkError();
        }
    }

    private void networkError() {
        AppUtility.alertDialog(((Context) itemListView), LanguageController.getInstance().
                getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().
                getMobileMsgByKey(AppConstant.err_check_network), LanguageController.getInstance().
                getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }
}
