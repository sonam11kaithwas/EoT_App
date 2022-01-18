package com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb;

import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.ans_model.Answer;

import java.util.ArrayList;

/**
 * Created by ubuntu on 23/6/18.
 */

public class EditSiteModel {
    String lat;
    String lng;
    private String cltId;
    private String siteId;
    private String snm;
    private String adr;
    private String city;
    private int state;
    private int ctry;
    private String zip;
    private String def;
    private String isAddContact;
    private String conId;
    private String cnm;
    private String email;
    private String mob1;
    private String mob2;
    private String fax;
    private String twitter;
    private String skype;
    private ArrayList<Answer> answerArray = new ArrayList<>();


    public EditSiteModel(String cltId, String siteId, String snm, String adr, String city, int state,
                         int ctry, String zip, String def, String lat, String lng
            , String isAddContact, String conId, String cnm, String email, String mob1, String mob2, String fax
            , String skype, String twitter, ArrayList<Answer> answerArray) {
        this.cltId = cltId;
        this.siteId = siteId;
        this.snm = snm;
        this.adr = adr;
        this.city = city;
        this.state = state;
        this.ctry = ctry;
        this.zip = zip;
        this.def = def;
        this.lat = lat;
        this.lng = lng;
        this.isAddContact = isAddContact;
        this.conId = conId;
        this.cnm = cnm;
        this.email = email;
        this.mob1 = mob1;
        this.mob2 = mob2;
        this.fax = fax;
        this.skype = skype;
        this.twitter = twitter;
        this.answerArray = answerArray;
    }

   /* public EditSiteModel(String cltId, String siteId, String snm, String adr, String city, int state, int ctry,
                         String zip, String def, String lat, String lng
            , String isAddContact, String conId, String cnm, Ans_Req answerArray) {
        this.cltId = cltId;
        this.siteId = siteId;
        this.snm = snm;
        this.adr = adr;
        this.city = city;
        this.state = state;
        this.ctry = ctry;
        this.zip = zip;
        this.def = def;
        this.lat = lat;
        this.lng = lng;
        this.isAddContact = isAddContact;
        this.conId = conId;
        this.cnm = cnm;
        this.answerArray = answerArray;
    }
*/

    public ArrayList<Answer> getAnswerArray() {
        return answerArray;
    }

    public void setAnswerArray(ArrayList<Answer> answerArray) {
        this.answerArray = answerArray;
    }

    public String getIsAddContact() {
        return isAddContact;
    }

    public void setIsAddContact(String isAddContact) {
        this.isAddContact = isAddContact;
    }

    public String getConId() {
        return conId;
    }

    public void setConId(String conId) {
        this.conId = conId;
    }

    public String getCnm() {
        return cnm;
    }

    public void setCnm(String cnm) {
        this.cnm = cnm;
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

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getCltId() {
        return cltId;
    }

    public void setCltId(String cltId) {
        this.cltId = cltId;
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

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCtry() {
        return ctry;
    }

    public void setCtry(int ctry) {
        this.ctry = ctry;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
