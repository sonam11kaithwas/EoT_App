package com.eot_app.nav_menu.admin_fw_chat_pkg.chatmember_list_pkg.chatmember_mvp;

/**
 * Created by Sonam-11 on 2020-03-06.
 */
public interface ChatUsersList_PI {
    void callApiGetUserList();//String searchCharUser

    void getChatUserList();

    void getUserBySearch(String search);

    void getChatUserListFromDB();

    void getChatUserByName(String search);
}
