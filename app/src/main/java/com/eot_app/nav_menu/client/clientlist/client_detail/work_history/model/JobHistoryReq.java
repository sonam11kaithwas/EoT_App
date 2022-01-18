package com.eot_app.nav_menu.client.clientlist.client_detail.work_history.model;

public class JobHistoryReq {

    private final int limit;
    private final int index;
    private final String search = "";
    private final String searchType = "0";
    private final String srtType = "asc";
    private final String dtf = "";
    private final String dtt = "";
    private final String jtId = "";
    private final String cltId;
    private final String isFilterBy = "1";
    private final String apiCallFrom = "1";

    public JobHistoryReq(String cltId, int index, int limit) {
        this.index = index;
        this.limit = limit;
        this.cltId = cltId;
    }


}
