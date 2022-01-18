package com.eot_app.utility.settings;

import android.text.TextUtils;
import android.util.Log;

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
import com.eot_app.utility.settings.client_refrence_db.ClientRefrenceModel;
import com.eot_app.utility.settings.clientaccount_db.ClientAccountType;
import com.eot_app.utility.settings.clientaccount_db.ClientIndustryReq;
import com.eot_app.utility.settings.clientindustry_db.ClientIndustryModel;
import com.eot_app.utility.settings.fieldworkerlist.FieldWorker_request;
import com.eot_app.utility.settings.firstSync.FirstSyncPC;
import com.eot_app.utility.settings.jobtitle.CommonModel;
import com.eot_app.utility.settings.setting_db.FieldWorker;
import com.eot_app.utility.settings.setting_db.JobTitle;
import com.eot_app.utility.settings.setting_db.TagData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by aplite_pc302 on 6/21/18.
 */

public class SettingUrls {
    private final int SUCCESS_STATUS = 1;
    private final int FAIL_STATUS = 0;
    private final int SESSION_EXPIRE = 2;
    private final String SUCCESS_MSG = "settings Load";
    private final String FAIL_MSG = "setting fail";
    private final FirstSyncPC.CallBackFirstSync callbacksSync;
    private final int compId;
    private final int updatelimit;
    private final boolean isSessionExpired = false;
    private int count;
    private int updateindex;


    public SettingUrls(int compId, FirstSyncPC.CallBackFirstSync callbacksSync) {
        this.compId = compId;
        this.callbacksSync = callbacksSync;
        this.updatelimit = AppConstant.LIMIT_MID;
        this.updateindex = 0;
    }

    public void getJobTitleList() {
        CommonModel model = new CommonModel(compId, updatelimit, updateindex);
        String data = new Gson().toJson(model);
        ApiClient.getservices().eotServiceCall(Service_apis.getJobTitleList, AppUtility.getApiHeaders(), getJsonObject(data))
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
                            Type listType = new TypeToken<List<JobTitle>>() {
                            }.getType();
                            List<JobTitle> data = new Gson().fromJson(convert, listType);
                            addIntoDataBase(data);
                        } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                            App_preference.getSharedprefInstance().setBlankTokenOnSessionExpire();

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callbacksSync.getCallBackOfComplete(FAIL_STATUS, FAIL_MSG);
                    }

                    @Override
                    public void onComplete() {
                        if ((updateindex + updatelimit) <= count) {
                            updateindex += updatelimit;
                            getJobTitleList();
                        } else {
                            updateindex = 0;
                            count = 0;
                            if (TextUtils.isEmpty(App_preference.getSharedprefInstance().getLoginRes().getToken())) {
                                callbacksSync.getCallBackOfComplete(SESSION_EXPIRE, FAIL_MSG);
                            } else
                                getClientAccountType();
                            //callbacksSync.getCallBackOfComplete(SUCCESS_STATUS, SUCCESS_MSG);
                        }
                    }
                });
    }

    private JsonObject getJsonObject(String params) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(params).getAsJsonObject();
        return obj;
    }

    private void addIntoDataBase(List<JobTitle> data) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobTitleModel().inserTitle(data);
    }

    public void getClientAccountType() {   //add client account

        CommonModel model = new CommonModel(compId, updatelimit, updateindex);
        String data = new Gson().toJson(model);

        ApiClient.getservices().eotServiceCall(Service_apis.getCompanyAccountTypeList, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
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
                            Type listType = new TypeToken<List<ClientAccountType>>() {
                            }.getType();
                            List<ClientAccountType> data = new Gson().fromJson(convert, listType);
                            addAccountToDB(data);
                        } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                            App_preference.getSharedprefInstance().setBlankTokenOnSessionExpire();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callbacksSync.getCallBackOfComplete(FAIL_STATUS, FAIL_MSG);
                    }

                    @Override
                    public void onComplete() {
                        if ((updateindex + updatelimit) <= count) {
                            updateindex += updatelimit;
                            getClientAccountType();
                        } else {
                            updateindex = 0;
                            count = 0;
                            if (TextUtils.isEmpty(App_preference.getSharedprefInstance().getLoginRes().getToken())) {
                                callbacksSync.getCallBackOfComplete(SESSION_EXPIRE, FAIL_MSG);
                            } else
                                getFieldWorkerList();
                        }
                    }
                });
    }

    private void addAccountToDB(List<ClientAccountType> data) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientAccount().insertClientAccount(data);
    }

    /*******    for get field worker list*/
    public void getFieldWorkerList() {   //add client account

//    CommonModel model = new CommonModel(compId, limit, updateindex);
        if (App_preference.getSharedprefInstance().getLoginRes() != null) {
            FieldWorker_request model = new FieldWorker_request(App_preference.getSharedprefInstance().getLoginRes().getCompId()
                    , updatelimit, updateindex, "1");
            String data = new Gson().toJson(model);
            ApiClient.getservices().eotServiceCall(Service_apis.getFieldWorkerList, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
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
                                Type listType = new TypeToken<List<FieldWorker>>() {
                                }.getType();
                                List<FieldWorker> data = new Gson().fromJson(convert, listType);
                                addFieldWorkerToDB(data);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                App_preference.getSharedprefInstance().setBlankTokenOnSessionExpire();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            callbacksSync.getCallBackOfComplete(FAIL_STATUS, FAIL_MSG);
                        }

                        @Override
                        public void onComplete() {
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                getFieldWorkerList();
                            } else {
                                updateindex = 0;
                                count = 0;
                                if (TextUtils.isEmpty(App_preference.getSharedprefInstance().getLoginRes().getToken())) {
                                    callbacksSync.getCallBackOfComplete(SESSION_EXPIRE, FAIL_MSG);
                                } else
                                    getTagList();
                            }
                        }
                    });
        }
    }

    public void getTagList() {   //add client account

//    CommonModel model = new CommonModel(compId, limit, updateindex);
        if (App_preference.getSharedprefInstance().getLoginRes() != null) {
            CommonModel model = new CommonModel(compId, updatelimit, updateindex);
            String data = new Gson().toJson(model);
            ApiClient.getservices().eotServiceCall(Service_apis.getTagList, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data)).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            Log.e("Tag :", "TAG");
                            if (jsonObject.get("success").getAsBoolean()) {
                                count = jsonObject.get("count").getAsInt();
                                String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                Type listType = new TypeToken<List<TagData>>() {
                                }.getType();
                                List<TagData> data = new Gson().fromJson(convert, listType);
                                addTagsIntoDataBase(data);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                App_preference.getSharedprefInstance().setBlankTokenOnSessionExpire();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            callbacksSync.getCallBackOfComplete(FAIL_STATUS, FAIL_MSG);
                        }

                        @Override
                        public void onComplete() {
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                getTagList();
                            } else {
                                updateindex = 0;
                                count = 0;
                                if (TextUtils.isEmpty(App_preference.getSharedprefInstance().getLoginRes().getToken())) {
                                    callbacksSync.getCallBackOfComplete(SESSION_EXPIRE, FAIL_MSG);
                                } else
                                    getClientIndustryList();
                            }
                        }
                    });
        }
    }


    void getClientIndustryList() {
        if (App_preference.getSharedprefInstance().getLoginRes() != null) {
            ClientIndustryReq model = new ClientIndustryReq(updatelimit, updateindex, "");
            String data = new Gson().toJson(model);
            ApiClient.getservices().eotServiceCall(Service_apis.getIndustryList,
                    AppUtility.getApiHeaders(), AppUtility.getJsonObject(data)).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            Log.e("Tag :", "TAG");
                            if (jsonObject.get("success").getAsBoolean()) {
                                count = jsonObject.get("count").getAsInt();
                                String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                Type listType = new TypeToken<List<ClientIndustryModel>>() {
                                }.getType();
                                List<ClientIndustryModel> data = new Gson().fromJson(convert, listType);
                                addIndustryIntoDataBase(data);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                App_preference.getSharedprefInstance().setBlankTokenOnSessionExpire();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            callbacksSync.getCallBackOfComplete(FAIL_STATUS, FAIL_MSG);
                        }

                        @Override
                        public void onComplete() {
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                getClientIndustryList();
                            } else {
                                updateindex = 0;
                                count = 0;
                                if (TextUtils.isEmpty(App_preference.getSharedprefInstance().getLoginRes().getToken())) {
                                    callbacksSync.getCallBackOfComplete(SESSION_EXPIRE, FAIL_MSG);
                                } else
                                    getClientReference();
//                                    getCustomFrom("3");
                            }
                        }
                    });
        }
    }


    /**
     * get Custom filed For Client Site
     *
     * @param type 1 for job and 2 for site fields
     ****/
    void getCustomFrom(final String type) {
        ApiClient.getservices().eotServiceCall(Service_apis.getFormDetail, AppUtility.getApiHeaders(),
                AppUtility.getJsonObject(new Gson().toJson(new CustOmFiledReqModel(type))))
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
                            Log.d("customData", convert);
                            CustOmFiledResModel resModel = new Gson().fromJson(convert, CustOmFiledResModel.class);
                            getCustomFieldByFormId(resModel.getFrmId(), type);
                        } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                            App_preference.getSharedprefInstance().setBlankTokenOnSessionExpire();
                        } else {
                            getCompanySettingDetails();
                        }
                    }


                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", e.getMessage());
                        callbacksSync.getCallBackOfComplete(FAIL_STATUS, FAIL_MSG);
                    }

                    @Override
                    public void onComplete() {
                        Log.e("", "");
                        if (TextUtils.isEmpty(App_preference.getSharedprefInstance().getLoginRes().getToken())) {
                            callbacksSync.getCallBackOfComplete(SESSION_EXPIRE, FAIL_MSG);
                        }
                    }
                });

    }


    /**
     * get fields by form id
     *
     * @param frmid formId
     */
    void getCustomFieldByFormId(String frmid, final String type) {
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
                            Log.d("customData", convert);
                            if (type.equals("3")) {
                                App_preference.getSharedprefInstance().setAuditCustomFiled(convert);
                                getCustomFrom("2");
                            } else if (type.equals("2")) {
                                App_preference.getSharedprefInstance().setSiteCustomFiled(convert);
                                getCustomFrom("1");
                            } else if (type.equals("1")) {
                                App_preference.getSharedprefInstance().setJobCustomField(convert);
                                getCompanySettingDetails();
                            }

                        } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                            App_preference.getSharedprefInstance().setBlankTokenOnSessionExpire();
                        }
                    }


                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", e.getMessage());
                        callbacksSync.getCallBackOfComplete(FAIL_STATUS, FAIL_MSG);
                    }

                    @Override
                    public void onComplete() {
                        if (TextUtils.isEmpty(App_preference.getSharedprefInstance().getLoginRes().getToken())) {
                            callbacksSync.getCallBackOfComplete(SESSION_EXPIRE, FAIL_MSG);
                        }
                    }
                });
    }


    /**
     * get company setting
     **/
    private void getCompanySettingDetails() {

//    CommonModel model = new CommonModel(compId, limit, updateindex);
        if (App_preference.getSharedprefInstance().getLoginRes() != null) {
//            CommonModel model = new CommonModel(compId, updatelimit, updateindex);
            HashMap<String, String> model = new HashMap<>();
            model.put("compId", App_preference.getSharedprefInstance().getLoginRes().getCompId());
            String data = new Gson().toJson(model);
            ApiClient.getservices().eotServiceCall(Service_apis.getCompanySettingDetails, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data)).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            Log.e("Tag :", "TAG");
                            if (jsonObject.get("success").getAsBoolean()) {
                                Gson gson = new Gson();
                                String jsonString = gson.toJson(jsonObject.getAsJsonObject("data"));
                                App_preference.getSharedprefInstance().setCompanySettingsDetails(jsonString);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            callbacksSync.getCallBackOfComplete(FAIL_STATUS, FAIL_MSG);
                        }

                        @Override
                        public void onComplete() {
                            callbacksSync.getCallBackOfComplete(SUCCESS_STATUS, SUCCESS_MSG);
                        }
                    });
        }

    }

    private void addTagsIntoDataBase(List<TagData> data) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).tagmodel().inserTags(data);
    }

    private void addFieldWorkerToDB(List<FieldWorker> data) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).fieldWorkerModel().inserFieldWorker(data);
    }

    private void addIndustryIntoDataBase(List<ClientIndustryModel> data) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientIndustryDao().insertClientIndustry(data);
    }


    public void getClientReference() {   //add client account

        CommonModel model = new CommonModel(compId, updatelimit, updateindex);
        String data = new Gson().toJson(model);

        ApiClient.getservices().eotServiceCall(Service_apis.getReferenceList, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
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
                            Type listType = new TypeToken<List<ClientRefrenceModel>>() {
                            }.getType();
                            List<ClientRefrenceModel> data = new Gson().fromJson(convert, listType);
                            addClientRefrenceListInDb(data);
                        } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                            App_preference.getSharedprefInstance().setBlankTokenOnSessionExpire();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callbacksSync.getCallBackOfComplete(FAIL_STATUS, FAIL_MSG);
                    }

                    @Override
                    public void onComplete() {
                        if ((updateindex + updatelimit) <= count) {
                            updateindex += updatelimit;
                            getClientReference();
                        } else {
                            updateindex = 0;
                            count = 0;
                            if (TextUtils.isEmpty(App_preference.getSharedprefInstance().getLoginRes().getToken())) {
                                callbacksSync.getCallBackOfComplete(SESSION_EXPIRE, FAIL_MSG);
                            } else
                                getCustomFrom("3");
                        }
                    }
                });
    }

    private void addClientRefrenceListInDb(List<ClientRefrenceModel> data) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientRefrenceDao().insertClientRefrenceList(data);

    }


}

