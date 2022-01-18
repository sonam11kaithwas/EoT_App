package com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.clientEqu;

public class ClientEquReq {
    private final int limit;
    private final int index;
    private final String search;
    private final String cltId;

    public ClientEquReq(String cltId) {
        this.limit = 100;
        this.index = 0;
        this.search = "";
        this.cltId = cltId;
    }


}
