package com.eot_app.nav_menu.jobs.job_detail.job_audit_remark_attchment_pkg.model_pkg;

import com.eot_app.utility.App_preference;

/**
 * Created by Sona-11 on 1/11/21.
 */
public class JobAuditSingleAttchReqModel {
    private final String audId;
    private final String usrId;
    private final String equId;
    private final String setTile;
    private final String setButtontxt;
    String isJob = "";
    private String path;


    public JobAuditSingleAttchReqModel(String audId, String equId, String setButtontxt, String setTile, String path, String isJob) {
        this.audId = audId;
        this.usrId = App_preference.getSharedprefInstance().getLoginRes().getUsrId();
        this.equId = equId;
        this.setTile = setTile;
        this.setButtontxt = setButtontxt;
        this.path = path;
        this.isJob = isJob;
    }

    public String getAudId() {
        return audId;
    }

    public String getUsrId() {
        return usrId;
    }

    public String getEquId() {
        return equId;
    }

    public String getSetTile() {
        return setTile;
    }

    public String getSetButtontxt() {
        return setButtontxt;
    }

    public String getIsJob() {
        return isJob;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
