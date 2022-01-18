package com.eot_app.nav_menu.addleave;

/**
 * Created by Sona-11 on 29/10/21.
 */
public class LeaveReSModel {
    //transVar: {startDateTime: "30-Oct-2021 01:00 AM", startDateTime: "30-Oct-2021 11:59 PM"}
    String startDateTime;
    String endDateTime;

    public LeaveReSModel(String startDateTime, String endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }
}
