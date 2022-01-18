package com.eot_app.nav_menu.audit.addAudit.add_audit_mvp;

import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
import com.eot_app.utility.Country;
import com.eot_app.utility.States;
import com.eot_app.utility.settings.contractdb.ContractRes;
import com.eot_app.utility.settings.setting_db.FieldWorker;
import com.eot_app.utility.settings.setting_db.TagData;

import java.util.ArrayList;
import java.util.List;

public interface Add_AduitView {
    void setWorkerList(ArrayList<FieldWorker> fieldWorkerlist);

    void setClientlist(List<Client> data);

    void setContractlist(List<ContractRes> contractlist);

    void setSiteList(List<Site_model> data);

    void setContactList(List<ContactData> data);

    void setCountryList(List<Country> countryList);

    void setStateList(List<States> statesList);

    void finishActivity();

    void clientNameError(String msg);

    void setAddr_Error(String msg);

    void set_auditor_Error(String title);

    void setMobError(String msg);

    void setEmailError(String msg);

    void set_Str_DTime(String str_dt_tm, String time_str);

    void set_End_Date_Time(String std);

    void set_str_DT_after_cur(String std);

    void setSetTagData(List<TagData> tagslist);

    void setCountryError(String msg);

    void setStateError(String msg);

    void errorMsg(String error);
}
