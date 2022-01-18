package com.eot_app.nav_menu.jobs.job_detail.documents.doc_model;

public class DocUpdateRequest {
    private final String jaId;
    private final String des;
    private String isAddAttachAsCompletionNote;

    public DocUpdateRequest(String jaId, String des, String isAddAttachAsCompletionNote) {
        this.jaId = jaId;
        this.des = des;
        this.isAddAttachAsCompletionNote = isAddAttachAsCompletionNote;
    }

    public DocUpdateRequest(String jaId, String des) {
        this.jaId = jaId;
        this.des = des;
    }

    public String getJaId() {
        return jaId;
    }

    public String getDes() {
        return des;
    }

    public String getIsAddAttachAsCompletionNote() {
        return isAddAttachAsCompletionNote;
    }
}
