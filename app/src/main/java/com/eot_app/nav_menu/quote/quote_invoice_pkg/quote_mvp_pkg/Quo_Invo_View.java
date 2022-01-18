package com.eot_app.nav_menu.quote.quote_invoice_pkg.quote_mvp_pkg;

import com.eot_app.nav_menu.quote.quote_invoice_pkg.quote_model_pkg.Quote_invoice_Details_Res;
import com.eot_app.nav_menu.quote.quote_invoice_pkg.quote_model_pkg.QuotesDetails;

public interface Quo_Invo_View {
    void setInvoiceDetails(Quote_invoice_Details_Res setInvoiceDetails);

    void dismissPullTorefresh();

    void onSessionExpire(String msg);

    void setQuotesDetails(QuotesDetails quotesDetails);

    void onGetPdfPath(String asString);

    void onConvertQuotationToJob(String message);

    //void InvoiceNotFound(String msg);
    void itemdeletedSuccefully();

}
