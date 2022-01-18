package com.eot_app.nav_menu.admin_fw_chat_pkg.chatmember_list_pkg.chatmember_model;

/**
 * Created by Sonam-11 on 3/18/20.
 */
public class ChatUserReqModel {
    private final int limit;
    private final int index;
    private final String search;


    public ChatUserReqModel(int updatelimit, int updateindex, String search) {
        this.limit = updatelimit;
        this.index = updateindex;
        this.search = search;
    }
}
