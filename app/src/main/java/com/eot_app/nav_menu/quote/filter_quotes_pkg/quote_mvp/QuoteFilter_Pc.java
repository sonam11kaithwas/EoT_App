package com.eot_app.nav_menu.quote.filter_quotes_pkg.quote_mvp;

import android.content.Context;

import com.eot_app.nav_menu.quote.filter_quotes_pkg.quote_filter_model.QuoteFilter_State_Model;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.language_support.LanguageController;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class QuoteFilter_Pc implements QuoteFilter_Pi {
    private final QuoteFilter_View quoteFilterView;

    public QuoteFilter_Pc(QuoteFilter_View quoteFilterView) {
        this.quoteFilterView = quoteFilterView;
    }

    @Override
    public void getQuotesStatesList() {
        final ArrayList<QuoteFilter_State_Model> quoteStatusList = new ArrayList<>();
        quoteStatusList.add(new QuoteFilter_State_Model(AppConstant.QuoteNew, AppConstant.status_new));
        quoteStatusList.add(new QuoteFilter_State_Model((AppConstant.QuoteAproved), AppConstant.approved));
        quoteStatusList.add(new QuoteFilter_State_Model(AppConstant.QuoteReject, AppConstant.status_reje));
        quoteStatusList.add(new QuoteFilter_State_Model(AppConstant.QuoteOnHold, AppConstant.status_onhold));
        quoteFilterView.setQuoteSStatesList(quoteStatusList);
    }

    @Override
    public void emptyFilterListDialog() {
        AppUtility.alertDialog(((Context) quoteFilterView), "", LanguageController.getInstance().getMobileMsgByKey(AppConstant.no_filter), LanguageController.getInstance().getMobileMsgByKey(AppConstant.ok), "", new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }
        });
    }
}
