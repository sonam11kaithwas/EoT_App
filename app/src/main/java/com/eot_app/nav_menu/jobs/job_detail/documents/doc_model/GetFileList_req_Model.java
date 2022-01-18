package com.eot_app.nav_menu.jobs.job_detail.documents.doc_model;

/**
 * Created by ubuntu on 9/10/18.
 */

public class GetFileList_req_Model {
    int index;
    int limit;
    String jobId;
    String usrId;
    String type;

    public GetFileList_req_Model(int index, int limit, String jobId, String usrId, String type) {
        this.index = index;
        this.limit = limit;
        this.jobId = jobId;
        this.usrId = usrId;
        this.type = type;
    }
}
