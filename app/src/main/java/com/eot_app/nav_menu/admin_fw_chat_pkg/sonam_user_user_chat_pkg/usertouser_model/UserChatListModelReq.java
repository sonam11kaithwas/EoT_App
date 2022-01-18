package com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model;

/**
 * Created by Sonam-11 on 13/4/20.
 */
public class UserChatListModelReq {
    private int usrId;
    private int limit;
    private int index;
    private String dateTime;
    private String isIncludingTeam;

    public UserChatListModelReq(int usrId, int limit, int index, String dateTime) {
        this.usrId = usrId;
        this.limit = limit;
        this.index = index;
        this.dateTime = dateTime;
        this.isIncludingTeam = "1";
    }

    public String getIsIncludingTeam() {
        return isIncludingTeam;
    }

    public void setIsIncludingTeam(String isIncludingTeam) {
        this.isIncludingTeam = isIncludingTeam;
    }

    public int getUsrId() {
        return usrId;
    }

    public void setUsrId(int usrId) {
        this.usrId = usrId;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
