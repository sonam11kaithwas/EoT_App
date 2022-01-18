package com.eot_app.nav_menu.jobs.job_db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by aplite_pc302 on 8/9/18.
 */

public class JtIdConverter {
    @TypeConverter
    public static List<JtId> toTagData(String strdata) {
//        String convert = new Gson().toJson(jobj.getJSONArray("data"));
        Type listType = new TypeToken<List<JtId>>() {
        }.getType();
        List<JtId> data = new Gson().fromJson(strdata, listType);
        return strdata == null ? null : data;
    }

    @TypeConverter
    public static String toStringTagData(List<JtId> data) {
        return data == null ? null : new Gson().toJson(data);
    }
}
