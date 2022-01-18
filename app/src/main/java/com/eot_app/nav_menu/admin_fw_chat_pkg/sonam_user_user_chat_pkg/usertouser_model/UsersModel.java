package com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sonam-11 on 9/4/20.
 */
public class UsersModel implements Serializable {
    ArrayList<String> users;

    public UsersModel(ArrayList<String> users) {
        this.users = users;
    }

    public UsersModel() {
    }


    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }
}
