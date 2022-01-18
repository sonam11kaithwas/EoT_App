package com.eot_app.nav_menu.client.clientlist.client_detail.contact.edit_contact.editmodel;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.activitylog.LogModel;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.Eot_Validation;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.db.OfflineDataController;
import com.eot_app.utility.language_support.LanguageController;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ubuntu on 9/6/18.
 */

public class EditContact_Pc implements EditContact_Pi {

    EditContact_View editContact_view;
    ContactData contactData;

    public EditContact_Pc(EditContact_View editContact_view) {
        this.editContact_view = editContact_view;
    }

    @Override
    public void setEditContactData(ContactData contactData) {
        contactData = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().getContactById(contactData.getConId());
        editContact_view.EditContactviewUI(contactData);
    }

    @Override
    public void getSiteFromdb(String cltId) {
        List<Site_model> siteModelList = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getSitesByCltId(cltId);
        editContact_view.setSiteList(siteModelList);
    }

    @Override
    public boolean checkValidation(String name, String email, String mob, String alterNate) {
        if (name.equals("")) {
            editContact_view.setNameError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.cont_name));
            return false;
        } else if (((!email.equals("")) && !Eot_Validation.email_checker(email).equals(""))) {
            editContact_view.setEmailError(Eot_Validation.email_checker(email));
            return false;
        } else if (!mob.isEmpty() && !mob.equalsIgnoreCase(App_preference.getSharedprefInstance().getLoginRes().getCtryCode()) && mob.length() < AppConstant.MOBILE_LIMIT) {
            editContact_view.setMobError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_mob_lent));
            return false;
        } else if (!alterNate.isEmpty() && !mob.equalsIgnoreCase(App_preference.getSharedprefInstance().getLoginRes().getCtryCode()) && alterNate.length() < AppConstant.MOBILE_LIMIT) {

            editContact_view.setAlterNateError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_alter_mob_lent));
            return false;

        }
        return true;

    }

    @Override  //add contact
    public void AddNewClientContact(ClientContactAddEdit_Model clientContactAddEdit_model) {
        clientContactAddEdit_model.setTempId(AppUtility.getTempIdFormat("Contact"));
        addtempContactIntoDB(clientContactAddEdit_model);
        EotApp.getAppinstance().notifyCon_SiteObserver(Service_apis.addClientContact);

        Gson gson = new Gson();
        String data = gson.toJson(clientContactAddEdit_model);

        String dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);
        OfflineDataController.getInstance().addInOfflineDB(Service_apis.addClientContact, data, dateTime);
//        refresh list after local changes.
        editContact_view.setResultForChangeInContact("NewContact", "");
        EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.contact_added_successfully));

        LogModel logModel = ActivityLogController
                .getObj(ActivityLogController.CLIENT_MODULE,
                        ActivityLogController.CLIENT_ADD_CONTACT,
                        ActivityLogController.CLIENT_MODULE);

        ActivityLogController.saveOfflineTable(logModel);
    }

    private void addtempContactIntoDB(ClientContactAddEdit_Model model) {
        ContactData tempContact = new ContactData();
        tempContact.setTempId(model.getTempId());
        tempContact.setConId(model.getTempId());
        tempContact.setDef(String.valueOf(model.getDef()));
        tempContact.setSkype(model.getSkype());
        tempContact.setTwitter(model.getTwitter());
        tempContact.setFax(model.getFax());
        tempContact.setMob2(model.getMob2());
        tempContact.setMob1(model.getMob1());
        tempContact.setEmail(model.getEmail());
        tempContact.setCnm(model.getCnm());
        tempContact.setCltId(model.getCltId());
        //  tempContact.setSiteId(model.getSiteIds());
        List<SiteId> siteIdsli = new ArrayList<>();

        for (String id : model.getSiteId()) {
            /**
             * Eye05307 crash issue fixes due to wrong query
             * */
            // JobTitle jobTitle = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).jobTitleModel().getJobTitleByid(id);
            Site_model site_model = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getSiteFromSiteId(id);
            if (site_model != null)
                siteIdsli.add(new SiteId(id, site_model.getSnm()));

        }
        tempContact.setSiteId(siteIdsli);
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().insertNewContact(tempContact);
        if (tempContact.getDef().equals("1")) {
            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().updateDefaultContact(tempContact.getConId(), tempContact.getCltId());
        }
    }

    @Override
    public void addNewContactToDB(ContactData newcontactdata) {
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().insertNewContact(newcontactdata);

    }

    @Override  //edit contact
    public void EditClientContact(ClientContactEdit_Model clientContactEdit, String cltId) {
        Gson gson = new Gson();
        String data = gson.toJson(clientContactEdit);

        String dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);

//      edit contact data
        OfflineDataController.getInstance().addInOfflineDB(Service_apis.updateClientContact, data, dateTime);
//      update contact in DB
        ContactData contactData = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().getContactById(clientContactEdit.getConId());
        contactData.setCnm(clientContactEdit.getCnm());
        contactData.setEmail(clientContactEdit.getEmail());
        contactData.setMob1(clientContactEdit.getMob1());
        contactData.setMob2(clientContactEdit.getMob2());
        contactData.setFax(clientContactEdit.getFax());
        contactData.setSkype(clientContactEdit.getSkype());
        contactData.setTwitter(clientContactEdit.getTwitter());
        contactData.setDef(String.valueOf(clientContactEdit.getDef()));

        List<SiteId> siteIdsli = new ArrayList<>();
        for (String id : clientContactEdit.getSiteId()) {
            Site_model jobTitle = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).sitemodel().getSiteFromSiteId(id);
            siteIdsli.add(new SiteId(id, jobTitle.getSnm()));
        }
        contactData.setSiteId(siteIdsli);
        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().update(contactData);
        if (clientContactEdit.getDef() == 1) {
            AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).contactModel().updateDefaultContact(contactData.getConId(), cltId);
        }
        editContact_view.setResultForChangeInContact("EditContact", contactData.getConId());

        LogModel logModel = ActivityLogController
                .getObj(ActivityLogController.CLIENT_MODULE,
                        ActivityLogController.CLIENT_EDIT_CONTACT,
                        ActivityLogController.CLIENT_MODULE);
        ActivityLogController.saveOfflineTable(logModel);
    }
}
