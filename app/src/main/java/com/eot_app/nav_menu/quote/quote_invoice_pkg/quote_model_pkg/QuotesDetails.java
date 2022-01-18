package com.eot_app.nav_menu.quote.quote_invoice_pkg.quote_model_pkg;

import android.os.Parcel;
import android.os.Parcelable;

import com.eot_app.nav_menu.jobs.job_db.JtId;

import java.util.List;

public class QuotesDetails implements Parcelable {

    public static final Creator<QuotesDetails> CREATOR = new Creator<QuotesDetails>() {
        @Override
        public QuotesDetails createFromParcel(Parcel in) {
            return new QuotesDetails(in);
        }

        @Override
        public QuotesDetails[] newArray(int size) {
            return new QuotesDetails[size];
        }
    };
    private final String term;
    private String quotId;
    private String cltId;
    private String siteId;
    private String conId;
    private String label;
    private String des;
    private String status;
    private String athr;
    private String inst;
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
    private String createDate;
    private String updateDate;
    private List<JtId> jtId = null;
    private Quote_invoice_Details_Res invData;
    private String assignByUser;

    protected QuotesDetails(Parcel in) {
        quotId = in.readString();
        cltId = in.readString();
        siteId = in.readString();
        conId = in.readString();
        label = in.readString();
        des = in.readString();
        status = in.readString();
        athr = in.readString();
        inst = in.readString();
        nm = in.readString();
        cnm = in.readString();
        snm = in.readString();
        email = in.readString();
        mob1 = in.readString();
        mob2 = in.readString();
        adr = in.readString();
        city = in.readString();
        state = in.readString();
        ctry = in.readString();
        zip = in.readString();
        createDate = in.readString();
        updateDate = in.readString();
        invData = in.readParcelable(Quote_invoice_Details_Res.class.getClassLoader());
        assignByUser = in.readString();
        term = in.readString();
    }

    public static Creator<QuotesDetails> getCREATOR() {
        return CREATOR;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(quotId);
        dest.writeString(cltId);
        dest.writeString(siteId);
        dest.writeString(conId);
        dest.writeString(label);
        dest.writeString(des);
        dest.writeString(status);
        dest.writeString(athr);
        dest.writeString(inst);
        dest.writeString(nm);
        dest.writeString(cnm);
        dest.writeString(snm);
        dest.writeString(email);
        dest.writeString(mob1);
        dest.writeString(mob2);
        dest.writeString(adr);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(ctry);
        dest.writeString(zip);
        dest.writeString(createDate);
        dest.writeString(updateDate);
        dest.writeParcelable(invData, flags);
        dest.writeString(assignByUser);
        dest.writeString(term);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getQuotId() {
        return quotId;
    }

    public void setQuotId(String quotId) {
        this.quotId = quotId;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAthr() {
        return athr;
    }

    public void setAthr(String athr) {
        this.athr = athr;
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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public List<JtId> getJtId() {
        return jtId;
    }

    public void setJtId(List<JtId> jtId) {
        this.jtId = jtId;
    }

    public String getAssignByUser() {
        return assignByUser;
    }

    public void setAssignByUser(String assignByUser) {
        this.assignByUser = assignByUser;
    }

    public Quote_invoice_Details_Res getInvData() {
        return invData;
    }

    public void setInvData(Quote_invoice_Details_Res invData) {
        this.invData = invData;
    }

    public String getTerm() {
        return term;
    }
}
