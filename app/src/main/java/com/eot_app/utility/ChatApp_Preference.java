package com.eot_app.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sonam-11 on 15/4/20.
 */

/***********Never clear this App prefrence because its for User admin chat notification******************/
public class ChatApp_Preference {
    public static ChatApp_Preference INSTANCE = new ChatApp_Preference();
    private final SharedPreferences.Editor editor;
    private final SharedPreferences sp;
    private final String PREF_NAME = "chat_pref";
    private final String CHAT_INFO = "chat_info";


    private final String USERGRPCHAT = "usr_grp_cht";

    public ChatApp_Preference() {
        sp = EotApp.getAppinstance().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static ChatApp_Preference getSharedprefInstance() {
        return INSTANCE;
    }

    public boolean getUserChatForGrp() {
        return sp.getBoolean(USERGRPCHAT, false);
    }

    public void setUserChatForGrp(boolean enable) {
        editor.putBoolean(USERGRPCHAT, enable);
        editor.commit();
    }

    public boolean getChatData() {
        return sp.getBoolean(CHAT_INFO, false);
    }

    public void setChatData(boolean enable) {
        editor.putBoolean(CHAT_INFO, enable);
        editor.commit();
    }
}
