package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.location_tax_dao;

/**
 * Created by Sona-11 on 23/8/21.
 */
public class TaxReqModel {
    String dateTime;
    int limit;
    int index;

    public TaxReqModel(int limit, int index, String dateTime) {
        this.dateTime = dateTime;
        this.limit = limit;
        this.index = index;
    }

    public TaxReqModel(String dateTime) {
        this.dateTime = dateTime;
    }
}
