package com.eot_app.nav_menu.quote.add_quotes_pkg.add_quote_mvp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.SpinnerCountrySite;
import com.eot_app.nav_menu.quote.add_quotes_pkg.model_pkg.Add_Quote_ReQ;
import com.eot_app.nav_menu.quote.quotes_list_pkg.qoute_model_pkg.Quote_ReQ;
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
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.setting_db.FieldWorker;
import com.eot_app.utility.settings.setting_db.JobTitle;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hypertrack.hyperlog.HyperLog;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Add_Quote_Pc implements Add_Quote_Pi {
    private final Add_Quote_View add_quote_view;
    private List<States> statesList = new ArrayList<>();
    private List<Country> countryList = new ArrayList<>();

    public Add_Quote_Pc(Add_Quote_View add_quote_view) {
        this.add_quote_view = add_quote_view;
    }

    @Override
    public void getActiveUserList() {
        List<FieldWorker> fwDataList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).fieldWorkerModel().getFieldWorkerlist();
        //   add_quote_view.setActiveUserList(fwDataList);//only set active fieldworkers
        add_quote_view.setfwListForQuotes(fwDataList);
    }


    @Override
    public boolean requiredFileds(Set<String> jobType, String cltId, String adr, String countryId, String stateId, String mob, String alterNateMob, String email) {
//        if (jobType.isEmpty()) {
//            add_quote_view.setquoteTypeError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_job_title));
//            return false;
//        } else
        if (cltId.equals("")) {
            add_quote_view.setClientNameError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_client_name));
            return false;
        } else if (adr.equals("")) {
            add_quote_view.setAddr_Error(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_addr));
            return false;
        } else if (!isValidCountry(countryId)) {
            add_quote_view.setCountryError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.please_select_country_first));
            return false;
        } else if (!isValidState(stateId)) {
            add_quote_view.setStateError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.state_error));
            return false;
        } else if (!mob.isEmpty() && !mob.equalsIgnoreCase(App_preference.getSharedprefInstance().getLoginRes().getCtryCode()) && mob.length() < AppConstant.MOBILE_LIMIT) {
            add_quote_view.setMobError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_mob_lent));
            return false;
        } else if (!alterNateMob.isEmpty() && !mob.equalsIgnoreCase(App_preference.getSharedprefInstance().getLoginRes().getCtryCode()) && alterNateMob.length() < AppConstant.MOBILE_LIMIT) {
            add_quote_view.setMobError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_alter_mob_lent));
            return false;
        } else if (!email.isEmpty() && (!Eot_Validation.email_checker(email).equals(""))) {
            add_quote_view.setEmailError(Eot_Validation.email_checker(email));
            return false;
        }
        return true;
    }

    @Override
    public boolean isValidCountry(String countryId) {//country
        for (Country ctry : countryList) {
            if (ctry.getId().equalsIgnoreCase(countryId))
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
    public void getJobServices() {
        List<JobTitle> data = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobTitleModel().getJobTitlelist();
        add_quote_view.setJobServiceslist(data);
    }

    @Override
    public void getClientList() {
        List<Client> data = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientModel().getActiveClientList();
        add_quote_view.setClientList(data);
    }


    @Override
    public void getCountryList() {
        countryList = SpinnerCountrySite.clientCountryList();// readCountryJsonFile("countries.json");
        add_quote_view.setCountryList(countryList);
    }


    @Override
    public void getStateList(String countyId) {
        statesList = SpinnerCountrySite.clientStatesList(countyId);//readStateJsonFile("states.json", countyId);
        add_quote_view.setStateList(statesList);
    }


    @Override
    public void getContactList(String cltId) {
        add_quote_view.setContactList(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().
                getContactFromcltId(cltId));
    }

    @Override
    public void getSilteList(String cltId) {
        add_quote_view.setSiteList(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getSitesFromCltId(Integer.parseInt(cltId)));
    }

    @Override
    public void addQuotes(Add_Quote_ReQ requestModel) {
        if (AppUtility.isInternetConnected()) {
            HyperLog.i("", "addQuotes PC(M) start");
            ActivityLogController.saveActivity(
                    ActivityLogController.QUOTE_MODULE,
                    ActivityLogController.QUOTE_ADD,
                    ActivityLogController.QUOTE_MODULE
            );
            AppUtility.progressBarShow((Context) add_quote_view);
            String data = new Gson().toJson(requestModel);

            ApiClient.getservices().eotServiceCall(Service_apis.addQuotationForMobile, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            HyperLog.i("", "Quotation:" + jsonObject.toString());
                            if (jsonObject.get("success").getAsBoolean()) {
                                EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                try {
                                    if (jsonObject != null && jsonObject.has("data")
                                            && jsonObject.get("data").isJsonObject()) {
                                        JsonObject data = jsonObject.getAsJsonObject("data");
                                        String quotId = data.get("quotId").getAsString();
                                        String label = data.get("label").getAsString();
                                        add_quote_view.onAddNewQuotes(quotId, label);
                                    } else add_quote_view.finishActivity();
                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
//                                add_quote_view.onSessionExpire(LanguageController.getInstance().
//                                        getServerMsgByKey(jsonObject.getAsJsonObject().get("message").toString()));
                                EotApp.getAppinstance().sessionExpired();
                            } else {
                                add_quote_view.errorMsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }

                        }


                        @Override
                        public void onError(Throwable e) {
                            AppUtility.progressBarDissMiss();
                            add_quote_view.finishActivity();
                            HyperLog.i("", e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                            Log.e("TAG", "");
                        }
                    });
        } else {
            networkError();
        }

    }

    @Override
    public void getTermsConditions() {
        if (AppUtility.isInternetConnected()) {
            ActivityLogController.saveActivity(
                    ActivityLogController.QUOTE_MODULE,
                    ActivityLogController.QUOTE_TERM,
                    ActivityLogController.QUOTE_MODULE
            );
            AppUtility.progressBarShow((Context) add_quote_view);
            String data = new Gson().toJson(new Quote_ReQ(1, 1));


            ApiClient.getservices().eotServiceCall(Service_apis.getTermsCondition, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                if (jsonObject.has("data")) {
                                    String termsConditions = jsonObject.getAsJsonObject("data").get("quotTerms").getAsString();
                                    add_quote_view.setTermsConditions(termsConditions);
                                    //  EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                }
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
//                                add_quote_view.onSessionExpire(LanguageController.getInstance().
//                                        getServerMsgByKey(jsonObject.getAsJsonObject().get("message").toString()));
                                EotApp.getAppinstance().sessionExpired();
                            } else {
                                //  add_quote_view.errorMsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }

                        }


                        @Override
                        public void onError(Throwable e) {
                            AppUtility.progressBarDissMiss();
                            Log.e("TAG", e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                            Log.e("TAG", "");
                        }
                    });
        } else {
            networkError();
        }

    }

    @Override
    public void addQuoteWithDocuments(Add_Quote_ReQ add_quote_reQ, ArrayList links, List<String> fileNames) {
        if (AppUtility.isInternetConnected()) {

            RequestBody leadId = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getLeadId() == null ? "" : add_quote_reQ.getLeadId());
            RequestBody appId = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getAppId() == null ? "" : add_quote_reQ.getAppId());//parent id
            RequestBody cltId = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getCltId());//contrId
            RequestBody siteId = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getSiteId());
            RequestBody conId = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getConId());
            RequestBody status = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getStatus());
            RequestBody invDate = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getInvDate());
            RequestBody dueDate = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getDueDate());
            RequestBody newcnrb = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getNm());
            RequestBody new_con_nmrb = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getCnm() + "");//new_con_nm
            RequestBody newsiterb = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getSnm());
            RequestBody emailrb = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getEmail());
            RequestBody mobilerb = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getMob1());
            RequestBody atmobrb = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getMob2());
            RequestBody adrrb = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getAdr());
            RequestBody citrb = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getCity());
            RequestBody ctryrb = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getCtry());//ctry_id
            RequestBody staterb = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getState());//state_id
            RequestBody postcoderb = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getZip());
            RequestBody clientForFuturerb = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getClientForFuture() + "");
            RequestBody siteforfuturerb = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getSiteForFuture() + "");
            RequestBody contactforfuturerb = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getContactForFuture() + "");

            List<MultipartBody.Part> jatIds = new ArrayList<>();
            if (jatIds != null)
                for (String s : add_quote_reQ.getJtId())
                    jatIds.add(MultipartBody.Part.createFormData("jtId[]", s));


            // RequestBody jtId = RequestBody.create(MultipartBody.FORM, String.valueOf(add_quote_reQ.getJtId()));
            RequestBody des = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getDes());
            RequestBody inst = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getInst());
            RequestBody athr = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getAthr());
            RequestBody note = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getNote());
            RequestBody assignByUser = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getAssignByUser());
            RequestBody quotId = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getQuotId());
            RequestBody invId = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getInvId());
            RequestBody term = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getTerm());
            RequestBody latrb = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getLat());
            RequestBody lngrb = RequestBody.create(MultipartBody.FORM, add_quote_reQ.getLng());

            String mimeType = "";
            MultipartBody.Part body = null;
            List<MultipartBody.Part> fileList = new ArrayList<>();
            for (int i = 0; i < links.size(); i++) {
                File file1 = new File((String) links.get(i));
                String s = fileNames.get(i);
                if (file1 != null) {
                    mimeType = URLConnection.guessContentTypeFromName(file1.getName());
                    if (mimeType == null) {
                        mimeType = s;
                    }
                    RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file1);
                    // MultipartBody.Part is used to send also the actual file name
                    body = MultipartBody.Part.createFormData("qa[]", s, requestFile);
                    fileList.add(body);
                }
            }


            AppUtility.progressBarShow((Activity) add_quote_view);
            ApiClient.getservices().addQuoteWithDocuments(AppUtility.getApiHeaders(),
                    leadId,
                    appId,
                    cltId,
                    siteId,
                    conId,
                    status,
                    invDate,
                    dueDate,
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
                    jatIds,
                    des,
                    inst,
                    athr,
                    note,
                    assignByUser,
                    quotId,
                    invId,
                    term,
                    latrb,
                    lngrb,
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
                                add_quote_view.finishActivity();
                                //refresh recent job on appointment details and show the label of recent job with code
//                                EotApp.getAppinstance().notifyApiObserver(Service_apis.addJob);
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

    private void networkError() {
        AppUtility.alertDialog(((Context) add_quote_view), LanguageController.getInstance().
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
