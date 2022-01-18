package com.eot_app.lat_lng_sync_pck;

import static android.content.Context.BATTERY_SERVICE;

import android.location.Location;
import android.os.BatteryManager;

import com.eot_app.utility.EotApp;

public class AddFWLatLng_Model1 {
    //  private String usrId;
    private final String lat;
    private final String lng;
    private final int btryStatus;
    private final String dateTime;


    public AddFWLatLng_Model1(String usrId, Location location, int battery, String dateTime) {
        BatteryManager bm = (BatteryManager) (EotApp.getAppinstance()).getSystemService(BATTERY_SERVICE);
        //  this.usrId = usrId;
        this.lat = location.getLatitude() + "";
        this.lng = location.getLongitude() + "";
        this.btryStatus = battery;
        this.dateTime = dateTime;

    }
}
