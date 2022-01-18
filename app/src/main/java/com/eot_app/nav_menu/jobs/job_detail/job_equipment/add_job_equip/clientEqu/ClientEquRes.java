package com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.clientEqu;

public class ClientEquRes {
    private String adr;
    private String city;
    private String cltId;
    private String cnm;
    private String conId;
    private String ctry;
    private String def;
    private String isdelete;
    private String lat;
    private String lng;
    private String siteId;
    private String snm;
    private String state;
    private String zip;


    public ClientEquRes(String adr, String city, String cltId, String cnm, String conId, String ctry, String def, String isdelete, String lat, String lng, String siteId, String snm, String state, String zip) {
        this.adr = adr;
        this.city = city;
        this.cltId = cltId;
        this.cnm = cnm;
        this.conId = conId;
        this.ctry = ctry;
        this.def = def;
        this.isdelete = isdelete;
        this.lat = lat;
        this.lng = lng;
        this.siteId = siteId;
        this.snm = snm;
        this.state = state;
        this.zip = zip;
    }

    public String getAdr() {
        return adr;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCltId() {
        return cltId;
    }

    public void setCltId(String cltId) {
        this.cltId = cltId;
    }

    public String getCnm() {
        return cnm;
    }

    public void setCnm(String cnm) {
        this.cnm = cnm;
    }

    public String getConId() {
        return conId;
    }

    public void setConId(String conId) {
        this.conId = conId;
    }

    public String getCtry() {
        return ctry;
    }

    public void setCtry(String ctry) {
        this.ctry = ctry;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
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

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSnm() {
        return snm;
    }

    public void setSnm(String snm) {
        this.snm = snm;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
