package com.eot_app.nav_menu.jobs.add_job.addjob_presenter;

import com.eot_app.nav_menu.jobs.add_job.addjobmodel.AddJob_Req;
import com.eot_app.utility.settings.setting_db.FieldWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface Add_job_pi {
    void getTimeShiftList();

    void getJobTitleList();

    void getClientList();

    void getSiteList(String cltId);

    void getCOntactList(String cltId);

    void getCountryList();

    void getStateList(String countyName);

    void getWorkerList();

    void callApiForAddJob(AddJob_Req addJobReq);

    void addJobWithImageDescription(AddJob_Req addJob_req, ArrayList<String> links, List<String> fileNames);

    boolean RequiredFields(String cltId, boolean contactSelf, boolean siteSelf, String conNm, String siteNm, String adr, Set<String> jtId,
                           String countryname, String statename, String mob, String alterNateMob, String email, boolean WEEKLYRECUR, int weekdaysize);


    void getCurrentdateTime();

    boolean isValidCountry(String countryaaAaA);

    boolean isValidState(String state);

    String cntryId(String cntId);

    String statId(String state, String statename);

    void getTagDataList();

    FieldWorker getDefaultFieldWorker();

    void getContractList(String cltId);

    boolean RequiredFieldsForRiviste(String clientReq, String adr, Set<String> jtIdList, String ctry_id, String state_id, String mob1, String mob11, String email);


    void getEndTime(String datestr, String s);

}
