package com.eot_app.nav_menu.audit.addAudit.add_aduit_model_pkg;

import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.ans_model.Answer;
import com.eot_app.utility.settings.setting_db.TagData;

import java.util.ArrayList;
import java.util.Set;

public class AddAudit_Req {
    int type;
    String cltId;
    String nm;
    String siteId;
    String conId;
    String contrId;
    String parentId;
    String des;
    int status;
    String athr;
    String kpr;
    String schdlStart;
    String schdlFinish;
    String inst;
    String cnm;
    String snm;
    String email;
    String mob1;
    String mob2;
    String adr;
    String city;
    String state;
    String ctry;
    String zip;
    Set<String> memIds;
    ArrayList<TagData> tagData;
    String dateTime;
    String lat;
    String lng;
    String landmark;
    String auditType;
    int clientForFuture;
    int siteForFuture;
    ArrayList<Answer> answerArray = new ArrayList<>();
    int contactForFuture;
    private String tempId;

    public AddAudit_Req(int type, String cltId, String nm, String siteId, String conId, String contrId,
                        String parentId, String des, int status, String athr, String kpr, String schdlStart,
                        String schdlFinish, String inst, String cnm, String snm, String email, String mob1,
                        String mob2, String adr, String city, String state, String ctry, String zip,
                        Set<String> memIds, ArrayList<TagData> tagData, String dateTime, String lat, String lng,
                        String landmark, String auditType, String tempId,
                        int clientForFuture, int siteForFuture, int contactForFuture) {
        this.type = type;
        this.cltId = cltId;
        this.nm = nm;
        this.siteId = siteId;
        this.conId = conId;
        this.contrId = contrId;
        this.parentId = parentId;
        this.des = des;
        this.status = status;
        this.athr = athr;
        this.kpr = kpr;
        this.schdlStart = schdlStart;
        this.schdlFinish = schdlFinish;
        this.inst = inst;
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
        this.memIds = memIds;
        this.tagData = tagData;
        this.dateTime = dateTime;
        this.lat = lat;
        this.lng = lng;
        this.landmark = landmark;
        this.auditType = auditType;
        this.tempId = tempId;
        this.clientForFuture = clientForFuture;
        this.siteForFuture = siteForFuture;
        this.contactForFuture = contactForFuture;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCltId() {
        return cltId;
    }

    public void setCltId(String cltId) {
        this.cltId = cltId;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
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

    public String getContrId() {
        return contrId;
    }

    public void setContrId(String contrId) {
        this.contrId = contrId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAthr() {
        return athr;
    }

    public void setAthr(String athr) {
        this.athr = athr;
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

    public String getSchdlFinish() {
        return schdlFinish;
    }

    public void setSchdlFinish(String schdlFinish) {
        this.schdlFinish = schdlFinish;
    }

    public String getInst() {
        return inst;
    }

    public void setInst(String inst) {
        this.inst = inst;
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

    public Set<String> getMemIds() {
        return memIds;
    }

    public void setMemIds(Set<String> memIds) {
        this.memIds = memIds;
    }

    public ArrayList<TagData> getTagData() {
        return tagData;
    }

    public void setTagData(ArrayList<TagData> tagData) {
        this.tagData = tagData;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
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

    public ArrayList<Answer> getAnswerArray() {
        return answerArray;
    }

    public void setAnswerArray(ArrayList<Answer> answerArray) {
        this.answerArray = answerArray;
    }
}
