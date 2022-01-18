package com.eot_app.nav_menu.audit.addAudit.add_audit_mvp;

import com.eot_app.nav_menu.audit.addAudit.add_aduit_model_pkg.AddAudit_Req;
import com.eot_app.nav_menu.client.clientlist.client_detail.contact.client_dao.ContactData;
import com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist.Site_model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface AddAduit_pi {


    void getWorkerList();

    void getClientList();

    void getSiteList(String cltId);

    void getCOntactList(String cltId);

    void getCountryList();

    void getStateList(String countyName);

    boolean RequiredFields(String cltId, boolean contactSelf, boolean siteSelf, String conNm, String siteNm, String adr, Set<String> auditorsId, String countryname, String statename, String mob, String alterNateMob, String email);

    void getCurrentdateTime();

    boolean isValidCountry(String countryaaAaA);

    boolean isValidState(String state);

    String cntryId(String cntId);

    String statId(String state, String statename);

    void getTagDataList();

    void getContractList(String cltId);

    void callApiForAddAudit(AddAudit_Req addAudit_req);

    ContactData getDefaultContactDataForClient(String cltId);

    Site_model getDefaultSiteDataForClient(String cltId);

    void addJobWithImageDescription(AddAudit_Req addJob_req, ArrayList<String> links, List<String> fileNames);


}
