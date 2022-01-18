package com.eot_app.nav_menu.client.client_db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Created by aplite_pc302 on 6/1/18.
 */
@Entity(indices = {@Index(value = "cltId", unique = true)}) // check user first name is not repeat.

public class Client implements Parcelable {
    public static final Creator<Client> CREATOR = new Creator<Client>() {
        @Override
        public Client createFromParcel(Parcel in) {
            return new Client(in);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[size];
        }
    };
    @PrimaryKey
    @NonNull
    private String cltId;
    private String nm;
    private String pymtType;
    private String gstNo;
    private String tinNo;
    private String industry;
    private String note;
    private String isactive;
    private String accid;
    private String acctype;
    private String siteId;
    private String snm;
    private String adr;
    private String city;
    private String state;
    private String ctry;
    private String conId;
    private String cnm;
    private String email;
    private String mob1;
    private String mob2;
    private String isdelete;
    private String tempId;
    private String zip;
    private String lat;
    private String lng;
    private String extra;
    private String industryName;
    private String referral;

    public Client() {
    }

    protected Client(Parcel in) {
        cltId = in.readString();
        nm = in.readString();
        pymtType = in.readString();
        gstNo = in.readString();
        tinNo = in.readString();
        industry = in.readString();
        note = in.readString();
        isactive = in.readString();
        accid = in.readString();
        acctype = in.readString();
        siteId = in.readString();
        snm = in.readString();
        adr = in.readString();
        city = in.readString();
        state = in.readString();
        ctry = in.readString();
        conId = in.readString();
        cnm = in.readString();
        email = in.readString();
        mob1 = in.readString();
        mob2 = in.readString();
        isdelete = in.readString();
        tempId = in.readString();
        zip = in.readString();
        lat = in.readString();
        lng = in.readString();
        extra = in.readString();
        industryName = in.readString();
        referral = in.readString();
    }

    public static Creator<Client> getCREATOR() {
        return CREATOR;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Client octClone = (Client) super.clone();
        return octClone;
    }


    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
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

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
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

    public String getPymtType() {
        return pymtType;
    }

    public void setPymtType(String pymtType) {
        this.pymtType = pymtType;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public String getTinNo() {
        return tinNo;
    }

    public void setTinNo(String tinNo) {
        this.tinNo = tinNo;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getAcctype() {
        return acctype;
    }

    public void setAcctype(String acctype) {
        this.acctype = acctype;
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

    public String getReferral() {
        return referral;
    }

    public void setReferral(String referral) {
        this.referral = referral;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(cltId);
        parcel.writeString(nm);
        parcel.writeString(pymtType);
        parcel.writeString(gstNo);
        parcel.writeString(tinNo);
        parcel.writeString(industry);
        parcel.writeString(note);
        parcel.writeString(isactive);
        parcel.writeString(accid);
        parcel.writeString(acctype);
        parcel.writeString(siteId);
        parcel.writeString(snm);
        parcel.writeString(adr);
        parcel.writeString(city);
        parcel.writeString(state);
        parcel.writeString(ctry);
        parcel.writeString(conId);
        parcel.writeString(cnm);
        parcel.writeString(email);
        parcel.writeString(mob1);
        parcel.writeString(mob2);
        parcel.writeString(isdelete);
        parcel.writeString(tempId);
        parcel.writeString(zip);
        parcel.writeString(lat);
        parcel.writeString(lng);
        parcel.writeString(extra);
        parcel.writeString(industryName);
        parcel.writeString(referral);
    }
}
