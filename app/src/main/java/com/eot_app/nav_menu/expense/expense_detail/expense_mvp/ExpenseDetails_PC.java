package com.eot_app.nav_menu.expense.expense_detail.expense_mvp;

import static com.eot_app.utility.AppUtility.getJsonObject;

import android.content.Context;

import com.eot_app.nav_menu.expense.expense_detail.expense_detail_model.ExpenseReQ;
import com.eot_app.nav_menu.expense.expense_detail.expense_detail_model.ExpenseRes;
import com.eot_app.nav_menu.expense.expense_detail.expense_history.ExpenseStatushistoryModel;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Sonam-11 on 7/5/20.
 */
public class ExpenseDetails_PC implements ExpenseDetails_PI {
    private final ExpenseDetails_View details_view;

    public ExpenseDetails_PC(ExpenseDetails_View details_view) {
        this.details_view = details_view;
    }

    @Override
    public void getExpenseDetails(final String expId) {
        if (AppUtility.isInternetConnected()) {
            AppUtility.progressBarShow((Context) details_view);
            ExpenseReQ model = new ExpenseReQ(
                    expId);
            String data = new Gson().toJson(model);
            ApiClient.getservices().eotServiceCall(Service_apis.getExpenseDetail,
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
                                String jsonStr = jsonObject.get("data").toString();
                                Gson gson = new Gson();
                                ExpenseRes expenseDetails = gson.fromJson(jsonStr, ExpenseRes.class);
                                details_view.setExpenseDetails(expenseDetails);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                details_view.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {
                                details_view.finishActivity();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            AppUtility.progressBarDissMiss();
                            details_view.finishActivity();
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                            getExpensehistory(expId);
                        }
                    });
        } else {

        }
    }

    @Override
    public void getExpensehistory(final String expId) {
        ExpenseReQ model = new ExpenseReQ(
                expId);
        String data = new Gson().toJson(model);
        ApiClient.getservices().eotServiceCall(Service_apis.getExpenseStatus,
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
                            String jsonStr = jsonObject.get("data").toString();
                            Type typeList = new TypeToken<List<ExpenseStatushistoryModel>>() {
                            }.getType();
                            List<ExpenseStatushistoryModel> expenseStatsuList = new Gson().fromJson(jsonStr, typeList);
                            details_view.setgetExpensehistory(expenseStatsuList);
                        } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                            details_view.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                        } else {
                            details_view.finishActivity();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        details_view.finishActivity();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
