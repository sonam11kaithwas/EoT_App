package com.eot_app.nav_menu.jobs.job_controller;

import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;

/**
 * Created by Sona-11 on 7/10/21.
 */
public class NotifyWebNewModel {
    private String type;
    private String action;
    private String msg;
    private String id;
    private String usrNm;
    private String usrId;
    private String usrType;
    private String time;
    private String statusId;


    public NotifyWebNewModel(String type, String action, String id, String statusId) {
        this.type = type;
        this.action = action;
        this.id = id;
        this.usrNm = App_preference.getSharedprefInstance().getLoginRes().getUsername();
        this.usrId = App_preference.getSharedprefInstance().getLoginRes().getUsrId();
        this.usrType = "2";
        this.time = AppUtility.getDateByMiliseconds();
        this.statusId = statusId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsrNm() {
        return usrNm;
    }

    public void setUsrNm(String usrNm) {
        this.usrNm = usrNm;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
