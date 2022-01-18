package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.model_pkg;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Sonam-11 on 4/6/20.
 */
public class RmvyItemStringTypeConverter {
    @TypeConverter
    public static List<String> toTaxData(String strdata) {
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        List<String> data = new Gson().fromJson(strdata, listType);
        return strdata == null ? null : data;
    }

    @TypeConverter
    public static String toStringData(List<String> data) {
        return data == null ? null : new Gson().toJson(data);
    }

}
