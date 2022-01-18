package com.eot_app.lat_lng_sync_pck;

import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.db.OfflineDataController;
import com.google.gson.Gson;

public class LatLngSycn_Controller {
    private static LatLngSycn_Controller latLngSycn_controller;
    private String lat = "0.0";
    private String lng = "0.0";

    public static LatLngSycn_Controller getInstance() {
        if (latLngSycn_controller == null) {
            latLngSycn_controller = new LatLngSycn_Controller();
        }
        return latLngSycn_controller;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }


    /**
     * call api for location update with baterry status
     */
    public void addFeildWorkerLatLng() {
        AddFWLatLng_Model addFWLatLng_model = new AddFWLatLng_Model(App_preference.getSharedprefInstance().getLoginRes().getUsrId(), getLat(), getLng());
        Gson gson = new Gson();
        String addFwlatlng = gson.toJson(addFWLatLng_model);
        String dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);
        OfflineDataController.getInstance().addInOfflineDB(Service_apis.addFWlatlong, addFwlatlng, dateTime);
    }


}
