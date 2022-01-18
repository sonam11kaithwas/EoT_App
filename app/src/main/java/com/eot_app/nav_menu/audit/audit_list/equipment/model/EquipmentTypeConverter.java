package com.eot_app.nav_menu.audit.audit_list.equipment.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Sonam-11 on 1/9/20.
 */
public class EquipmentTypeConverter {
    @TypeConverter
    public static List<Equipment_Res> toEquipmentData(String strdata) {
        Type listType = new TypeToken<List<Equipment_Res>>() {
        }.getType();
        List<Equipment_Res> data = new Gson().fromJson(strdata, listType);
        return strdata == null ? null : data;
    }

    @TypeConverter
    public static String toStringEquipmentData(List<Equipment_Res> data) {
        return data == null ? null : new Gson().toJson(data);
    }
}
