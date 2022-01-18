package com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitemvp;

import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.AddSiteModel;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.EditSiteModel;
import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFormQuestionsRes;

import java.util.ArrayList;

/**
 * Created by ubuntu on 14/6/18.
 */

public interface EditSite_Pi {
    boolean validationCheck(String name, String add, String country, String state);

    void setEditSiteData(String siteData);

    void AddNewSite(AddSiteModel addSiteModel, ArrayList<CustOmFormQuestionsRes> custOmFormQuestionsList);

    void addnewSiteToDB(Site_model site_model);

    void EditClientSite(EditSiteModel editSiteModel, ArrayList<CustOmFormQuestionsRes> custOmFormQuestionsList);

    void getCountryList();

    void getStateList(String ctry);

    boolean isValidCountry(String country);

    boolean isValidState(String state);

    void getContactList(String cltId);

    boolean validationForContact(String email, String name, String mob, String alterNate);

    void getCustomFieldData(String sid);
}
