package com.eot_app.registration_form.company_model_pkg;

public class CompanyDefaultSetting {

    private String city;
    private String duration;
    private String state;
    private String ctry;
    private String cur;
    private String timezone;
    private String lngId;
    private String localId;
    private String mob;

    public CompanyDefaultSetting(String city, String duration, String state, String ctry, String cur, String timezone,
                                 String lngId, String localId, String mob) {
        this.city = city;
        this.duration = duration;
        this.state = state;
        this.ctry = ctry;
        this.cur = cur;
        this.timezone = timezone;
        this.lngId = lngId;
        this.localId = localId;
        this.mob = mob;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCtry() {
        return ctry;
    }

    public void setCtry(String ctry) {
        this.ctry = ctry;
    }

    public String getCur() {
        return cur;
    }

    public void setCur(String cur) {
        this.cur = cur;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLngId() {
        return lngId;
    }

    public void setLngId(String lngId) {
        this.lngId = lngId;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }
}
