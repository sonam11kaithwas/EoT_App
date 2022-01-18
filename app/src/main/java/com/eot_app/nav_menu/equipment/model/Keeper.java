package com.eot_app.nav_menu.equipment.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Keeper implements Parcelable {
    public static final Creator<Keeper> CREATOR = new Creator<Keeper>() {
        @Override
        public Keeper createFromParcel(Parcel in) {
            return new Keeper(in);
        }

        @Override
        public Keeper[] newArray(int size) {
            return new Keeper[size];
        }
    };
    private String img;
    private String status;
    private String usrId;

    protected Keeper(Parcel in) {
        img = in.readString();
        status = in.readString();
        usrId = in.readString();
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(img);
        dest.writeString(status);
        dest.writeString(usrId);
    }
}
