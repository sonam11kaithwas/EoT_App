package com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model;

import java.io.Serializable;

/**
 * Created by Sona-11 on 24/12/21.
 */
public class TeamMemIdModel implements Serializable {
    private String usrNm;
    private String usrId;
    /***** usrType--- 1 for Fw & 2 for Admin *********/
    private String usrType;

    public TeamMemIdModel() {
    }

    public String getUsrNm() {
        return usrNm;
    }

    public void setUsrNm(String usrNm) {
        this.usrNm = usrNm;
    }

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    public String getUsrType() {
        return usrType;
    }

    public void setUsrType(String usrType) {
        this.usrType = usrType;
    }
}
