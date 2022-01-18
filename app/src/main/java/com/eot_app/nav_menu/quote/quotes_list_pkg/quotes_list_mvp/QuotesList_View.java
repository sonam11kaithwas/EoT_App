package com.eot_app.nav_menu.quote.quotes_list_pkg.quotes_list_mvp;

import com.eot_app.nav_menu.quote.quotes_list_pkg.QuotesFilter;
import com.eot_app.nav_menu.quote.quotes_list_pkg.qoute_model_pkg.Quote_ReS;

import java.util.List;

public interface QuotesList_View {
    void setQuoteList(List<Quote_ReS> quoteList);

    void refereshQuotesList();

    void chipAdd(QuotesFilter quotesFilter);

    void setSearchVisibility(boolean b);


    void onSessionExpired(String message);

    void disableSwipeReferesh();

}
