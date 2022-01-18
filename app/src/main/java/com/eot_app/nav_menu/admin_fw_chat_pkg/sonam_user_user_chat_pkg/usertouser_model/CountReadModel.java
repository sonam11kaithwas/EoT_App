package com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model;

/**
 * Created by Sonam-11 on 11/4/20.
 */
public class CountReadModel {
    private String lgnusrId;
    private String usrIds;
    private int readCount = 0;

    public CountReadModel(String lgnusrId, String usrIds, int readCount) {
        this.lgnusrId = lgnusrId;
        this.usrIds = usrIds;
        this.readCount = readCount;
    }

    public String getLgnusrId() {
        return lgnusrId;
    }

    public void setLgnusrId(String lgnusrId) {
        this.lgnusrId = lgnusrId;
    }

    public String getUsrIds() {
        return usrIds;
    }

    public void setUsrIds(String usrIds) {
        this.usrIds = usrIds;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }
}
