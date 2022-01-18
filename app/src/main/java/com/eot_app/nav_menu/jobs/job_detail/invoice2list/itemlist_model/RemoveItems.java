package com.eot_app.nav_menu.jobs.job_detail.invoice2list.itemlist_model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Sonam-11 on 15/6/20.
 */
public class RemoveItems implements Parcelable {
    public static final Creator<RemoveItems> CREATOR = new Creator<RemoveItems>() {
        @Override
        public RemoveItems createFromParcel(Parcel in) {
            return new RemoveItems(in);
        }

        @Override
        public RemoveItems[] newArray(int size) {
            return new RemoveItems[size];
        }
    };
    private String jobId;
    private ArrayList<String> ijmmId;

    public RemoveItems(String jobId, ArrayList<String> ijmmId) {
        this.jobId = jobId;
        this.ijmmId = ijmmId;
    }

    public RemoveItems() {
    }

    protected RemoveItems(Parcel in) {
        jobId = in.readString();
        ijmmId = in.createStringArrayList();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jobId);
        dest.writeStringList(ijmmId);
    }
}
