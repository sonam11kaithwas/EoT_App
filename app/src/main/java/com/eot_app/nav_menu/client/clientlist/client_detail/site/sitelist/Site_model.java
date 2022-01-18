package com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFormQuestionsRes;
import com.eot_app.utility.DropdownListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by geet-pc on 14/5/18.
 */
@Entity(indices = {@Index(value = "siteId", unique = true)})
public class Site_model implements Parcelable, DropdownListBean {
    public static final Creator<Site_model> CREATOR = new Creator<Site_model>() {
        @Override
        public Site_model createFromParcel(Parcel in) {
            return new Site_model(in);
        }

        @Override
        public Site_model[] newArray(int size) {
            return new Site_model[size];
        }
    };

    @TypeConverters(SiteCustomFieldConverter.class)
    private List<CustOmFormQuestionsRes> customFieldArray = new ArrayList<>();
    @PrimaryKey
    @NonNull
    private String siteId;
    private String cltId;
    private String snm;
    private String adr;
    private String city;
    private String state;
    private String ctry;
    private String zip;
    private String lat;
    private String tempId;
    private String extra;
    private String lng;
    private String def;
    private String isdelete;
    private String conId;
    private String cnm;

    public Site_model() {
    }

    protected Site_model(Parcel in) {
        siteId = in.readString();
        cltId = in.readString();
        snm = in.readString();
        adr = in.readString();
        city = in.readString();
        state = in.readString();
        ctry = in.readString();
        zip = in.readString();
        lat = in.readString();
        tempId = in.readString();
        extra = in.readString();
        lng = in.readString();
        def = in.readString();
        isdelete = in.readString();
        conId = in.readString();
        cnm = in.readString();
        customFieldArray = in.createTypedArrayList(CustOmFormQuestionsRes.CREATOR);
    }

    public static Creator<Site_model> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String getKey() {
        return getSiteId();
    }

    @Override
    public String getName() {
        return getSnm();
    }

    @NonNull
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(@NonNull String siteId) {
        this.siteId = siteId;
    }

    public String getCltId() {
        return cltId;
    }

    public void setCltId(String cltId) {
        this.cltId = cltId;
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

    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
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

    public List<CustOmFormQuestionsRes> getCustomFieldArray() {
        return customFieldArray;
    }

    public void setCustomFieldArray(List<CustOmFormQuestionsRes> customFieldArray) {
        this.customFieldArray = customFieldArray;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(siteId);
        parcel.writeString(cltId);
        parcel.writeString(snm);
        parcel.writeString(adr);
        parcel.writeString(city);
        parcel.writeString(state);
        parcel.writeString(ctry);
        parcel.writeString(zip);
        parcel.writeString(lat);
        parcel.writeString(tempId);
        parcel.writeString(extra);
        parcel.writeString(lng);
        parcel.writeString(def);
        parcel.writeString(isdelete);
        parcel.writeString(conId);
        parcel.writeString(cnm);
        parcel.writeTypedList(customFieldArray);
    }
}

