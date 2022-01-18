package com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model;


import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Sonam-11 on 31/3/20.
 */
public class ChatMsgDataModel implements Serializable {
    private UserChatModel userChatModel;
    private MsgModel msgModel;
    private UserOflineOnlineModel userStatusModel;

    public ChatMsgDataModel() {
    }

    public MsgModel getMsgModel() {
        return msgModel;
    }

    public void setMsgModel(MsgModel msgModel) {
        this.msgModel = msgModel;
    }

    public UserChatModel getUserChatModel() {
        return userChatModel;
    }

    public void setUserChatModel(UserChatModel userChatModel) {
        this.userChatModel = userChatModel;
    }

    public UserOflineOnlineModel getUserStatusModel() {
        return userStatusModel;
    }

    public void setUserStatusModel(UserOflineOnlineModel userStatusModel) {
        this.userStatusModel = userStatusModel;
    }


    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
