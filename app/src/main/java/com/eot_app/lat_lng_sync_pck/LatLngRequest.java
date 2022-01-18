package com.eot_app.lat_lng_sync_pck;

import java.util.List;

/**
 * Created by Mahendra Dabi on 16/3/20.
 */
public class LatLngRequest {
    private final String usrId;
    private final List<AddFWLatLng_Model1> latLongData;

    public LatLngRequest(String usrId, List<AddFWLatLng_Model1> latLangsList) {
        this.usrId = usrId;
        this.latLongData = latLangsList;
    }


}
