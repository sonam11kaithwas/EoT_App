package com.eot_app.nav_menu.jobs.job_db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class KprConverter {

    @TypeConverter
    public static List<String> kpr(String strdata) {

        Type listType = new TypeToken<List<String>>() {
        }.getType();
        List<String> data = new Gson().fromJson(strdata, listType);
        return strdata == null ? null : data;
    }

    @TypeConverter
    public static String toStringkpr(List<String> data) {
        return data == null ? null : new Gson().toJson(data);
    }
}
