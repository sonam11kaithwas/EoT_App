package com.eot_app.nav_menu.client.clientlist.client_detail.work_history.model;

public class AduitAppointmentHistoryReq {
    private final int index;
    private final int limit;
    private final String search = "";
    private final String searchType = "0";
    private final String srtType = "asc";
    private final String dtf = "";
    private final String dtt = "";
    private final String cltId;

    public AduitAppointmentHistoryReq(String cltId, int index, int limit) {
        this.cltId = cltId;
        this.index = index;
        this.limit = limit;
    }
}
