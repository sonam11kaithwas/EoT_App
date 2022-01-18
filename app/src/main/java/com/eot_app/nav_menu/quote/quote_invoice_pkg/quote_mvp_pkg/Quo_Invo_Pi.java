package com.eot_app.nav_menu.quote.quote_invoice_pkg.quote_mvp_pkg;

import java.util.ArrayList;

public interface Quo_Invo_Pi {
    void getQuotesInvoiceDetails(String quotId);

    void removeQuotesItem(ArrayList<String> rmvItem, String invId);

    void generateQuotPDF(String quotId);

    void convertQuotationToJob(String quotId);
}
