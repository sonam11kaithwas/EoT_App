package com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitemvp;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.activitylog.LogModel;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.AddSiteModel;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.EditSiteModel;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.SpinnerCountrySite;
import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFormQuestionsRes;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.Country;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.Eot_Validation;
import com.eot_app.utility.States;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.db.OfflineDataController;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ubuntu on 14/6/18.
 */

public class EditSite_Pc implements EditSite_Pi {
    EditSite_View editSite_view;
    Site_model site_model;
    List<Country> counrtyList;
    List<States> statesList;

    public EditSite_Pc(EditSite_View editSite_view) {
        this.editSite_view = editSite_view;
    }

    @Override
    public boolean validationCheck(String name, String add, String country, String state) {
        boolean returncall = true;
        if (name.equals("")) {
            editSite_view.setNameError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_site_name));
            returncall = false;
        } else if (!(add.length() > 0)) {
            editSite_view.setAddError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_addr));
            return false;
        } else if (!isValidCountry(country)) {
            editSite_view.setCountryError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.country_error));
            returncall = false;
        } else if (!isValidState(state)) {
            editSite_view.setStateError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.state_error));
            returncall = false;
        }
        return returncall;
    }


    @Override
    public boolean validationForContact(String name, String email, String mob, String alterNate) {
        if (name.equals("")) {
            editSite_view.setNameError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.cont_name));
            return false;
        } else if ((!email.equals("")) && !Eot_Validation.email_checker(email).equals("")) {
            editSite_view.setEmailError(Eot_Validation.email_checker(email));
            return false;
        } else if ((!mob.equals("")) && !Eot_Validation.mob_checker(mob).equals("")) {
            editSite_view.setMobError(Eot_Validation.mob_checker(mob));
            return false;
        } else if (!alterNate.equals("")) {
            if (!Eot_Validation.mob_checker(alterNate).equals("")) {
                editSite_view.setAlterNateError(Eot_Validation.mob_checker(alterNate));
                return false;
            }
        }
        return true;
    }

    @Override
    public void setEditSiteData(String siteId) {
        Site_model siteData = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getSiteFromSiteId(siteId);
        getStateList(siteData.getCtry());
        editSite_view.EditSiteviewUI(siteData);
    }

    @Override  //addsite
    public void AddNewSite(AddSiteModel addSiteModel, ArrayList<CustOmFormQuestionsRes> custOmFormQuestionsList) {
//        use for save site in db
//        int countrow = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getTotalCount();
        addSiteModel.setTempId(AppUtility.getTempIdFormat("Site"));
        addtempSiteIntoDB(addSiteModel, custOmFormQuestionsList);
        EotApp.getAppinstance().notifyCon_SiteObserver(Service_apis.addClientSite);

        Gson gson = new Gson();
        String data = gson.toJson(addSiteModel);

        String dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);

        OfflineDataController.getInstance().addInOfflineDB(Service_apis.addClientSite, data, dateTime);
//        editSite_view.refreshList(AppConstant.Add, site_model.getSiteId());
        editSite_view.refreshList(AppConstant.Add, "");
        EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.site_add));

        LogModel logModel = ActivityLogController
                .getObj(ActivityLogController.CLIENT_MODULE, ActivityLogController.SITE_ADD, ActivityLogController.CLIENT_MODULE);
        ActivityLogController.saveOfflineTable(logModel);
    }

    private void addtempSiteIntoDB(AddSiteModel addSiteModel, ArrayList<CustOmFormQuestionsRes> custOmFormQuestionsList) {
        Site_model tempSite = new Site_model();
        tempSite.setTempId(addSiteModel.getTempId());
        tempSite.setSiteId(addSiteModel.getTempId());
        tempSite.setDef(addSiteModel.getDef());
        tempSite.setZip(addSiteModel.getZip());
        tempSite.setCtry(String.valueOf(addSiteModel.getCtry()));
        tempSite.setState(String.valueOf(addSiteModel.getState()));
        tempSite.setCity(addSiteModel.getCity());
        tempSite.setAdr(addSiteModel.getAdr());
        tempSite.setSnm(addSiteModel.getSnm());
        tempSite.setCltId(addSiteModel.getCltId());
        tempSite.setLat(addSiteModel.getLat());
        tempSite.setLng(addSiteModel.getLng());
        tempSite.setCnm(addSiteModel.getCnm());
        tempSite.setConId(addSiteModel.getConId());
        tempSite.setCustomFieldArray(custOmFormQuestionsList);

        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().insertNewSite(tempSite);
        if (tempSite.getDef().equals("1")) {
            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().updateDefault(tempSite.getCltId(), tempSite.getSiteId());
        }
    }

    @Override
    public void addnewSiteToDB(Site_model site_model) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().insertNewSite(site_model);
        if (site_model.getDef().equals("1")) {
            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().updateDefault(site_model.getCltId(), site_model.getSiteId());
        }
    }

    @Override  //editsite
    public void EditClientSite(EditSiteModel editSiteModel, ArrayList<CustOmFormQuestionsRes> custOmFormQuestionsList) {

        Gson gson = new Gson();
        String data = gson.toJson(editSiteModel);

        String dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);

        OfflineDataController.getInstance().addInOfflineDB(Service_apis.updateClientSite, data, dateTime);


        Site_model site = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getSiteFromSiteId(editSiteModel.getSiteId());
        site.setSnm(editSiteModel.getSnm());
        site.setAdr(editSiteModel.getAdr());
        site.setCity(editSiteModel.getCity());
        site.setState(String.valueOf(editSiteModel.getState()));
        site.setCtry(String.valueOf(editSiteModel.getCtry()));
        site.setZip(editSiteModel.getZip());
        site.setDef(editSiteModel.getDef());
        site.setLat(editSiteModel.getLat());
        site.setLng(editSiteModel.getLng());

        site.setCnm(editSiteModel.getCnm());
        site.setConId(editSiteModel.getConId());
        site.setCustomFieldArray(custOmFormQuestionsList);
        //site.setCltId(editSiteModel.getCltId());


        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().update(site);
        if (site.getDef().equals("1")) {
            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().updateDefault(site.getCltId(), site.getSiteId());
        }
        editSite_view.refreshList(AppConstant.Edit, site.getSiteId());

        LogModel logModel = ActivityLogController
                .getObj(ActivityLogController.CLIENT_MODULE, ActivityLogController.SITE_EDIT, ActivityLogController.CLIENT_MODULE);
        ActivityLogController.saveOfflineTable(logModel);
    }

    @Override
    public void getCountryList() {
        counrtyList = SpinnerCountrySite.clientCountryList();
        editSite_view.setCountryList(counrtyList);
    }

    @Override
    public void getStateList(String ctry) {
        statesList = SpinnerCountrySite.clientStatesList(ctry);
        editSite_view.setStateList(statesList);
    }

    @Override
    public boolean isValidCountry(String country) {
        if (counrtyList != null)
            for (Country ctry : counrtyList) {
                if (ctry.getName().equalsIgnoreCase(country))
                    return true;
            }
        return false;
    }

    @Override
    public boolean isValidState(String state) {
        if (statesList != null)
            for (States item : statesList) {
                if (item.getName().equalsIgnoreCase(state))
                    return true;
            }
        return false;
    }


    @Override
    public void getContactList(String cltId) {
        List<ContactData> contactDataList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().getContactByClId(cltId);
        editSite_view.setContactList(contactDataList);
    }

    @Override
    public void getCustomFieldData(String sid) {
        Site_model site_model = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getSiteFromSiteId(sid);
        editSite_view.setCustomFieldData(site_model);
    }

}
