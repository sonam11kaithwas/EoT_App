package com.eot_app.nav_menu.jobs.job_db;

import androidx.room.TypeConverter;

import com.eot_app.utility.settings.setting_db.TagData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by aplite_pc302 on 8/9/18.
 */

public class TagDataConverter {
    @TypeConverter
    public static List<TagData> toTagData(String strdata) {
        Type listType = new TypeToken<List<TagData>>() {
        }.getType();
        List<TagData> data = new Gson().fromJson(strdata, listType);
        return strdata == null ? null : data;
    }

    @TypeConverter
    public static String toStringTagData(List<TagData> data) {
        return data == null ? null : new Gson().toJson(data);
    }

}
