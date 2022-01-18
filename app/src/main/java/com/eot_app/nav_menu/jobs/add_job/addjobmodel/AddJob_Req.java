package com.eot_app.nav_menu.jobs.add_job.addjobmodel;

import com.eot_app.nav_menu.jobs.add_job.add_job_recr.daily_recr_pkg.daily_recur_model.JobRecurModel;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.ans_model.Answer;
import com.eot_app.utility.settings.setting_db.TagData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by ubuntu on 9/7/18.
 */

public class AddJob_Req {
    String tempId;
    int parentId;
    String compId;
    String cltId;
    String siteId;
    String conId;
    String quotId;
    Set<String> jtId;
    String des;
    int type;
    String prty;
    String status;
    String athr;
    String kpr;
    String schdlStart;
    String schdlFinish;
    String inst;
    String nm;
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
    int clientForFuture;
    int siteForFuture;
    int contactForFuture;
    ArrayList<TagData> tagData;
    String dateTime;
    String lat;
    String lng;
    String landmark;
    String contrId;
    String isRecur;
    //add param to create sub job of contract under contract
    String appId;
    ArrayList<Answer> answerArray;
    ArrayList<JobRecurModel> recurData = new ArrayList<>();
    private String recurType;
    private List<LinkedHashMap<String, Boolean>> selectedDays = new ArrayList<>();
    private String isMailSentToClt = "0";

    /********This for ReVisit JOB*******/
    public AddJob_Req(String tempId, int parentId, String contrId, String compId,
                      String quotId, Set<String> jtId, String des, String prty, String inst,
                      String cltId, String new_cnm, int clientForFuture,
                      String siteId, String new_st_nm, int siteForFuture,
                      String conId, String new_con_nm, int contactForFuture,

                      String mob_no, String mob2, String email, String adderes, String city
            , String cntry, String state, String post_code, int type, Set<String> memIds, String schdlStart, String schdlFinish,
                      ArrayList<TagData> tagData, String athr, String kpr, String dateTime,
                      String lat, String lng, String landmark, String appId) {
        this.tempId = tempId;
        this.parentId = parentId;
        this.contrId = contrId;
        this.compId = compId;
        this.cltId = cltId;
        this.siteId = siteId;
        this.conId = conId;
        this.quotId = quotId;
        this.jtId = jtId;
        this.des = des;
        this.inst = inst;
        this.prty = prty;
        this.mob1 = mob_no;
        this.mob2 = mob2;
        this.email = email;
        this.adr = adderes;
        this.city = city;
        this.ctry = cntry;
        this.state = state;
        this.zip = post_code;
        this.type = type;
        this.schdlStart = schdlStart;
        this.schdlFinish = schdlFinish;
        this.status = "1";
        this.nm = new_cnm;//new client name
        this.snm = new_st_nm;//new site name
        this.cnm = new_con_nm;//new contact
        this.clientForFuture = clientForFuture;
        this.siteForFuture = siteForFuture;
        this.contactForFuture = contactForFuture;
        this.tagData = tagData;
        this.athr = athr;
        this.kpr = kpr;
        this.memIds = memIds;
        this.dateTime = dateTime;
        this.lat = lat;
        this.lng = lng;
        this.landmark = landmark;
        this.appId = appId;

    }

    public AddJob_Req(String tempId, int parentId, String contrId, String compId,
                      String quotId, Set<String> jtId, String des, String prty, String inst,
                      String cltId, String new_cnm, int clientForFuture,
                      String siteId, String new_st_nm, int siteForFuture,
                      String conId, String new_con_nm, int contactForFuture,

                      String mob_no, String mob2, String email, String adderes, String city
            , String cntry, String state, String post_code, int type, Set<String> memIds, String schdlStart, String schdlFinish,
                      ArrayList<TagData> tagData, String athr, String kpr, String dateTime,
                      String lat, String lng, String landmark, String appId, ArrayList<JobRecurModel> recurData,
                      String recurType, List<LinkedHashMap<String, Boolean>> selectedDays, String isRecur
                      //        ,String isMailSentToClt
    ) {
        this.tempId = tempId;
        this.parentId = parentId;
        this.contrId = contrId;
        this.compId = compId;
        this.cltId = cltId;
        this.siteId = siteId;
        this.conId = conId;
        this.quotId = quotId;
        this.jtId = jtId;
        this.des = des;
        this.inst = inst;
        this.prty = prty;
        this.mob1 = mob_no;
        this.mob2 = mob2;
        this.email = email;
        this.adr = adderes;
        this.city = city;
        this.ctry = cntry;
        this.state = state;
        this.zip = post_code;
        this.type = type;
        this.schdlStart = schdlStart;
        this.schdlFinish = schdlFinish;
        this.status = "1";
        this.nm = new_cnm;//new client name
        this.snm = new_st_nm;//new site name
        this.cnm = new_con_nm;//new contact
        this.clientForFuture = clientForFuture;
        this.siteForFuture = siteForFuture;
        this.contactForFuture = contactForFuture;
        this.tagData = tagData;
        this.athr = athr;
        this.kpr = kpr;
        this.memIds = memIds;
        this.dateTime = dateTime;
        this.lat = lat;
        this.lng = lng;
        this.landmark = landmark;
        this.appId = appId;
        this.recurData = recurData;
        this.recurType = recurType;
        this.selectedDays = selectedDays;
        this.isRecur = isRecur;
        //    this.isMailSentToClt=isMailSentToClt;
    }

    public List<LinkedHashMap<String, Boolean>> getSelectedDays() {
        return selectedDays;
    }

    public void setSelectedDays(List<LinkedHashMap<String, Boolean>> selectedDays) {
        this.selectedDays = selectedDays;
    }

    public ArrayList<Answer> getAnswerArrayList() {
        return answerArray;
    }

    public void setAnswerArrayList(ArrayList<Answer> answerArrayList) {
        this.answerArray = answerArrayList;
    }

    public String getIsMailSentToClt() {
        return isMailSentToClt;
    }

    public void setIsMailSentToClt(String isMailSentToClt) {
        this.isMailSentToClt = isMailSentToClt;
    }

    public String getAppId() {
        return appId;
    }

    public String getContrId() {
        return contrId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public String getPrty() {
        return prty;
    }

    public void setPrty(String prty) {
        this.prty = prty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSchdlStart() {
        return schdlStart;
    }

    public void setSchdlStart(String schdlStart) {
        this.schdlStart = schdlStart;
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

    public Set<String> getMemIds() {
        return memIds;
    }

    public void setMemIds(Set<String> memIds) {
        this.memIds = memIds;
    }

    public String getCltId() {
        return cltId;
    }

    public void setCltId(String cltId) {
        this.cltId = cltId;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
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

    public String getQuotId() {
        return quotId;
    }

    public void setQuotId(String quotId) {
        this.quotId = quotId;
    }

    public Set<String> getJtId() {
        return jtId;
    }

    public void setJtId(Set<String> jtId) {
        this.jtId = jtId;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public ArrayList<JobRecurModel> getRecurData() {
        return recurData;
    }

    public void setRecurData(ArrayList<JobRecurModel> recurData) {
        this.recurData = recurData;
    }

    public String getRecurType() {
        return recurType;
    }

    public void setRecurType(String recurType) {
        this.recurType = recurType;
    }

    public String getIsRecur() {
        return isRecur;
    }

    public void setIsRecur(String isRecur) {
        this.isRecur = isRecur;
    }
}
