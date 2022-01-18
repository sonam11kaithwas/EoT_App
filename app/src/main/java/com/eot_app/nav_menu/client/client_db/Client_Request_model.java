package com.eot_app.nav_menu.client.client_db;

/**
 * Created by ubuntu on 1/6/18.
 */

public class Client_Request_model {
    int compId;
    int limit;
    int index;
    String dateTime;

    public Client_Request_model(int compId, int limit, int index, String dateTime) {
        this.compId = compId;
        this.limit = limit;
        this.index = index;
        this.dateTime = dateTime;
    }
}
