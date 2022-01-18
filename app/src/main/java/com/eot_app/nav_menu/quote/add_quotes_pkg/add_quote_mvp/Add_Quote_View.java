package com.eot_app.nav_menu.quote.add_quotes_pkg.add_quote_mvp;

import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
import com.eot_app.utility.Country;
import com.eot_app.utility.States;
import com.eot_app.utility.settings.setting_db.FieldWorker;
import com.eot_app.utility.settings.setting_db.JobTitle;

import java.util.List;

public interface Add_Quote_View {
    void setJobServiceslist(List<JobTitle> jobServiceslist);

    void setClientList(List<Client> clientList);

    void setCountryList(List<Country> countryList);

    void setStateList(List<States> statesList);

    void setContactList(List<ContactData> contactList);

    void setSiteList(List<Site_model> statesList);

    void setquoteTypeError(String errorMsg);

    void setClientNameError(String errorMsg);

    void setAddr_Error(String errorMsg);

    void setCountryError(String msg);

    void setStateError(String msg);

    void setMobError(String msg);

    void setEmailError(String msg);

    void onAddNewQuotes(String quoteId, String lable);

    void onUpdateQuote();

    void setfwListForQuotes(List<FieldWorker> fwList);

    void onSessionExpire(String msg);

    void errorMsg(String error);

    void setTermsConditions(String termsConditions);

    void finishActivity();
}
