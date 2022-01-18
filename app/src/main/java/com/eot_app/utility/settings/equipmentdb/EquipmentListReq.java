package com.eot_app.utility.settings.equipmentdb;

public class EquipmentListReq {
    int limit;
    int index;
    String search;
    String dateTime;

    public EquipmentListReq(int limit, int index, String search, String dateTime) {
        this.limit = limit;
        this.index = index;
        this.search = search;
        this.dateTime = dateTime;
    }


}
