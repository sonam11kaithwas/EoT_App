package com.eot_app.utility.util_interfaces;

import com.eot_app.login_next.login_next_model.Login_Responce_Model;
import com.eot_app.login_next.login_next_model.ResLoginData;
import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFormQuestionsRes;
import com.eot_app.utility.settings.company_settings.CompanyDetailsModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by geet-pc on 28/5/18.
 */

public interface Sp_model {
    void setLoginCredentials(String userId, String pass);

    String[] getLoginCredentials();

    void setLoginResponse(String res);

    ResLoginData getLoginRes();

    String getJobSyncTime();

    void setJobSyncTime(String dateTime);

    String getAppointmentSyncTime();

    void setAppointmentSyncTime(String dateTime);

    String getClientSyncTime();

    void setClientSyncTime(String dateTime);

    String getContactSyncTime();

    void setContactSyncTime(String dateTime);

    String getSiteSyncTime();

    void setSiteSyncTime(String dateTime);

    void clearSharedPreference();

    int getFirstSyncState();

    void setFirstSyncState(int state);

    Set<String> getHeadersSet(Set<String> objects);

    void setHeadersSet(HashSet<String> cookies);

    String getCompCode();

    void setCompCode(String cc);

    String getBaseURL();

    void setBaseURL(String url);

    void setEmailResponce(String emailResponce);

    Login_Responce_Model getEmailRespone();

    String getRegion();

    void setRegion(String region);

    int getBatteryRequest();

    void setBatteryRequest(int baterryStatus);

    String getIS_24HOURS();

    void setIS_24HOURS(String is_24HOURS);

    CompanyDetailsModel getCompanySettingsDetails();

    void setCompanySettingsDetails(String res);

    String getFirebaseDeviceToken();

    void setFirebaseDeviceToken(String firebase_token);

    void setcheckId(String check_in_out);

    String getcheckId();

    void setBlankTokenOnSessionExpire();

    String getUsersSyncTime();

    void setUsersSyncTime(String user);

    String getInventryItemSyncTime();

    void setInventryItemSyncTime(String inventryItemSyncTime);

    String getInventryTaxesSyncTime();

    void setInventryTaxesSyncTime(String inventryTaxesyncTime);

    String getAuditSyncTime();

    void setAuditSyncTime(String dateTime);

    String getContractSyncTime();

    void setContractSyncTime(String dateTime);

    String getAllEquipmentSyncTime();

    void setAllEquipmentSyncTime(String dateTime);

    String getEquipmentSyncTime();

    void setEquipmentSyncTime(String dateTime);

    float getLanFileVer();

    void setLanFileVer(float dateTime);

    void deleteLoginResponce();

    ArrayList<CustOmFormQuestionsRes> getSiteCustomFiled();

    void setSiteCustomFiled(String customFiled);

    void setJobCustomField(String customFields);

    ArrayList<CustOmFormQuestionsRes> getJobCustomFields();

    void setResgistrationToken(String token);

    String getResgistartionToken();

    void deleteRegistrationToekn();

    boolean getSiteNameShowInSetting();

    void setSiteNameShowInSetting(boolean isshow);

    ArrayList<CustOmFormQuestionsRes> getAuditCustomFiled();

    void setAuditCustomFiled(String customFiled);

    String getTaxLocationSyncTime();

    void setTaxLocationSyncTime(String dateTime);

    String getShiftTimeSyncTime();

    void setShiftTimeSyncTime(String dateTime);

    String getShiftClockTime();

    void setShiftClockime(String dateTime);

    String getCheckInTime();

    void setCheckInTime(String dateTime);
//
//    String getLastAppKillTime();
//
//    void setLastAppKillTime(String dateTime);


}
