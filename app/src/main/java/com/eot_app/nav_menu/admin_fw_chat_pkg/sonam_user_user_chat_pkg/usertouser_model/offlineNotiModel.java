package com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sonam-11 on 27/4/20.
 */
public class offlineNotiModel implements Serializable {
    private NotModel msg;
    private String notyType;
    private ArrayList<String> usrId;

    public offlineNotiModel() {
    }

    public offlineNotiModel(String msgModel, String docurl, ArrayList<String> userId) {
        this.msg = new NotModel(msgModel, docurl);
        this.notyType = "one2one";
        this.usrId = userId;
    }


    public NotModel getMsg() {
        return msg;
    }

    public void setMsg(NotModel msg) {
        this.msg = msg;
    }

    public String getNotyType() {
        return notyType;
    }

    public void setNotyType(String notyType) {
        this.notyType = notyType;
    }

    public ArrayList<String> getUsrId() {
        return usrId;
    }

    public void setUsrId(ArrayList<String> usrId) {
        this.usrId = usrId;
    }

}

