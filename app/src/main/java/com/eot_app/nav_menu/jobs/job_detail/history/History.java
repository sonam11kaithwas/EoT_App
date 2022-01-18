package com.eot_app.nav_menu.jobs.job_detail.history;

public class History {
    private String name;
    private String status;
    private String time;
    private String referenceby;
    private String referencebyType;
    private String referencebyName;

    public String getReferencebyType() {
        return referencebyType;
    }

    public void setReferencebyType(String referencebyType) {
        this.referencebyType = referencebyType;
    }

    public String getReferencebyName() {
        return referencebyName;
    }

    public void setReferencebyName(String referencebyName) {
        this.referencebyName = referencebyName;
    }

    public String getReferenceby() {
        return referenceby;
    }

    public void setReferenceby(String referenceby) {
        this.referenceby = referenceby;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
