package com.eot_app.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.eot_app.login_next.login_next_model.Login_Responce_Model;
import com.eot_app.login_next.login_next_model.ResLoginData;
import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFormQuestionsRes;
import com.eot_app.utility.settings.company_settings.CompanyDetailsModel;
import com.eot_app.utility.util_interfaces.Sp_model;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by geet-pc on 28/5/18.
 */

public class App_preference implements Sp_model {
    // ***** make it singleton *******
    public static Sp_model INSTANCE = new App_preference();
    //    app open
    private final String IS_24HOURS = "is_24hours";
    private final String BASE_URL = "base_url";
    private final String COMPNY_SETTINGS = "CompanySettingDetails";
    private final String FIREBASE_DEVICE_TOKEN = "firebase_device_token";
    private final String CHECK_IN_OUT = "check_in_out";
    //   *********** setup preferences ********
    private final SharedPreferences sp;
    private final SharedPreferences.Editor editor;
    private final String PREF_NAME = "eot_pref";
    private final String LOGIN_RESPONSE = "login_response";
    private final String LOGIN_ID = "login_id";
    private final String LOGIN_PASS = "login_pass";
    private final String USERCHAT_TIME = "userchat_time";
    private final String INVENTRYITEM_TIME = "inventryitem_time";
    private final String INVEBTRYTAXES_TIME = "inventrytaxes_time";
    //    time stamp for update
    private final String JOBLIST_TIME = "joblist_time";
    private final String AUDITLIST_TIME = "auditlist_time";
    private final String EQUIPMENT_TIME = "equpmentlist_time";
    private final String APPOINTMENT_LIST_TIME = "appointmentlist_time";
    private final String CLIENTLIST_TIME = "clientlist_time";
    private final String CONTACTLIST_TIME = "contactlist_time";
    private final String SITELIST_TIME = "sitelist_pass";
    private final String FIRSTSYNC_STATE = "firstsync_state";
    private final String COMPNY_CODE = "cc";
    private final String REGION = "region";
    private final String PREF_COOKIES = "pref_cookies";
    private final String EMAIL_RESPONCE = "email_responce";
    private final String LAG_FILE_VERSION = "lag_file_version";
    private final String CONTRACTLIST_TIME = "contractlist_time";
    private final String ALL_EQUIPMENT_SYNC_TIME = "allequipmentsynctime";
    private final String SITECUSTOMFILED = "siteCustomFiled";
    private final String AUDITCUSTOMFILED = "auditCustomFiled";
    private final String JOBCUSTOMFIELD = "jobcustomFields";
    private final String REGISTRATION_TOKEN = "registration_toekn";
    private final String SHOW_SITE_NAME = "show_site_name";
    private final String TAX_LOCATION = "tax_location";
    private final String SHIFT_TIME = "shift_time";
    private final String SHIFT_CLOCK_TIME = "shift_clock_time";
    private final String CHECK_IN_TIME = "check_in_time";
    //    private final String APP_KILL_TIME = "app_kill_time";
    private String REQUEST_IGNORE_BATTERY_OPTIMIZATIONS;

    public App_preference() {
        sp = EotApp.getAppinstance().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static Sp_model getSharedprefInstance() {
        return INSTANCE;
    }

    @Override
    public ArrayList<CustOmFormQuestionsRes> getSiteCustomFiled() {
        String convert = sp.getString(SITECUSTOMFILED, "");
        Type listType = new TypeToken<List<CustOmFormQuestionsRes>>() {
        }.getType();
        ArrayList<CustOmFormQuestionsRes> data = new Gson().fromJson(convert, listType);
        return data;
    }

    @Override
    public void setSiteCustomFiled(String customFiled) {
        editor.putString(SITECUSTOMFILED, customFiled);
        editor.commit();
    }

    @Override
    public ArrayList<CustOmFormQuestionsRes> getAuditCustomFiled() {
        String convert = sp.getString(AUDITCUSTOMFILED, "");
        Type listType = new TypeToken<List<CustOmFormQuestionsRes>>() {
        }.getType();
        ArrayList<CustOmFormQuestionsRes> data = new Gson().fromJson(convert, listType);
        return data;
    }

    @Override
    public void setAuditCustomFiled(String customFiled) {
        editor.putString(AUDITCUSTOMFILED, customFiled);
        editor.commit();
    }

    @Override
    public ArrayList<CustOmFormQuestionsRes> getJobCustomFields() {
        String convert = sp.getString(JOBCUSTOMFIELD, "");
        Type listType = new TypeToken<List<CustOmFormQuestionsRes>>() {
        }.getType();
        ArrayList<CustOmFormQuestionsRes> data = new Gson().fromJson(convert, listType);
        return data;
    }

    @Override
    public void setJobCustomField(String customFields) {
        editor.putString(JOBCUSTOMFIELD, customFields);
        editor.commit();
    }

    @Override
    public float getLanFileVer() {
        return sp.getFloat(LAG_FILE_VERSION, 0.0f);
    }

    @Override
    public void setLanFileVer(float ver) {
        editor.putFloat(LAG_FILE_VERSION, ver);
        editor.commit();
    }

    @Override
    public String getRegion() {
        return sp.getString(REGION, "");
    }

    @Override
    public void setRegion(String region) {
        editor.putString(REGION, region);
        editor.commit();
    }

    @Override
    public int getBatteryRequest() {
        return sp.getInt(REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, 0);
    }

    @Override
    public void setBatteryRequest(int baterryStatus) {
        editor.putInt(REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, baterryStatus);
        editor.commit();
    }

    @Override
    public String getInventryItemSyncTime() {
        return sp.getString(INVENTRYITEM_TIME, "");
    }

    @Override
    public void setInventryItemSyncTime(String inventryItemSyncTime) {
        editor.putString(INVENTRYITEM_TIME, inventryItemSyncTime);
        editor.commit();
    }

    @Override
    public String getTaxLocationSyncTime() {
        return sp.getString(TAX_LOCATION, "");
    }

    @Override
    public void setTaxLocationSyncTime(String dateTime) {
        editor.putString(TAX_LOCATION, dateTime);
        editor.commit();
    }

    @Override
    public String getShiftTimeSyncTime() {
        return sp.getString(SHIFT_TIME, "");
    }

    @Override
    public void setShiftTimeSyncTime(String dateTime) {
        editor.putString(SHIFT_TIME, dateTime);
        editor.commit();
    }

    @Override
    public String getShiftClockTime() {
        return sp.getString(SHIFT_CLOCK_TIME, "00");
    }

    @Override
    public void setShiftClockime(String dateTime) {
        editor.putString(SHIFT_CLOCK_TIME, dateTime);
        editor.commit();
    }
//
//    @Override
//    public String getLastAppKillTime() {
//        return sp.getString(APP_KILL_TIME, "");
//
//    }
//
//    @Override
//    public void setLastAppKillTime(String dateTime) {
//        editor.putString(APP_KILL_TIME, dateTime);
//        editor.commit();
//    }

    @Override
    public String getCheckInTime() {
        return sp.getString(CHECK_IN_TIME, "");
    }

    @Override
    public void setCheckInTime(String dateTime) {
        editor.putString(CHECK_IN_TIME, dateTime);
        editor.commit();
    }

    @Override
    public String getInventryTaxesSyncTime() {
        return sp.getString(INVEBTRYTAXES_TIME, "");
    }

    @Override
    public void setInventryTaxesSyncTime(String inventryTaxesyncTime) {
        editor.putString(INVEBTRYTAXES_TIME, inventryTaxesyncTime);
        editor.commit();
    }

    @Override
    public String getUsersSyncTime() {
        return sp.getString(USERCHAT_TIME, "");
    }

    @Override
    public void setUsersSyncTime(String dateTime) {
        editor.putString(USERCHAT_TIME, dateTime);
        editor.commit();
    }

    @Override
    public String getBaseURL() {
        return sp.getString(BASE_URL, AppConstant.BASEURL);
    }

    @Override
    public void setBaseURL(String url) {
        editor.putString(BASE_URL, url);
        editor.commit();
        editor.apply();
    }

    @Override
    public void setEmailResponce(String emailResponce) {
        editor.putString(EMAIL_RESPONCE, emailResponce);
        editor.commit();
    }

    @Override
    public Login_Responce_Model getEmailRespone() {
        Gson gson = new Gson();
        Login_Responce_Model responce = gson.fromJson(sp.getString(EMAIL_RESPONCE, null), Login_Responce_Model.class);
        return responce;
    }

    @Override
    public void setLoginCredentials(String userId, String pass) {
        editor.putString(LOGIN_ID, userId);
        editor.putString(LOGIN_PASS, pass);
        editor.commit();
    }

    @Override
    public String[] getLoginCredentials() {
        String[] loginCredentials = new String[2];
        loginCredentials[0] = sp.getString(LOGIN_ID, "");
        loginCredentials[1] = sp.getString(LOGIN_PASS, "");
        return loginCredentials;
    }

    @Override
    public void setLoginResponse(String res) {
        editor.putString(LOGIN_RESPONSE, res);
        editor.commit();
    }

    @Override
    public ResLoginData getLoginRes() {
        Gson gson = new Gson();
        ResLoginData res = gson.fromJson(sp.getString(LOGIN_RESPONSE, null), ResLoginData.class);
        return res;
    }

    @Override
    public String getJobSyncTime() {
        return sp.getString(JOBLIST_TIME, "");
    }

    @Override
    public void setJobSyncTime(String dateTime) {
        editor.putString(JOBLIST_TIME, dateTime);
        editor.commit();
    }


    @Override
    public String getAuditSyncTime() {
        return sp.getString(AUDITLIST_TIME, "");
    }

    @Override
    public void setAuditSyncTime(String dateTime) {
        editor.putString(AUDITLIST_TIME, dateTime);
        editor.commit();
    }

    @Override
    public String getContractSyncTime() {
        return sp.getString(CONTRACTLIST_TIME, "");
    }

    @Override
    public void setContractSyncTime(String dateTime) {
        editor.putString(CONTRACTLIST_TIME, dateTime);
        editor.commit();
    }

    @Override
    public String getAllEquipmentSyncTime() {
        return sp.getString(ALL_EQUIPMENT_SYNC_TIME, "");
    }

    @Override
    public void setAllEquipmentSyncTime(String dateTime) {
        editor.putString(ALL_EQUIPMENT_SYNC_TIME, dateTime);
        editor.commit();
    }

    @Override
    public String getEquipmentSyncTime() {
        return sp.getString(EQUIPMENT_TIME, "");
    }

    @Override
    public void setEquipmentSyncTime(String dateTime) {
        editor.putString(EQUIPMENT_TIME, dateTime);
        editor.commit();
    }

    @Override
    public String getAppointmentSyncTime() {
        return sp.getString(APPOINTMENT_LIST_TIME, "");
    }

    @Override
    public void setAppointmentSyncTime(String dateTime) {
        editor.putString(APPOINTMENT_LIST_TIME, dateTime);
        editor.commit();
    }

    @Override
    public String getClientSyncTime() {
        return sp.getString(CLIENTLIST_TIME, "");
    }

    @Override
    public void setClientSyncTime(String dateTime) {
        editor.putString(CLIENTLIST_TIME, dateTime);
        editor.commit();
    }

    @Override
    public String getContactSyncTime() {
        return sp.getString(CONTACTLIST_TIME, "");
    }

    @Override
    public void setContactSyncTime(String dateTime) {
        editor.putString(CONTACTLIST_TIME, dateTime);
        editor.commit();
    }

    @Override
    public String getSiteSyncTime() {
        return sp.getString(SITELIST_TIME, "");
    }

    @Override
    public void setSiteSyncTime(String dateTime) {
        editor.putString(SITELIST_TIME, dateTime);
        editor.commit();
    }

    @Override
    public void clearSharedPreference() {
        editor.clear();
        editor.commit();
    }

    @Override
    public int getFirstSyncState() {
        return sp.getInt(FIRSTSYNC_STATE, 0);
    }

    @Override
    public void setFirstSyncState(int state) {
        editor.putInt(FIRSTSYNC_STATE, state);
        editor.commit();
    }

    @Override
    public Set<String> getHeadersSet(Set<String> objects) {
        return sp.getStringSet(PREF_COOKIES, objects);
    }

    @Override
    public void setHeadersSet(HashSet<String> cookies) {
        editor.putStringSet(PREF_COOKIES, cookies);
        editor.commit();
    }

    @Override
    public String getCompCode() {
        return sp.getString(COMPNY_CODE, "");
    }

    @Override
    public void setCompCode(String cc) {
        editor.putString(COMPNY_CODE, cc);
        editor.commit();
    }

    @Override
    public String getIS_24HOURS() {
        try {
            return sp.getString(IS_24HOURS, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void setIS_24HOURS(String is_24HOURS) {
        editor.putString(IS_24HOURS, is_24HOURS);
        editor.commit();
    }

    @Override
    public CompanyDetailsModel getCompanySettingsDetails() {
        try {
            Gson gson = new Gson();
            CompanyDetailsModel res = gson.fromJson(sp.getString(COMPNY_SETTINGS, null), CompanyDetailsModel.class);
            return res;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setCompanySettingsDetails(String res) {
        editor.putString(COMPNY_SETTINGS, res);
        editor.commit();
    }

    @Override
    public String getFirebaseDeviceToken() {
        return sp.getString(FIREBASE_DEVICE_TOKEN, "");
    }

    @Override
    public void setFirebaseDeviceToken(String firebase_token) {
        editor.putString(FIREBASE_DEVICE_TOKEN, firebase_token);
        editor.commit();
    }

    @Override
    public void setcheckId(String check_in_out) {
        editor.putString(CHECK_IN_OUT, check_in_out);
        editor.commit();
    }

    @Override
    public String getcheckId() {
        return sp.getString(CHECK_IN_OUT, "");
    }

    @Override
    public void setBlankTokenOnSessionExpire() {
        ResLoginData data = App_preference.getSharedprefInstance().getLoginRes();
        data.setToken("");

        App_preference.getSharedprefInstance().setLoginResponse(new Gson().toJson(data));
    }

    @Override
    public void deleteLoginResponce() {
        editor.remove(LOGIN_RESPONSE);
        editor.commit();
    }

    @Override
    public void setResgistrationToken(String token) {
        editor.putString(REGISTRATION_TOKEN, token);
        editor.commit();
    }

    @Override
    public String getResgistartionToken() {

        return sp.getString(REGISTRATION_TOKEN, "");
    }

    @Override
    public void deleteRegistrationToekn() {
        editor.remove(REGISTRATION_TOKEN);
        editor.commit();
    }

    @Override
    public boolean getSiteNameShowInSetting() {
        return sp.getBoolean(SHOW_SITE_NAME, false);
    }

    @Override
    public void setSiteNameShowInSetting(boolean isshow) {
        editor.putBoolean(SHOW_SITE_NAME, isshow);
        editor.commit();
    }
}
