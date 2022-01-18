package com.eot_app.utility.db;

import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import com.eot_app.BuildConfig;
import com.eot_app.nav_menu.appointment.addupdate.model.AppointmentAddReq;
import com.eot_app.nav_menu.appointment.addupdate.model.AppointmentUpdateReq;
import com.eot_app.nav_menu.appointment.dbappointment.Appointment;
import com.eot_app.nav_menu.audit.addAudit.add_aduit_model_pkg.AddAudit_Req;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
import com.eot_app.nav_menu.audit.audit_list.report.mode.ReportRequest;
import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
import com.eot_app.nav_menu.item.QtyReqModel;
import com.eot_app.nav_menu.jobs.add_job.addjobmodel.AddJob_Req;
import com.eot_app.nav_menu.jobs.job_controller.ChatController;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.AddInvoiceItemReqModel;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.InvoiceItemDataModel;
import com.eot_app.nav_menu.jobs.job_detail.detail.jobdetial_model.Jobdetail_status_res;
import com.eot_app.nav_menu.jobs.joboffline_db.JobOfflineDataModel;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.firstSync.FirstSyncPC;
import com.eot_app.utility.settings.setting_db.ErrorLog;
import com.eot_app.utility.settings.setting_db.Offlinetable;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.hypertrack.hyperlog.HyperLog;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by aplite_pc302 on 7/5/18.
 */

public class OfflineDataController {
    private static final OfflineDataController ourInstance = new OfflineDataController();
    private final String TAG = "OfflineDataController";
    private final Gson gson = new Gson();
    FirstSyncPC.CallBackFirstSync callBackFirstSync;
    private boolean isSync = false;
    OfflineSericeCallBack callBack = new OfflineSericeCallBack() {
        @Override
        public void getResponse(Offlinetable data, JsonObject obj) {
            Log.e("TAG : STATUS :", data.getParams());
//            before deleting the row some thing (*Add) have to confirm.
            switch (data.getService_name()) {
                case Service_apis.updateClientSite:
                    Site_model siteModel = gson.fromJson(obj.get("data").getAsJsonObject().toString(), Site_model.class);
                    AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().update(siteModel);
                    EotApp.getAppinstance().notifyCon_SiteObserver(data.getService_name());
                    break;
                case Service_apis.updateAppointment:
                    AppointmentUpdateReq updateReq = gson.fromJson(data.getParams(), AppointmentUpdateReq.class);
                    if (updateReq.getMemIds() == null || updateReq.getMemIds().size() == 0) {
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel().deleteAppointmentById(updateReq.getAppId());
                    } else if (!(updateReq.getMemIds()).contains(App_preference.getSharedprefInstance().getLoginRes().getUsrId())) {
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel().deleteAppointmentById(updateReq.getAppId());
                    }
                    EotApp.getAppinstance().notifyApiObserver(data.getService_name());
                    break;
                case Service_apis.addAppointment:
                    Appointment appointmentItem = gson.fromJson(obj.get("data").getAsJsonObject().toString(), Appointment.class);
                    AppointmentAddReq addAppointment_req = gson.fromJson(data.getParams(), AppointmentAddReq.class);
                    if (addAppointment_req.getMemIds() == null || addAppointment_req.getMemIds().size() == 0) {
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel().deleteAppointmentByTempId(addAppointment_req.getTempId());
                    } else if (!(addAppointment_req.getMemIds()).contains(App_preference.getSharedprefInstance().getLoginRes().getUsrId())) {
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel().deleteAppointmentByTempId(addAppointment_req.getTempId());

                    } else {
                        Appointment oldAppointment = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel().getAppointmentById(addAppointment_req.getTempId());
                        oldAppointment.setAppId(appointmentItem.getAppId());
                        oldAppointment.setLabel(appointmentItem.getLabel());
                        List<Appointment> appointmentList = new ArrayList<>();
                        appointmentList.add(oldAppointment);
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel().insertAppointments(appointmentList);
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel().deleteAppointmentById(addAppointment_req.getTempId());
                    }
                    EotApp.getAppinstance().notifyApiObserver(data.getService_name());
                    break;

                case Service_apis.addAudit:
                    AuditList_Res auditList_res = gson.fromJson(obj.get("data").getAsJsonObject().toString(), AuditList_Res.class);
                    AddAudit_Req addAudit_req = gson.fromJson(data.getParams(), AddAudit_Req.class);
                    if (addAudit_req.getMemIds() == null || addAudit_req.getMemIds().size() == 0) {
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().deleteAuditByTempId(addAudit_req.getTempId());
                    } else if (!(addAudit_req.getMemIds()).contains(App_preference.getSharedprefInstance().getLoginRes().getUsrId())) {
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().deleteAuditByTempId(addAudit_req.getTempId());

                    } else {
                        AuditList_Res oldAuditList_res = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().getAuditById(addAudit_req.getTempId());
                        oldAuditList_res.setAudId(auditList_res.getAudId());
                        oldAuditList_res.setLabel(auditList_res.getLabel());
                        List<AuditList_Res> auditList_res1 = new ArrayList<>();
                        auditList_res1.add(oldAuditList_res);
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().inserAuditList(auditList_res1);
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().deletAuditById(addAudit_req.getTempId());
                    }
                    EotApp.getAppinstance().notifyApiObserver(data.getService_name());
                    break;

                case Service_apis.addJob:
                    Job jobitem = gson.fromJson(obj.get("data").getAsJsonObject().toString(), Job.class);

                    AddJob_Req addJob_req = gson.fromJson(data.getParams(), AddJob_Req.class);
                    if (addJob_req.getMemIds() == null) {
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().deleteJobByTempId(jobitem.getTempId());
                    } else if (!(addJob_req.getMemIds()).contains(App_preference.getSharedprefInstance().getLoginRes().getUsrId())) {
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().deleteJobByTempId(jobitem.getTempId());

                        /***initialize all fieldworkers unread count "0" with admin and super admin*/
                        if (App_preference.getSharedprefInstance().getLoginRes().getAdminIds() != null) {
                            List<String> userIds = App_preference.getSharedprefInstance().getLoginRes().getAdminIds();
                            userIds.addAll(addJob_req.getMemIds());
                            ChatController.getInstance().initializeUnreadFromJobID(jobitem.getLabel(), jobitem.getJobId(), userIds);
                        }
                    } else {
                        /**    ********  android.database.sqlite.SQLiteConstraintException: Unique constrains issue  ************  **/
                        Job oldJod = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(jobitem.getTempId());
                        oldJod.setJobId(jobitem.getJobId());
                        oldJod.setLabel(jobitem.getLabel());
                        List<Job> jobList = new ArrayList<>();
                        jobList.add(oldJod);
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().inserJob(jobList);

//                    add job into database
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().deleteJobById(jobitem.getTempId());
                    }
                    jobStatusAndItemSync(jobitem);
                    EotApp.getAppinstance().notifyApiObserver(data.getService_name());

                    try {
                        if (jobitem != null && jobitem.getJobId() != null && jobitem.getLabel() != null) {
                            String msg = "A new Job has been created with Job code " + jobitem.getLabel() + ".";// in the system
                            String s1 = (Html.fromHtml("<b>" + jobitem.getLabel()).toString() + "</b>");

                            String tempMsg = "A new Job has been " + (Html.fromHtml("<b>created</b>"))
                                    + " with Job code " + s1 + ".";

                            ChatController.getInstance().notifyWeBforNew("JOB", "AddJob", jobitem.getJobId(), tempMsg, "");
                        }
                    } catch (Exception exception) {
                        exception.getMessage();
                    }
                    HyperLog.i(TAG, "Add job pending operation completed");

                    break;
                case Service_apis.addClientContact:
                    ContactData contactitem = gson.fromJson(obj.get("data").getAsJsonObject().toString(), ContactData.class);
                    AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().udpateContactByTempIdtoOriganalId(contactitem.getConId(), contactitem.getTempId());
                    if (contactitem.getDef().equals("1")) {
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().updateDefaultContact(contactitem.getConId(), contactitem.getCltId());
                    }
                    EotApp.getAppinstance().notifyCon_SiteObserver(data.getService_name());
                    break;
                case Service_apis.addClientSite:
                    Site_model siteitem = gson.fromJson(obj.get("data").getAsJsonObject().toString(), Site_model.class);
                    AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel()
                            .updateSiteByTempIdtoOriganalId(siteitem.getSiteId(), siteitem.getTempId());
                    if (siteitem.getDef().equals("1")) {
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().updateDefault(siteitem.getCltId(), siteitem.getSiteId());
                    } else {
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().update(siteitem);
                    }
                    EotApp.getAppinstance().notifyCon_SiteObserver(data.getService_name());

                    break;
                case Service_apis.addClient:
//                  add client into data base
                    Client clientitem = gson.fromJson(obj.get("data").getAsJsonObject().toString(), Client.class);
                    AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientModel().udpateClientByTempIdtoOriganalId(clientitem.getCltId(), clientitem.getTempId());
                    EotApp.getAppinstance().notifyApiObserver(data.getService_name());
                    try {
                        if (clientitem != null && clientitem.getCltId() != null && clientitem.getNm() != null) {
                            String msg = "A new Client has been created with Client Name " + clientitem.getNm() + ".";// in the system

                            ChatController.getInstance().notifyWeBforNew("CLIENT", "AddClient", clientitem.getCltId(), msg, "");
                        }
                    } catch (Exception exception) {
                        exception.getMessage();
                    }
                    break;
                case Service_apis.changeJobStatus:
                    if (obj.get("data").isJsonArray()
                            && obj.get("data").getAsJsonArray().size() > 0
                            && obj.get("data").getAsJsonArray().get(0).getAsJsonObject().get("status_code").
                            getAsString().equals("1")) {
                        String jobId = obj.get("data").getAsJsonArray().get(0).getAsJsonObject().get("jobid").getAsString();
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().deleteJobById(jobId);
//  for alert msg to remove job
                        EotApp.getAppinstance().notifyObserver("removeFW", jobId, LanguageController.getInstance().getServerMsgByKey(obj.get("message").getAsString()));
// delete all record related to job which is deleted by server
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().deleteFromSearchJobID("%" + jobId + "%");
                    }
                    EotApp.getAppinstance().notifyApiObserver(data.getService_name());

                    try {
                        Job j = gson.fromJson(data.getParams(), Job.class);
                        if (j != null && j.getJobId() != null) {
                            Job tempJob = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(j.getJobId());
                            if (tempJob != null && tempJob.getJobId() != null && tempJob.getNm() != null) {

                                String s1 = (Html.fromHtml("<b>" + tempJob.getLabel()).toString() + "</b>");

                                String tempMsg = "A Job " + (Html.fromHtml("<b>status</b>"))
                                        + " of " + s1 + " has been updated by fieldworker " + App_preference.getSharedprefInstance().getLoginRes().getUsername() + ".";

                                ChatController.getInstance().notifyWeBforNew("JOB", "JobStatus", tempJob.getJobId(), tempMsg, tempJob.getStatus());
                            }
                        }
                    } catch (Exception exception) {
                        exception.getMessage();
                    }
                    break;
                case Service_apis.addItemOnJob:
                case Service_apis.deleteItemFromJob:
                case Service_apis.updateItemInJobMobile: {
                    updateJobItems(data, obj);
                    break;
                }
                case Service_apis.updateItemQuantity:
                    notifyJobItemData(obj, data);
                    break;
            }
            int check = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().deleteFromId(data.getId());
            if (check == 1) {
                getTotalRequest();
            }

        }
    };


    private OfflineDataController() {
    }

    public static OfflineDataController getInstance() {
        return ourInstance;
    }

    /****Notify Job Item List after QTy update***/
    private void notifyJobItemData(JsonObject obj, Offlinetable data) {
        HyperLog.i(TAG, "notifyJobItemData(M) start");
        try {
            if (obj != null && obj.has("data") && obj.get("data").isJsonArray()
                    && obj.get("data").getAsJsonArray().size() > 0) {
                Type listType = new TypeToken<List<InvoiceItemDataModel>>() {
                }.getType();
                List<InvoiceItemDataModel> dataList = new Gson().fromJson(new Gson().toJson(obj.get("data").getAsJsonArray()), listType);

                QtyReqModel qtyReqModel = gson.fromJson(data.getParams(), QtyReqModel.class);
                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().updateJobitems(qtyReqModel.getJobId(), dataList);

                EotApp.getAppinstance().notifyApiItemAddEdit_Observer(data.getService_name(), qtyReqModel.getJobId());

            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            HyperLog.i(TAG, "JsonSyntaxException " + e.toString());
        }
        HyperLog.i(TAG, "notifyJobItemData(M) Completed");
    }

    public void updateSyncdata() {
        int i = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().getList().size();
        if (i > 0) {
            HyperLog.i(TAG, "updateSyncdata(M) Hard Refresh calling....");
            isSync = false;
            getTotalRequest();
        }
    }


    /***Sync Job State's & Item's from Local Table to OfflineTable ****/
    private void jobStatusAndItemSync(Job jobitem) {
        try {
            List<JobOfflineDataModel> offlineJobstatusdata = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobOfflineDao().getJobofflineDataById(jobitem.getTempId(),
                    Service_apis.changeJobStatus);
            try {
                if (offlineJobstatusdata != null)
                    HyperLog.i(TAG, "job status Temp to offlineTable start:" + offlineJobstatusdata.size());
                if (offlineJobstatusdata != null && offlineJobstatusdata.size() > 0) {
                    for (JobOfflineDataModel model : offlineJobstatusdata) {
                        Jobdetail_status_res jobdetail_status_res = new Gson().fromJson(model.getParams(), Jobdetail_status_res.class);
                        jobdetail_status_res.setJobId(jobitem.getJobId());
                        String param = new Gson().toJson(jobdetail_status_res);
                        model.setParams(param);

                        addInOfflineDB(model.getService_name(), model.getParams(), model.getTimestamp());
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobOfflineDao().deleteFromId(model.getId());
                    }
                }
                HyperLog.i(TAG, "job status Temp to offlineTable completed success:");

            } catch (Exception ex) {
                ex.printStackTrace();
                HyperLog.i(TAG, "job status Temp to offlineTable exception:" + ex.toString());

            }

            try {
                JobOfflineDataModel offlineJobItemdata = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                        .jobOfflineDao().getJobofflineDataForInvoice(Service_apis.addItemOnJob, jobitem.getTempId());
                if (offlineJobItemdata != null) {
                    HyperLog.i(TAG, "Job item temp to offline start:" + offlineJobItemdata.toString());
                    AddInvoiceItemReqModel addInvoiceItemReqModel = new Gson().fromJson(offlineJobItemdata.getParams(), AddInvoiceItemReqModel.class);
                    addInvoiceItemReqModel.setJobId(jobitem.getJobId());
                    String param = new Gson().toJson(addInvoiceItemReqModel);
                    offlineJobItemdata.setParams(param);
                    addInOfflineDB(offlineJobItemdata.getService_name(), offlineJobItemdata.getParams(), offlineJobItemdata.getTimestamp());

                    AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobOfflineDao().deleteFromId(offlineJobItemdata.getId());
                    HyperLog.i(TAG, "Job item temp to offline completed success:" + offlineJobItemdata.toString());

                }
            } catch (Exception ex) {
                ex.getStackTrace();
                HyperLog.i(TAG, "Job item temp to offline exception:" + ex.toString());

            }


        } catch (Exception ex) {
            ex.getStackTrace();
            HyperLog.i(TAG, "jobStatusAndItemSync(M) exception:" + ex.toString());
        }
    }

    /**
     * Notify Add/Update?Remove Item's
     ****/
    private void updateJobItems(Offlinetable data, JsonObject obj) {
        try {
            HyperLog.i(TAG, "updateJobItems(M) start");

            AddInvoiceItemReqModel addInvoiceItemReqModel = gson.fromJson(data.getParams(), AddInvoiceItemReqModel.class);

            if (data.getService_name().equals(Service_apis.deleteItemFromJob)) {
                addInvoiceItemReqModel.setAddItemOnInvoice(true);
            }
            if (obj != null && obj.has("data") && obj.get("data").isJsonArray()
                    && obj.get("data").getAsJsonArray().size() > 0) {
//        if (obj.get("data").isJsonArray() && obj.get("data").getAsJsonArray().size() > 0) {
                Type listType = new TypeToken<List<InvoiceItemDataModel>>() {
                }.getType();
                List<InvoiceItemDataModel> dataList = new Gson().fromJson(new Gson().toJson(obj.get("data").getAsJsonArray()), listType);
                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().updateJobitems(addInvoiceItemReqModel.
                        getJobId(), dataList);
                if (!addInvoiceItemReqModel.isAddItemOnInvoice()) {
                    EotApp.getAppinstance().notifyApiItemAddEdit_Observer(data.getService_name(), addInvoiceItemReqModel.getJobId());
                } else {
                    EotApp.getAppinstance().notifyInvoiceItemObserver(obj.get("totalAmount").toString());
                }
            }

            HyperLog.i(TAG, "updateJobItems(M) completed");

        } catch (Exception ex) {
            ex.printStackTrace();
            HyperLog.i(TAG, "updateJobItems(M) exception:" + ex.toString());

        }


    }

    public void addInOfflineDB(String url, String params, String timestamp) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().insertOffline(new Offlinetable(url, params, timestamp));
        if (!isSync) {
            HyperLog.i(TAG, "Syncing start pending request:" +
                    AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().getCountOfRow() + "");
            getTotalRequest();
        } else
            HyperLog.i(TAG, "syncing already in progress pending:" + AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().getCountOfRow());
    }

    public void getTotalRequest() {
        if (AppUtility.isInternetConnected()) {
            Offlinetable record = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().getSingleRecord();
            if (record != null) {
                isSync = true;
                callPendingRequest(record);
            } else {
                HyperLog.i(TAG, "Sync Completed pending request: " + AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().getCountOfRow());
                isSync = false;
                if (callBackFirstSync != null) { // for first time call from sync
                    callBackFirstSync.getCallBackOfComplete(1, "no pending records");
                    callBackFirstSync = null;
                }
                int rows = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).errorLogmodel().getNoOfRows();
                if (rows > 0) {
                    ErrorLogController ctr = new ErrorLogController();
                    ctr.sendErrorReports();
                }
//                remove all jobs, clients, sites, from main tables.
//                removeAllTempJobClientContactSite();
            }
        }
    }


    private synchronized void callPendingRequest(final Offlinetable table) {
        Log.e("TAG's", "" + table.getParams());
        HyperLog.i(TAG, "API NAME: " + table.getService_name());
        HyperLog.i(TAG, "PARAM:" + table.getParams());
        HyperLog.i(TAG, "OFFLINE TABLE Data Synced:");
        if (table.getService_name().equals(Service_apis.addAppointment) || table.getService_name().equals(Service_apis.updateAppointment)) {
            callPendingAppointmentRequest(table);
            return;
        } else if (table.getService_name().equals(Service_apis.addAuditReport)) {
            equipmentReport(table);
            return;
        }
        ApiClient.getservices().eotServiceCall(table.getService_name(), AppUtility.getApiHeaders(), getJsonObject(table.getParams()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        Log.e("TAG's", "" + jsonObject.toString());
                        if (jsonObject != null)
                            HyperLog.i(TAG, "Response:" + jsonObject.toString());

                        if (jsonObject.get("success").getAsBoolean()) {
                            callBack.getResponse(table, jsonObject);
                        } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
//                           onSessionExpire(jsonObject.get("message").getAsString());
//                            when session expires
                            EotApp.getAppinstance().sessionExpired();
                            isSync = false;
                        } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.ALREADY_SYNC)) {
//                            when record already exist.
//                            Log.e(TAG, jsonObject.get("message").getAsString());
                            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().deleteFromId(table.getId());
                            isSync = false;
                        } else {
                            if (table.getService_name().equals(Service_apis.addFWlatlong)) {
                                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).errorLogmodel().deleteById(table.getId());
                            } else {
                                /**very important ***/
                                sendForErrorLog(table, LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }

                            if (callBackFirstSync != null) { // for first time call from sync
                                callBackFirstSync.getCallBackOfComplete(0, "Error Occur");
                                callBackFirstSync = null;
                            }
                            isSync = false;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG's", "" + e.getMessage());

                        HyperLog.i(TAG, "onError:" + e.toString());
                        if (table.getService_name().equals(Service_apis.addFWlatlong)) {
                            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).errorLogmodel().deleteById(table.getId());
                        } else {
                            /**very important ***/
                            sendForErrorLog(table, e.getMessage());
                        }

                        isSync = false;
                        if (callBackFirstSync != null) { // for first time call from sync
                            callBackFirstSync.getCallBackOfComplete(0, "Error Occur");
                            callBackFirstSync = null;
                        }
                    }

                    @Override
                    public void onComplete() {
                        /*****/
                        Log.e("", "");
                        Log.e("", "");
                        // isSync = false;
                    }
                });
    }

    private synchronized void callPendingAppointmentRequest(final Offlinetable table) {
        if (table.getService_name().equals(Service_apis.updateAppointment)) {
            callPendingUpdateAppointment(table);
            return;
        }
        final AppointmentAddReq appointmentModel = new Gson().fromJson(table.getParams(), AppointmentAddReq.class);
        RequestBody conId = null;
        RequestBody siteId = null;
        RequestBody cnm = null;
        RequestBody snm = null;


        if (TextUtils.isEmpty(appointmentModel.getConId()))
            conId = RequestBody.create(MultipartBody.FORM, "");
        else
            conId = RequestBody.create(MultipartBody.FORM, appointmentModel.getConId());

        if (TextUtils.isEmpty(appointmentModel.getSiteId()))
            siteId = RequestBody.create(MultipartBody.FORM, "");
        else
            siteId = RequestBody.create(MultipartBody.FORM, appointmentModel.getSiteId());

        if (TextUtils.isEmpty(appointmentModel.getCnm()))
            cnm = RequestBody.create(MultipartBody.FORM, "");
        else cnm = RequestBody.create(MultipartBody.FORM, appointmentModel.getCnm());

        if (TextUtils.isEmpty(appointmentModel.getSnm()))
            snm = RequestBody.create(MultipartBody.FORM, "");
        else snm = RequestBody.create(MultipartBody.FORM, appointmentModel.getSnm());

        RequestBody cltId = RequestBody.create(MultipartBody.FORM, appointmentModel.getCltId());
        RequestBody leadId = RequestBody.create(MultipartBody.FORM, appointmentModel.getLeadId());
        RequestBody des = RequestBody.create(MultipartBody.FORM, appointmentModel.getDes());
        RequestBody schdlStart = RequestBody.create(MultipartBody.FORM, appointmentModel.getSchdlStart());
        RequestBody schdlFinish = RequestBody.create(MultipartBody.FORM, appointmentModel.getSchdlFinish());
        RequestBody nm = RequestBody.create(MultipartBody.FORM, appointmentModel.getNm());
        RequestBody email = RequestBody.create(MultipartBody.FORM, appointmentModel.getEmail());
        RequestBody mob1 = RequestBody.create(MultipartBody.FORM, appointmentModel.getMob1());
        RequestBody mob2 = RequestBody.create(MultipartBody.FORM, appointmentModel.getMob2());
        RequestBody adr = RequestBody.create(MultipartBody.FORM, appointmentModel.getAdr());
        RequestBody city = RequestBody.create(MultipartBody.FORM, appointmentModel.getCity());
        RequestBody state = RequestBody.create(MultipartBody.FORM, appointmentModel.getState());
        RequestBody ctry = RequestBody.create(MultipartBody.FORM, appointmentModel.getCtry());
        RequestBody zip = RequestBody.create(MultipartBody.FORM, appointmentModel.getZip());
        RequestBody fclient = RequestBody.create(MultipartBody.FORM, appointmentModel.getClientForFuture());
        RequestBody fcontact = RequestBody.create(MultipartBody.FORM, appointmentModel.getContactForFuture());
        RequestBody fsite = RequestBody.create(MultipartBody.FORM, appointmentModel.getSiteForFuture());

        String meidString = "";
        if (appointmentModel.getMemIds() != null)
            meidString = new Gson().toJson(appointmentModel.getMemIds());

        RequestBody rmids = RequestBody.create(MultipartBody.FORM, meidString);

        String mimeType = "";
        MultipartBody.Part body = null;
        List<MultipartBody.Part> files = new ArrayList<>();
        for (int i = 0; i < appointmentModel.getAppDoc().size(); i++) {
            File file1 = new File(appointmentModel.getAppDoc().get(i));
            String s = appointmentModel.getFileNames().get(i);
            if (file1 != null) {
                mimeType = URLConnection.guessContentTypeFromName(file1.getName());
                if (mimeType == null) {
                    mimeType = s;
                }
                RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file1);
                // MultipartBody.Part is used to send also the actual file name
                body = MultipartBody.Part.createFormData("appDoc[]", s, requestFile);
                files.add(body);
            }
        }

        ApiClient.getservices().addAppointment(AppUtility.getApiHeaders(),
                cltId, siteId, conId, leadId, des, schdlStart, schdlFinish, nm
                , cnm, snm, email, mob1, mob2, adr, city, state, ctry, zip, fclient, fsite, fcontact, rmids, files)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        if (jsonObject.get("success").getAsBoolean()) {
                            callBack.getResponse(table, jsonObject);
                        } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
//                           onSessionExpire(jsonObject.get("message").getAsString());
//                            when session expires
                            EotApp.getAppinstance().sessionExpired();
                            isSync = false;
                        } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.ALREADY_SYNC)) {
//                            when record already exist.
//                            Log.e(TAG, jsonObject.get("message").getAsString());
                            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().deleteFromId(table.getId());
                        } else {
                            sendForErrorLog(table, LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            if (callBackFirstSync != null) { // for first time call from sync
                                callBackFirstSync.getCallBackOfComplete(0, "Error Occur");
                                callBackFirstSync = null;
                            }
                            isSync = false;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        sendForErrorLog(table, e.getMessage());
                        isSync = false;
                        if (callBackFirstSync != null) { // for first time call from sync
                            callBackFirstSync.getCallBackOfComplete(0, "Error Occur");
                            callBackFirstSync = null;
                        }


                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public synchronized void equipmentReport(final Offlinetable table) {
        ReportRequest request = new Gson().fromJson(table.getParams(), ReportRequest.class);
        String mimeType = "";
        MultipartBody.Part custbody = null;
        MultipartBody.Part auditbbody = null;
        if (request.getFileCustomerSign() != null) {
            mimeType = URLConnection.guessContentTypeFromName(request.getFileCustomerSign().getName());

            RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), request.getFileCustomerSign());
            // MultipartBody.Part is used to send also the actual file name
            custbody = MultipartBody.Part.createFormData("custSign", request.getFileCustomerSign().getName(), requestFile);
        }

        if (request.getFileAuditorSign() != null) {
            mimeType = URLConnection.guessContentTypeFromName(request.getFileAuditorSign().getName());

            RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), request.getFileAuditorSign());
            // MultipartBody.Part is used to send also the actual file name
            auditbbody = MultipartBody.Part.createFormData("audSign", request.getFileAuditorSign().getName(), requestFile);
        }

        RequestBody description = RequestBody.create(MultipartBody.FORM, request.getDes());
        RequestBody audId = RequestBody.create(MultipartBody.FORM, String.valueOf(request.getAudId()));
        RequestBody userId = RequestBody.create(MultipartBody.FORM, App_preference.getSharedprefInstance().getLoginRes().getUsrId());


        ApiClient.getservices().addAuditFeedback(AppUtility.getApiHeaders(),
                userId, audId, description, custbody, auditbbody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {

                        if (jsonObject.get("success").getAsBoolean()) {
                            callBack.getResponse(table, jsonObject);
                        } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
//                           onSessionExpire(jsonObject.get("message").getAsString());
//                            when session expires
                            EotApp.getAppinstance().sessionExpired();
                            isSync = false;
                        } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.ALREADY_SYNC)) {
//                            when record already exist.
//                            Log.e(TAG, jsonObject.get("message").getAsString());
                            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().deleteFromId(table.getId());
                        } else {
                            sendForErrorLog(table, LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            if (callBackFirstSync != null) { // for first time call from sync
                                callBackFirstSync.getCallBackOfComplete(0, "Error Occur");
                                callBackFirstSync = null;
                            }
                            isSync = false;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        sendForErrorLog(table, e.getMessage());
                        isSync = false;
                        if (callBackFirstSync != null) { // for first time call from sync
                            callBackFirstSync.getCallBackOfComplete(0, "Error Occur");
                            callBackFirstSync = null;
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private synchronized void callPendingUpdateAppointment(final Offlinetable table) {
        final AppointmentUpdateReq liveAppointmentModel = new Gson().fromJson(table.getParams(), AppointmentUpdateReq.class);
        RequestBody conId = null;
        RequestBody siteId = null;
        RequestBody nm = null;
        RequestBody des = null;
        RequestBody email = null;
        RequestBody mob1 = null;
        RequestBody adr = null;
        RequestBody city = null;
        RequestBody zip = null;
        RequestBody status = null;

        if (TextUtils.isEmpty(liveAppointmentModel.getConId()))
            conId = RequestBody.create(MultipartBody.FORM, "");
        else
            conId = RequestBody.create(MultipartBody.FORM, liveAppointmentModel.getConId());

        if (TextUtils.isEmpty(liveAppointmentModel.getSiteId()))
            siteId = RequestBody.create(MultipartBody.FORM, "");
        else
            siteId = RequestBody.create(MultipartBody.FORM, liveAppointmentModel.getSiteId());

        RequestBody cnm = RequestBody.create(MultipartBody.FORM, "");
        RequestBody cltId = RequestBody.create(MultipartBody.FORM, liveAppointmentModel.getCltId());


        if (TextUtils.isEmpty(liveAppointmentModel.getStatus()))
            status = RequestBody.create(MultipartBody.FORM, "");
        else status = RequestBody.create(MultipartBody.FORM, liveAppointmentModel.getStatus());

        if (TextUtils.isEmpty(liveAppointmentModel.getDes()))
            des = RequestBody.create(MultipartBody.FORM, "");
        else des = RequestBody.create(MultipartBody.FORM, liveAppointmentModel.getDes());

        if (TextUtils.isEmpty(liveAppointmentModel.getNm()))
            nm = RequestBody.create(MultipartBody.FORM, "");
        else
            nm = RequestBody.create(MultipartBody.FORM, liveAppointmentModel.getNm());

        if (TextUtils.isEmpty(liveAppointmentModel.getEmail()))
            email = RequestBody.create(MultipartBody.FORM, "");
        else
            email = RequestBody.create(MultipartBody.FORM, liveAppointmentModel.getEmail());

        if (TextUtils.isEmpty(liveAppointmentModel.getMob1()))
            mob1 = RequestBody.create(MultipartBody.FORM, "");
        else
            mob1 = RequestBody.create(MultipartBody.FORM, liveAppointmentModel.getMob1());

        if (TextUtils.isEmpty(liveAppointmentModel.getAdr()))
            adr = RequestBody.create(MultipartBody.FORM, "");
        else
            adr = RequestBody.create(MultipartBody.FORM, liveAppointmentModel.getAdr());
        if (TextUtils.isEmpty(liveAppointmentModel.getCity()))
            city = RequestBody.create(MultipartBody.FORM, "");
        else
            city = RequestBody.create(MultipartBody.FORM, liveAppointmentModel.getCity());

        RequestBody state = RequestBody.create(MultipartBody.FORM, liveAppointmentModel.getState());
        RequestBody ctry = RequestBody.create(MultipartBody.FORM, liveAppointmentModel.getCtry());
        if (TextUtils.isEmpty(liveAppointmentModel.getZip()))
            zip = RequestBody.create(MultipartBody.FORM, "");
        else
            zip = RequestBody.create(MultipartBody.FORM, liveAppointmentModel.getZip());

        //converting datetime format
        String startTime = "";
        String endTime = "";
        if (!TextUtils.isEmpty(liveAppointmentModel.getSchdlStart()))
            startTime = liveAppointmentModel.getSchdlStart();
        if (!TextUtils.isEmpty(liveAppointmentModel.getSchdlFinish()))
            endTime = liveAppointmentModel.getSchdlFinish();


        RequestBody schdlStart = RequestBody.create(MultipartBody.FORM, startTime);
        RequestBody schdlFinish = RequestBody.create(MultipartBody.FORM, endTime);


        String meidString = new Gson().toJson(liveAppointmentModel.getMemIds());

        RequestBody rmids = RequestBody.create(MultipartBody.FORM, meidString);
        RequestBody rbAppId = RequestBody.create(MultipartBody.FORM, liveAppointmentModel.getAppId());


        String mimeType = "";
        MultipartBody.Part body = null;
        List<MultipartBody.Part> files = new ArrayList<>();
        for (int i = 0; i < liveAppointmentModel.getAppDoc().size(); i++) {
            File file1 = new File(liveAppointmentModel.getAppDoc().get(i));
            String s = liveAppointmentModel.getFileNames().get(i);
            if (file1 != null) {
                mimeType = URLConnection.guessContentTypeFromName(file1.getName());
                if (mimeType == null) {
                    mimeType = s;
                }
                RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file1);
                // MultipartBody.Part is used to send also the actual file name
                body = MultipartBody.Part.createFormData("appDoc[]", s, requestFile);
                files.add(body);
            }
        }


        ApiClient.getservices().updateAppointment(AppUtility.getApiHeaders(),
                rbAppId,
                cltId,
                siteId,
                conId,
                status,
                des,
                schdlStart,
                schdlFinish,
                nm,
                cnm,
                email,
                mob1,
                adr,
                city,
                state,
                ctry,
                zip,
                rmids,
                files)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        if (jsonObject.get("success").getAsBoolean()) {
                            callBack.getResponse(table, jsonObject);
                        } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
//                           onSessionExpire(jsonObject.get("message").getAsString());
//                            when session expires
                            EotApp.getAppinstance().sessionExpired();
                            isSync = false;
                        } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.ALREADY_SYNC)) {
//                            when record already exist.
//                            Log.e(TAG, jsonObject.get("message").getAsString());
                            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().deleteFromId(table.getId());
                        } else {
                            sendForErrorLog(table, LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            if (callBackFirstSync != null) { // for first time call from sync
                                callBackFirstSync.getCallBackOfComplete(0, "Error Occur");
                                callBackFirstSync = null;
                            }
                            isSync = false;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        sendForErrorLog(table, e.getMessage());
                        isSync = false;
                        if (callBackFirstSync != null) { // for first time call from sync
                            callBackFirstSync.getCallBackOfComplete(0, "Error Occur");
                            callBackFirstSync = null;
                        }


                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    public MultipartBody.Part prepareFilePart(String partName, File file) {
        // create RequestBody instance from file
        if (file == null) return null;
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        file
                );
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private void sendForErrorLog(Offlinetable table, String message) {
        if (table.getCount() > 2) {
            ErrorLog errorLog = new ErrorLog();
            errorLog.setApiUrl(App_preference.getSharedprefInstance().getBaseURL() + table.getService_name());
            errorLog.setRequestParam(table.getParams());
            errorLog.setResponse(message);
            errorLog.setVersion(Build.MODEL + ", " + Build.VERSION.RELEASE
                    + " " + Build.VERSION_CODES.class.getFields()[Build.VERSION.SDK_INT].getName() + ", " + BuildConfig.VERSION_NAME);
            errorLog.setTime(table.getTimestamp());
            int check = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().deleteFromId(table.getId());
            if (check == 1) {
                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).errorLogmodel().insertError(errorLog);
            }
        } else {
            int update_count = table.getCount() + 1;
            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).offlinemodel().updateCountById(table.getId(), update_count);
        }

    }

    private JsonObject getJsonObject(String params) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(params).getAsJsonObject();
        return obj;
    }

    public void fromOutSideCall() {
        if (!isSync) {
            getTotalRequest();
        }
    }

    /**
     * call from First sync Controller
     **/
    public void fromFirstTimeSyncCall(FirstSyncPC.CallBackFirstSync callBackFirstSync) {
        this.callBackFirstSync = callBackFirstSync;
        if (!isSync) {
            getTotalRequest();
        }
    }

    public interface OfflineSericeCallBack {
        void getResponse(Offlinetable data, JsonObject obj);
    }
}
