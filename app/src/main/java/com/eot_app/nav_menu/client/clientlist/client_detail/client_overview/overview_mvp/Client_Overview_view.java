package com.eot_app.nav_menu.client.clientlist.client_detail.client_overview.overview_mvp;

import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;

/**
 * Created by aplite_pc302 on 6/18/18.
 */

public interface Client_Overview_view {
    void updateUI(Client client, Site_model site, ContactData contact);

    void refreshUI(String clienId);
}
