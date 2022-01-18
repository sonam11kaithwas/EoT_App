package com.eot_app.utility.settings.contractdb;

public class ContractReq {
    int limit;
    int index;
    String search;
    String dateTime;

    public ContractReq(int limit, int index, String search, String dateTime) {
        this.limit = limit;
        this.index = index;
        this.search = search;
        this.dateTime = dateTime;
    }


}
