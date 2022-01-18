package com.eot_app.utility.settings.firstSync;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;

import com.eot_app.BuildConfig;
import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.login_next.login_next_model.MobileDefaultSettings;
import com.eot_app.login_next.login_next_model.ResLoginData;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UserChatListModelReq;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UserChatModel;
import com.eot_app.nav_menu.appointment.dbappointment.Appointment;
import com.eot_app.nav_menu.appointment.list.model.AppointmentListReq;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditListRequestModel;
import com.eot_app.nav_menu.audit.audit_list.audit_mvp.model.AuditList_Res;
import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.client_db.Client_Request_model;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_db.JobListRequestModel;
import com.eot_app.nav_menu.jobs.job_detail.invoice.inventry_pkg.Inventry_ReQ_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.inventry_pkg.Inventry_ReS_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.location_tax_dao.TaxReqModel;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.location_tax_dao.TaxesLocation;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Tax;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_model_pkg.TaxList_ReQ_Model;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.time_shift_pkg.ShiftTimeReSModel;
import com.eot_app.time_shift_pkg.ShiftTimeReqModel;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.db.OfflineDataController;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.language_support.Language_Preference;
import com.eot_app.utility.settings.SettingUrls;
import com.eot_app.utility.settings.contractdb.ContractReq;
import com.eot_app.utility.settings.contractdb.ContractRes;
import com.eot_app.utility.settings.equipmentdb.Equipment;
import com.eot_app.utility.settings.equipmentdb.EquipmentListReq;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.hypertrack.hyperlog.HyperLog;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by aplite_pc302 on 7/23/18.
 */

public class FirstSyncPC implements FirstSyncPi {
    private final String ERROR_MSG = "Please Retry!";
    private final FirstSyncView firstSyncView;
    private final int updatelimit;
    float new_version = 0.0f;
    private int count;
    private int updateindex;

    public FirstSyncPC(FirstSyncView firstSync) {
        this.firstSyncView = firstSync;
        this.updatelimit = AppConstant.LIMIT_HIGH;
        /****set current Activity/Fragment Context***/
    }

    private void getEquipmentList() {
        EquipmentListReq equipmentListReq = new EquipmentListReq(
                updatelimit, updateindex, "", App_preference.getSharedprefInstance().getAllEquipmentSyncTime());
        String data = new Gson().toJson(equipmentListReq);
        ApiClient.getservices().eotServiceCall(Service_apis.getAllEquipments, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        Log.d("equipmentlist", jsonObject.toString());
                        if (jsonObject.get("success").getAsBoolean()) {
                            count = jsonObject.get("count").getAsInt();
                            String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                            Type listType = new TypeToken<List<Equipment>>() {
                            }.getType();
                            List<Equipment> equipmentList = new Gson().fromJson(convert, listType);
                            if (equipmentList != null)
                                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).equipmentDao().insertEquipmentList(equipmentList);

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        /******/
                        firstSyncView.errorMsg(ERROR_MSG);

                    }

                    @Override
                    public void onComplete() {
                        if ((updateindex + updatelimit) <= count) {
                            updateindex += updatelimit;
                            getEquipmentList();
                        } else {
                            if (count != 0) {
                                App_preference.getSharedprefInstance().setAllEquipmentSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                            }
                            updateindex = 0;
                            count = 0;
                            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).equipmentDao().deleteEquipmentByIsDelete();
                            App_preference.getSharedprefInstance().setFirstSyncState(11);
                            firstSyncView.progressStatus(App_preference.getSharedprefInstance().getFirstSyncState());
                            startSyncFromStatus();
                        }
                    }
                });
    }

    /***get Contract List****/
    private void getContractList() {
        ContractReq contractReq = new ContractReq(
                updatelimit, updateindex, "", App_preference.getSharedprefInstance().getContractSyncTime());
        String data = new Gson().toJson(contractReq);
        ApiClient.getservices().eotServiceCall(Service_apis.getContractList, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        Log.d("contractList", jsonObject.toString());
                        if (jsonObject.get("success").getAsBoolean()) {
                            count = jsonObject.get("count").getAsInt();
                            String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                            Type listType = new TypeToken<List<ContractRes>>() {
                            }.getType();
                            List<ContractRes> contractList = new Gson().fromJson(convert, listType);
                            if (contractList != null)
                                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contractDao().insertContractList(contractList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        /******/
                        firstSyncView.errorMsg(ERROR_MSG);

                    }

                    @Override
                    public void onComplete() {
                        if ((updateindex + updatelimit) <= count) {
                            updateindex += updatelimit;
                            getContractList();
                        } else {
                            if (count != 0) {
                                App_preference.getSharedprefInstance().setContractSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                            }
                            updateindex = 0;
                            count = 0;
                            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contractDao().deleteContractByIsDelete();
                            App_preference.getSharedprefInstance().setFirstSyncState(10);
                            firstSyncView.progressStatus(App_preference.getSharedprefInstance().getFirstSyncState());
                            startSyncFromStatus();
                        }
                    }
                });
    }

    private void getAuditList() {
        AuditListRequestModel auditListRequestModel = new AuditListRequestModel(Integer.parseInt(App_preference.getSharedprefInstance().getLoginRes().getUsrId()),
                updatelimit, updateindex, App_preference.getSharedprefInstance().getAuditSyncTime());
        String data = new Gson().toJson(auditListRequestModel);
        ApiClient.getservices().eotServiceCall(Service_apis.getAuditList, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        Log.d("mahi", jsonObject.toString());
                        if (jsonObject.get("success").getAsBoolean()) {
                            count = jsonObject.get("count").getAsInt();
                            String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                            Type listType = new TypeToken<List<AuditList_Res>>() {
                            }.getType();
                            List<AuditList_Res> auditList = new Gson().fromJson(convert, listType);
                            Log.e("data----", "");
                            addAuditListInDB(auditList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        firstSyncView.errorMsg(ERROR_MSG);
                    }

                    @Override
                    public void onComplete() {
                        Log.e("", "");
                        if ((updateindex + updatelimit) <= count) {
                            updateindex += updatelimit;
                            getAuditList();
                        } else {
                            if (count != 0) {
                                App_preference.getSharedprefInstance().setAuditSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                            }
                            updateindex = 0;
                            count = 0;
                            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().deleteJobByIsDelete();
                            //     AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().deleteAuditStatusNot();
                            App_preference.getSharedprefInstance().setFirstSyncState(9);
                            firstSyncView.progressStatus(App_preference.getSharedprefInstance().getFirstSyncState());
                            startSyncFromStatus();
                        }
                    }
                });

    }

    private void addAuditListInDB(List<AuditList_Res> auditList) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).auditDao().inserAuditList(auditList);
    }

    /***get invoice item taxex list****/
    private void getInvoiceTaxesList() {
        TaxList_ReQ_Model model = new TaxList_ReQ_Model(App_preference.getSharedprefInstance().getLoginRes().getCompId()
                , updatelimit, updateindex, App_preference.getSharedprefInstance().getInventryTaxesSyncTime());
        String data = new Gson().toJson(model);
        ApiClient.getservices().eotServiceCall(Service_apis.getTaxList, AppUtility.getApiHeaders(), getJsonObject(data))
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
                            Type listType = new TypeToken<List<Tax>>() {
                            }.getType();
                            List<Tax> temptaxList = new Gson().fromJson(convert, listType);
                            addInvoiceTaxexListInDB(temptaxList);
                            Log.e("data----", "");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("ERROR", e.getMessage());
                        firstSyncView.errorMsg(ERROR_MSG);
                    }

                    @Override
                    public void onComplete() {
                        Log.e("", "");
                        if ((updateindex + updatelimit) <= count) {
                            updateindex += updatelimit;
                            getInvoiceTaxesList();
                        } else {
                            if (count != 0) {
                                App_preference.getSharedprefInstance().setInventryTaxesSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                            }
                            updateindex = 0;
                            count = 0;
                            App_preference.getSharedprefInstance().setFirstSyncState(8);
                            firstSyncView.progressStatus(App_preference.getSharedprefInstance().getFirstSyncState());
                            startSyncFromStatus();
                        }
                    }
                });
    }

    @Override
    public void startSync() {
        getMobileDefaultSettings();
    }

    private void getMobileDefaultSettings() {//get default company setting
        if (AppUtility.isInternetConnected()) {
            Map<String, String> hm = new HashMap<>();
            hm.put("usrId", App_preference.getSharedprefInstance().getLoginRes().getUsrId());
            hm.put("devType", "1");//1 is a device type for android
            String data = new Gson().toJson(hm);
//AppUtility.getApiHeaders(),
            ApiClient.getservices().userLogin(Service_apis.getMobileDefaultSettings, getJsonObject(data))
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
                                String jsonString = gson.toJson(jsonObject.get("data").getAsJsonObject());
                                Log.e("TAG :", "HII  " + jsonString);
                                MobileDefaultSettings mobileDefaultSettings = gson.fromJson(jsonString, MobileDefaultSettings.class);

                                ResLoginData resLoginData = App_preference.getSharedprefInstance().getLoginRes();


                                if (resLoginData != null) {
                                    resLoginData.setMobileDefaultSettings(mobileDefaultSettings);
                                }
                                String saveLoginData = gson.toJson(resLoginData);
                                App_preference.getSharedprefInstance().setLoginResponse(saveLoginData);
                                App_preference.getSharedprefInstance().setcheckId(
                                        App_preference.getSharedprefInstance().getLoginRes().getCheckId());

                                Log.e("TAG:", "TAG");


                                /***** check for language control ******/
                                checkForLanguageSettings();

                                if (App_preference.getSharedprefInstance().getLoginRes().getExpireStatus().equals("0")) {
                                    subscriptionExpire();
                                } else {
                                    /***  check for App version update ***/
                                    checkForVersionUpdate();
                                }

                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            OfflineDbSync();
                        }

                        @Override
                        public void onComplete() {
                            Log.e("", "");
                        }
                    });
        } else {
            AppUtility.alertDialog(((Context) firstSyncView), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    firstSyncView.goHomePage();
                    return null;
                }
            });
        }
    }

    private void subscriptionExpire() {
        Map<String, String> hm = new HashMap<>();
        String data = new Gson().toJson(hm);
        if (AppUtility.isInternetConnected())
            ApiClient.getservices().eotServiceCall(Service_apis.getSubscriptionData, AppUtility.getApiHeaders(),
                    AppUtility.getJsonObject(data))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                firstSyncView.setSubscriptionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                ResLoginData data = App_preference.getSharedprefInstance().getLoginRes();
                                data.setToken("");
                                App_preference.getSharedprefInstance().setLoginResponse(new Gson().toJson(data));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            firstSyncView.errorMsg(ERROR_MSG);
                            Log.e("", e.getMessage());

                        }

                        @Override
                        public void onComplete() {
                            Log.e("", "");
                        }
                    });

    }

    private void checkForVersionUpdate() {
        try {
            float force_version = Float.valueOf(App_preference.getSharedprefInstance().getLoginRes().getForceupdate_version());
            float version = Float.valueOf(App_preference.getSharedprefInstance().getLoginRes().getVersion());

            PackageInfo pInfo = EotApp.getAppinstance().getPackageManager().getPackageInfo(EotApp.getAppinstance().getPackageName(), 0);
            float app_version = Float.valueOf(pInfo.versionName);
            if (force_version > app_version) {
                firstSyncView.upateForcefully();
            } else if (version > app_version && check24HourComplete()) {
//                check 24 hours complete ?
                firstSyncView.updateNotForcefully();
            } else {
                OfflineDbSync();
            }
        } catch (Exception e) {
            e.printStackTrace();
            OfflineDbSync();
        }

    }

    private boolean check24HourComplete() {
        String datevalue = App_preference.getSharedprefInstance().getIS_24HOURS();
        if (datevalue.equals("")) {
            App_preference.getSharedprefInstance().setIS_24HOURS(AppUtility.getDateByMiliseconds());
            return true;
        }
        Date last_date = new Date(Long.valueOf(datevalue)); // last save date on update check.
        Date current_date = new Date(Long.valueOf(AppUtility.getDateByMiliseconds())); // current date.
        long duration = current_date.getTime() - last_date.getTime();
        long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
        if (diffInDays >= 1) {
            App_preference.getSharedprefInstance().setIS_24HOURS(AppUtility.getDateByMiliseconds());
            return true;
        }
        return false;
    }

    private void checkForLanguageSettings() {

        try {

            float existing_version = Float.parseFloat(Language_Preference.getSharedprefInstance().getlanguageVersion());
            String existing_name = Language_Preference.getSharedprefInstance().getlanguageFilename();
            if (App_preference.getSharedprefInstance().getLoginRes().getLanguage() != null) {
                final String new_filename = App_preference.getSharedprefInstance().getLoginRes().getLanguage().getFileName();
                try {
                    try {

                        if ((App_preference.getSharedprefInstance().getLoginRes().getLanguage().getVersion()) != null) {
                            new_version = Float.parseFloat(App_preference.getSharedprefInstance().getLoginRes().getLanguage().
                                    getVersion());
                        } else {
                            new_version = 0.0f;
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        new_version = 0.0f;
                    }

                    boolean isLanChangebyUser = Language_Preference.getSharedprefInstance().isUserChangeLang();
                    String islock = App_preference.getSharedprefInstance().getLoginRes().getLanguage().getIsLock();//"1";
                    //the language file only download if user not change by it self or admin change the language
                    if (!islock.equals("0") || !isLanChangebyUser || (!existing_name.equals(new_filename) && existing_version != new_version)) {
                        String file_path = App_preference.getSharedprefInstance().getLoginRes().getLanguage().getFilePath();
                        String download_url = file_path + new_filename + ".json";
                        LanguageController.getInstance().downloadFile(download_url, new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                App_preference.getSharedprefInstance().setLanFileVer(Float.parseFloat(BuildConfig.VERSION_NAME));
                                Language_Preference.getSharedprefInstance().setisUserChangeLang(false);
                                Language_Preference.getSharedprefInstance().setLanguageFilename(new_filename);
                                Language_Preference.getSharedprefInstance().setLanguageVersion(String.valueOf(new_version));
                                return null;
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OfflineDbSync() {
        OfflineDataController.getInstance().fromFirstTimeSyncCall(new CallBackFirstSync() {
            @Override
            public void getCallBackOfComplete(int success_no, String msg) {
                if (success_no == 1) {
                    firstSyncView.progressStatus(0);
                    firstSyncView.setUI(0);
                }
                getSettingsData();
            }
        });
    }

    private void getSettingsData() {
        String compid = String.valueOf(App_preference.getSharedprefInstance().getLoginRes().getCompId());
        if (compid != null) {
            SettingUrls settingUrls = new SettingUrls(Integer.parseInt(compid), new CallBackFirstSync() {
                @Override
                public void getCallBackOfComplete(int success_no, String msg) {
                    if (success_no == 1) {
                        firstSyncView.progressStatus(0);
                        firstSyncView.setUI(0);
//                        firstSyncView.goHomePage();
//                        call other apis like job, client,
                        startSyncFromStatus();
                    } else if (success_no == 2) {
                        EotApp.getAppinstance().sessionExpired();
                        firstSyncView.sessionExpiredFinishActivity();
                    } else {
                        firstSyncView.errorMsg(ERROR_MSG);
                    }
                }
            });
            settingUrls.getJobTitleList();
        }
    }

    @Override
    public void startSyncFromStatus() {
        int status_no = App_preference.getSharedprefInstance().getFirstSyncState();
        switch (status_no) {
            case 0:
                getJobSyncService();//get job list
                break;
            case 1:
                getClientSyncService();//sync client list
                break;
            case 2:
                getContactSyncService();//sync contact list
                break;
            case 3:
                getSiteSyncService();//get Site list
                break;
            case 4:
                getChatgrpUserSyncService();//get chat user list
                break;
            case 5:
                getAppointmentSyncService();
                break;
            case 6:
                getInvoiceItemList();//get inventry item's
                break;
            case 7:
                getInvoiceTaxesList();//get taxes for invoice item's
                break;
            case 8:
                getAuditList();
                break;
            case 9:
                getContractList();
                break;
            case 10:
                getEquipmentList();
                break;
            case 11:
                getTaxLocations();
                break;
            case 12:
                getJobTimeShiftList();
                break;
            case 13:
                firstSyncView.goHomePage();
                App_preference.getSharedprefInstance().setFirstSyncState(0);
                break;
        }
    }

    private void getJobTimeShiftList() {
        ShiftTimeReqModel equipmentListReq = new ShiftTimeReqModel(
                App_preference.getSharedprefInstance().getShiftTimeSyncTime(), updatelimit, updateindex);
        String data = new Gson().toJson(equipmentListReq);
        ApiClient.getservices().eotServiceCall(Service_apis.getShiftList, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        Log.e("TaxLOcationList:", jsonObject.toString());
                        if (jsonObject.get("success").getAsBoolean()) {
                            count = jsonObject.get("count").getAsInt();
                            String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                            Type listType = new TypeToken<List<ShiftTimeReSModel>>() {
                            }.getType();
                            List<ShiftTimeReSModel> equipmentList = new Gson().fromJson(convert, listType);
                            if (equipmentList != null)
                                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).shiftTimeDao().insertAllShiftTimeList(equipmentList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        /******/
                        firstSyncView.errorMsg(ERROR_MSG);

                    }

                    @Override
                    public void onComplete() {
                        if ((updateindex + updatelimit) <= count) {
                            updateindex += updatelimit;
                            getJobTimeShiftList();
                        } else {
                            if (count != 0) {
                                App_preference.getSharedprefInstance().setShiftTimeSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                            }
                            updateindex = 0;
                            count = 0;
                            App_preference.getSharedprefInstance().setFirstSyncState(13);
                            firstSyncView.progressStatus(App_preference.getSharedprefInstance().getFirstSyncState());
                            startSyncFromStatus();
                        }
                    }
                });


    }

    private void getTaxLocations() {
        TaxReqModel equipmentListReq = new TaxReqModel(
                App_preference.getSharedprefInstance().getTaxLocationSyncTime());
        String data = new Gson().toJson(equipmentListReq);
        ApiClient.getservices().eotServiceCall(Service_apis.getLocationList, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        Log.e("TaxLOcationList:", jsonObject.toString());
                        if (jsonObject.get("success").getAsBoolean()) {
                            count = jsonObject.get("count").getAsInt();
                            String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                            Type listType = new TypeToken<List<TaxesLocation>>() {
                            }.getType();
                            List<TaxesLocation> equipmentList = new Gson().fromJson(convert, listType);
                            if (equipmentList != null)
                                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).taxesLocationDao().insertAllLocationList(equipmentList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        /******/
                        firstSyncView.errorMsg(ERROR_MSG);

                    }

                    @Override
                    public void onComplete() {
                        if ((updateindex + updatelimit) <= count) {
                            updateindex += updatelimit;
                            getTaxLocations();
                        } else {
                            if (count != 0) {
                                App_preference.getSharedprefInstance().setTaxLocationSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                            }
                            updateindex = 0;
                            count = 0;
                            App_preference.getSharedprefInstance().setFirstSyncState(12);
                            firstSyncView.progressStatus(App_preference.getSharedprefInstance().getFirstSyncState());
                            startSyncFromStatus();
                        }
                    }
                });
    }

    @Override
    public void retryCall() {
        int status_no = App_preference.getSharedprefInstance().getFirstSyncState();
        Log.e("Sonam Status===", "Sonam Status===" + status_no + "");
        if (status_no == 0) {
            OfflineDbSync();
        } else {
            startSyncFromStatus();
        }
    }

    /***get invoice Invetrt Item list****/
    private void getInvoiceItemList() {
        Log.e("data--->>>", "data--->>>");
        if (AppUtility.isInternetConnected()) {
            Inventry_ReQ_Model inventry_model = new Inventry_ReQ_Model(Integer.parseInt(App_preference.getSharedprefInstance().getLoginRes().getCompId()),
                    "",

                    updatelimit, updateindex, App_preference.getSharedprefInstance().getInventryItemSyncTime());//

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
                                Log.e("Count", "" + count);
                                if (count < 2000) {
                                    String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                    Type listType = new TypeToken<List<Inventry_ReS_Model>>() {
                                    }.getType();
                                    List<Inventry_ReS_Model> inventryitemlist = new Gson().fromJson(convert, listType);
                                    addInvoiceItemInDB(inventryitemlist);
                                    Log.e("Count", "" + count);
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG : error----", e.getMessage());
                            firstSyncView.errorMsg(ERROR_MSG);
                        }

                        @Override
                        public void onComplete() {
                            Log.e("TAG onComplete------", "onComplete");
//                            if (count < 2000) {
                            if (count < 2000 && (updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                getInvoiceItemList();
                            } else {
                                if (count != 0) {
                                    App_preference.getSharedprefInstance()
                                            .setInventryItemSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                                }
                                updateindex = 0;
                                count = 0;
                                App_preference.getSharedprefInstance().setFirstSyncState(7);
                                firstSyncView.progressStatus(App_preference.getSharedprefInstance().getFirstSyncState());
                                startSyncFromStatus();

                            }
                        }

                    });
        }
    }


    private void addInvoiceTaxexListInDB(List<Tax> temptaxList) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).invoiceTaxDao().insertInvoiceTaxes(temptaxList);
    }

    private void addInvoiceItemInDB(List<Inventry_ReS_Model> inventryitemlist) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).invoiceItemDao().insertInvebtryItems(inventryitemlist);
    }

    /***get admin one-to-one chat group chat user list***/
    private void getChatgrpUserSyncService() {
        if (AppUtility.isInternetConnected()) {
            ActivityLogController.saveActivity(
                    ActivityLogController.LOGIN_MODULE,
                    ActivityLogController.LOGIN_SYNC_USER_LIST_CHAT,
                    ActivityLogController.LOGIN_MODULE
            );
            UserChatListModelReq model = new UserChatListModelReq(Integer.valueOf(App_preference.getSharedprefInstance().getLoginRes().getCompId()),
                    updatelimit, updateindex
                    , App_preference.getSharedprefInstance().getUsersSyncTime());
            String data = new Gson().toJson(model);
            ApiClient.getservices().eotServiceCall(Service_apis.groupUserListForChat,
                    AppUtility.getApiHeaders(), getJsonObject(data))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            Log.e("", "");
                            if (jsonObject.get("success").getAsBoolean()) {
                                count = jsonObject.get("count").getAsInt();
                                String jsonString = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                Type typeList = new TypeToken<List<UserChatModel>>() {
                                }.getType();
                                List<UserChatModel> chatList = new Gson().fromJson(jsonString, typeList);
                                addChatUserListDataToDB(chatList);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            firstSyncView.errorMsg(ERROR_MSG);
                        }

                        @Override
                        public void onComplete() {
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                getChatgrpUserSyncService();
                            } else {
                                if (count != 0) {
                                    App_preference.getSharedprefInstance().setUsersSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                                }
                                updateindex = 0;
                                count = 0;
                                App_preference.getSharedprefInstance().setFirstSyncState(5);
                                firstSyncView.progressStatus(App_preference.getSharedprefInstance().getFirstSyncState());
                                startSyncFromStatus();
                            }
                        }
                    });
        }
    }

    private void getSiteSyncService() {
        if (AppUtility.isInternetConnected()) {
            ActivityLogController.saveActivity(
                    ActivityLogController.LOGIN_MODULE,
                    ActivityLogController.LOGIN_CLIENT_SITE_SYN,
                    ActivityLogController.LOGIN_MODULE
            );
            Client_Request_model client_request_model = new Client_Request_model(Integer.parseInt(App_preference.getSharedprefInstance().getLoginRes().getCompId()),
                    updatelimit, updateindex, App_preference.getSharedprefInstance().getSiteSyncTime());
//            Client_Request_model client_request_model = new Client_Request_model(Integer.parseInt(App_preference.getSharedprefInstance().getLoginRes().getCompId()),
//                    5000, 0, App_preference.getSharedprefInstance().getSiteSyncTime());

            String data = new Gson().toJson(client_request_model);

            ApiClient.getservices().eotServiceCall(Service_apis.getClientSiteSink, AppUtility.getApiHeaders(), getJsonObject(data))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            Log.e("SiteDataList", jsonObject.toString());
                            if (jsonObject.get("success").getAsBoolean()) {
                                count = jsonObject.get("count").getAsInt();
                                String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                Type listType = new TypeToken<List<Site_model>>() {
                                }.getType();
                                List<Site_model> data = new Gson().fromJson(convert, listType);
                                addSiteDataToDB(data);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            firstSyncView.errorMsg(ERROR_MSG);
                        }

                        @Override
                        public void onComplete() {
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                getSiteSyncService();
                            } else {
                                if (count != 0) {
                                    App_preference.getSharedprefInstance().setSiteSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                                }
                                updateindex = 0;
                                count = 0;
                                /****very very importanmt sonam**/
                                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().deleteSiteByIsDelete();


                                App_preference.getSharedprefInstance().setFirstSyncState(4);
                                firstSyncView.progressStatus(App_preference.getSharedprefInstance().getFirstSyncState());
                                startSyncFromStatus();
                            }
                        }
                    });
        }
    }

    private void addSiteDataToDB(List<Site_model> data) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().insertSiteList(data);
    }

    private void addChatUserListDataToDB(List<UserChatModel> data) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).userChatModel().insertChatUserList(data);
    }

    private void getContactSyncService() {
        if (AppUtility.isInternetConnected()) {
            ActivityLogController.saveActivity(
                    ActivityLogController.LOGIN_MODULE,
                    ActivityLogController.LOGIN_CLIENT_CONTACT_SYN,
                    ActivityLogController.LOGIN_MODULE
            );
            Client_Request_model client_request_model = new Client_Request_model(Integer.parseInt(App_preference.getSharedprefInstance().getLoginRes().getCompId()),
                    updatelimit, updateindex, App_preference.getSharedprefInstance().getContactSyncTime());

            String data = new Gson().toJson(client_request_model);

            ApiClient.getservices().eotServiceCall(Service_apis.getClientContactSink, AppUtility.getApiHeaders(), getJsonObject(data))
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
                                addContactDataToDB(data);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            firstSyncView.errorMsg(ERROR_MSG);
                        }

                        @Override
                        public void onComplete() {
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                getContactSyncService();
                            } else {
                                if (count != 0) {
                                    App_preference.getSharedprefInstance().setContactSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                                }
                                updateindex = 0;
                                count = 0;
                                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().deleteContactByIsDelete();
                                App_preference.getSharedprefInstance().setFirstSyncState(3);
                                firstSyncView.progressStatus(App_preference.getSharedprefInstance().getFirstSyncState());
                                startSyncFromStatus();
                            }
                        }
                    });
        }
    }

    private void addContactDataToDB(List<ContactData> data) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().insertContactList(data);
    }

    private void getClientSyncService() {
        if (AppUtility.isInternetConnected()) {
            ActivityLogController.saveActivity(
                    ActivityLogController.LOGIN_MODULE,
                    ActivityLogController.LOGIN_CLIENT_SYNC,
                    ActivityLogController.LOGIN_MODULE
            );
            Client_Request_model client_request_model = new Client_Request_model(
                    Integer.parseInt(App_preference.getSharedprefInstance().getLoginRes().getCompId()),
                    updatelimit, updateindex, App_preference.getSharedprefInstance().getClientSyncTime());

            String data = new Gson().toJson(client_request_model);

            ApiClient.getservices().eotServiceCall(Service_apis.getClientSink, AppUtility.getApiHeaders(), getJsonObject(data))
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
                                Type listType = new TypeToken<List<Client>>() {
                                }.getType();
                                List<Client> data = new Gson().fromJson(convert, listType);
                                addClientDataToDB(data);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            firstSyncView.errorMsg(ERROR_MSG);
                        }

                        @Override
                        public void onComplete() {
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                getClientSyncService();
                            } else {
                                if (count != 0) {
                                    App_preference.getSharedprefInstance().setClientSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                                }
                                updateindex = 0;
                                count = 0;
                                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientModel().deleteClientByIsDelete();
                                App_preference.getSharedprefInstance().setFirstSyncState(2);
                                firstSyncView.progressStatus(App_preference.getSharedprefInstance().getFirstSyncState());
                                startSyncFromStatus();
                            }
                        }
                    });
        }
    }

    private void addClientDataToDB(List<Client> data) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientModel().insertUser(data);
    }

    private JsonObject getJsonObject(String params) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(params).getAsJsonObject();
        return obj;
    }

    private void getJobSyncService() {
        if (AppUtility.isInternetConnected()) {
            ActivityLogController.saveActivity(
                    ActivityLogController.LOGIN_MODULE,
                    ActivityLogController.LOGIN_JOB_SYNC,
                    ActivityLogController.LOGIN_MODULE
            );
            JobListRequestModel jobListRequestModel = new JobListRequestModel(Integer.parseInt(App_preference.getSharedprefInstance().getLoginRes().getUsrId()),
                    updatelimit, updateindex, App_preference.getSharedprefInstance().getJobSyncTime());
            String data = new Gson().toJson(jobListRequestModel);


            ApiClient.getservices().eotServiceCall(Service_apis.getUserJobList, AppUtility.getApiHeaders(), getJsonObject(data))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            Log.e("responce:", jsonObject.toString());
                            if (jsonObject.get("success").getAsBoolean()) {
                                count = jsonObject.get("count").getAsInt();
                                String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                Type listType = new TypeToken<List<Job>>() {
                                }.getType();
                                List<Job> data = new Gson().fromJson(convert, listType);
                                addJobInToDB(data);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            firstSyncView.errorMsg(ERROR_MSG);
                        }

                        @Override
                        public void onComplete() {
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                getJobSyncService();
                            } else {
                                if (count != 0) {
                                    App_preference.getSharedprefInstance().setJobSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                                }
                                updateindex = 0;
                                count = 0;
                                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().deleteJobByIsDelete();
//                                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().deleteJobStatusNot(
//                                        "1","2","3","4","5","6","7","8","9","10","11","12"
//                                );
                                App_preference.getSharedprefInstance().setFirstSyncState(1);
                                firstSyncView.progressStatus(App_preference.getSharedprefInstance().getFirstSyncState());
                                startSyncFromStatus();
                            }
                        }
                    });
        }
    }

    private void addJobInToDB(List<Job> data) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().inserJob(data);
    }

    private void getAppointmentSyncService() {
        int userId = Integer.parseInt(App_preference.getSharedprefInstance().getLoginRes().getUsrId());

        AppointmentListReq model = new AppointmentListReq(
                userId,
                updatelimit, updateindex
                , App_preference.getSharedprefInstance().getAppointmentSyncTime());
        String data = new Gson().toJson(model);
        Log.d("Data error", "error" + data);
        HyperLog.i("TAG", "Data error" + data);

        ApiClient.getservices().eotServiceCall(Service_apis.getAppointmentUserList, AppUtility.getApiHeaders(), getJsonObject(data))
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
                            Type listType = new TypeToken<List<Appointment>>() {
                            }.getType();
                            List<Appointment> data = new Gson().fromJson(convert, listType);
                            addAppointmentToDB(data);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("error", e.getMessage());
                        HyperLog.i("TAG", "Data error" + e.getMessage());
                        firstSyncView.errorMsg(ERROR_MSG);
                    }

                    @Override
                    public void onComplete() {
                        if ((updateindex + updatelimit) <= count) {
                            updateindex += updatelimit;
                            getAppointmentSyncService();
                        } else {
                            if (count != 0) {
                                App_preference.getSharedprefInstance().setAppointmentSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                            }
                            updateindex = 0;
                            count = 0;
                            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel().deleteAppointmentByIsDelete();
                            App_preference.getSharedprefInstance().setFirstSyncState(6);
                            firstSyncView.progressStatus(App_preference.getSharedprefInstance().getFirstSyncState());
                            startSyncFromStatus();
                        }
                    }
                });
    }

    private void addAppointmentToDB(List<Appointment> data) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).appointmentModel().insertAppointments(data);

    }

    public interface CallBackFirstSync {
        void getCallBackOfComplete(int success_no, String msg); // if -1 server call fail , 0 means
    }
}
