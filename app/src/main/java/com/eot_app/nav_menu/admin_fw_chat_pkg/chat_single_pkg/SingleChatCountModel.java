package com.eot_app.nav_menu.admin_fw_chat_pkg.chat_single_pkg;

/**
 * Created by Sonam-11 on 2020-03-11.
 */
public class SingleChatCountModel {
    int chatCount;

    public SingleChatCountModel(int chatCount) {
        this.chatCount = chatCount;
    }

    public SingleChatCountModel() {
    }

    public int getChatCount() {
        return chatCount;
    }

    public void setChatCount(int chatCount) {
        this.chatCount = chatCount;
    }
}
