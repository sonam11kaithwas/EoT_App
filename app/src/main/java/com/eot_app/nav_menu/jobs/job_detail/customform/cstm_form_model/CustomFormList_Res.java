package com.eot_app.nav_menu.jobs.job_detail.customform.cstm_form_model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ubuntu on 17/9/18.
 */

public class CustomFormList_Res implements Parcelable {
    public static final Creator<CustomFormList_Res> CREATOR = new Creator<CustomFormList_Res>() {
        @Override
        public CustomFormList_Res createFromParcel(Parcel in) {
            return new CustomFormList_Res(in);
        }

        @Override
        public CustomFormList_Res[] newArray(int size) {
            return new CustomFormList_Res[size];
        }
    };
    private final Map<String, Object> additionalProperties = new HashMap<String, Object>();
    String type = "form";
    private String frmId;
    private String jtId;
    private String frmnm;
    private String event;
    private String tab;
    private String mandatory;
    private String totalQues;

    public CustomFormList_Res(String type, String tab) {
        this.type = type;
        this.tab = tab;
    }

    protected CustomFormList_Res(Parcel in) {
        type = in.readString();
        frmId = in.readString();
        jtId = in.readString();
        frmnm = in.readString();
        event = in.readString();
        tab = in.readString();
        mandatory = in.readString();
        totalQues = in.readString();
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


    public String getEvent() {
        return event;
    }


    public void setEvent(String event) {
        this.event = event;
    }


    public String getTab() {
        return tab;
    }


    public void setTab(String tab) {
        this.tab = tab;
    }


    public String getMandatory() {
        return mandatory;
    }


    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }


    public String getTotalQues() {
        return totalQues;
    }


    public void setTotalQues(String totalQues) {
        this.totalQues = totalQues;
    }


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }


    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(frmId);
        dest.writeString(jtId);
        dest.writeString(frmnm);
        dest.writeString(event);
        dest.writeString(tab);
        dest.writeString(mandatory);
        dest.writeString(totalQues);
    }
}
