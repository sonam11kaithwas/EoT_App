package com.eot_app.nav_menu.quote.quotes_list_pkg.quotes_list_mvp;

import com.eot_app.nav_menu.quote.quotes_list_pkg.QuotesFilter;

public interface QuotesList_Pi {

    void getQuotesDataList();

    void reuestTogetNewRecords(QuotesFilter quotesFilter);
}
