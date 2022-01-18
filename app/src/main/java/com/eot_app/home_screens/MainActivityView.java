package com.eot_app.home_screens;

import com.eot_app.utility.language_support.Language_Model;

/**
 * Created by aplite_pc302 on 8/9/18.
 */

public interface MainActivityView {
    void onLogout(String msg);

    void onLogoutErrorOccur(String msg);

    void onClearCache();

    void seletedLanguage(Language_Model language_settings);

    void checkIdUpdateUI(String checkId, String msg);

    void check_In_Out_Fail();

    void updateVersionForcFully();

}
