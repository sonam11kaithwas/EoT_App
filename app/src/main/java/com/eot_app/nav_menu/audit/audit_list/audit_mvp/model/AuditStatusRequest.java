package com.eot_app.nav_menu.audit.audit_list.audit_mvp.model;

/**
 * Created by Mahendra Dabi on 12/11/19.
 */
public class AuditStatusRequest {
    String audId;
    String usrId;
    String type;
    String status;
    String dateTime;
    String device_Type;
    String lat;
    String lng;


    public String getAudId() {
        return audId;
    }

    public void setAudId(String audId) {
        this.audId = audId;
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
