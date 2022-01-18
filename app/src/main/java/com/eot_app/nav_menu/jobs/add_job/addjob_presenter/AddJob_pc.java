package com.eot_app.nav_menu.jobs.add_job.addjob_presenter;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.activitylog.LogModel;
import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.SpinnerCountrySite;
import com.eot_app.nav_menu.jobs.add_job.AddjobView;
import com.eot_app.nav_menu.jobs.add_job.add_job_recr.RecurReqResModel;
import com.eot_app.nav_menu.jobs.add_job.addjobmodel.AddJob_Req;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.time_shift_pkg.ShiftTimeReSModel;
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
import com.eot_app.utility.settings.setting_db.JobTitle;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hypertrack.hyperlog.HyperLog;

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
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class AddJob_pc implements Add_job_pi {
    private final AddjobView addjobView;
    private List<States> statesList;
    private List<Country> countryList;
    private String date_str, time_str;

    public AddJob_pc(AddjobView addjobView) {
        this.addjobView = addjobView;
    }

    @Override
    public void getTimeShiftList() {
        List<ShiftTimeReSModel> list = new ArrayList<>();
        list = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).shiftTimeDao().getShiftTimeList();
        addjobView.setTimeShiftList(list);
    }

    @Override
    public boolean RequiredFieldsForRiviste(String cltId, String adr, Set<String> jtId, String countryId, String stateId, String mob, String alterNateMob, String email) {
        if (jtId.isEmpty()) {
            addjobView.showErrorMsgsForValidation(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_job_title));
            return false;
        } else if (cltId.equals("")) {
            addjobView.showErrorMsgsForValidation(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_client_name));
            return false;
        } else if (!mob.isEmpty() && !mob.equalsIgnoreCase(App_preference.getSharedprefInstance().getLoginRes().getCtryCode()) && mob.length() < AppConstant.MOBILE_LIMIT) {
            addjobView.showErrorMsgsForValidation(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_mob_lent));
            return false;
        } else if (!alterNateMob.isEmpty() && !mob.equalsIgnoreCase(App_preference.getSharedprefInstance().getLoginRes().getCtryCode()) && alterNateMob.length() < AppConstant.MOBILE_LIMIT) {
            addjobView.showErrorMsgsForValidation(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_alter_mob_lent));
            return false;
        } else if (!email.isEmpty() && (!Eot_Validation.email_checker(email).equals(""))) {
            addjobView.showErrorMsgsForValidation(Eot_Validation.email_checker(email));
            return false;
        } else if (adr.equals("")) {
            addjobView.showErrorMsgsForValidation(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_addr));
            return false;
        } else if (!isValidCountry(countryId)) {
            addjobView.showErrorMsgsForValidation(LanguageController.getInstance().getMobileMsgByKey(AppConstant.please_select_country_first));
            return false;
        } else if (!isValidState(stateId)) {
            addjobView.showErrorMsgsForValidation(LanguageController.getInstance().getMobileMsgByKey(AppConstant.state_error));
            return false;
        }
        return true;
    }


    @Override
    public boolean RequiredFields(String cltId, boolean contactSelf, boolean siteSelf, String conNm,
                                  String siteNm, String adr, Set<String> jtId, String countryId, String stateId, String mob, String
                                          alterNateMob, String email, boolean WEEKLYRECUR, int weekdaysize) {
        if (jtId.isEmpty()) {
            addjobView.showErrorMsgsForValidation(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_job_title));
            return false;
        } else if (cltId.equals("")) {
            addjobView.showErrorMsgsForValidation(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_client_name));
            return false;
        } else if (!mob.isEmpty() && !mob.equalsIgnoreCase(App_preference.getSharedprefInstance().getLoginRes().getCtryCode()) && mob.length() < AppConstant.MOBILE_LIMIT) {
            addjobView.showErrorMsgsForValidation(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_mob_lent));
            return false;
        } else if (!alterNateMob.isEmpty() && !mob.equalsIgnoreCase(App_preference.getSharedprefInstance().getLoginRes().getCtryCode()) && alterNateMob.length() < AppConstant.MOBILE_LIMIT) {
            addjobView.showErrorMsgsForValidation(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_alter_mob_lent));
            return false;
        } else if (!email.isEmpty() && (!Eot_Validation.email_checker(email).equals(""))) {
            addjobView.showErrorMsgsForValidation(Eot_Validation.email_checker(email));
            return false;
        } else if (adr.equals("")) {
            addjobView.showErrorMsgsForValidation(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_addr));
            return false;
        } else if (!isValidCountry(countryId)) {
            addjobView.showErrorMsgsForValidation(LanguageController.getInstance().getMobileMsgByKey(AppConstant.please_select_country_first));
            return false;
        } else if (!isValidState(stateId)) {
            addjobView.showErrorMsgsForValidation(LanguageController.getInstance().getMobileMsgByKey(AppConstant.state_error));
            return false;
        } else if (WEEKLYRECUR && weekdaysize == 0) {
            addjobView.showErrorMsgsForValidation(LanguageController.getInstance().getMobileMsgByKey(AppConstant.select_week_days));
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


        if (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable() != null &&
                App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("0")) {
            getEndTime(datestr, date_Time[1] + " " + date_Time[2]);
        } else {
            getEndTime(datestr, date_Time[1]);
        }
    }


    @Override
    public void getEndTime(String datestr, String dateTime) {
        String sch_tm_dt = App_preference.getSharedprefInstance().getLoginRes().getJobCurrentTime();
        if (!TextUtils.isEmpty(sch_tm_dt)) {
            sch_time_cur(datestr, dateTime, sch_tm_dt);
        }
    }

    private void sch_time_cur(String datestr, String date_Time, String sch_tm_dt) {
        String an_pm = "";
        try {
            String[] remv_sec = date_Time.split(":");
            try {
                if (App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable() != null &&
                        App_preference.getSharedprefInstance().getLoginRes().getIs24hrFormatEnable().equals("0")) {
                    String[] am_pm = date_Time.split(" ");
                    an_pm = " " + am_pm[1];
                }

            } catch (Exception e) {
                e.getMessage();
            }

            //  String[] am_pm = date_Time.split(" ");
            String cur_start = remv_sec[0] + ":" + remv_sec[1] + an_pm;
            String date_time = datestr + " " + cur_start;

            String[] time_dur = sch_tm_dt.split(":");
            long dur_milliseconds = TimeUnit.SECONDS.toMillis(TimeUnit.HOURS.toSeconds(Integer.parseInt(time_dur[0])) +
                    TimeUnit.MINUTES.toSeconds(Integer.parseInt(time_dur[1])));

            SimpleDateFormat simpleDate = new SimpleDateFormat(
                    AppUtility.dateTimeByAmPmFormate("dd-MM-yyyy hh:mm a", "dd-MM-yyyy kk:mm"), Locale.US);
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
            addjobView.set_str_DT_after_cur(std);
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
            else time_str = sch_time[1] + "";
        } catch (Exception e) {

        }
        addjobView.set_Str_DTime(date_str, time_str);
        end_Date_Time();
    }

    private void end_Date_Time() {
        String date_time = date_str + " " + time_str;
        SimpleDateFormat simpleDate = new SimpleDateFormat(
                AppUtility.dateTimeByAmPmFormate("dd-MM-yyyy hh:mm a", "dd-MM-yyyy kk:mm"), Locale.US);
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
        addjobView.set_End_Date_Time(std);
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
        addjobView.setContractlist(contractResList);
    }

    @Override
    public void getJobTitleList() {
        List<JobTitle> data = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobTitleModel().getJobTitlelist();
        addjobView.SetJobTittle((ArrayList<JobTitle>) data);
    }

    @Override
    public void getClientList() {
        List<Client> data = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientModel().getActiveClientList();
        addjobView.setClientlist(data);
    }

    @Override
    public void getSiteList(String cltId) {
        if (Integer.parseInt(cltId) > 0) {
            addjobView.setSiteList(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getSitesFromCltId(Integer.parseInt(cltId)));
        }

    }

    @Override
    public void getCOntactList(String cltId) {
        if (Integer.parseInt(cltId) > 0) {
            addjobView.setContactList(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().getContactFromcltId(cltId));
        }
    }

    @Override
    public void getCountryList() {
        countryList = SpinnerCountrySite.clientCountryList();// clientCountryList("countries.json");
        addjobView.setCountryList(countryList);
    }

    @Override
    public void getStateList(String countyId) {
        statesList = SpinnerCountrySite.clientStatesList(countyId);//clientStatesList("states.json", countyId);
        addjobView.setStateList(statesList);
    }

    @Override
    public void getWorkerList() {
        addjobView.setWorkerList(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).fieldWorkerModel().getFieldWorkerlist());
    }


    @Override
    public void callApiForAddJob(AddJob_Req addJobReq) {
        HyperLog.i("AddJob", new Gson().toJson(addJobReq));

        addJobReq.setTempId(AppUtility.getTempIdFormat("Job"));
        String dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);
        Gson gson = new Gson();
        String addJobReqest = gson.toJson(addJobReq);
//      add temp job in db
        addtempJobInDb(addJobReq);
        OfflineDataController.getInstance().addInOfflineDB(Service_apis.addJob, addJobReqest, dateTime);
        EotApp.getAppinstance().notifyApiObserver(Service_apis.addJob);
        addjobView.finishActivity();
    }


    private void addtempJobInDb(AddJob_Req addJobReq) {
        Job tempJob = new Job();
//https://www.javatpoint.com/java-date-to-timestamp
        tempJob.setJobId(addJobReq.getTempId());
        tempJob.setTempId(addJobReq.getTempId());
        tempJob.setNm(addJobReq.getNm());
        tempJob.setCltId(addJobReq.getCltId());
        tempJob.setAdr(addJobReq.getAdr());
        tempJob.setUpdateDate(AppUtility.getDateByMiliseconds());
        try {
            tempJob.setSchdlStart(getTimeStampFromFormatedDate(addJobReq.getSchdlStart()));
            //tempJob.setSchdlStart((addJobReq.getSchdlStart()));
            tempJob.setSchdlFinish(getTimeStampFromFormatedDate(addJobReq.getSchdlFinish()));
            // tempJob.setSchdlFinish((addJobReq.getSchdlFinish()));
        } catch (Exception e) {
            e.getMessage();
        }

        tempJob.setPrty(addJobReq.getPrty());
        tempJob.setStatus(addJobReq.getStatus());
        tempJob.setCity(addJobReq.getCity());
        tempJob.setLandmark(addJobReq.getLandmark());
        tempJob.setEmail(addJobReq.getEmail());
        tempJob.setContrId(addJobReq.getContrId());
        if (addJobReq.getRecurData() != null && addJobReq.getRecurData().size() > 0) {
            List<RecurReqResModel> recurData = new ArrayList<>();
            recurData.add(new RecurReqResModel(addJobReq.getRecurData().get(0)));
            tempJob.setRecurData(recurData);
            tempJob.setRecurType(addJobReq.getRecurType());
            tempJob.setSelectedDays(addJobReq.getSelectedDays());
            tempJob.setIsRecur(addJobReq.getIsRecur());
        }


        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().inserSingleJob(tempJob);

        LogModel logModel = ActivityLogController
                .getObj(ActivityLogController.JOB_MODULE, ActivityLogController.JOB_ADD, ActivityLogController.JOB_MODULE);
        ActivityLogController.saveOfflineTable(logModel);
    }

    private String getTimeStampFromFormatedDate(String schdlStart) {
        SimpleDateFormat gettingfmt = new SimpleDateFormat(
                //AppUtility.dateTimeByAmPmFormate
                ("dd-MM-yyyy hh:mm a"
                        //                , "dd-MM-yyyy kk:mm"
                )
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
        addjobView.setSetTagData(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).tagmodel().getTagslist());
    }

    @Override
    public FieldWorker getDefaultFieldWorker() {
        return AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).fieldWorkerModel().getFieldWorkerByID(App_preference.getSharedprefInstance().getLoginRes().getUsrId());
    }

    @Override
    public void addJobWithImageDescription(AddJob_Req addJob_req, ArrayList<String> links, List<String> fileNames) {
        if (AppUtility.isInternetConnected()) {

            RequestBody userIdrb = RequestBody.create(MultipartBody.FORM, App_preference.getSharedprefInstance().getLoginRes().getUsrId());
            RequestBody parentId = RequestBody.create(MultipartBody.FORM, "");//parent id
            RequestBody contractId = RequestBody.create(MultipartBody.FORM, addJob_req.getContrId());//contrId
            RequestBody compId = RequestBody.create(MultipartBody.FORM, (App_preference.getSharedprefInstance().getLoginRes().getCompId()));
            RequestBody quoteId = RequestBody.create(MultipartBody.FORM, "");
            RequestBody jobDes = RequestBody.create(MultipartBody.FORM, addJob_req.getDes());
            RequestBody prtyrb = RequestBody.create(MultipartBody.FORM, addJob_req.getPrty());
            RequestBody jobInstruction = RequestBody.create(MultipartBody.FORM, addJob_req.getInst());
            RequestBody cltIdReq = RequestBody.create(MultipartBody.FORM, addJob_req.getCltId());
            RequestBody newcnrb = RequestBody.create(MultipartBody.FORM, addJob_req.getNm());
            RequestBody clientForFuturerb = RequestBody.create(MultipartBody.FORM, addJob_req.getClientForFuture() + "");
            RequestBody siteIfrb = RequestBody.create(MultipartBody.FORM, addJob_req.getSiteId());
            RequestBody newsiterb = RequestBody.create(MultipartBody.FORM, addJob_req.getSnm());
            RequestBody siteforfuturerb = RequestBody.create(MultipartBody.FORM, addJob_req.getSiteForFuture() + "");
            RequestBody conIdrb = RequestBody.create(MultipartBody.FORM, addJob_req.getConId() + "");//conId
            RequestBody new_con_nmrb = RequestBody.create(MultipartBody.FORM, addJob_req.getCnm() + "");//new_con_nm
            RequestBody contactforfuturerb = RequestBody.create(MultipartBody.FORM, addJob_req.getContactForFuture() + "");
            RequestBody mobilerb = RequestBody.create(MultipartBody.FORM, addJob_req.getMob1());
            RequestBody atmobrb = RequestBody.create(MultipartBody.FORM, addJob_req.getMob2());
            RequestBody emailrb = RequestBody.create(MultipartBody.FORM, addJob_req.getEmail());
            RequestBody adrrb = RequestBody.create(MultipartBody.FORM, addJob_req.getAdr());
            RequestBody citrb = RequestBody.create(MultipartBody.FORM, addJob_req.getCity());
            RequestBody ctryrb = RequestBody.create(MultipartBody.FORM, addJob_req.getCtry());//ctry_id
            RequestBody staterb = RequestBody.create(MultipartBody.FORM, addJob_req.getState());//state_id
            RequestBody postcoderb = RequestBody.create(MultipartBody.FORM, addJob_req.getZip());
            RequestBody memebretyprb = RequestBody.create(MultipartBody.FORM, addJob_req.getType() + "");
            RequestBody schdlStartrb = RequestBody.create(MultipartBody.FORM, addJob_req.getSchdlStart());
            RequestBody schdlFinishrb = RequestBody.create(MultipartBody.FORM, addJob_req.getSchdlFinish());
            RequestBody kprrb;
            if (addJob_req.getKpr() != null)
                kprrb = RequestBody.create(MultipartBody.FORM, addJob_req.getKpr());
            else kprrb = RequestBody.create(MultipartBody.FORM, "");
            RequestBody statusrb = RequestBody.create(MultipartBody.FORM, "1");
            RequestBody dateTimerb = RequestBody.create(MultipartBody.FORM, AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
            RequestBody latrb = RequestBody.create(MultipartBody.FORM, addJob_req.getLat());
            RequestBody lngrb = RequestBody.create(MultipartBody.FORM, addJob_req.getLng());
            RequestBody landmarkrb = RequestBody.create(MultipartBody.FORM, addJob_req.getLandmark());
            RequestBody appIdrb = RequestBody.create(MultipartBody.FORM, addJob_req.getAppId());

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

            RequestBody tagDatRb = null;
            if (addJob_req.getTagData() != null) {
                Gson gson = new Gson();
                String s = gson.toJson(addJob_req.getTagData());
                tagDatRb = RequestBody.create(MultipartBody.FORM, s);
            }


            RequestBody answerRequestBody = null;
            if (addJob_req.getAnswerArrayList() != null) {
                Gson gson = new Gson();
                String answerJson = gson.toJson(addJob_req.getAnswerArrayList());
                answerRequestBody = RequestBody.create(MultipartBody.FORM, answerJson);
            }

            List<MultipartBody.Part> listworkrb = new ArrayList<>();
            if (addJob_req.getMemIds() != null)
                for (String s : addJob_req.getMemIds())
                    listworkrb.add(MultipartBody.Part.createFormData("memIds[]", s));


            List<MultipartBody.Part> jatIds = new ArrayList<>();
            if (jatIds != null)
                for (String s : addJob_req.getJtId())
                    jatIds.add(MultipartBody.Part.createFormData("jtId[]", s));

            AppUtility.progressBarShow((Activity) addjobView);
            ApiClient.getservices().addJobWithDocuments(AppUtility.getApiHeaders(),
                    parentId,
                    compId,
                    contractId,
                    appIdrb,
                    cltIdReq,
                    siteIfrb,
                    conIdrb,
                    quoteId,
                    memebretyprb,
                    prtyrb,
                    statusrb,
                    userIdrb,
                    kprrb,
                    jobDes,
                    jobInstruction,
                    schdlStartrb,
                    schdlFinishrb,
                    newcnrb,
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
                    clientForFuturerb,
                    siteforfuturerb,
                    contactforfuturerb,
                    dateTimerb,
                    latrb,
                    lngrb,
                    landmarkrb,
                    tagDatRb,
                    answerRequestBody,
                    jatIds,
                    listworkrb,
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
                                //  EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("messgae").toString()));
                                addjobView.finishActivity();
                                //refresh recent job on appointment details and show the label of recent job with code
                                EotApp.getAppinstance().notifyApiObserver(Service_apis.addJob);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                EotApp.getAppinstance().sessionExpired();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            AppUtility.progressBarDissMiss();
                            EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.something_wrong));
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                        }
                    });
        }
    }
}
