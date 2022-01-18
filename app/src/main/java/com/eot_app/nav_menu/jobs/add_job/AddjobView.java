package com.eot_app.nav_menu.jobs.add_job;

import com.eot_app.nav_menu.client.client_db.Client;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
import com.eot_app.time_shift_pkg.ShiftTimeReSModel;
import com.eot_app.utility.Country;
import com.eot_app.utility.States;
import com.eot_app.utility.settings.contractdb.ContractRes;
import com.eot_app.utility.settings.setting_db.FieldWorker;
import com.eot_app.utility.settings.setting_db.JobTitle;
import com.eot_app.utility.settings.setting_db.TagData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aplite_pc302 on 6/27/18.
 */

public interface AddjobView {
    void setTimeShiftList(List<ShiftTimeReSModel> list);

    void SetJobTittle(ArrayList<JobTitle> datastr);

    void setClientlist(List<Client> data);

    void setContractlist(List<ContractRes> contractlist);

    void setSiteList(List<Site_model> data);

    void setContactList(List<ContactData> data);

    void setCountryList(List<Country> countryList);

    void setStateList(List<States> statesList);

    void setWorkerList(List<FieldWorker> fieldWorkerlist);

    void finishActivity();

    void jobPrioritySet();

    void set_Str_DTime(String str_dt_tm, String time_str);

    void set_End_Date_Time(String std);

    void set_str_DT_after_cur(String std);

    void setSetTagData(List<TagData> tagslist);

    void showErrorMsgsForValidation(String msg);
}
