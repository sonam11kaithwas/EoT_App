package com.eot_app.nav_menu.client.clientlist.client_detail.contact.edit_contact.editmodel;

import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;

import java.util.List;

/**
 * Created by ubuntu on 9/6/18.
 */

public interface EditContact_View {
    void setNameError(String msg);

    void setEmailError(String msg);

    void setMobError(String msg);

    void setAlterNateError(String alternate_No);

    void EditContactviewUI(ContactData contactData);

    void setResultForChangeInContact(String edit, String conId);

    void setSiteList(List<Site_model> siteList);

}
