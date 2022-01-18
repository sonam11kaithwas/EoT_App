package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_model_pkg;

public class TaxList_ReQ_Model {
    private final String compId;
    private final String show_Invoice;
    private final int limit;
    private final int index;
    private final String dateTime;
    private String isactive;


    public TaxList_ReQ_Model(String compId, int limit, int index, String dateTime) {
        this.compId = compId;
        this.limit = limit;
        this.index = index;
        this.show_Invoice = "1";
        this.dateTime = dateTime;
    }
}
