package com.eot_app.nav_menu.usr_time_sheet_pkg.timeshet_model;

import com.eot_app.utility.App_preference;

/**
 * Created by Sona-11 on 20/8/21.
 */

public class TimeSheetReqModel {
    private final String usrId;
    private final String dtf;
    private final String dtt;

    public TimeSheetReqModel(String dtf, String dtt) {
        this.usrId = App_preference.getSharedprefInstance().getLoginRes().getUsrId();
        this.dtf = dtf + " 00:00:00";
        this.dtt = dtt + " 23:59:59";
    }
}
