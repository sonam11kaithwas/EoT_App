package com.eot_app.nav_menu.appointment;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class KeeparConverter {
    @TypeConverter
    public static List<Keepar> toKeepar(String strdata) {
        Type listType = new TypeToken<List<Keepar>>() {
        }.getType();
        List<Keepar> data = new Gson().fromJson(strdata, listType);
        return strdata == null ? null : data;
    }

    @TypeConverter
    public static String toStringKeepar(List<Keepar> data) {
        return data == null ? null : new Gson().toJson(data);
    }

}
