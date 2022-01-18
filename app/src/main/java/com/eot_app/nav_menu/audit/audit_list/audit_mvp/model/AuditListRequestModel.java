package com.eot_app.nav_menu.audit.audit_list.audit_mvp.model;

/**
 * Created by aplite_pc302 on 6/1/18.
 */

public class AuditListRequestModel {
    private final int usrId;
    private final int limit;
    private final int index;
    private final String dateTime;
    /*issue in ios for backward app to fix*/
    private final int isCallFromCurrent = 1;
    private String search;

    public AuditListRequestModel(int usrId, int limit, int index, String dateTime) {
        this.usrId = usrId;
        this.limit = limit;
        this.index = index;
        this.dateTime = dateTime;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public int getUsrId() {
        return usrId;
    }

    public int getLimit() {
        return limit;
    }

    public int getIndex() {
        return index;
    }

    public String getDateTime() {
        return dateTime;
    }
}
