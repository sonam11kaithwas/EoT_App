package com.eot_app.nav_menu.userleave_list_pkg;

import com.eot_app.utility.App_preference;

/**
 * Created by Sona-11 on 9/11/21.
 */
public class UserLeaveListReqModel {
    String usrId;

    public UserLeaveListReqModel() {
        this.usrId = App_preference.getSharedprefInstance().getLoginRes().getUsrId();
    }
}
