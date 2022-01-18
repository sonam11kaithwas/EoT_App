package com.eot_app.utility.settings.client_refrence_db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Created by Sona-11 on 13/9/21.
 */
@Entity(tableName = "ClientRefrenceModel")
public class ClientRefrenceModel implements Parcelable {
    public static final Creator<ClientRefrenceModel> CREATOR = new Creator<ClientRefrenceModel>() {
        @Override
        public ClientRefrenceModel createFromParcel(Parcel in) {
            return new ClientRefrenceModel(in);
        }

        @Override
        public ClientRefrenceModel[] newArray(int size) {
            return new ClientRefrenceModel[size];
        }
    };
    @PrimaryKey
    @NonNull
    private String refId;
    private String refName;
    private String isDefault;

    public ClientRefrenceModel() {
    }

    protected ClientRefrenceModel(Parcel in) {
        refId = in.readString();
        refName = in.readString();
        isDefault = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(refId);
        dest.writeString(refName);
        dest.writeString(isDefault);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientRefrenceModel)) return false;
        ClientRefrenceModel that = (ClientRefrenceModel) o;
        return getRefId().equals(that.getRefId()) &&
                Objects.equals(getRefName(), that.getRefName()) &&
                Objects.equals(getIsDefault(), that.getIsDefault());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRefId(), getRefName(), getIsDefault());
    }


    @NonNull
    public String getRefId() {
        return refId;
    }

    public void setRefId(@NonNull String refId) {
        this.refId = refId;
    }

    public String getRefName() {
        return refName;
    }

    public void setRefName(String refName) {
        this.refName = refName;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}
