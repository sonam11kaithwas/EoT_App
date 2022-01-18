package com.eot_app.nav_menu.quote.quotes_list_pkg.qoute_model_pkg;

import android.os.Parcel;
import android.os.Parcelable;

import com.eot_app.nav_menu.jobs.job_db.JtId;

import java.util.List;

public class Quote_ReS implements Parcelable {
    public static final Creator<Quote_ReS> CREATOR = new Creator<Quote_ReS>() {
        @Override
        public Quote_ReS createFromParcel(Parcel in) {
            return new Quote_ReS(in);
        }

        @Override
        public Quote_ReS[] newArray(int size) {
            return new Quote_ReS[size];
        }
    };
    private String quotId;
    private String label;
    private String des;
    private String status;
    private String adr;
    private String snm;
    private String nm;
    private String total;
    // private String city;
    private String duedate;
    private String quotDate;
    private List<JtId> jtId = null;

    protected Quote_ReS(Parcel in) {
        quotId = in.readString();
        label = in.readString();
        des = in.readString();
        status = in.readString();
        adr = in.readString();
        snm = in.readString();
        nm = in.readString();
        //city = in.readString();
        total = in.readString();
        duedate = in.readString();
        quotDate = in.readString();
    }

    public static Creator<Quote_ReS> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(quotId);
        parcel.writeString(label);
        parcel.writeString(des);
        parcel.writeString(status);
        parcel.writeString(adr);
        parcel.writeString(snm);
        parcel.writeString(nm);
        // parcel.writeString(city);
        parcel.writeString(total);
        parcel.writeString(duedate);
        parcel.writeString(quotDate);
    }

    public String getQuotId() {
        return quotId;
    }

    public void setQuotId(String quotId) {
        this.quotId = quotId;
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

    public String getAdr() {
        return adr;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    public String getSnm() {
        return snm;
    }

    public void setSnm(String snm) {
        this.snm = snm;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public String getQuotDate() {
        return quotDate;
    }

    public void setQuotDate(String quotDate) {
        this.quotDate = quotDate;
    }

    public List<JtId> getJtId() {
        return jtId;
    }

    public void setJtId(List<JtId> jtId) {
        this.jtId = jtId;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }


}
