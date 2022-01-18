package com.eot_app.lat_lng_sync_pck;

import static android.content.Context.BATTERY_SERVICE;

import android.os.BatteryManager;

import com.eot_app.utility.AppUtility;
import com.eot_app.utility.EotApp;

public class AddFWLatLng_Model {
    private final String usrId;
    private final String lat;
    private final String lng;
    private final int btryStatus;
    private final String dateTime;


    public AddFWLatLng_Model(String usrId, String lat, String lng) {
        BatteryManager bm = (BatteryManager) (EotApp.getAppinstance()).getSystemService(BATTERY_SERVICE);
        this.usrId = usrId;
        this.lat = lat;
        this.lng = lng;
        this.btryStatus = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        this.dateTime = AppUtility.getDateByFormat("yyyy-MM-dd hh:mm:ss a");

    }
}
