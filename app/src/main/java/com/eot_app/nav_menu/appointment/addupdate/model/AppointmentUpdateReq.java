package com.eot_app.nav_menu.appointment.addupdate.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AppointmentUpdateReq {
    List<String> appDoc = new ArrayList<>();
    List<String> fileNames = new ArrayList<>();
    String cltId;
    String siteId = "";
    String conId = "";
    String des;
    String schdlStart;
    String schdlFinish;
    Set<String> memIds;
    String adr;
    String city;
    String state;
    String ctry;
    String zip;
    String appId;


    String email;
    String mob1;
    String nm = "";
    String status;
    String attachCount;

    public String getAttachCount() {
        return attachCount;
    }

    public void setAttachCount(String attachCount) {
        this.attachCount = attachCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getAppDoc() {
        return appDoc;
    }

    public void setAppDoc(List<String> appDoc) {
        this.appDoc = appDoc;
    }

    public String getCltId() {
        return cltId;
    }

    public void setCltId(String cltId) {
        this.cltId = cltId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getConId() {
        return conId;
    }

    public void setConId(String conId) {
        this.conId = conId;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getSchdlStart() {
        return schdlStart;
    }

    public void setSchdlStart(String schdlStart) {
        this.schdlStart = schdlStart;
    }

    public String getSchdlFinish() {
        return schdlFinish;
    }

    public void setSchdlFinish(String schdlFinish) {
        this.schdlFinish = schdlFinish;
    }

    public Set<String> getMemIds() {
        return memIds;
    }

    public void setMemIds(Set<String> memIds) {
        this.memIds = memIds;
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

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMob1() {
        return mob1;
    }

    public void setMob1(String mob1) {
        this.mob1 = mob1;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }
}
