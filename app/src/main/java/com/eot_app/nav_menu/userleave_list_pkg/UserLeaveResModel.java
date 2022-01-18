package com.eot_app.nav_menu.userleave_list_pkg;

import java.io.Serializable;

/**
 * Created by Sona-11 on 9/11/21.
 */
public class UserLeaveResModel implements Serializable {
    private String leaveId;
    private String reason;
    private String startDateTime;
    private String finishDateTime;
    private String note;
    private String status;

    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getFinishDateTime() {
        return finishDateTime;
    }

    public void setFinishDateTime(String finishDateTime) {
        this.finishDateTime = finishDateTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
