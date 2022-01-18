package com.eot_app.nav_menu.client.clientlist.client_detail.contact.clientcontact_mvp;

import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;

import java.util.List;

/**
 * Created by ubuntu on 7/6/18.
 */

public interface Contact_View {
    void setData(List<ContactData> contactData);

    void contactUpdate(int result, String conId);

    void setSearchData(List<ContactData> searchData);

    void updateFromObserver();

    void disableSwiprefresh();

}
