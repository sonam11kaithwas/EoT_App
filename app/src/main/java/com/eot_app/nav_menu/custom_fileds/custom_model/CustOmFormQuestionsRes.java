package com.eot_app.nav_menu.custom_fileds.custom_model;

import android.os.Parcel;
import android.os.Parcelable;

import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model.AnswerModel;
import com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model.OptionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sonam-11 on 17/9/20.
 */
public class CustOmFormQuestionsRes implements Parcelable {
    public static final Creator<CustOmFormQuestionsRes> CREATOR = new Creator<CustOmFormQuestionsRes>() {
        @Override
        public CustOmFormQuestionsRes createFromParcel(Parcel in) {
            return new CustOmFormQuestionsRes(in);
        }

        @Override
        public CustOmFormQuestionsRes[] newArray(int size) {
            return new CustOmFormQuestionsRes[size];
        }
    };
    private String queId;
    private String parentId;
    private String parentAnsId;
    private String frmId;
    private String des;
    private String type;
    private String mandatory;
    private String frmType;
    private int index;
    private List<AnswerModel> ans = new ArrayList<>();
    private List<OptionModel> opt = new ArrayList<>();

    public CustOmFormQuestionsRes() {
    }

    protected CustOmFormQuestionsRes(Parcel in) {
        queId = in.readString();
        parentId = in.readString();
        parentAnsId = in.readString();
        frmId = in.readString();
        des = in.readString();
        type = in.readString();
        mandatory = in.readString();
        frmType = in.readString();
        ans = in.createTypedArrayList(AnswerModel.CREATOR);
        opt = in.createTypedArrayList(OptionModel.CREATOR);
    }

    public static Creator<CustOmFormQuestionsRes> getCREATOR() {
        return CREATOR;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getQueId() {
        return queId;
    }

    public void setQueId(String queId) {
        this.queId = queId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentAnsId() {
        return parentAnsId;
    }

    public void setParentAnsId(String parentAnsId) {
        this.parentAnsId = parentAnsId;
    }

    public String getFrmId() {
        return frmId;
    }

    public void setFrmId(String frmId) {
        this.frmId = frmId;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMandatory() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }

    public String getFrmType() {
        return frmType;
    }

    public void setFrmType(String frmType) {
        this.frmType = frmType;
    }

    public List<AnswerModel> getAns() {
        return ans;
    }

    public void setAns(List<AnswerModel> ans) {
        this.ans = ans;
    }

    public List<OptionModel> getOpt() {
        return opt;
    }

    public void setOpt(List<OptionModel> opt) {
        this.opt = opt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(queId);
        dest.writeString(parentId);
        dest.writeString(parentAnsId);
        dest.writeString(frmId);
        dest.writeString(des);
        dest.writeString(type);
        dest.writeString(mandatory);
        dest.writeString(frmType);
        dest.writeTypedList(ans);
        dest.writeTypedList(opt);
    }
}
