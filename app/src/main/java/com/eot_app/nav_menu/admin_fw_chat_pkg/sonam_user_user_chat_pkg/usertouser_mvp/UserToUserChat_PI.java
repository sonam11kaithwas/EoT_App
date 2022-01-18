package com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_mvp;

import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.MsgModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.TeamMemIdModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UserMsgSend;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UsersModel;
import com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model.UsersModelForgrp;

import java.util.List;

/**
 * Created by Sonam-11 on 7/4/20.
 */
public interface UserToUserChat_PI {
    void sendMsgs(MsgModel msgModel, String docId);

    void craeteFirstnode(UserMsgSend msgModel, String documentId);

    void createUsers(UsersModel userList, UserMsgSend userMsgSend);

    void createUsersForgrp(UsersModelForgrp userList, UserMsgSend userMsgSend);

    void uploadDoc(String path, String cd);

    void getUserListsByIds(List<TeamMemIdModel> tempusersList);

}
