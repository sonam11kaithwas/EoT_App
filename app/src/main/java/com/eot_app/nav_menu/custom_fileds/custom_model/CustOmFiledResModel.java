package com.eot_app.nav_menu.custom_fileds.custom_model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sonam-11 on 16/9/20.
 */
public class CustOmFiledResModel implements Parcelable {
    public static final Creator<CustOmFiledResModel> CREATOR = new Creator<CustOmFiledResModel>() {
        @Override
        public CustOmFiledResModel createFromParcel(Parcel in) {
            return new CustOmFiledResModel(in);
        }

        @Override
        public CustOmFiledResModel[] newArray(int size) {
            return new CustOmFiledResModel[size];
        }
    };
    private String frmId;
    private String jtId;
    private String frmnm;
    private String smt;
    private String event;
    private String mm;

    public CustOmFiledResModel() {
    }

    protected CustOmFiledResModel(Parcel in) {
        frmId = in.readString();
        jtId = in.readString();
        frmnm = in.readString();
        smt = in.readString();
        event = in.readString();
        mm = in.readString();
    }

    public String getFrmId() {
        return frmId;
    }

    public void setFrmId(String frmId) {
        this.frmId = frmId;
    }

    public String getJtId() {
        return jtId;
    }

    public void setJtId(String jtId) {
        this.jtId = jtId;
    }

    public String getFrmnm() {
        return frmnm;
    }

    public void setFrmnm(String frmnm) {
        this.frmnm = frmnm;
    }

    public String getSmt() {
        return smt;
    }

    public void setSmt(String smt) {
        this.smt = smt;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getMm() {
        return mm;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(frmId);
        dest.writeString(jtId);
        dest.writeString(frmnm);
        dest.writeString(smt);
        dest.writeString(event);
        dest.writeString(mm);
    }
}
