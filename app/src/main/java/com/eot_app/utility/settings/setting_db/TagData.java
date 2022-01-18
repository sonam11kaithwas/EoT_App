package com.eot_app.utility.settings.setting_db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.eot_app.utility.DropdownListBean;

/**
 * Created by aplite_pc302 on 8/31/18.
 */
@Entity(indices = {@Index(value = "tagId", unique = true)}) // check user first name is not repeat.
public class TagData implements DropdownListBean, Parcelable {
    public static final Creator<TagData> CREATOR = new Creator<TagData>() {
        @Override
        public TagData createFromParcel(Parcel in) {
            return new TagData(in);
        }

        @Override
        public TagData[] newArray(int size) {
            return new TagData[size];
        }
    };
    @NonNull
    @PrimaryKey
    String tagId;
    String tnm;
    private String extra;

    public TagData() {
    }

    protected TagData(Parcel in) {
        tagId = in.readString();
        tnm = in.readString();
        extra = in.readString();
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @NonNull
    public String getTagId() {
        return tagId;
    }

    public void setTagId(@NonNull String tagId) {
        this.tagId = tagId;
    }

    public String getTnm() {
        return tnm;
    }

    public void setTnm(String tnm) {
        this.tnm = tnm;
    }

    @Override
    public String getKey() {
        return getTagId();
    }

    @Override
    public String getName() {
        return getTnm();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagData)) return false;

        TagData tagData = (TagData) o;

        if (!getTagId().equals(tagData.getTagId())) return false;
        return getTnm() != null ? getTnm().equals(tagData.getTnm()) : tagData.getTnm() == null;
    }

    @Override
    public int hashCode() {
        int result = getTagId().hashCode();
        result = 31 * result + (getTnm() != null ? getTnm().hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tagId);
        dest.writeString(tnm);
        dest.writeString(extra);
    }
}
