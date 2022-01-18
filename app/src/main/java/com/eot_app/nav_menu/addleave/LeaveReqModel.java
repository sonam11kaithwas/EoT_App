package com.eot_app.nav_menu.addleave;

import com.eot_app.utility.App_preference;

import java.util.ArrayList;

/**
 * Created by Sona-11 on 28/10/21.
 */
public class LeaveReqModel {
    private final String reason;
    private final String note;
    private final String startDateTime;
    private final String finishDateTime;
    private final ArrayList<String> usrId = new ArrayList<>();

    public LeaveReqModel(String reason, String note, String startDateTime, String finishDateTime) {
        this.usrId.add(App_preference.getSharedprefInstance().getLoginRes().getUsrId());
        this.reason = reason;
        this.note = note;
        this.startDateTime = startDateTime;
        this.finishDateTime = finishDateTime;
    }
}
