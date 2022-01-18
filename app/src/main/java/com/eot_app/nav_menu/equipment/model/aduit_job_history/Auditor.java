package com.eot_app.nav_menu.equipment.model.aduit_job_history;


import android.os.Parcel;
import android.os.Parcelable;

public class Auditor implements Parcelable {
    public static final Creator<Auditor> CREATOR = new Creator<Auditor>() {
        @Override
        public Auditor createFromParcel(Parcel in) {
            return new Auditor(in);
        }

        @Override
        public Auditor[] newArray(int size) {
            return new Auditor[size];
        }
    };
    private String usrId;
    private String status;

    protected Auditor(Parcel in) {
        usrId = in.readString();
        status = in.readString();
    }

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(usrId);
        dest.writeString(status);
    }
}
