package com.eot_app.nav_menu.appointment.details.documents;

import java.util.List;

/**
 * Created by Mahendra Dabi on 04-08-2020.
 */
public class DocumentExportReq {
    private final int moduleType = 2;
    List<String> documentId;
    private String jobId;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public List<String> getDocumentId() {
        return documentId;
    }

    public void setDocumentId(List<String> documentId) {
        this.documentId = documentId;
    }
}
