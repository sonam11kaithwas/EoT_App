package com.eot_app.nav_menu.jobs.job_db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Sonam-11 on 22/9/20.
 */
public class EquArrayConvrtr {
    @TypeConverter
    public static List<EquArrayModel> toequpArray(String strdata) {
        Type listType = new TypeToken<List<EquArrayModel>>() {
        }.getType();
        List<EquArrayModel> data = new Gson().fromJson(strdata, listType);
        return strdata == null ? null : data;
    }

    @TypeConverter
    public static String toStringequpArray(List<EquArrayModel> data) {
        return data == null ? null : new Gson().toJson(data);
    }
}
