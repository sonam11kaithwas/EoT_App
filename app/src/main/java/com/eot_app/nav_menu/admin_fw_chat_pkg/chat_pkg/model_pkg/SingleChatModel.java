package com.eot_app.nav_menu.admin_fw_chat_pkg.chat_pkg.model_pkg;

import com.eot_app.utility.App_preference;

/**
 * Created by Sonam-11 on 2020-03-09.
 */
public class SingleChatModel {
    private String usrid;
    private String usrnm;
    private String msg;
    private String file;
    private String time;
    private String type;

    public SingleChatModel(String msg, String file, String time, String type) {
        this.usrid = App_preference.getSharedprefInstance().getLoginRes().getUsrId();
        this.usrnm = App_preference.getSharedprefInstance().getLoginRes().getFnm()
                + " " + App_preference.getSharedprefInstance().getLoginRes().getLnm();
        this.msg = msg;
        this.file = file;
        this.time = time;
        this.type = type;
    }

    public SingleChatModel() {
    }

    public String getUsrid() {
        return usrid;
    }

    public void setUsrid(String usrid) {
        this.usrid = usrid;
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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
