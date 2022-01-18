package com.eot_app.nav_menu.admin_fw_chat_pkg.chatmember_list_pkg.chatmember_model;

/**
 * Created by Sonam-11 on 2020-03-07.
 */
public class ChatUserListResModel {
    private String usrId;
    private String fnm;
    private String lnm;
    private String img;
    private String userStatus = "0";

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    public String getFnm() {
        return fnm;
    }

    public void setFnm(String fnm) {
        this.fnm = fnm;
    }

    public String getLnm() {
        return lnm;
    }

    public void setLnm(String lnm) {
        this.lnm = lnm;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
