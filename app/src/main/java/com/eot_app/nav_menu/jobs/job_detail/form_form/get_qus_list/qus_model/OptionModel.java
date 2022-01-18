package com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model;

import android.os.Parcel;
import android.os.Parcelable;

import com.eot_app.utility.DropdownListBean;

import java.util.List;

/**
 * Created by ubuntu on 18/9/18.
 */

public class OptionModel implements DropdownListBean, Parcelable {
    public static final Creator<OptionModel> CREATOR = new Creator<OptionModel>() {
        @Override
        public OptionModel createFromParcel(Parcel in) {
            return new OptionModel(in);
        }

        @Override
        public OptionModel[] newArray(int size) {
            return new OptionModel[size];
        }
    };
    private String key;
    private List<Questions> questions = null;
    private String value;
    private String optHaveChild;

    public OptionModel() {
    }

    protected OptionModel(Parcel in) {
        key = in.readString();
        value = in.readString();
        optHaveChild = in.readString();
    }

    public static Creator<OptionModel> getCREATOR() {
        return CREATOR;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getName() {
        return value;
    }

    public List<Questions> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Questions> questions) {
        this.questions = questions;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOptHaveChild() {
        return optHaveChild;
    }

    public void setOptHaveChild(String optHaveChild) {
        this.optHaveChild = optHaveChild;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(value);
        dest.writeString(optHaveChild);
    }
}


class Questions {

    private List<OptionModel> opt = null;

    public List<OptionModel> getOpt() {
        return opt;
    }

    public void setOpt(List<OptionModel> opt) {
        this.opt = opt;
    }
}
