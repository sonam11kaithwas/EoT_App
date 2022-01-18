package com.eot_app.nav_menu.appointment.list;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.eot_app.nav_menu.appointment.Requestor;
import com.eot_app.nav_menu.appointment.ServerResponse;
import com.eot_app.nav_menu.appointment.calendar.data.Event;
import com.eot_app.nav_menu.appointment.dbappointment.Appointment;
import com.eot_app.nav_menu.appointment.list.common.CommonAppointmentModel;
import com.eot_app.nav_menu.appointment.list.model.AppointmentListReq;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditListRequestModel;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_db.JobListRequestModel;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppointmentListViewModel extends AndroidViewModel implements ServerResponse, SearchEventsAsync.OnEventSearchCompletion {
    private final int updatelimit;
    private final SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
    private final List<Appointment> tempList = new ArrayList<>();
    MutableLiveData<Boolean> isRefresh = new MutableLiveData<>();
    MutableLiveData<List<CommonAppointmentModel>> liveTodayAppointments = new MutableLiveData<>();
    MutableLiveData<List<Event>> liveEvents = new MutableLiveData<>();
    MutableLiveData<Boolean> liveNotificationRedirect = new MutableLiveData<>();
    private int count;
    private int updateindex;
    private String search;
    private String selectedDate;

    public AppointmentListViewModel(@NonNull Application application) {
        super(application);
        //    this.context = getApplication().getApplicationContext();
        this.updatelimit = AppConstant.LIMIT_HIGH;
        this.updateindex = 0;
        this.search = "";
    }

    public MutableLiveData<Boolean> getLiveNotificationRedirect() {
        return liveNotificationRedirect;
    }

    public MutableLiveData<Boolean> getIsRefresh() {
        return isRefresh;
    }


    public MutableLiveData<List<CommonAppointmentModel>> getLiveTodayAppointments() {
        return liveTodayAppointments;
    }

    public void refreshList() {
        if (AppUtility.isInternetConnected()) {
            isRefresh.setValue(true);
            loadJob();
        }
    }

    public void localRefresh() {
        searchRecordOnGivenDates(selectedDate);
    }

    public void searchRecordOnGivenDates(String date) {
        this.selectedDate = date;
        liveTodayAppointments.setValue(null);
        SearchRecordInBackground(date);
        //   new SearchRecordInBackground().execute(date);

    }

    public void refreshAppoiListFromServer() {
        loadList();
    }

    public void refreshListFromServer() {
        loadJob();
        //loadAdit();
    }

    @Override
    public void onSuccess(Object successObject, int requestCode) {
        JsonObject jsonObject = (JsonObject) successObject;

        switch (requestCode) {
            case reqCode:
                if (jsonObject.get("success").getAsBoolean()) {
                    count = jsonObject.get("count").getAsInt();
                    String jsonString = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                    Type typeList = new TypeToken<List<Appointment>>() {
                    }.getType();
                    List<Appointment> appointmentModelList = new Gson().fromJson(jsonString, typeList);
                    if (appointmentModelList != null && appointmentModelList.size() > 0) {
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel().insertAppointments(appointmentModelList);
                        tempList.addAll(appointmentModelList);
                    }
                    boolean b = checkForMoreItem();
                    if (!b) {
                        tempList.clear();
                        updateindex = 0;
                        search = "";
                        isRefresh.setValue(false);
                        searchRecordOnGivenDates(selectedDate);

                        liveNotificationRedirect.setValue(true);
                        liveNotificationRedirect.setValue(false);


                        //refresh recent job on appointment details and show the label of recent job with code
                        LocalBroadcastManager.getInstance(EotApp.getAppinstance()).sendBroadcast(new Intent("appointment_details_refresh"));

                    }

                } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                    //   expenseListView.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                }
                break;

            case reqCode2:
                try {
                    if (jsonObject.get("success").getAsBoolean()) {
                        count = jsonObject.get("count").getAsInt();
                        String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                        Type listType = new com.google.common.reflect.TypeToken<List<AuditList_Res>>() {
                        }.getType();
                        List<AuditList_Res> data = new Gson().fromJson(convert, listType);
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().inserAuditList(data);
                        App_preference.getSharedprefInstance().setAuditSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                        if ((updateindex + updatelimit) <= count) {
                            updateindex += updatelimit;
                            loadAudit();
                        } else {
                            updateindex = 0;
                            count = 0;
                            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().deleteJobByIsDelete();
                            loadList();
                        }


                    } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                        //  auditListView.onSessionExpired(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case reqCode3:
                if (jsonObject.get("success").getAsBoolean()) {
                    count = jsonObject.get("count").getAsInt();
                    String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                    Type listType = new TypeToken<List<Job>>() {
                    }.getType();
                    List<Job> data = new Gson().fromJson(convert, listType);
                    AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().inserJob(data);

                    if ((updateindex + updatelimit) <= count) {
                        updateindex += updatelimit;
                        loadJob();
                    } else {
                        if (count != 0) {
                            App_preference.getSharedprefInstance().setJobSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                        }
                        updateindex = 0;
                        count = 0;
                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().deleteJobByIsDelete();
                        loadAudit();
                    }
                }
                break;
        }

    }


    @Override
    public void onError(Object errorObject, int requestCode) {
        isRefresh.setValue(false);
        searchRecordOnGivenDates(selectedDate);

    }

    /*load update jobs from server*/
    private void loadJob() {
        JobListRequestModel jobListRequestModel = new JobListRequestModel(Integer.parseInt(App_preference.getSharedprefInstance().getLoginRes().getUsrId()),
                updatelimit, updateindex, App_preference.getSharedprefInstance().getJobSyncTime());
        Requestor requestor = new Requestor(this, reqCode3);
        requestor.sendRequestToServer(Service_apis.getUserJobList, jobListRequestModel);
    }

    /*load updated audit from server*/
    private void loadAudit() {
        int userId = Integer.parseInt(App_preference.getSharedprefInstance().getLoginRes().getUsrId());
        AuditListRequestModel auditListRequestModel = new AuditListRequestModel(userId,
                updatelimit, updateindex, App_preference.getSharedprefInstance().getAuditSyncTime());
        Requestor requestor = new Requestor(this, reqCode2);
        requestor.sendRequestToServer(Service_apis.getAuditList, auditListRequestModel);

    }

    //load more appointment list with index
    private void loadList() {
        isRefresh.setValue(true);
        int userId = Integer.parseInt(App_preference.getSharedprefInstance().getLoginRes().getUsrId());

        AppointmentListReq model = new AppointmentListReq(
                userId,
                updatelimit, updateindex
                , App_preference.getSharedprefInstance().getAppointmentSyncTime());
        model.setSearch(search);
        Requestor requestor = new Requestor(this, reqCode);
        requestor.sendRequestToServer(Service_apis.getAppointmentUserList, model);
    }

    private boolean checkForMoreItem() {
        boolean isMore;
        if ((updateindex + updatelimit) <= count) {
            updateindex += updatelimit;
            loadList();
            isMore = true;
        } else {
            if (count != 0) {
                App_preference.getSharedprefInstance().setAppointmentSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
            }
            updateindex = 0;
            count = 0;
            isMore = false;
            isRefresh.setValue(false);
            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel().deleteAppointmentByIsDelete();

        }

        return isMore;
    }

    @Override
    public void onEventFounds(List<Event> eventList) {
        liveEvents.setValue(eventList);
    }

    /*search events*/
    public void searchEvents(int currentMonth, int currentYear) {
        currentMonth++;
        String startDate = "01-" + currentMonth + "-" + currentYear + " 00:00:00";

        SearchEventsAsync searchEventsAsync = new SearchEventsAsync(startDate);
        searchEventsAsync.setOnEventSearchCompletion(this);
        searchEventsAsync.addItemDotsForAllInBackGround();
        // searchEventsAsync.execute();

    }


    private void SearchRecordInBackground(String date) {
        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            final List<CommonAppointmentModel> commonList = new ArrayList<>();

            executorService.execute(new Runnable() {
                @Override
                public void run() {

                    String startDate = selectedDate + " 00:00:00";
                    String endDate = selectedDate + " 23:59:59";


                    try {
                        Date parseStart = f.parse(startDate);
                        long startTime = parseStart.getTime();

                        Date parseEnd = f.parse(endDate);
                        long endTime = parseEnd.getTime();

                        /**
                         * Appointment search on local db
                         * */
                        List<Appointment> appointmentList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                                .appointmentModel().getAppointmentByDate(startTime, endTime);

                        if (appointmentList != null)
                            for (Appointment appointment : appointmentList) {
                                CommonAppointmentModel cm = new CommonAppointmentModel();
                                cm.setStartDateTime(appointment.getSchdlStart());
                                cm.setType(0);
                                cm.setId(appointment.getAppId());
                                cm.setTempId(appointment.getTempId());
                                cm.setStatus(appointment.getStatus());

                                if (!TextUtils.isEmpty(appointment.getConId())) {
                                    if (!TextUtils.isEmpty(appointment.getLabel()))
                                        cm.setTitle(appointment.getLabel() + " " + appointment.getConId());
                                    else cm.setTitle(appointment.getConId());
                                    cm.setNm(appointment.getConId());
                                } else {
                                    cm.setTitle(appointment.getLabel() + " " + appointment.getNm());
                                    cm.setNm(appointment.getNm());
                                }


                                cm.setDes(appointment.getAdr());

                                if (appointment.getAttachCount() != null && Integer.parseInt(appointment.getAttachCount()) > 0) {
                                    cm.setAttchmentCount(Integer.parseInt(appointment.getAttachCount()));
                                } else cm.setAttchmentCount(0);


                                commonList.add(cm);

                            }

                        /**
                         * Job search on local db
                         * */
                        List<Job> jobList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                                .appointmentModel().getJobsByDate(startTime, endTime);
                        if (jobList != null)
                            for (Job job : jobList) {
                                try {
                                    if (job != null && job.getStatus() != null && !AppUtility.getJobStatusList().contains(job.getStatus())
                                            && job.getJobId() != null) {
                                        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().deleteJobById(job.getJobId());
                                    } else {
                                        CommonAppointmentModel cm = new CommonAppointmentModel();
                                        cm.setStartDateTime(job.getSchdlStart());
                                        cm.setType(1);
                                        cm.setId(job.getJobId());
                                        cm.setTempId(job.getTempId());

                                        if (!TextUtils.isEmpty(job.getLabel()) && !TextUtils.isEmpty(job.getNm())) {
                                            cm.setTitle(job.getLabel() + " " + job.getNm());
                                            cm.setNm(job.getNm());
                                        } else {
                                            if (!TextUtils.isEmpty(job.getNm())) {
                                                cm.setTitle(job.getNm());
                                                cm.setNm(job.getNm());
                                            } else if (!TextUtils.isEmpty(job.getCltId())) {
                                                int parseInt = Integer.parseInt(job.getCltId());
                                                Client clientFromId = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientModel().getClientFromId(parseInt);
                                                if (clientFromId != null) {
                                                    cm.setNm(clientFromId.getNm());
                                                    if (!TextUtils.isEmpty(job.getLabel()))
                                                        cm.setTitle(job.getLabel() + " " + clientFromId.getNm());
                                                    else
                                                        cm.setTitle(clientFromId.getNm());
                                                }
                                            }
                                        }
                                        cm.setDes(job.getAdr());

                                        /*****Job Item Count set For Flag***/
                                        if (job.getItemData() != null && job.getItemData().size() > 0) {
                                            cm.setJobItemCount(job.getItemData().size());
                                        } else cm.setJobItemCount(0);

                                        if (job.getEquArray() != null && job.getEquArray().size() > 0) {
                                            cm.setEquipmentCount(job.getEquArray().size());
                                        } else cm.setEquipmentCount(0);


                                        if (job.getAttachCount() != null && Integer.parseInt(job.getAttachCount()) > 0) {
                                            cm.setAttchmentCount(Integer.parseInt(job.getAttachCount()));
                                        } else cm.setAttchmentCount(0);

                                        commonList.add(cm);

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                        /**
                         * Audit search on local Db
                         * */

                        /**
                         * check permission is allowed th audit or not
                         * */
                        if (App_preference.getSharedprefInstance().getLoginRes().getRights().get(0).getIsAuditVisible() == 0) {
                            List<AuditList_Res> auditList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance())
                                    .auditDao().getAuditListByDate(startTime, endTime);
                            if (auditList != null)
                                for (AuditList_Res audit : auditList) {
                                    try {
                                        if (audit != null && audit.getStatus() != null && !AppUtility.auditStatusList().contains(audit.getStatus())
                                                && audit.getAudId() != null) {
                                            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().deletAuditById(audit.getAudId());
                                        } else {
                                            CommonAppointmentModel cm = new CommonAppointmentModel();
                                            cm.setStartDateTime(audit.getSchdlStart());
                                            cm.setType(2);
                                            cm.setId(audit.getAudId());
                                            cm.setTempId(audit.getTempId());
                                            cm.setDes(audit.getAdr());

                                            if (!TextUtils.isEmpty(audit.getLabel()) && !TextUtils.isEmpty(audit.getNm())) {
                                                cm.setTitle(audit.getLabel() + " " + audit.getNm());
                                                cm.setNm(audit.getNm());
                                            } else if (!TextUtils.isEmpty(audit.getLabel()) && TextUtils.isEmpty(audit.getNm())) {
                                                cm.setTitle(audit.getLabel());
                                            } else {
                                                if (!TextUtils.isEmpty(audit.getNm())) {
                                                    cm.setTitle(audit.getNm());
                                                    cm.setNm(audit.getNm());
                                                } else if (!TextUtils.isEmpty(audit.getCltId())) {
                                                    int parseInt = Integer.parseInt(audit.getCltId());
                                                    Client clientFromId = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientModel().getClientFromId(parseInt);
                                                    if (clientFromId != null) {
                                                        cm.setNm(clientFromId.getNm());
                                                        if (!TextUtils.isEmpty(audit.getLabel()) && !TextUtils.isEmpty(audit.getNm())) {
                                                            cm.setTitle(audit.getLabel() + " " + clientFromId.getNm());
                                                        } else if (!TextUtils.isEmpty(audit.getLabel()) && TextUtils.isEmpty(audit.getNm())) {
                                                            cm.setTitle(audit.getLabel());
                                                        } else
                                                            cm.setTitle(clientFromId.getNm());
                                                    }
                                                }
                                            }


                                            if (audit.getEquArray() != null && audit.getEquArray().size() > 0) {
                                                cm.setEquipmentCount(audit.getEquArray().size());
                                            } else cm.setEquipmentCount(0);


                                            if (audit.getAttachCount() != null && Integer.parseInt(audit.getAttachCount()) > 0) {
                                                cm.setAttchmentCount(Integer.parseInt(audit.getAttachCount()));
                                            } else cm.setAttchmentCount(0);

                                            commonList.add(cm);

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                        }


                        Collections.sort(commonList);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            liveTodayAppointments.setValue(commonList);

                        }
                    });

                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }


}
