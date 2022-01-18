package com.eot_app.nav_menu.jobs.job_detail.history;

public class History_Request_model {
    String compId;
    String usrId;
    int limit;
    int index;
    String jobId;

    public History_Request_model(String compId, String usrId, int limit, int index, String jobId) {
        this.compId = compId;
        this.usrId = usrId;
        this.limit = limit;
        this.index = index;
        this.jobId = jobId;
    }
}

