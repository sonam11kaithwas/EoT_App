package com.eot_app.nav_menu.quote.quotes_list_pkg.quotes_list_mvp;

import androidx.fragment.app.Fragment;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.nav_menu.quote.quotes_list_pkg.QuotesFilter;
import com.eot_app.nav_menu.quote.quotes_list_pkg.qoute_model_pkg.Quote_ReQ;
import com.eot_app.nav_menu.quote.quotes_list_pkg.qoute_model_pkg.Quote_ReS;
import com.eot_app.services.ApiClient;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;
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

public class QuotesList_Pc implements QuotesList_Pi {
    private final QuotesList_View quotesList_view;
    private final int updatelimit;
    private final List<Quote_ReS> quotesDataList = new ArrayList<>();
    private int count;
    private int updateindex;
    private QuotesFilter quotesFilter;
    private boolean isSearching;

    public QuotesList_Pc(QuotesList_View quotesList_view) {
        this.quotesList_view = quotesList_view;
        this.updatelimit = AppConstant.LIMIT_MID;
    }

    @Override
    public void reuestTogetNewRecords(QuotesFilter quotesFilter) {
        this.quotesDataList.clear();
        if (this.quotesFilter != null) {
            this.quotesFilter = null;
        }
        this.quotesFilter = quotesFilter;
        if (!isSearching)
            getQuotesDataList();
    }

    @Override
    public void getQuotesDataList() {
        if (AppUtility.isInternetConnected()) {
            ActivityLogController.saveActivity(
                    ActivityLogController.QUOTE_MODULE,
                    ActivityLogController.QUOTE_LIST,
                    ActivityLogController.QUOTE_MODULE
            );
            AppUtility.progressBarShow(((Fragment) quotesList_view).getActivity());
            isSearching = true;
            Quote_ReQ quote_reQ = new Quote_ReQ(updateindex, updatelimit);
            quote_reQ.addFilters(quotesFilter);
            String data = new Gson().toJson(quote_reQ);
            ApiClient.getservices().eotServiceCall(Service_apis.getAdminQuoteList, AppUtility.getApiHeaders(), AppUtility.getJsonObject(data))
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
                                Type listType = new TypeToken<List<Quote_ReS>>() {
                                }.getType();
                                List<Quote_ReS> data = new Gson().fromJson(convert, listType);
                                quotesDataList.addAll(data);
                            } else if (jsonObject.get("statusCode") != null && jsonObject.get("statusCode").getAsString().equals(AppConstant.SESSION_EXPIRE)) {
                                quotesList_view.onSessionExpired(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            } else {
                                EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getServerMsgByKey(jsonObject.get("message").getAsString()));
                            }
                        }


                        @Override
                        public void onError(Throwable e) {
                            AppUtility.progressBarDissMiss();
                        }

                        @Override
                        public void onComplete() {
                            AppUtility.progressBarDissMiss();
                            if ((updateindex + updatelimit) <= count) {
                                updateindex += updatelimit;
                                getQuotesDataList();
                            } else {
                                updateindex = 0;
                                count = 0;
                                quotesList_view.setQuoteList(quotesDataList);
                                isSearching = false;
                            }
                        }
                    });
        } else {
            networkError();
            quotesList_view.disableSwipeReferesh();
        }
    }

    private void networkError() {
        AppUtility.alertDialog(((Fragment) quotesList_view).getActivity(), LanguageController.getInstance().
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



