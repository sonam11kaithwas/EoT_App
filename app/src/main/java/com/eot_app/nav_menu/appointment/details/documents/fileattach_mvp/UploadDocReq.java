package com.eot_app.nav_menu.appointment.details.documents.fileattach_mvp;

public class UploadDocReq {
    int index;
    int limit;
    String appId;

    public UploadDocReq(int index, int limit, String appId) {
        this.index = index;
        this.limit = limit;
        this.appId = appId;
    }
}
