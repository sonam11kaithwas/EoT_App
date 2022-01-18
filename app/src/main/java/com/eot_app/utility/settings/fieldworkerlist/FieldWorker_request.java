package com.eot_app.utility.settings.fieldworkerlist;

/**
 * Created by aplite_pc302 on 7/3/18.
 */

public class FieldWorker_request {
    String compId;
    int limit;
    int index;
    String isactive;
    String search;
    String dateTime;

    public FieldWorker_request(String compId, int limit, int index, String isactive) {
        this.compId = compId;
        this.limit = limit;
        this.index = index;
        this.isactive = isactive;
    }
}
