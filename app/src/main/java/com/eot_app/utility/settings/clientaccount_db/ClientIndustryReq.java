package com.eot_app.utility.settings.clientaccount_db;

/**
 * Created by Sonam-11 on 10/7/20.
 */
public class ClientIndustryReq {
    private final int limit;
    private final int index;
    private final String search;

    public ClientIndustryReq(int limit, int index, String search) {
        this.limit = limit;
        this.index = index;
        this.search = search;
    }
}
