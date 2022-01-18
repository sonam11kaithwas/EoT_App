package com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model;

import com.eot_app.utility.App_preference;

import java.io.Serializable;

/**
 * Created by Sonam-11 on 27/4/20.
 */
public class NotModel implements Serializable {
    private String usrnm;
    private String msg;
    private String senderid;
    private String msgUrl;

    public NotModel(String subtitle, String msgUrl) {
        this.usrnm = App_preference.getSharedprefInstance().getLoginRes().getUsername();
        this.msg = subtitle;
        this.senderid = App_preference.getSharedprefInstance().getLoginRes().getUsrId();
        this.msgUrl = msgUrl;
    }

    public NotModel() {
    }

    public String getMsgUrl() {
        return msgUrl;
    }

    public void setMsgUrl(String msgUrl) {
        this.msgUrl = msgUrl;
    }

    public String getUsrnm() {
        return usrnm;
    }

    public void setUsrnm(String usrnm) {
        this.usrnm = usrnm;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }
}
