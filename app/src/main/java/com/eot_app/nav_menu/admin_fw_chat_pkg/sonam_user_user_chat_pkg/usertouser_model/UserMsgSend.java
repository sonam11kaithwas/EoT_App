package com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sonam-11 on 7/4/20.
 */
public class UserMsgSend implements Serializable {
    private ArrayList<MsgModel> messages = new ArrayList<>();

    public UserMsgSend() {
    }

    public UserMsgSend(ArrayList<MsgModel> messages) {
        this.messages = messages;
    }

    public ArrayList<MsgModel> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<MsgModel> messages) {
        this.messages = messages;
    }
}
