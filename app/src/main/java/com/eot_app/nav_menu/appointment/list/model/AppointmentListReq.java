package com.eot_app.nav_menu.appointment.list.model;

public class AppointmentListReq {
    private final int usrId;
    private final int limit;
    private final int index;
    private final String dateTime;
    private String search;

    public AppointmentListReq(int usrId, int limit, int index, String dateTime) {
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
