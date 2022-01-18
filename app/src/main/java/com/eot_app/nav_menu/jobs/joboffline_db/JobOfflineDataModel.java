package com.eot_app.nav_menu.jobs.joboffline_db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Sonam-11 on 17/7/20.
 */
@Entity(tableName = "JobOfflineDataModel")
public class JobOfflineDataModel implements Parcelable {
    public static final Creator<JobOfflineDataModel> CREATOR = new Creator<JobOfflineDataModel>() {
        @Override
        public JobOfflineDataModel createFromParcel(Parcel in) {
            return new JobOfflineDataModel(in);
        }

        @Override
        public JobOfflineDataModel[] newArray(int size) {
            return new JobOfflineDataModel[size];
        }
    };
    @PrimaryKey(autoGenerate = true)
    @NonNull
    int id;
    String service_name;
    String params;
    String timestamp;
    String tempId;

    public JobOfflineDataModel(String service_name, String params, String timestamp, String tempId) {
        this.service_name = service_name;
        this.params = params;
        this.timestamp = timestamp;
        this.tempId = tempId;
    }

    public JobOfflineDataModel() {
    }

    protected JobOfflineDataModel(Parcel in) {
        id = in.readInt();
        service_name = in.readString();
        params = in.readString();
        timestamp = in.readString();
        tempId = in.readString();
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(service_name);
        dest.writeString(params);
        dest.writeString(timestamp);
        dest.writeString(tempId);
    }
}
