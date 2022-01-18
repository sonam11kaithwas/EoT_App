package com.eot_app.nav_menu.admin_fw_chat_pkg.chatmember_list_pkg.chatmember_model;

/**
 * Created by Sonam-11 on 3/20/20.
 */
public class UserOflineOnlineModel {
    String gpsStatus;
    String isOnline;
    String userId;

    public UserOflineOnlineModel() {
    }

    public UserOflineOnlineModel(String gpsStatus, String isOnline, String userId) {
        this.gpsStatus = gpsStatus;
        this.isOnline = isOnline;
        this.userId = userId;
    }

    public UserOflineOnlineModel(Object isOnline, Object isOnline1) {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGpsStatus() {
        return gpsStatus;
    }

    public void setGpsStatus(String gpsStatus) {
        this.gpsStatus = gpsStatus;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }
}
