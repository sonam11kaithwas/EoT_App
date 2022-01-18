package com.eot_app.nav_menu.jobs.job_db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Mahendra Dabi on 7/4/21.
 */
public class SelecetedDaysConverter {
    @TypeConverter
    public static List<LinkedHashMap<String, Boolean>> toEquipmentData(String strdata) {
        Type listType = new TypeToken<List<LinkedHashMap<String, Boolean>>>() {
        }.getType();
        List<LinkedHashMap<String, Boolean>> data = new Gson().fromJson(strdata, listType);
        return strdata == null ? null : data;
    }

    @TypeConverter
    public static String toStringEquipmentData(List<LinkedHashMap<String, Boolean>> data) {
        return data == null ? null : new Gson().toJson(data);
    }
}
