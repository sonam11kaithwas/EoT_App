package com.eot_app.time_shift_pkg;

/**
 * Created by Sona-11 on 15/11/21.
 */

public class ShiftTimeReqModel {
    private String dateTime;
    private int limit;
    private int index;

    public ShiftTimeReqModel(String dateTime, int limit, int index) {
        this.dateTime = dateTime;
        this.limit = limit;
        this.index = index;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
