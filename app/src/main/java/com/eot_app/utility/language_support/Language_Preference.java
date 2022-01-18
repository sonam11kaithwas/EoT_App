package com.eot_app.utility.language_support;

import android.content.Context;
import android.content.SharedPreferences;

import com.eot_app.nav_menu.setting.ModuleCodeModel;
import com.eot_app.utility.EotApp;

public class Language_Preference implements Language_Preference_pi {
    public static Language_Preference_pi INSTANCE = new Language_Preference();
    //   *********** setup Language preferences ********
    private final SharedPreferences sp;
    private final SharedPreferences.Editor editor;
    private final String PREF_NAME = "eot_pref_language";

    private final String BACKEND_MSG_MODEL = "backendMsgsModel";
    private final String MOBILE_MSG_MODEL = "mobileMsgsModel";
    private final String LANG_VERSION = "lang_version";
    private final String LANG_FILE_NAME = "language_filename";
    private final String IS_CHANGE_BY_USER = "is_change_by_user";
    private final String DEFAULT_PAGE_VIEW = "page_view";


    public Language_Preference() {
        sp = EotApp.getAppinstance().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static Language_Preference_pi getSharedprefInstance() {
        return INSTANCE;
    }

    @Override
    public String getBackendMsgsModel() {
        return sp.getString(BACKEND_MSG_MODEL, null);
    }

    @Override
    public void setBackendMsgsModel(String backendMsgsModel) {
        editor.putString(BACKEND_MSG_MODEL, backendMsgsModel);
        editor.commit();
    }

    @Override
    public String getMobileMsgsModel() {
        return sp.getString(MOBILE_MSG_MODEL, null);
    }

    @Override
    public void setMobileMsgsModel(String mobileMsgsModel) {
        editor.putString(MOBILE_MSG_MODEL, mobileMsgsModel);
        editor.commit();
    }

    @Override
    public String getlanguageVersion() {
        return sp.getString(LANG_VERSION, "0");
    }

    @Override
    public void setLanguageVersion(String version_no) {
        editor.putString(LANG_VERSION, version_no);
        editor.commit();
    }

    @Override
    public String getlanguageFilename() {
        return sp.getString(LANG_FILE_NAME, "");
    }

    @Override
    public void setLanguageFilename(String lang_filename) {
        editor.putString(LANG_FILE_NAME, lang_filename);
        editor.commit();
    }

    @Override
    public boolean isUserChangeLang() {
        return sp.getBoolean(IS_CHANGE_BY_USER, false);
    }

    @Override
    public void setisUserChangeLang(boolean is_change) {
        editor.putBoolean(IS_CHANGE_BY_USER, is_change);
        editor.commit();
    }

    @Override
    public void clearPreference() {
        editor.clear();
        editor.commit();
    }

    @Override
    public int getDefaultPageView() {
        return sp.getInt(DEFAULT_PAGE_VIEW, ModuleCodeModel.CALENDAR);
    }

    @Override
    public void setDefaultPageView(int moduleCode) {
        editor.putInt(DEFAULT_PAGE_VIEW, moduleCode);
        editor.commit();
    }
}
