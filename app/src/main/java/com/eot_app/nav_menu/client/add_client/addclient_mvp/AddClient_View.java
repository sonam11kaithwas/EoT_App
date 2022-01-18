package com.eot_app.nav_menu.client.add_client.addclient_mvp;

import com.eot_app.utility.Country;
import com.eot_app.utility.States;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ubuntu on 14/6/18.
 */

public interface AddClient_View {

    void setNameError(String msg);

    void setEmailError(String msg);

    void setMobError(String msg);

    void setAddError(String msg);

    void onAddNewClient(String cltId);

    void setCountryList(List<Country> countryList);

    void setStateList(List<States> stateList);

    void setAccountType(ArrayList<String> accountList);

    void setIndustry();

    void setCountryError(String s);

    void setStateError(String s);

    void setRefrences();
}
