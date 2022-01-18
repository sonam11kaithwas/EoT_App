package com.eot_app.nav_menu.jobs.job_db;

/**
 * Created by aplite_pc302 on 6/1/18.
 */

public class JobListRequestModel {
    private final int usrId;
    private final int limit;
    private final int index;
    private final String dateTime;

    public JobListRequestModel(int usrId, int limit, int index, String dateTime) {
        this.usrId = usrId;
        this.limit = limit;
        this.index = index;
        this.dateTime = dateTime;
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
