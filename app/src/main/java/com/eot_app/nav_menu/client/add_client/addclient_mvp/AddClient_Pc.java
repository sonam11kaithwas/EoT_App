package com.eot_app.nav_menu.client.add_client.addclient_mvp;

import com.eot_app.activitylog.ActivityLogController;
import com.eot_app.activitylog.LogModel;
import com.eot_app.nav_menu.client.add_client.addclient_model_pkg.AddClientModel;
import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.editsite.editsitedb.SpinnerCountrySite;
import com.eot_app.services.Service_apis;
import com.eot_app.utility.AppConstant;
import com.eot_app.utility.AppUtility;
import com.eot_app.utility.App_preference;
import com.eot_app.utility.Country;
import com.eot_app.utility.EotApp;
import com.eot_app.utility.Eot_Validation;
import com.eot_app.utility.States;
import com.eot_app.utility.db.AppDataBase;
import com.eot_app.utility.db.OfflineDataController;
import com.eot_app.utility.language_support.LanguageController;
import com.eot_app.utility.settings.clientaccount_db.ClientAccountType;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ubuntu on 14/6/18.
 */

public class AddClient_Pc implements AddClient_Pi {
    AddClient_View addClient_view;
    AppDataBase myDB;
    List<ClientAccountType> accountItem;
    int accId = 0;
    List<Country> countrylist;//= new ArrayList<>();
    List<States> stateslist;//= new ArrayList<>();

    public AddClient_Pc(AddClient_View addClient_view) {
        myDB = AppDataBase.getInMemoryDatabase(EotApp.getAppinstance());
        this.addClient_view = addClient_view;
        countrylist = new ArrayList<>();
        stateslist = new ArrayList<>();
    }

    @Override
    public Boolean addClientValidation(String name, String contactName, String email, String siteName, String add, String countryname, String statename, String mob) {
        if (name.equals("")) {
            addClient_view.setNameError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_client_name));
            return false;
        } else if (contactName.equals("")) {
            addClient_view.setNameError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.cont_name));
            return false;
        } else if ((!email.equals("")) && !Eot_Validation.email_checker(email).equals("")) {
            addClient_view.setEmailError(Eot_Validation.email_checker(email));
            return false;
        }
        //else if ((!mob.equals("")) && !Eot_Validation.mob_checker(mob).equals("")) {
        else if (!mob.isEmpty() && !mob.equalsIgnoreCase(App_preference.getSharedprefInstance().getLoginRes().getCtryCode()) && mob.length() < AppConstant.MOBILE_LIMIT) {
            addClient_view.setMobError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_mob_lent));
            return false;
        } else if (siteName.equals("")) {
            addClient_view.setNameError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_site_name));
            return false;
        } else if (!(add.length() > 0)) {
            addClient_view.setAddError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.err_addr));
            return false;
        } else if (!isValidCountry(countryname)) {
            addClient_view.setCountryError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.country_error));
            return false;
        } else if (!isValidState(statename)) {
            addClient_view.setStateError(LanguageController.getInstance().getMobileMsgByKey(AppConstant.state_error));
            return false;
        }
        return true;
    }


    @Override
    public void accountItemSpinner() {
        accountItem = myDB.clientAccount().getAccountList();
        ArrayList<String> accountList = new ArrayList<String>();
        for (int i = 0; i < accountItem.size(); i++) {
            accountList.add(accountItem.get(i).getType());
        }
        addClient_view.setAccountType(accountList);
    }

    @Override
    public int accountItemSelect(String name) {
        for (int i = 0; i < accountItem.size(); i++)
            if (accountItem.get(i).getType().equals(name)) {
                accId = Integer.parseInt(accountItem.get(i).getAccId());
                break;
            }
        return accId;
    }


    @Override
    public void clientCountryList() {
        countrylist = SpinnerCountrySite.clientCountryList();
        addClient_view.setCountryList(countrylist);
    }


    @Override
    public void clientStatesList(String cntryId) {
        stateslist = SpinnerCountrySite.clientStatesList(cntryId);
        addClient_view.setStateList(stateslist);
    }

    @Override
    public void addClientCall(AddClientModel addClientModel) {
        addClientModel.setTempId(AppUtility.getTempIdFormat("Client"));
        addtempClientIntoDB(addClientModel);

        Gson gson = new Gson();
        String data = gson.toJson(addClientModel);
        String dateTime = AppUtility.getDateByFormat(AppConstant.DATE_TIME_FORMAT);

        OfflineDataController.getInstance().addInOfflineDB(Service_apis.addClient, data, dateTime);

//      update adapter list
        EotApp.getAppinstance().notifyApiObserver(Service_apis.addClient);

        addClient_view.onAddNewClient("");
        EotApp.getAppinstance().showToastmsg(LanguageController.getInstance().getMobileMsgByKey(AppConstant.clt_added));

        LogModel logModel = ActivityLogController
                .getObj(ActivityLogController.CLIENT_MODULE, ActivityLogController.CLIENT_ADD, ActivityLogController.CLIENT_MODULE);
        ActivityLogController.saveOfflineTable(logModel);
    }


    /**
     * Add new client record in DB
     */
    private void addtempClientIntoDB(AddClientModel addClientModel) {
        Client tempclient = new Client();
        tempclient.setTempId(addClientModel.getTempId());
        tempclient.setCltId(addClientModel.getTempId());
        tempclient.setNm(addClientModel.getNm());
        /***/
        tempclient.setCnm(addClientModel.getCnm());
        tempclient.setPymtType(String.valueOf(addClientModel.getPymtType()));
        tempclient.setSnm(addClientModel.getSnm());
        tempclient.setZip(addClientModel.getZip());

        tempclient.setAdr(addClientModel.getAdr());
        tempclient.setCity(addClientModel.getCity());
        tempclient.setState(addClientModel.getState());
        tempclient.setCtry(addClientModel.getCtry());
        tempclient.setNote(addClientModel.getNote());
        tempclient.setIndustry(String.valueOf(addClientModel.getIndustry()));
        tempclient.setTinNo(addClientModel.getTinNo());
        tempclient.setGstNo(addClientModel.getGstNo());
        tempclient.setMob1(addClientModel.getMob1());
        tempclient.setEmail(addClientModel.getEmail());
        tempclient.setLat(addClientModel.getLat());
        tempclient.setLng(addClientModel.getLng());
        tempclient.setLng(addClientModel.getIndustryName());
        tempclient.setReferral(addClientModel.getReferral());


        AppDataBase.getInMemoryDatabase(EotApp.getAppinstance()).clientModel().insertNewClient(tempclient);
    }

    @Override
    public boolean isValidCountry(String country) {
        for (Country ctry : countrylist) {
            if (ctry.getName().equalsIgnoreCase(country))
                return true;
        }
        return false;
    }

    @Override
    public boolean isValidState(String state) {
        for (States item : stateslist) {
            if (item.getName().equalsIgnoreCase(state))
                return true;
        }
        return false;
    }
}
