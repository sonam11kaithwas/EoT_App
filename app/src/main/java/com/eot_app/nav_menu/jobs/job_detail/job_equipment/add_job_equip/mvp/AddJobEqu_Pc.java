package com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.mvp;

import static com.eot_app.utility.AppUtility.getJsonObject;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.activitylog.LogModel;
import com.eot_app.nav_menu.audit.audit_list.equipment.model.EquipmentStatus;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.SpinnerCountrySite;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.nav_menu.jobs.job_db.JobListRequestModel;
import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.InvoiceItemDataModel;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.model_pkg.ItembyJobModel;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.clientEqu.ClientEquReq;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.clientEqu.ClientEquRes;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.model_pkg.AddEquReq;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.model_pkg.BrandData;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.model_pkg.GetCatgData;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.model_pkg.GetListModel;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.model_pkg.GetgrpData;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.model.EquipmentStatusReq;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.Country;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.States;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Sonam-11 on 30/9/20.
 */
public class AddJobEqu_Pc implements AddJobEqu_Pi {
    private final AddJobEqu_View addJobEquView;
    private final int updatelimit;
    List<ClientEquRes> clientEquRes;
    String cltId;
    private List<States> statesList;
    private List<Country> countryList;
    private int count;
    private int updateindex;

    public AddJobEqu_Pc(AddJobEqu_View addJobEquView) {
        this.addJobEquView = addJobEquView;
        clientEquRes = new ArrayList<>();
        this.updatelimit = AppConstant.LIMIT_HIGH;
        this.updateindex = 0;
    }


    @Override
    public boolean RequiredFields(String countryId, String stateId, String equNm) {
        if (equNm.equals("")) {
            addJobEquView.setEquReqError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.equp_nm_req));
            return false;
        } else if (equNm.length() < 3) {
            addJobEquView.setEquReqError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.equp_nm_minimun));
            return false;
        }
        if (!isValidCountry(countryId)) {
            addJobEquView.setCountryError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.please_select_country_first));
            return false;
        } else if (!isValidState(stateId)) {
            addJobEquView.setStateError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.state_error));
            return false;
        }

        return true;
    }

    @Override
    public void getClientSiteList(String clientId) {
        addJobEquView.setClientSiteList(AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getSitesByCltId(clientId));
    }


    /****After Convert Item to Equipment Item list Update in Job Dao*/
    public void getClientSiteListServer(String clientId) {
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow((Context) addJobEquView);
            ClientEquReq model = new ClientEquReq(clientId);
            String data = new Gson().toJson(model);
            ApiClient.getservices().eotServiceCall(Service_apis.getClientSiteList,
                    AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
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
                                Type listType = new TypeToken<List<ClientEquRes>>() {
                                }.getType();
                                List<ClientEquRes> list = new Gson().fromJson(convert, listType);
                                clientEquRes.addAll(list);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            AppUtility.progressBarDissMiss();
                        }

                        @Override
                        public void onComplete() {
                            addJobEquView.setClientSiteListServer(clientEquRes);
                            AppUtility.progressBarDissMiss();
                        }
                    });
        }

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
    public void convertItemToequip(final AddEquReq addEquReq, String path) {
        if (AppUtility.isInternetConnected()) {
            RequestBody equnm = null;
            RequestBody brand = null;
            RequestBody mno = null;
            RequestBody sno = null;
            RequestBody expiryDate = null;
            RequestBody manufactureDate = null;
            RequestBody purchaseDate = null;
            RequestBody status = null;
            RequestBody notes = null;
            RequestBody isBarcodeGenerate = null;
            RequestBody state = null;
            RequestBody ctry = null;
            RequestBody adr = null;
            RequestBody city = null;
            RequestBody zip = null;
            RequestBody ecId = null;
            RequestBody type = null;
            RequestBody egId = null;
            RequestBody jobId = null;
            RequestBody cltId = null;
            RequestBody contrId = null;
            RequestBody itemId = null;
            RequestBody supplier = null;
            RequestBody rate = null;
            RequestBody isPart = null;
            RequestBody siteId = null;
            RequestBody invId = null;
            RequestBody extraField1 = null;
            RequestBody extraField2 = null;
            try {
                equnm = RequestBody.create(MultipartBody.FORM, addEquReq.getEqunm());
                brand = RequestBody.create(MultipartBody.FORM, addEquReq.getBrand());
                mno = RequestBody.create(MultipartBody.FORM, addEquReq.getMno());
                sno = RequestBody.create(MultipartBody.FORM, addEquReq.getSno());
                supplier = RequestBody.create(MultipartBody.FORM, addEquReq.getSupplier());
                expiryDate = RequestBody.create(MultipartBody.FORM, addEquReq.getExpiryDate());
                manufactureDate = RequestBody.create(MultipartBody.FORM, addEquReq.getManufactureDate());
                purchaseDate = RequestBody.create(MultipartBody.FORM, addEquReq.getPurchaseDate());
                status = RequestBody.create(MultipartBody.FORM, addEquReq.getStatus());
                notes = RequestBody.create(MultipartBody.FORM, addEquReq.getNotes());
                isBarcodeGenerate = RequestBody.create(MultipartBody.FORM, addEquReq.getIsBarcodeGenerate());
                state = RequestBody.create(MultipartBody.FORM, addEquReq.getState());
                ctry = RequestBody.create(MultipartBody.FORM, addEquReq.getCtry());
                adr = RequestBody.create(MultipartBody.FORM, addEquReq.getAdr());
                city = RequestBody.create(MultipartBody.FORM, addEquReq.getCity());
                zip = RequestBody.create(MultipartBody.FORM, addEquReq.getZip());
                ecId = RequestBody.create(MultipartBody.FORM, addEquReq.getEcId());
                type = RequestBody.create(MultipartBody.FORM, addEquReq.getType());
                egId = RequestBody.create(MultipartBody.FORM, addEquReq.getEgId());
                jobId = RequestBody.create(MultipartBody.FORM, addEquReq.getJobId());
                cltId = RequestBody.create(MultipartBody.FORM, addEquReq.getCltId());
                contrId = RequestBody.create(MultipartBody.FORM, addEquReq.getContrId());
                itemId = RequestBody.create(MultipartBody.FORM, addEquReq.getItemId());
                supplier = RequestBody.create(MultipartBody.FORM, addEquReq.getSupplier());
                rate = RequestBody.create(MultipartBody.FORM, addEquReq.getRate());
                isPart = RequestBody.create(MultipartBody.FORM, addEquReq.getIsPart());
                siteId = RequestBody.create(MultipartBody.FORM, addEquReq.getSiteId());
                invId = RequestBody.create(MultipartBody.FORM, addEquReq.getInvId());
                extraField1 = RequestBody.create(MultipartBody.FORM, addEquReq.getExtraField1());
                extraField2 = RequestBody.create(MultipartBody.FORM, addEquReq.getExtraField2());

            } catch (Exception e) {
                e.printStackTrace();
            }


            String mimeType = "";
            MultipartBody.Part body = null;
            List<MultipartBody.Part> filesList = new ArrayList<>();
            if (!TextUtils.isEmpty(path)) {
                File file1 = new File(path);
                if (file1 != null) {
                    mimeType = URLConnection.guessContentTypeFromName(file1.getName());
                    if (mimeType == null) {
                        mimeType = file1.getName();
                    }
                    RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file1);
                    // MultipartBody.Part is used to send also the actual file name
                    body = MultipartBody.Part.createFormData("image[]", file1.getName() + path.substring(path.lastIndexOf(".")), requestFile);
                    filesList.add(body);
                }
            }


            AppUtility.progressBarShow(((Context) addJobEquView));
            final RequestBody finalJobId = jobId;
            ApiClient.getservices().convertItemToEquipment(AppUtility.getApiHeaders(),
                    filesList, equnm, brand, mno, sno,
                    expiryDate, manufactureDate, purchaseDate,
                    status, notes, isBarcodeGenerate, state
                    , ctry, adr, city, zip, ecId, type,
                    egId, jobId, cltId, contrId, itemId, supplier, rate, isPart, siteId, invId, extraField1, extraField2)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                updateEquipmentCount(addEquReq.getJobId());
                                updateJobItemData(addEquReq.getJobId(), LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                addJobEquView.sessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {
                                addJobEquView.setEquReqError(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                                //addJobEquView.finishActivity();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG : error----", e.getMessage());
                            AppUtility.progressBarDissMiss();
                            addJobEquView.finishActivity();
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                            Log.e("TAG onComplete------", "onComplete");
                        }
                    });
        } else {
            netWork_erroR();
        }
    }

    private void updateEquipmentCount(String jobId) {
        Job job = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(jobId);
        if (job.getEquArray() != null && job.getEquArray().size() == 0) {
            refreshList(jobId);
        }
    }


    /******First time convert Item to equipment Flag refreshing*****/
    public void refreshList(final String jobId) {
        if (AppUtility.isInternetConnected()) {
            LogModel logModel = ActivityLogController
                    .getObj(ActivityLogController.JOB_MODULE, ActivityLogController.JOB_LIST, ActivityLogController.JOB_MODULE);
            ActivityLogController.saveOfflineTable(logModel);

            JobListRequestModel jobListRequestModel = new JobListRequestModel(Integer.parseInt(App_preference.getSharedprefInstance().getLoginRes().getUsrId()),
                    updatelimit, updateindex, App_preference.getSharedprefInstance().getJobSyncTime());

            String data = new Gson().toJson(jobListRequestModel);
            ApiClient.getservices().eotServiceCall(Service_apis.getUserJobList, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
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
                                Type listType = new TypeToken<List<Job>>() {
                                }.getType();
                                List<Job> data = new Gson().fromJson(convert, listType);
                                if (data != null && data.size() > 0) {
                                    updateEquipmentDataInDb(jobId, data);
                                }
                            }
                        }


                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG", e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                refreshList(jobId);
                            } else {
                                if (count != 0) {
                                    App_preference.getSharedprefInstance().setJobSyncTime(AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT));
                                }
                                updateindex = 0;
                                count = 0;
                                AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().deleteJobByIsDelete();

                            }
                        }
                    });
        }
    }

    private void updateEquipmentDataInDb(String jobId, List<Job> data) {
        Job job = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJobsById(jobId);
        /******Notify JOB overView for Equipmetn Added first time ****/
        if (job.getEquArray() != null && job.getEquArray().size() == 0) {
            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().inserJob(data);
            EotApp.getAppinstance().getJobFlagOverView();
        } else {
            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().inserJob(data);

        }
    }


    /****After Convert Item to Equipment Item list Update in Job Dao*/
    private void updateJobItemData(final String jobId, final String msg) {
        if (AppUtility.isInternetConnected()) {
            ItembyJobModel model = new ItembyJobModel(jobId);//, App_preference.getSharedprefInstance().getJobSyncTime()
            String data = new Gson().toJson(model);
            ApiClient.getservices().eotServiceCall(Service_apis.getItemFromJob,
                    AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
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
                                Type listType = new com.google.common.reflect.TypeToken<List<InvoiceItemDataModel>>() {
                                }.getType();
                                List<InvoiceItemDataModel> data = new Gson().fromJson(convert, listType);
                                if (data.size() > 0) {
                                    addgetItemFromJobToDB(data, jobId);
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            addJobEquView.addExpenseSuccesFully(msg);
                        }
                    });
        }

    }

    private void addgetItemFromJobToDB(List<InvoiceItemDataModel> data, String jobId) {
        if (data != null && data.size() > 0) {
            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().updateJobitems(jobId, data);
        }
    }

    @Override
    public void addNewEquipment(AddEquReq addEquReq, String path) {
        if (AppUtility.isInternetConnected()) {
            RequestBody equnm = null;
            RequestBody brand = null;
            RequestBody mno = null;
            RequestBody sno = null;
            RequestBody expiryDate = null;
            RequestBody manufactureDate = null;
            RequestBody purchaseDate = null;
            RequestBody status = null;
            RequestBody notes = null;
            RequestBody isBarcodeGenerate = null;
            RequestBody state = null;
            RequestBody ctry = null;
            RequestBody adr = null;
            RequestBody city = null;
            RequestBody zip = null;
            RequestBody ecId = null;
            RequestBody type = null;
            RequestBody egId = null;
            RequestBody jobId = null;
            RequestBody cltId = null;
            RequestBody contrId = null;
            RequestBody isPart = null;
            RequestBody siteId = null;
            RequestBody extraField1 = null;
            RequestBody extraField2 = null;
            //  RequestBody invId = null;
            try {
                equnm = RequestBody.create(MultipartBody.FORM, addEquReq.getEqunm());
                brand = RequestBody.create(MultipartBody.FORM, addEquReq.getBrand());
                mno = RequestBody.create(MultipartBody.FORM, addEquReq.getMno());
                sno = RequestBody.create(MultipartBody.FORM, addEquReq.getSno());
                RequestBody supplier = RequestBody.create(MultipartBody.FORM, addEquReq.getSupplier());
                expiryDate = RequestBody.create(MultipartBody.FORM, addEquReq.getExpiryDate());
                manufactureDate = RequestBody.create(MultipartBody.FORM, addEquReq.getManufactureDate());
                purchaseDate = RequestBody.create(MultipartBody.FORM, addEquReq.getPurchaseDate());
                status = RequestBody.create(MultipartBody.FORM, addEquReq.getStatus());
                notes = RequestBody.create(MultipartBody.FORM, addEquReq.getNotes());
                isBarcodeGenerate = RequestBody.create(MultipartBody.FORM, addEquReq.getIsBarcodeGenerate());
                state = RequestBody.create(MultipartBody.FORM, addEquReq.getState());
                ctry = RequestBody.create(MultipartBody.FORM, addEquReq.getCtry());
                adr = RequestBody.create(MultipartBody.FORM, addEquReq.getAdr());
                city = RequestBody.create(MultipartBody.FORM, addEquReq.getCity());
                zip = RequestBody.create(MultipartBody.FORM, addEquReq.getZip());
                ecId = RequestBody.create(MultipartBody.FORM, addEquReq.getEcId());
                type = RequestBody.create(MultipartBody.FORM, addEquReq.getType());
                egId = RequestBody.create(MultipartBody.FORM, addEquReq.getEgId());
                jobId = RequestBody.create(MultipartBody.FORM, addEquReq.getJobId());
                cltId = RequestBody.create(MultipartBody.FORM, addEquReq.getCltId());
                contrId = RequestBody.create(MultipartBody.FORM, addEquReq.getContrId());
                isPart = RequestBody.create(MultipartBody.FORM, addEquReq.getIsPart());
                siteId = RequestBody.create(MultipartBody.FORM, addEquReq.getSiteId());
                extraField1 = RequestBody.create(MultipartBody.FORM, addEquReq.getExtraField1());
                extraField2 = RequestBody.create(MultipartBody.FORM, addEquReq.getExtraField2());
                // invId = RequestBody.create(MultipartBody.FORM, addEquReq.getInvId());
            } catch (Exception e) {
                e.printStackTrace();
            }


            String mimeType = "";
            MultipartBody.Part body = null;
            List<MultipartBody.Part> filesList = new ArrayList<>();
            if (!TextUtils.isEmpty(path)) {
                File file1 = new File(path);
                if (file1 != null) {
                    mimeType = URLConnection.guessContentTypeFromName(file1.getName());
                    if (mimeType == null) {
                        mimeType = file1.getName();
                    }
                    RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file1);
                    // MultipartBody.Part is used to send also the actual file name
                    body = MultipartBody.Part.createFormData("image[]", file1.getName() + path.substring(path.lastIndexOf(".")), requestFile);
                    filesList.add(body);
                }
            }


            AppUtility.progressBarShow(((Context) addJobEquView));
            ApiClient.getservices().addEquipment(AppUtility.getApiHeaders(),
                    filesList, equnm, brand, mno, sno,
                    expiryDate, manufactureDate, purchaseDate,
                    status, notes, isBarcodeGenerate, state
                    , ctry, adr, city, zip, ecId, type,
                    egId, jobId, cltId, contrId, isPart, siteId, extraField1, extraField2)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                addJobEquView.addExpenseSuccesFully(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                addJobEquView.sessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {
                                addJobEquView.setEquReqError(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG : error----", e.getMessage());
                            AppUtility.progressBarDissMiss();
                            addJobEquView.setEquReqError(e.getMessage());
                            addJobEquView.finishActivity();
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                            Log.e("TAG onComplete------", "onComplete");
                        }
                    });
        } else {
            netWork_erroR();
        }
    }


    @Override
    public void getCountryList() {
        countryList = SpinnerCountrySite.clientCountryList();
        addJobEquView.setCountryList(countryList);
    }

    private void netWork_erroR() {
        AppUtility.alertDialog(((Context) addJobEquView), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }

    @Override
    public void getStateList(String countyId) {
        statesList = SpinnerCountrySite.clientStatesList(countyId);
        addJobEquView.setStateList(statesList);
    }

    @Override
    public void getCageryList() {
        if (AppUtility.isInternetConnected()) {
            GetListModel model = new GetListModel();
            String data = new Gson().toJson(model);
            ApiClient.getservices().eotServiceCall(Service_apis.getEquCategoryList, AppUtility.getApiHeaders(), getJsonObject(data))
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
                                Type listType = new TypeToken<List<GetCatgData>>() {
                                }.getType();
                                List<GetCatgData> cateList = new Gson().fromJson(convert, listType);
                                addJobEquView.setCategList(cateList);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("", "");
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        } else {
            AppUtility.alertDialog(((Context) addJobEquView), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return null;
                }
            });
        }
    }

    @Override
    public void getGrpList() {
        if (AppUtility.isInternetConnected()) {
            GetListModel model = new GetListModel();
            String data = new Gson().toJson(model);
            ApiClient.getservices().eotServiceCall(Service_apis.getEquGroupList, AppUtility.getApiHeaders(), getJsonObject(data))
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
                                Type listType = new TypeToken<List<GetgrpData>>() {
                                }.getType();
                                List<GetgrpData> cateList = new Gson().fromJson(convert, listType);
                                addJobEquView.setGrpList(cateList);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        } else {
            AppUtility.alertDialog(((Context) addJobEquView), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return null;
                }
            });
        }
    }

    @Override
    public void getBrandList() {
        if (AppUtility.isInternetConnected()) {
            GetListModel model = new GetListModel();
            String data = new Gson().toJson(model);
            ApiClient.getservices().eotServiceCall(Service_apis.getBrandList, AppUtility.getApiHeaders(), getJsonObject(data))
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
                                Type listType = new TypeToken<List<BrandData>>() {
                                }.getType();
                                List<BrandData> brandList = new Gson().fromJson(convert, listType);
                                addJobEquView.setBrandList(brandList);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        } else {
            AppUtility.alertDialog(((Context) addJobEquView), LanguageController.getInstance().getMobileMsgByKey(AppConstant.dialog_alert), LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_check_network), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return null;
                }
            });
        }
    }


    @Override
    public void getEquStatusList() {
        if (AppUtility.isInternetConnected()) {
            EquipmentStatusReq equipmentListReq = new EquipmentStatusReq();
            String data = new Gson().toJson(equipmentListReq);
            ApiClient.getservices().eotServiceCall(Service_apis.getEquipmentStatus, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
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
                                String convert = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                Type listType = new TypeToken<List<EquipmentStatus>>() {
                                }.getType();
                                List<EquipmentStatus> equipmentStatusList = new Gson().fromJson(convert, listType);
                                addJobEquView.setEquStatusList(equipmentStatusList);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        }

    }


}
