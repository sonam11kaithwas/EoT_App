package com.eot_app.nav_menu.client.clientlist.client_presenter;

import com.eot_app.nav_menu.client.client_db.Client;

import java.util.List;

/**
 * Created by ubuntu on 1/6/18.
 */

public interface Client_pi {
    void client_Responce();

    void addRecordsToDB(List<Client> data);

    void getClientList();

    void getclientListfromDB(String query);

    void getSIteList();

}
