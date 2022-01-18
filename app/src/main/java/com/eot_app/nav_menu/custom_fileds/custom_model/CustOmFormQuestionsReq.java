package com.eot_app.nav_menu.custom_fileds.custom_model;

/**
 * Created by Sonam-11 on 16/9/20.
 */
public class CustOmFormQuestionsReq {
    private String ansId = "-1";
    private String frmId;
    private String usrId;
    private String jobId;
    private String audId;

    public CustOmFormQuestionsReq(String frmId, String jobId) {
        this.frmId = frmId;
        this.jobId = jobId;
    }

    public CustOmFormQuestionsReq(String audId) {
        this.audId = audId;
    }

    public String getAudId() {
        return audId;
    }

    public void setAudId(String audId) {
        this.audId = audId;
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

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
}
