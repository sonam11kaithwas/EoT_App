package com.eot_app.nav_menu.audit.addAudit.add_audit_mvp;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.activitylog.LogModel;
import com.eot_app.nav_menu.audit.addAudit.add_aduit_model_pkg.AddAudit_Req;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.SpinnerCountrySite;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.Country;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.Eot_Validation;
import com.eot_app.utility.States;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.db.OfflineDataController;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.contractdb.ContractRes;
import com.eot_app.utility.settings.setting_db.FieldWorker;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddAudit_pc implements AddAduit_pi {
    private final Add_AduitView add_aduitView;
    private List<States> statesList;
    private List<Country> countryList;
    private String date_str, time_str;


    public AddAudit_pc(Add_AduitView add_aduitView) {
        this.add_aduitView = add_aduitView;

    }

    @Override
    public ContactData getDefaultContactDataForClient(String cltId) {
        return AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().getDefaultConFromCltId(cltId);
    }

    @Override
    public Site_model getDefaultSiteDataForClient(String cltId) {
        return AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getDefaultFromCltId(cltId);
    }

    @Override
    public void addJobWithImageDescription(AddAudit_Req addJob_req, ArrayList<String> links, List<String> fileNames) {
        if (AppUtility.isInternetConnected()) {

            RequestBody type = RequestBody.create(MultipartBody.FORM, String.valueOf(addJob_req.getType()));
            RequestBody cltId = RequestBody.create(MultipartBody.FORM, addJob_req.getCltId());//parent id
            RequestBody nm = RequestBody.create(MultipartBody.FORM, addJob_req.getNm());//contrId
            RequestBody sideId = RequestBody.create(MultipartBody.FORM, addJob_req.getSiteId());
            RequestBody conId = RequestBody.create(MultipartBody.FORM, addJob_req.getConId() + "");//conId
            RequestBody contrId = RequestBody.create(MultipartBody.FORM, addJob_req.getContrId());
            RequestBody parentId = RequestBody.create(MultipartBody.FORM, addJob_req.getParentId());
            RequestBody jobDes = RequestBody.create(MultipartBody.FORM, addJob_req.getDes());
            RequestBody status = RequestBody.create(MultipartBody.FORM, String.valueOf(addJob_req.getStatus()));
            RequestBody athr = RequestBody.create(MultipartBody.FORM, addJob_req.getAthr());
            RequestBody kprrb;
            if (addJob_req.getKpr() != null)
                kprrb = RequestBody.create(MultipartBody.FORM, addJob_req.getKpr());
            else kprrb = RequestBody.create(MultipartBody.FORM, "");
            RequestBody schdlStartrb = RequestBody.create(MultipartBody.FORM, addJob_req.getSchdlStart());
            RequestBody schdlFinishrb = RequestBody.create(MultipartBody.FORM, addJob_req.getSchdlFinish());
            RequestBody jobInstruction = RequestBody.create(MultipartBody.FORM, addJob_req.getInst());
            RequestBody new_con_nmrb = RequestBody.create(MultipartBody.FORM, addJob_req.getCnm() + "");//new_con_nm
            RequestBody newsiterb = RequestBody.create(MultipartBody.FORM, addJob_req.getSnm());
            RequestBody emailrb = RequestBody.create(MultipartBody.FORM, addJob_req.getEmail());
            RequestBody mobilerb = RequestBody.create(MultipartBody.FORM, addJob_req.getMob1());
            RequestBody atmobrb = RequestBody.create(MultipartBody.FORM, addJob_req.getMob2());
            RequestBody adrrb = RequestBody.create(MultipartBody.FORM, addJob_req.getAdr());
            RequestBody citrb = RequestBody.create(MultipartBody.FORM, addJob_req.getCity());
            RequestBody staterb = RequestBody.create(MultipartBody.FORM, addJob_req.getState());//state_id
            RequestBody ctryrb = RequestBody.create(MultipartBody.FORM, addJob_req.getCtry());//ctry_id
            RequestBody postcoderb = RequestBody.create(MultipartBody.FORM, addJob_req.getZip());

            List<MultipartBody.Part> listworkrb = new ArrayList<>();
            if (addJob_req.getMemIds() != null)
                for (String s : addJob_req.getMemIds())
                    listworkrb.add(MultipartBody.Part.createFormData("memIds[]", s));

            RequestBody tagDatRb = null;
            if (addJob_req.getTagData() != null) {
                Gson gson = new Gson();
                String s = gson.toJson(addJob_req.getTagData());
                tagDatRb = RequestBody.create(MultipartBody.FORM, s);
            }
            RequestBody dateTimerb = RequestBody.create(MultipartBody.FORM, AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
            RequestBody latrb = RequestBody.create(MultipartBody.FORM, addJob_req.getLat());
            RequestBody lngrb = RequestBody.create(MultipartBody.FORM, addJob_req.getLng());
            RequestBody landmarkrb = RequestBody.create(MultipartBody.FORM, addJob_req.getLandmark());
            RequestBody auditType = RequestBody.create(MultipartBody.FORM, addJob_req.getAuditType());
            RequestBody clientForFuturerb = RequestBody.create(MultipartBody.FORM, addJob_req.getClientForFuture() + "");
            RequestBody siteforfuturerb = RequestBody.create(MultipartBody.FORM, addJob_req.getSiteForFuture() + "");

            RequestBody answerRequestBody = null;
            if (addJob_req.getAnswerArray() != null) {
                Gson gson = new Gson();
                String answerJson = gson.toJson(addJob_req.getAnswerArray());
                answerRequestBody = RequestBody.create(MultipartBody.FORM, answerJson);
            }
            RequestBody contactforfuturerb = RequestBody.create(MultipartBody.FORM, addJob_req.getContactForFuture() + "");
            RequestBody tempId = RequestBody.create(MultipartBody.FORM, addJob_req.getTempId());

            // RequestBody appIdrb = RequestBody.create(MultipartBody.FORM, addJob_req.getAppId());

            String mimeType = "";
            MultipartBody.Part body = null;
            List<MultipartBody.Part> fileList = new ArrayList<>();
            for (int i = 0; i < links.size(); i++) {
                File file1 = new File(links.get(i));
                String s = fileNames.get(i);
                if (file1 != null) {
                    mimeType = URLConnection.guessContentTypeFromName(file1.getName());
                    if (mimeType == null) {
                        mimeType = s;
                    }
                    RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file1);
                    // MultipartBody.Part is used to send also the actual file name
                    body = MultipartBody.Part.createFormData("ja[]", s, requestFile);
                    fileList.add(body);
                }
            }

//            List<MultipartBody.Part> jatIds = new ArrayList<>();
//            if (jatIds != null)
//                for (String s : addJob_req.getJtId())
//                    jatIds.add(MultipartBody.Part.createFormData("jtId[]", s));

            AppUtility.progressBarShow((Activity) add_aduitView);
            ApiClient.getservices().addAuditWithDocuments(AppUtility.getApiHeaders(),
                    type,
                    cltId,
                    nm,
                    sideId,
                    conId,
                    contrId,
                    parentId,
                    jobDes,
                    status,
                    athr,
                    kprrb,
                    schdlStartrb,
                    schdlFinishrb,
                    jobInstruction,
                    new_con_nmrb,
                    newsiterb,
                    emailrb,
                    mobilerb,
                    atmobrb,
                    adrrb,
                    citrb,
                    staterb,
                    ctryrb,
                    postcoderb,
                    listworkrb,
                    tagDatRb,
                    dateTimerb,
                    latrb,
                    lngrb,
                    landmarkrb,
                    auditType,
                    clientForFuturerb,
                    siteforfuturerb,
                    answerRequestBody,
                    contactforfuturerb,
                    //   tempId,
                    fileList
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            AppUtility.progressBarDissMiss();
                            if (jsonObject.get("success").getAsBoolean()) {
                                EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").toString()));
                                add_aduitView.finishActivity();
                                //refresh recent job on appointment details and show the label of recent job with code
                                EotApp.getAppinstance().notifyApiObserver(Service_apis.addAudit);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                EotApp.getAppinstance().sessionExpired();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            AppUtility.progressBarDissMiss();
                            add_aduitView.finishActivity();
                            EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.something_wrong));
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                        }
                    });
        }
    }

    @Override
    public boolean RequiredFields(String cltId, boolean contactSelf, boolean siteSelf, String conNm, String siteNm,
                                  String adr, Set<String> auditorsId, String countryId, String stateId, String mob, String alterNateMob, String email) {
        if (auditorsId.isEmpty()) {
            add_aduitView.set_auditor_Error(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_auditorsname));
            return false;
        } else if (cltId.equals("")) {
            add_aduitView.clientNameError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_client_name));
            return false;
        } else if (!mob.isEmpty() && !mob.equalsIgnoreCase(App_preference.getSharedprefInstance().getLoginRes().getCtryCode()) &&
                mob.length() < AppConstant.MOBILE_LIMIT) {
            add_aduitView.setMobError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_mob_lent));
            return false;
        } else if (!alterNateMob.isEmpty() && !mob.equalsIgnoreCase(App_preference.getSharedprefInstance().getLoginRes().getCtryCode()) && alterNateMob.length() < AppConstant.MOBILE_LIMIT) {
            add_aduitView.setMobError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_alter_mob_lent));
            return false;
        } else if (!email.isEmpty() && (!Eot_Validation.email_checker(email).equals(""))) {
            add_aduitView.setEmailError(Eot_Validation.email_checker(email));
            return false;
        } else if (adr.equals("")) {
            add_aduitView.setAddr_Error(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_addr));
            return false;
        } else if (!isValidCountry(countryId)) {
            add_aduitView.setCountryError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.please_select_country_first));
            return false;
        } else if (!isValidState(stateId)) {
            add_aduitView.setStateError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.state_error));
            return false;
        }
        return true;
    }

    @Override
    public void getCurrentdateTime() {
        String dateTime = AppUtility.getDateByFormat(AppUtility.dateTimeByAmPmFormate("dd-MM-yyyy hh:mm:ss a"
                , "dd-MM-yyyy kk:mm:ss"));
        String[] date_Time = dateTime.split(" ");
        String datestr = date_Time[0];

        String time1 = App_preference.getSharedprefInstance().getLoginRes().getJobSchedule();
        if (!TextUtils.isEmpty(time1)) {
            schdul_Start_Date_Time(AppUtility.getFormatedTime(time1), datestr);
        }

        String sch_tm_dt = App_preference.getSharedprefInstance().getLoginRes().getJobCurrentTime();
        if (!TextUtils.isEmpty(sch_tm_dt)) {

            if (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable() != null &&
                    App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("0")) {
                sch_time_cur(datestr, date_Time[1] + " " + date_Time[2], sch_tm_dt);
            } else {
                sch_time_cur(datestr, date_Time[1], sch_tm_dt);
            }
//            sch_time_cur(datestr, date_Time[1] + " " + date_Time[2], sch_tm_dt);
        }
    }


    private void sch_time_cur(String datestr, String date_Time, String sch_tm_dt) {
        try {
            String[] remv_sec = date_Time.split(":");
            String an_pm = "";

            try {
                if (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable() != null &&
                        App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("0")) {
                    String[] am_pm = date_Time.split(" ");
                    an_pm = " " + am_pm[1];
                }

            } catch (Exception e) {
                e.getMessage();
            }
            //String[] am_pm = date_Time.split(" ");
            String cur_start = remv_sec[0] + ":" + remv_sec[1] + an_pm;
            String date_time = datestr + " " + cur_start;

            String[] time_dur = sch_tm_dt.split(":");
            long dur_milliseconds = TimeUnit.SECONDS.toMillis(TimeUnit.HOURS.toSeconds(Integer.parseInt(time_dur[0])) +
                    TimeUnit.MINUTES.toSeconds(Integer.parseInt(time_dur[1])));

            SimpleDateFormat simpleDate = new SimpleDateFormat(AppUtility.dateTimeByAmPmFormate(
                    "dd-MM-yyyy hh:mm a", "dd-MM-yyyy kk:mm"), Locale.US);
            Date past = null;
            long milisce = 0;
            try {
                past = simpleDate.parse(date_time);
                milisce = past.getTime() + dur_milliseconds;
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milisce);
            String std = simpleDate.format(calendar.getTime());
            add_aduitView.set_str_DT_after_cur(std);
            String[] time_duration = std.split(" ");
            date_str = time_duration[0];
            try {
                if (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable() != null &&
                        App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("0"))
                    time_str = time_duration[1] + " " + time_duration[2];
                else time_str = time_duration[1] + "";

            } catch (Exception e) {
                e.getMessage();
            }
            end_Date_Time();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }


    private void schdul_Start_Date_Time(String[] sch_time, String datestr) {
        date_str = datestr;

        try {
            if (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable() != null &&
                    App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("0"))
                time_str = sch_time[1] + " " + sch_time[2];
            else time_str = sch_time[1] + " ";
        } catch (Exception e) {

        }
        add_aduitView.set_Str_DTime(date_str, time_str);
        end_Date_Time();
    }

    private void end_Date_Time() {
        String date_time = date_str + " " + time_str;
        SimpleDateFormat simpleDate = new SimpleDateFormat(AppUtility.dateTimeByAmPmFormate(
                "dd-MM-yyyy hh:mm a", "dd-MM-yyyy kk:mm"), Locale.US);
        Date past = null;
        long milisce = 0;
        try {
            past = simpleDate.parse(date_time);
            milisce = past.getTime() + duration_Time();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milisce);
        String std = simpleDate.format(calendar.getTime());
        add_aduitView.set_End_Date_Time(std);
    }

    //set schedula date time accoroding to duration
    private long duration_Time() {
        String duration = App_preference.getSharedprefInstance().getLoginRes().getDuration();
        String[] time_dur = duration.split(":");
        long dur_milliseconds = TimeUnit.SECONDS.toMillis(TimeUnit.HOURS.toSeconds(Integer.parseInt(time_dur[0])) +
                TimeUnit.MINUTES.toSeconds(Integer.parseInt(time_dur[1])));
        return dur_milliseconds;
    }

    @Override
    public void getContractList(String cltId) {
        List<ContractRes> contractResList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contractDao().getContractListById(cltId, (System.currentTimeMillis() / 1000) + "");
        Log.e("", "");
        add_aduitView.setContractlist(contractResList);
    }


    @Override
    public void getClientList() {
        List<Client> data = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientModel().getActiveClientList();
        add_aduitView.setClientlist(data);
    }

    @Override
    public void getSiteList(String cltId) {
        if (Integer.parseInt(cltId) > 0) {
            add_aduitView.setSiteList(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getSitesFromCltId(Integer.parseInt(cltId)));
        }

    }

    @Override
    public void getCOntactList(String cltId) {
        if (Integer.parseInt(cltId) > 0) {
            add_aduitView.setContactList(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().getContactFromcltId(cltId));
        }
    }

    @Override
    public void getCountryList() {
        countryList = SpinnerCountrySite.clientCountryList();// clientCountryList("countries.json");
        add_aduitView.setCountryList(countryList);
    }


    @Override
    public void getWorkerList() {
        add_aduitView.setWorkerList((ArrayList<FieldWorker>) AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).fieldWorkerModel().getFieldWorkerlist());
    }

    @Override
    public void getStateList(String countyId) {
        statesList = SpinnerCountrySite.clientStatesList(countyId);//clientStatesList("states.json", countyId);
        add_aduitView.setStateList(statesList);
    }


    @Override
    public void callApiForAddAudit(AddAudit_Req addJobReq) {
//        int tempId = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getTotleCount();
        addJobReq.setTempId(AppUtility.getTempIdFormat("Audit"));
        String dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);
        Gson gson = new Gson();
        String addJobReqest = gson.toJson(addJobReq);
//      add temp job in db
        addtempJobInDb(addJobReq);
        OfflineDataController.getInstance().addInOfflineDB(Service_apis.addAudit, addJobReqest, dateTime);
        //EotApp.getAppinstance().showToastmsg("Job Added");
        EotApp.getAppinstance().notifyApiObserver(Service_apis.addAudit);
        add_aduitView.finishActivity();
    }

    private void addtempJobInDb(AddAudit_Req addAudit_req) {
        AuditList_Res tempJob = new AuditList_Res();
//https://www.javatpoint.com/java-date-to-timestamp
        tempJob.setAudId(addAudit_req.getTempId());
        tempJob.setTempId(addAudit_req.getTempId());
        tempJob.setCltId(addAudit_req.getCltId());
        tempJob.setAdr(addAudit_req.getAdr());
        tempJob.setUpdateDate(AppUtility.getDateByMiliseconds());
        try {
            tempJob.setSchdlStart(getTimeStampFromFormatedDate(addAudit_req.getSchdlStart()));
            tempJob.setSchdlFinish(getTimeStampFromFormatedDate(addAudit_req.getSchdlFinish()));
        } catch (Exception e) {
            e.getMessage();
        }

        tempJob.setKpr(addAudit_req.getKpr());
        //  tempJob.setSchdlFinish(getTimeStampFromFormatedDate(addJobReq.getSchdlFinish()));

        tempJob.setStatus(String.valueOf(addAudit_req.getStatus()));
        tempJob.setCity(addAudit_req.getCity());
        tempJob.setLandmark(addAudit_req.getLandmark());
        tempJob.setEmail(addAudit_req.getEmail());
        tempJob.setContrId(addAudit_req.getContrId());

        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().inserSingleAudit(tempJob);

        LogModel logModel = ActivityLogController
                .getObj(ActivityLogController.AUDIT_MODULE, ActivityLogController.AUDIT_ADD, ActivityLogController.AUDIT_MODULE);
        ActivityLogController.saveOfflineTable(logModel);
    }

    private String getTimeStampFromFormatedDate(String schdlStart) {
        SimpleDateFormat gettingfmt = new SimpleDateFormat(
                //AppUtility.dateTimeByAmPmFormate(
                "dd-MM-yyyy hh:mm a"
                //        , "dd-MM-yyyy kk:mm")
                , Locale.US);
        try {
            Date formated = gettingfmt.parse(schdlStart);
            String str = String.valueOf(formated.getTime() / 1000);
            return str;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isValidCountry(String country) {
        for (Country ctry : countryList) {
            if (ctry.getId().equalsIgnoreCase(country))
                return true;
        }
        return false;
    }

    @Override
    public boolean isValidState(String state) {
        for (States item : statesList) {
            if (item.getId().equalsIgnoreCase(state))
                return true;
        }

        return false;
    }

    @Override
    public String cntryId(String country) {
        String cId = SpinnerCountrySite.getCountryId(country);
        statesList = SpinnerCountrySite.clientStatesList(cId);
        return cId;
    }

    @Override
    public String statId(String state, String statename) {
        String sId = SpinnerCountrySite.getStateId(state, statename);
        return sId;
    }

    @Override
    public void getTagDataList() {
        add_aduitView.setSetTagData(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).tagmodel().getTagslist());
    }


    private void networkError() {
        AppUtility.alertDialog(((Context) add_aduitView), LanguageController.getInstance().
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
