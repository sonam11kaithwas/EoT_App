package com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist;

import java.util.List;

/**
 * Created by ubuntu on 8/6/18.
 */

public interface Site_pi {
    void siteResponce();

    void addSiteListToDB(List<Site_model> siteDao);

    void GetSiteDetails();

    void getsiteList(String query, String cltId);

    void GetSiteDetailsFromDB();

//    void getSIteList();

}
