package com.eot_app.nav_menu.jobs.job_detail.invoice.inventry_pkg;

public class Inventry_ReQ_Model {
    int compId;
    String type;
    int limit;
    int index;
    String search;
    String activeRecord;
    String showInvoice;
    String dateTime;

    public Inventry_ReQ_Model(int compId, String search, int limit, int index, String dateTime) {//
        this.compId = compId;
        this.type = "0";
        this.limit = limit;
        this.index = index;
        this.search = search;
        this.activeRecord = "0";
        this.showInvoice = "0";
        this.dateTime = dateTime;
    }


}
