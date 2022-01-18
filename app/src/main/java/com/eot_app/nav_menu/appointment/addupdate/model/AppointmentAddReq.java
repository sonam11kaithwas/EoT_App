package com.eot_app.nav_menu.appointment.addupdate.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AppointmentAddReq {
    String tempId = "0";
    String cltId;
    String leadId = "";
    String des; // description
    String nm = "";

    String schdlStart;//      shedule start
    String schdlFinish;//     shedule finish


    String email;//email
    String mob1;//  mobile no.1

    String mob2 = "";//     mobile no.2

    String adr;// address
    String city;// city
    String state;// state
    String ctry;//country
    String zip;//zipcode
    Set<String> memIds;//     members id array


    String cnm = "";
    String snm = "";


    String siteId = "";
    String conId = "";
    String clientForFuture = "0";
    String contactForFuture = "";
    String siteForFuture = "";

    List<String> appDoc = new ArrayList<>();
    List<String> fileNames = new ArrayList<>();

    public List<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }

    public String getSnm() {
        return snm;
    }

    public void setSnm(String snm) {
        this.snm = snm;
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

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getContactForFuture() {
        return contactForFuture;
    }

    public void setContactForFuture(String contactForFuture) {
        this.contactForFuture = contactForFuture;
    }

    public String getSiteForFuture() {
        return siteForFuture;
    }

    public void setSiteForFuture(String siteForFuture) {
        this.siteForFuture = siteForFuture;
    }

    public String getCltId() {
        return cltId;
    }

    public void setCltId(String cltId) {
        this.cltId = cltId;
    }

    public String getLeadId() {
        return leadId;
    }

    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getClientForFuture() {
        return clientForFuture;
    }

    public void setClientForFuture(String clientForFuture) {
        this.clientForFuture = clientForFuture;
    }

    public List<String> getAppDoc() {
        return appDoc;
    }

    public void setAppDoc(List<String> appDoc) {
        this.appDoc = appDoc;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
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

    public String getMob2() {
        return mob2;
    }

    public void setMob2(String mob2) {
        this.mob2 = mob2;
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

    public Set<String> getMemIds() {
        return memIds;
    }

    public void setMemIds(Set<String> memIds) {
        this.memIds = memIds;
    }

}
