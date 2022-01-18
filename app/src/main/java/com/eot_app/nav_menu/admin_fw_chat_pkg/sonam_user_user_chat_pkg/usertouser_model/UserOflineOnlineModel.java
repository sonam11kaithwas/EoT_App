package com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model;

import java.io.Serializable;

/**
 * Created by Sonam-11 on 3/20/20.
 */
public class UserOflineOnlineModel implements Serializable {
    private String isOnline = "";
    private String isInactive = "";

    public UserOflineOnlineModel() {
    }

    public UserOflineOnlineModel(String isOnline, String isInactive) {
        this.isOnline = isOnline;
        this.isInactive = isInactive;
    }


    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getIsInactive() {
        return isInactive;
    }

    public void setIsInactive(String isInactive) {
        this.isInactive = isInactive;
    }
}
