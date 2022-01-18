package com.eot_app.nav_menu.admin_fw_chat_pkg.sonam_user_user_chat_pkg.usertouser_model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Sona-11 on 24/12/21.
 */
public class TeamMemrConverter {
    @TypeConverter
    public static List<TeamMemIdModel> toTagData(String strdata) {
        Type listType = new TypeToken<List<TeamMemIdModel>>() {
        }.getType();
        List<TeamMemIdModel> data = new Gson().fromJson(strdata, listType);
        return strdata == null ? null : data;
    }

    @TypeConverter
    public static String toStringTagData(List<TeamMemIdModel> data) {
        return data == null ? null : new Gson().toJson(data);
    }
}
