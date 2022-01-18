package com.eot_app.nav_menu.jobs.job_detail.detail.jobdetial_model;

import java.io.Serializable;

/**
 * Created by aplite_pc302 on 6/25/18.
 */

public class Jobdetail_status_res implements Serializable {
    String jobId;
    String usrId;
    String type;
    String status;
    String dateTime;
    String device_Type;
    String lat;
    String lng;
    String isMailSentToClt;

    public Jobdetail_status_res(String jobId, String usrId, String type, String status, String dateTime, String lat, String lng
            , String isMailSentToClt
    ) {
        this.jobId = jobId;
        this.usrId = usrId;
        this.type = type;
        this.status = status;
        this.dateTime = dateTime;
        this.device_Type = "1";
        this.lat = lat;
        this.lng = lng;
        this.isMailSentToClt = isMailSentToClt;
    }

    public String getIsMailSentToClt() {
        return isMailSentToClt;
    }

    public void setIsMailSentToClt(String isMailSentToClt) {
        this.isMailSentToClt = isMailSentToClt;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDevice_Type() {
        return device_Type;
    }

    public void setDevice_Type(String device_Type) {
        this.device_Type = device_Type;
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
}
