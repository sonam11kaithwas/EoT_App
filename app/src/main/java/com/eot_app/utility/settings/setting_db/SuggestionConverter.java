package com.eot_app.utility.settings.setting_db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SuggestionConverter {
    @TypeConverter
    public static List<Suggestion> toTaxData(String strdata) {
        Type listType = new TypeToken<List<Suggestion>>() {
        }.getType();
        List<Suggestion> data = new Gson().fromJson(strdata, listType);
        return strdata == null ? null : data;
    }

    @TypeConverter
    public static String toStringData(List<Suggestion> data) {
        return data == null ? null : new Gson().toJson(data);
    }
}
