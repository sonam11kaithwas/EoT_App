package com.eot_app.time_shift_pkg;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Sona-11 on 15/11/21.
 */
@Entity(tableName = "ShiftTimeReSModel")
public class ShiftTimeReSModel implements Parcelable {
    public static final Creator<ShiftTimeReSModel> CREATOR = new Creator<ShiftTimeReSModel>() {
        @Override
        public ShiftTimeReSModel createFromParcel(Parcel in) {
            return new ShiftTimeReSModel(in);
        }

        @Override
        public ShiftTimeReSModel[] newArray(int size) {
            return new ShiftTimeReSModel[size];
        }
    };
    @PrimaryKey
    @NonNull
    private String shiftId;
    private String shiftNm;
    private String isDefault;
    private String shiftStartTime;
    private String shiftEndTime;

    public ShiftTimeReSModel() {
    }

    protected ShiftTimeReSModel(Parcel in) {
        shiftId = in.readString();
        shiftNm = in.readString();
        isDefault = in.readString();
        shiftStartTime = in.readString();
        shiftEndTime = in.readString();
    }

    @NonNull
    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(@NonNull String shiftId) {
        this.shiftId = shiftId;
    }

    public String getShiftNm() {
        return shiftNm;
    }

    public void setShiftNm(String shiftNm) {
        this.shiftNm = shiftNm;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getShiftStartTime() {
        return shiftStartTime;
    }

    public void setShiftStartTime(String shiftStartTime) {
        this.shiftStartTime = shiftStartTime;
    }

    public String getShiftEndTime() {
        return shiftEndTime;
    }

    public void setShiftEndTime(String shiftEndTime) {
        this.shiftEndTime = shiftEndTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shiftId);
        dest.writeString(shiftNm);
        dest.writeString(isDefault);
        dest.writeString(shiftStartTime);
        dest.writeString(shiftEndTime);
    }
}
