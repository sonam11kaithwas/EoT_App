package com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.mvp;

import com.eot_app.nav_menu.audit.audit_list.equipment.model.EquipmentStatus;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.clientEqu.ClientEquRes;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.model_pkg.BrandData;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.model_pkg.GetCatgData;
import com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.model_pkg.GetgrpData;
import com.eot_app.utility.Country;
import com.eot_app.utility.States;

import java.util.List;

/**
 * Created by Sonam-11 on 30/9/20.
 */
public interface AddJobEqu_View {
    void setCountryList(List<Country> countryList);

    void setStateList(List<States> statesList);

    void setCategList(List<GetCatgData> cateList);

    void setGrpList(List<GetgrpData> grpList);

    void setBrandList(List<BrandData> brandList);

    void setEquReqError(String eqNm);

    void finishActivity();

    void sessionExpire(String msg);

    void addExpenseSuccesFully(String msg);

    void setClientSiteList(List<Site_model> siteModelList);

    void setEquStatusList(List<EquipmentStatus> equipmentStatusList);

    void convertEquipment(String msg);

    void setCountryError(String msg);

    void setStateError(String msg);

    void setClientSiteListServer(List<ClientEquRes> clientEquRes);
}
