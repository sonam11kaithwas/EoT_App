package com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitemvp;

import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
import com.eot_app.utility.Country;
import com.eot_app.utility.States;

import java.util.List;

/**
 * Created by ubuntu on 14/6/18.
 */

public interface EditSite_View {
    void setNameError(String msg);

    void setAddError(String msg);

    void EditSiteviewUI(Site_model site_model);


    void refreshList(String change, String siteId);

    void setCountryList(List<Country> counrtyList);

    void setStateList(List<States> statesList);

    void setCountryError(String s);

    void setStateError(String s);

    void setContactList(List<ContactData> contactList);


    void setEmailError(String msg);

    void setMobError(String msg);

    void setAlterNateError(String alternate_No);

    void setCustomFieldData(Site_model site_model);
}
