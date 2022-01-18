package com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model;

import java.util.Comparator;

/**
 * Created by Sonam-11 on 14/4/20.
 */
public class UserCompareByTime implements Comparator<ChatMsgDataModel> {

    @Override
    public int compare(ChatMsgDataModel o1, ChatMsgDataModel o2) {
        if (o1.getMsgModel().getCreatedAt() == null || o2.getMsgModel().getCreatedAt() == null) {
            return 0;
        }
        return o2.getMsgModel().getCreatedAt().compareTo(o1.getMsgModel().getCreatedAt());
    }
}