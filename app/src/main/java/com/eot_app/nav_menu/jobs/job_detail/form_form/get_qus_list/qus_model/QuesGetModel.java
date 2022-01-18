package com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model;

/**
 * Created by ubuntu on 18/9/18.
 */

public class QuesGetModel {
    String ansId;
    String frmId;
    String usrId;
    String jobId;
    String equId;

    public QuesGetModel(String ansId, String frmId, String usrId, String jobId) {
        this.ansId = ansId;
        this.frmId = frmId;
        this.usrId = usrId;
        this.jobId = jobId;
    }

    public QuesGetModel(String ansId, String frmId, String usrId, String jobId, String equId) {
        this.ansId = ansId;
        this.frmId = frmId;
        this.usrId = usrId;
        this.jobId = jobId;
        this.equId = equId;
    }

    public String getEquId() {
        return equId;
    }

    public void setEquId(String equId) {
        this.equId = equId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getAnsId() {
        return ansId;
    }

    public void setAnsId(String ansId) {
        this.ansId = ansId;
    }

    public String getFrmId() {
        return frmId;
    }

    public void setFrmId(String frmId) {
        this.frmId = frmId;
    }

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }
}
