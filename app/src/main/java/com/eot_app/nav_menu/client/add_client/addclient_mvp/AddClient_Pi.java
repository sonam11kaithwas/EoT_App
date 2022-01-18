package com.eot_app.nav_menu.client.add_client.addclient_mvp;

import com.eot_app.nav_menu.client.add_client.addclient_model_pkg.AddClientModel;

/**
 * Created by ubuntu on 14/6/18.
 */

public interface AddClient_Pi {
    Boolean addClientValidation(String name, String contactName, String email, String siteName, String add, String countryname, String statename, String mob);//,

    void addClientCall(AddClientModel addClientModel);

    boolean isValidCountry(String country);

    boolean isValidState(String state);

    void clientCountryList();

    void clientStatesList(String cntryId);

    void accountItemSpinner();

    int accountItemSelect(String name);

    // void getReferenceList();
}
