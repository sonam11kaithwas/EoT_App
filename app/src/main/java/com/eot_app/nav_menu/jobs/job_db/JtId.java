package com.eot_app.nav_menu.jobs.job_db;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aplite_pc302 on 8/9/18.
 */

public class JtId implements Parcelable {
    public static final Creator<JtId> CREATOR = new Creator<JtId>() {
        @Override
        public JtId createFromParcel(Parcel in) {
            return new JtId(in);
        }

        @Override
        public JtId[] newArray(int size) {
            return new JtId[size];
        }
    };
    private String jtId;
    private String title;
    private String labour;

    protected JtId(Parcel in) {
        jtId = in.readString();
        title = in.readString();
        labour = in.readString();
    }

    public JtId() {
    }

    public String getJtId() {
        return jtId;
    }

    public void setJtId(String jtId) {
        this.jtId = jtId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabour() {
        return labour;
    }

    public void setLabour(String labour) {
        this.labour = labour;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jtId);
        dest.writeString(title);
        dest.writeString(labour);
    }
}
