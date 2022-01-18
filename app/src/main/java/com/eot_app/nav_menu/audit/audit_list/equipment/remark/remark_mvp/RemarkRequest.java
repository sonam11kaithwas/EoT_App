package com.eot_app.nav_menu.audit.audit_list.equipment.remark.remark_mvp;

import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.ans_model.Ans_Req;

/**
 * Created by Mahendra Dabi on 12/11/19.
 */
public class RemarkRequest {
    String audId;
    String equId;
    String remark;
    String status;
    String lat;
    String lng;
    String usrId;
    String isJob = "";
    Ans_Req answerArray = new Ans_Req();

    //ppor - 2 ggod -1
    public RemarkRequest() {
    }

    public Ans_Req getAnswerArray() {
        return answerArray;
    }

    public void setAnswerArray(Ans_Req answerArray) {
        this.answerArray = answerArray;
    }

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    public String getIsJob() {
        return isJob;
    }

    public void setIsJob(String isJob) {
        this.isJob = isJob;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAudId() {
        return audId;
    }

    public void setAudId(String audId) {
        this.audId = audId;
    }

    public String getEquId() {
        return equId;
    }

    public void setEquId(String equId) {
        this.equId = equId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
