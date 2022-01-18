package com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.eot_app.nav_menu.client.clientlist.client_detail.contact.edit_contact.editmodel.SiteId;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.edit_contact.editmodel.SiteidConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ubuntu on 7/6/18.
 */
@Entity(indices = {@Index(value = "conId", unique = true)})
public class ContactData implements Parcelable {

    public static final Creator<ContactData> CREATOR = new Creator<ContactData>() {
        @Override
        public ContactData createFromParcel(Parcel in) {
            return new ContactData(in);
        }

        @Override
        public ContactData[] newArray(int size) {
            return new ContactData[size];
        }
    };
    @PrimaryKey
    @NonNull
    private String conId;
    private String cltId;
    private String cnm;
    private String email;
    private String mob1;
    private String mob2;
    private String fax;
    private String twitter;
    private String skype;
    private String def;
    private String tempId;
    private String extra;
    private String isdelete;
    @TypeConverters(SiteidConverter.class)
    private List<SiteId> siteId = new ArrayList<>();

    public ContactData() {
    }

    protected ContactData(Parcel in) {
        conId = in.readString();
        cltId = in.readString();
        cnm = in.readString();
        email = in.readString();
        mob1 = in.readString();
        mob2 = in.readString();
        fax = in.readString();
        twitter = in.readString();
        skype = in.readString();
        def = in.readString();
        tempId = in.readString();
        extra = in.readString();
        isdelete = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(conId);
        dest.writeString(cltId);
        dest.writeString(cnm);
        dest.writeString(email);
        dest.writeString(mob1);
        dest.writeString(mob2);
        dest.writeString(fax);
        dest.writeString(twitter);
        dest.writeString(skype);
        dest.writeString(def);
        dest.writeString(tempId);
        dest.writeString(extra);
        dest.writeString(isdelete);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public List<SiteId> getSiteId() {
        return siteId;
    }

    public void setSiteId(List<SiteId> siteId) {
        this.siteId = siteId;
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

    public String getCltId() {
        return cltId;
    }

    public void setCltId(String cltId) {
        this.cltId = cltId;
    }

    @NonNull
    public String getConId() {
        return conId;
    }

    public void setConId(@NonNull String conId) {
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

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }


}
