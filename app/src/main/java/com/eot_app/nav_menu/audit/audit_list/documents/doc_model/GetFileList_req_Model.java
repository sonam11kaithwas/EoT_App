package com.eot_app.nav_menu.audit.audit_list.documents.doc_model;

/**
 * Created by ubuntu on 9/10/18.
 */

public class GetFileList_req_Model {
    int index;
    int limit;
    String jobId;

    public GetFileList_req_Model(int index, int limit, String jobId) {
        this.index = index;
        this.limit = limit;
        this.jobId = jobId;
    }
}
