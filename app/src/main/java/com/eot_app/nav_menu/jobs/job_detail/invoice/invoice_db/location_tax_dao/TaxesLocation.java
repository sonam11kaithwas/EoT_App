package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.location_tax_dao;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Sona-11 on 19/8/21.
 */
@Entity(tableName = "TaxesLocation")
public class TaxesLocation implements Parcelable {
    public static final Creator<TaxesLocation> CREATOR = new Creator<TaxesLocation>() {
        @Override
        public TaxesLocation createFromParcel(Parcel in) {
            return new TaxesLocation(in);
        }

        @Override
        public TaxesLocation[] newArray(int size) {
            return new TaxesLocation[size];
        }
    };
    @PrimaryKey
    @NonNull
    private String locId;
    private String stateId;
    private String location;

    protected TaxesLocation(Parcel in) {
        locId = in.readString();
        stateId = in.readString();
        location = in.readString();
    }

    public TaxesLocation() {
    }

    public static Creator<TaxesLocation> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(locId);
        dest.writeString(stateId);
        dest.writeString(location);
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
