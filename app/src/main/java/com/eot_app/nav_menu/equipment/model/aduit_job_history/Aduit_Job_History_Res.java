package com.eot_app.nav_menu.equipment.model.aduit_job_history;

import java.util.List;

public class Aduit_Job_History_Res {
    private String audId;
    private String label;
    private String type;
    private String status;
    private String nm;
    private String adr;
    private String kpr;
    private String schdlStart;
    private String schdlStartTime;
    private String schdlFinish;
    private String schdlFinishTime;
    private String isRemark;
    private String datetime;
    private String remarkStatus;
    private Object statusText;
    private String remark;
    private String location;
    private String contrid;
    private String moduleId;
    private List<Auditor> auditor = null;
    private List<Object> attachments = null;


    public String getAudId() {
        return audId;
    }

    public void setAudId(String audId) {
        this.audId = audId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getAdr() {
        return adr;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    public String getKpr() {
        return kpr;
    }

    public void setKpr(String kpr) {
        this.kpr = kpr;
    }

    public String getSchdlStart() {
        return schdlStart;
    }

    public void setSchdlStart(String schdlStart) {
        this.schdlStart = schdlStart;
    }

    public String getSchdlStartTime() {
        return schdlStartTime;
    }

    public void setSchdlStartTime(String schdlStartTime) {
        this.schdlStartTime = schdlStartTime;
    }

    public String getSchdlFinish() {
        return schdlFinish;
    }

    public void setSchdlFinish(String schdlFinish) {
        this.schdlFinish = schdlFinish;
    }

    public String getSchdlFinishTime() {
        return schdlFinishTime;
    }

    public void setSchdlFinishTime(String schdlFinishTime) {
        this.schdlFinishTime = schdlFinishTime;
    }

    public String getIsRemark() {
        return isRemark;
    }

    public void setIsRemark(String isRemark) {
        this.isRemark = isRemark;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getRemarkStatus() {
        return remarkStatus;
    }

    public void setRemarkStatus(String remarkStatus) {
        this.remarkStatus = remarkStatus;
    }

    public Object getStatusText() {
        return statusText;
    }

    public void setStatusText(Object statusText) {
        this.statusText = statusText;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContrid() {
        return contrid;
    }

    public void setContrid(String contrid) {
        this.contrid = contrid;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public List<Auditor> getAuditor() {
        return auditor;
    }

    public void setAuditor(List<Auditor> auditor) {
        this.auditor = auditor;
    }

    public List<Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Object> attachments) {
        this.attachments = attachments;
    }


}
