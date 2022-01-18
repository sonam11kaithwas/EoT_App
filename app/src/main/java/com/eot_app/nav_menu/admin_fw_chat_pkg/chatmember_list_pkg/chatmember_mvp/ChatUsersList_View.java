package com.eot_app.nav_menu.admin_fw_chat_pkg.chatmember_list_pkg.chatmember_mvp;

import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UserChatModel;

import java.util.List;

/**
 * Created by Sonam-11 on 2020-03-06.
 */
public interface ChatUsersList_View {
    void setSearchVisibility(boolean searchVisibility);

    void setChatUserS(List<UserChatModel> chatUserList);

    void disableRefersh();
}

