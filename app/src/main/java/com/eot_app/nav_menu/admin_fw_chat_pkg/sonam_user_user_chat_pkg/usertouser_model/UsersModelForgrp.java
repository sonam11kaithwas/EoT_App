package com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sona-11 on 27/12/21.
 */
public class UsersModelForgrp implements Serializable {

    ArrayList<Object> groups = new ArrayList<>();

    public UsersModelForgrp(ArrayList<Object> groups) {
        this.groups = groups;
    }

    public UsersModelForgrp() {
    }

    public ArrayList<Object> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Object> groups) {
        this.groups = groups;
    }
}
