package com.eot_app.nav_menu.jobs.job_detail.form_form.get_qus_list.qus_model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ubuntu on 18/9/18.
 */

public class AnswerModel implements Parcelable {
    public static final Creator<AnswerModel> CREATOR = new Creator<AnswerModel>() {
        @Override
        public AnswerModel createFromParcel(Parcel in) {
            return new AnswerModel(in);
        }

        @Override
        public AnswerModel[] newArray(int size) {
            return new AnswerModel[size];
        }
    };
    private String key;
    private String value;

    public AnswerModel() {
    }

    protected AnswerModel(Parcel in) {
        key = in.readString();
        value = in.readString();
    }

    public AnswerModel(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static Creator<AnswerModel> getCREATOR() {
        return CREATOR;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(value);
    }
}
