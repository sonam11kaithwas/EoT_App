package com.eot_app.nav_menu.client.clientlist.client_detail.contact.clientcontact_mvp;

import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;

import java.util.List;

/**
 * Created by ubuntu on 7/6/18.
 */

public interface Contact_pi {
    void contactResponce();

    void addDataToDB(List<ContactData> contactData);

    void contactDetails();

    void getcontactList(String query, String cltID);

    void getContactListFromDB();

}
