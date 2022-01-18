package com.eot_app.nav_menu.expense.expense_list.mvp;

import static com.eot_app.utility.AppUtility.getJsonObject;

import androidx.fragment.app.Fragment;

import com.eot_app.nav_menu.expense.expense_list.model.ExpenseReqModel;
import com.eot_app.nav_menu.expense.expense_list.model.ExpenseResModel;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
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

/**
 * Created by Sonam-11 on 6/5/20.
 */
public class ExpenseList_pc implements ExpenseList_PI {
    private final ExpenseList_View expenseListView;
    private final int updatelimit;
    private final List<ExpenseResModel> expenseList;
    private int count;
    private int updateindex;

    public ExpenseList_pc(ExpenseList_View expenseListView) {
        this.expenseListView = expenseListView;
        this.updatelimit = AppConstant.LIMIT_HIGH;
        this.updateindex = 0;
        expenseList = new ArrayList<>();
    }

    @Override
    public void getExpenseList(final String search) {
        if (AppUtility.isInternetConnected()) {
            // AppUtility.progressBarShow(((Fragment)expenseListView).getActivity());
            ExpenseReqModel model = new ExpenseReqModel(
                    updatelimit, updateindex
                    , search);
            String data = new Gson().toJson(model);
            ApiClient.getservices().eotServiceCall(Service_apis.getExpenseList,
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
                                Type typeList = new TypeToken<List<ExpenseResModel>>() {
                                }.getType();
                                List<ExpenseResModel> expense_List = new Gson().fromJson(jsonString, typeList);
                                expenseList.addAll(expense_List);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                expenseListView.onSessionExpire(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {
                                expenseListView.finish();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            AppUtility.progressBarDissMiss();
                            expenseListView.finish();
                        }

                        @Override
                        public void onComplete() {
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                getExpenseList(search);
                            } else {
                                updateindex = 0;
                                count = 0;
                                List<ExpenseResModel> templis = new ArrayList<>();
                                templis.addAll(expenseList);
                                expenseList.clear();

//                                Collections.sort(templis, new Comparator<ExpenseResModel>() {
//                                    @Override
//                                    public int compare(ExpenseResModel o1, ExpenseResModel o2) {
//                                        return o1.getDateTime().compareTo(o2.getDateTime());
//                                    }
//                                });

                                // Collections.sort(templis);
                                expenseListView.setExpenseList(templis);
                            }
                        }
                    });
        } else {
            networkError();
            expenseListView.disableRefersh();
        }
    }

    private void networkError() {
        AppUtility.alertDialog(((Fragment) expenseListView).getActivity(), LanguageController.getInstance().
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
