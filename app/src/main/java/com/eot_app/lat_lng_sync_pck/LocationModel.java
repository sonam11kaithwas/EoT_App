package com.eot_app.lat_lng_sync_pck;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mahendra Dabi on 24/1/20.
 */
public class LocationModel {
    @SerializedName("usrId")
    @Expose
    private String usrId;

    @SerializedName("latLongData")
    @Expose
    private List<String> latLongData;

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    public List<String> getLatLongData() {
        return latLongData;
    }

    public void setLatLongData(List<String> latLongData) {
        this.latLongData = latLongData;
    }
}
