package com.eot_app.nav_menu.client.clientlist.client_detail.work_history.model;

public class AppointmentAttachmentReq {
    private final Integer limit = 10;
    private final Integer index = 0;
    private final String usrId = "";
    private final String appId;

    public AppointmentAttachmentReq(String appId) {
        this.appId = appId;
    }
}
