package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model;

public class Inv_List_Req_Model {
    private final String jobId;
    private final String invId = "";
    private String isCallFromBackward;
    //  private int isAdmin;

    public Inv_List_Req_Model(String jobId, int isAdmin) {
        this.jobId = jobId;
        isCallFromBackward = "0";
        //    this.isAdmin = isAdmin;
    }

    public Inv_List_Req_Model(String jobId) {
        this.jobId = jobId;
    }
}
