package com.eot_app.nav_menu.client.clientlist.client_detail.contact.edit_contact.editmodel;

import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;

/**
 * Created by ubuntu on 9/6/18.
 */

public interface EditContact_Pi {
    boolean checkValidation(String email, String name, String mob, String alterNate);

    void AddNewClientContact(ClientContactAddEdit_Model clientContactAddEdit_model);

    void addNewContactToDB(ContactData newcontactdata);

    void setEditContactData(ContactData contactData);

    void EditClientContact(ClientContactEdit_Model clientContactEdit_model, String cltId);

    void getSiteFromdb(String cltId);
}
