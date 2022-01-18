package com.eot_app.nav_menu.client.clientlist.client_presenter;

import com.eot_app.nav_menu.client.client_db.Client;

import java.util.List;

/**
 * Created by ubuntu on 4/6/18.
 */

public interface ClientList_View {
    void setdata(List<Client> data);

    void refreshList(String cltId);

    void setSearchData(List<Client> searchData);

    void updateFromApiObserver();

    void onSessionExpire(String msg);

    void disableSwiprefresh();
}
