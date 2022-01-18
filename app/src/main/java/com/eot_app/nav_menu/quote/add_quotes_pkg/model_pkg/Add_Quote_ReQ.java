package com.eot_app.nav_menu.quote.add_quotes_pkg.model_pkg;

import java.util.Set;

public class Add_Quote_ReQ {

    private String leadId;
    private String appId;
    private String cltId;
    private String siteId;
    private String conId;
    private String status;
    private String invDate;
    private String dueDate;
    private String nm;
    private String cnm;
    private String snm;
    private String email;
    private String mob1;
    private String mob2;
    private String adr;
    private String city;
    private String state;
    private String ctry;
    private String zip;
    private int clientForFuture;
    private int siteForFuture;
    private int contactForFuture;
    private Set<String> jtId;
    private String des;
    private String inst;
    private String athr;
    private String note;
    private String assignByUser;
    private String quotId;
    private String invId;
    private String term;
    private String lat;
    private String lng;
    private String isMailSentToClt;

    public Add_Quote_ReQ(Set<String> jtId, String cltId, String siteId, String conId, String des, String athr, String inst, String nm, String cnm, String snm, String email, String mob1, String mob2, String adr, String city, String state, String ctry, String zip, int clientForFuture, int contactForFuture, int siteForFuture, String invDate, String dueDate, String note, String assignByUser, String quotId, String invId,
                         String term, String status, String lat, String lng) {
        this.cltId = cltId;
        this.siteId = siteId;
        this.conId = conId;
        this.invDate = invDate;
        this.dueDate = dueDate;
        this.nm = nm;
        this.cnm = cnm;
        this.snm = snm;
        this.email = email;
        this.mob1 = mob1;
        this.mob2 = mob2;
        this.adr = adr;
        this.city = city;
        this.state = state;
        this.ctry = ctry;
        this.zip = zip;
        this.clientForFuture = clientForFuture;
        this.siteForFuture = siteForFuture;
        this.contactForFuture = contactForFuture;
        this.jtId = jtId;
        this.des = des;
        this.inst = inst;
        this.athr = athr;
        this.status = status;
        this.note = note;
        this.assignByUser = assignByUser;
        this.quotId = quotId;
        this.invId = invId;
        this.term = term;
        this.lat = lat;
        this.lng = lng;
    }

    public String getIsMailSentToClt() {
        return isMailSentToClt;
    }

    public void setIsMailSentToClt(String isMailSentToClt) {
        this.isMailSentToClt = isMailSentToClt;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getLeadId() {
        return leadId;
    }

    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInvDate() {
        return invDate;
    }

    public void setInvDate(String invDate) {
        this.invDate = invDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getCnm() {
        return cnm;
    }

    public void setCnm(String cnm) {
        this.cnm = cnm;
    }

    public String getSnm() {
        return snm;
    }

    public void setSnm(String snm) {
        this.snm = snm;
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

    public int getClientForFuture() {
        return clientForFuture;
    }

    public void setClientForFuture(int clientForFuture) {
        this.clientForFuture = clientForFuture;
    }

    public int getSiteForFuture() {
        return siteForFuture;
    }

    public void setSiteForFuture(int siteForFuture) {
        this.siteForFuture = siteForFuture;
    }

    public int getContactForFuture() {
        return contactForFuture;
    }

    public void setContactForFuture(int contactForFuture) {
        this.contactForFuture = contactForFuture;
    }

    public Set<String> getJtId() {
        return jtId;
    }

    public void setJtId(Set<String> jtId) {
        this.jtId = jtId;
    }

    public String getInst() {
        return inst;
    }

    public void setInst(String inst) {
        this.inst = inst;
    }

    public String getAthr() {
        return athr;
    }

    public void setAthr(String athr) {
        this.athr = athr;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAssignByUser() {
        return assignByUser;
    }

    public void setAssignByUser(String assignByUser) {
        this.assignByUser = assignByUser;
    }

    public String getQuotId() {
        return quotId;
    }

    public void setQuotId(String quotId) {
        this.quotId = quotId;
    }

    public String getInvId() {
        return invId;
    }

    public void setInvId(String invId) {
        this.invId = invId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
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
