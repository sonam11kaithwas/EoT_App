package com.eot_app.nav_menu.expense.add_expense.add_expense_mvp;

import static com.eot_app.utility.AppUtility.getJsonObject;

import android.content.Context;
import android.util.Log;

import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.expense.add_expense.add_expense_model.AddExpenseReq;
import com.eot_app.nav_menu.expense.add_expense.add_expense_model.RemoveImageView;
import com.eot_app.nav_menu.expense.add_expense.add_expense_model.UpdateExpenseReq;
import com.eot_app.nav_menu.expense.add_expense.category_tag.CategoryModel;
import com.eot_app.nav_menu.expense.add_expense.category_tag.TagModel;
import com.eot_app.nav_menu.expense.expense_detail.expense_detail_model.ExpenseRes;
import com.eot_app.nav_menu.expense.expense_list.model.ExpenseReqModel;
import com.eot_app.nav_menu.jobs.job_db.Job;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Sonam-11 on 7/5/20.
 */
public class AddExpense_PC implements AddExpense_PI {
    private final AddExpense_View addExpenseView;
    private final int updatelimit;
    List<CategoryModel> categoryModelList;
    List<TagModel> expensetagList;
    private int count;
    private int updateindex;

    public AddExpense_PC(AddExpense_View addExpenseView) {
        this.addExpenseView = addExpenseView;
        this.updatelimit = AppConstant.LIMIT_HIGH;
        this.updateindex = 0;
        categoryModelList = new ArrayList<>();
        expensetagList = new ArrayList<>();
    }

    @Override
    public void getClientList() {
        List<Client> data = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientModel().getActiveClientListInDescOrdr();
        addExpenseView.setClientList(data);
    }

    @Override
    public void getJobServices() {
        List<Job> jobservice = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobModel().getJoblistByJobLable();
        //jobTitleModel().getJobTitlelist();
        addExpenseView.setJobServiceList(jobservice);
    }

    @Override
    public boolean validExpenseName(String exNm, String exAmount) {
        if (exNm.equals("")) {
            addExpenseView.errorDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_nm_required));
            return false;
        } else if (exAmount.equals("")) {
            addExpenseView.errorDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.expense_amount_required));
            return false;
        }

        //job_validation
        return true;
    }

    @Override
    public boolean jobValidation(String jobId) {
        if (jobId.equals("") || jobId.equals("0")) {
            addExpenseView.errorDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.job_validation));
            return false;
        }
        return true;
    }

    @Override
    public boolean clientValidation(String clientId) {
        if (clientId.equals("") || clientId.equals("0")) {
            addExpenseView.errorDialog(LanguageController.getInstance().getMobileMsgByKey(AppConstant.client_validation));
            return false;
        }
        return true;
    }


    @Override
    public void removeExpanceImage(String id) {
        if (AppUtility.isInternetConnected()) {
            RemoveImageView model = new RemoveImageView(id);
            String data = new Gson().toJson(model);
            ApiClient.getservices().eotServiceCall(Service_apis.deleteExpenseReceipt,
                    AppUtility.getApiHeaders(), getJsonObject(data))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                addExpenseView.imgRemoveSuccessfully();
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                addExpenseView.sessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {

                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("", "");
                            //  addExpenseView.finishActivity();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            netWork_erroR();
        }
    }

    @Override
    public void getExpenseTagList() {
        if (AppUtility.isInternetConnected()) {
            ExpenseReqModel model = new ExpenseReqModel(
                    updatelimit, updateindex
                    , "");
            String data = new Gson().toJson(model);
            ApiClient.getservices().eotServiceCall(Service_apis.getExpenseTagList,
                    AppUtility.getApiHeaders(), getJsonObject(data))
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
                                String jsonString = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                Type typeList = new TypeToken<List<TagModel>>() {
                                }.getType();
                                List<TagModel> list = new Gson().fromJson(jsonString, typeList);
                                expensetagList.addAll(list);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                addExpenseView.sessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            addExpenseView.finishActivity();
                        }

                        @Override
                        public void onComplete() {
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                getExpenseTagList();
                            } else {
                                updateindex = 0;
                                count = 0;
                                getJobServices();
                                getClientList();
                                addExpenseView.setExpenseTagList(expensetagList);
                            }
                        }
                    });
        } else {

        }
    }

    @Override
    public void getCategoryList() {
        if (AppUtility.isInternetConnected()) {
            ExpenseReqModel model = new ExpenseReqModel(
                    updatelimit, updateindex
                    , "");
            String data = new Gson().toJson(model);
            ApiClient.getservices().eotServiceCall(Service_apis.getCategoryList,
                    AppUtility.getApiHeaders(), getJsonObject(data))
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
                                String jsonString = new Gson().toJson(jsonObject.get("data").getAsJsonArray());
                                Type typeList = new TypeToken<List<CategoryModel>>() {
                                }.getType();
                                List<CategoryModel> list = new Gson().fromJson(jsonString, typeList);
                                categoryModelList.addAll(list);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                addExpenseView.sessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            addExpenseView.finishActivity();
                        }

                        @Override
                        public void onComplete() {
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                getCategoryList();
                            } else {
                                updateindex = 0;
                                count = 0;
                                getExpenseTagList();
                                //  addExpenseView.setCategoryList(categoryModelList);
                                addExpenseView.setCategorySpinnerList(categoryModelList);
                            }
                        }
                    });
        } else {
            netWork_erroR();
        }
    }

    @Override
    public void addExpense(AddExpenseReq addExpenseReq) {
        RequestBody jobId = RequestBody.create(MultipartBody.FORM, addExpenseReq.getJobId());
        RequestBody cltId = RequestBody.create(MultipartBody.FORM, addExpenseReq.getCltId());
        RequestBody usrId = RequestBody.create(MultipartBody.FORM, addExpenseReq.getUsrId());
        RequestBody name = RequestBody.create(MultipartBody.FORM, addExpenseReq.getName());
        RequestBody amt = RequestBody.create(MultipartBody.FORM, addExpenseReq.getAmt());
        RequestBody dateTime = RequestBody.create(MultipartBody.FORM, addExpenseReq.getDateTime());
        RequestBody category = RequestBody.create(MultipartBody.FORM, addExpenseReq.getCategory());
        RequestBody tag = RequestBody.create(MultipartBody.FORM, addExpenseReq.getTag());
        RequestBody status = RequestBody.create(MultipartBody.FORM, addExpenseReq.getStatus());
        RequestBody des = RequestBody.create(MultipartBody.FORM, addExpenseReq.getDes());
        RequestBody comment = RequestBody.create(MultipartBody.FORM, addExpenseReq.getComment());


        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow(((Context) addExpenseView));
            ApiClient.getservices().addExpense(AppUtility.getApiHeaders(),
                    addExpenseReq.getReceipt(), jobId, cltId, usrId, name, amt, dateTime, category, tag, status, des
                    , comment)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                addExpenseView.addExpenseSuccesFully();
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                addExpenseView.sessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {

                                addExpenseView.msg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG : error----", e.getMessage());
                            AppUtility.progressBarDissMiss();
                            addExpenseView.finishActivity();

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
    public void updateExpense(UpdateExpenseReq addExpenseReq) {
        RequestBody jobId = RequestBody.create(MultipartBody.FORM, addExpenseReq.getJobId());
        RequestBody expId = RequestBody.create(MultipartBody.FORM, addExpenseReq.getExpId());
        RequestBody cltId = RequestBody.create(MultipartBody.FORM, addExpenseReq.getCltId());
        RequestBody usrId = RequestBody.create(MultipartBody.FORM, addExpenseReq.getUsrId());
        RequestBody name = RequestBody.create(MultipartBody.FORM, addExpenseReq.getName());
        RequestBody amt = RequestBody.create(MultipartBody.FORM, addExpenseReq.getAmt());
        RequestBody dateTime = RequestBody.create(MultipartBody.FORM, addExpenseReq.getDateTime());
        RequestBody category = RequestBody.create(MultipartBody.FORM, addExpenseReq.getCategory());
        RequestBody tag = RequestBody.create(MultipartBody.FORM, addExpenseReq.getTag());
        RequestBody status = RequestBody.create(MultipartBody.FORM, addExpenseReq.getStatus());
        RequestBody des = RequestBody.create(MultipartBody.FORM, addExpenseReq.getDes());
        RequestBody comment = RequestBody.create(MultipartBody.FORM, addExpenseReq.getComment());


        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow(((Context) addExpenseView));
            ApiClient.getservices().updateExpense2(AppUtility.getApiHeaders(),
                    jobId, cltId, usrId, name, amt, dateTime, category, tag, status, des
                    , comment, expId, addExpenseReq.getReceipt())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(JsonObject jsonObject) {
                            if (jsonObject.get("success").getAsBoolean()) {
                                String convert = new Gson().toJson(jsonObject.get("data"));
                                ExpenseRes model = new Gson().fromJson(convert, ExpenseRes.class);
                                addExpenseView.updateExpenseDetails(model);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                addExpenseView.sessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {
                                addExpenseView.msg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TAG : error----", e.getMessage());
                            AppUtility.progressBarDissMiss();
                            addExpenseView.finishActivity();
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

    private void netWork_erroR() {
        AppUtility.alertDialog(((Context) addExpenseView), LanguageController.getInstance().
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
