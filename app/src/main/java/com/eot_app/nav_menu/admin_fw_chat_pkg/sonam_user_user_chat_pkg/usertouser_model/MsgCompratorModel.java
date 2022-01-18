package com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model;

import java.util.Comparator;

/**
 * Created by Sonam-11 on 14/4/20.
 */
public class MsgCompratorModel implements Comparator<MsgModel> {

    @Override
    public int compare(MsgModel o1, MsgModel o2) {
        try {
            return o1.getCreatedAt().compareTo(o1.getCreatedAt());
        } catch (Exception ex) {
            ex.getMessage();
        }
        return 0;
    }
}

