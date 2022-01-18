package com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.mvp;

import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.model_pkg.AddEquReq;

/**
 * Created by Sonam-11 on 30/9/20.
 */
public interface AddJobEqu_Pi {
    void getCountryList();

    void getStateList(String countyId);

    void getCageryList();

    void getGrpList();

    void getBrandList();

    void addNewEquipment(AddEquReq addEquReq, String path);

    void convertItemToequip(AddEquReq addEquReq, String path);

    void getClientSiteList(String clientId);

    void getEquStatusList();

    String cntryId(String cntId);

    String statId(String state, String statename);

    boolean isValidCountry(String countryaaAaA);

    boolean isValidState(String state);

    boolean RequiredFields(String countryname, String statename, String equNm);

    void getClientSiteListServer(String clientId);
}
