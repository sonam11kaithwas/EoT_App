package com.eot_app.utility.settings.jobtitle;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by aplite_pc302 on 8/6/18.
 */

public class TaxDataConverter {
    @TypeConverter
    public static List<TaxData> toTaxData(String strdata) {
//        String convert = new Gson().toJson(jobj.getJSONArray("data"));
        Type listType = new TypeToken<List<TaxData>>() {
        }.getType();
        List<TaxData> data = new Gson().fromJson(strdata, listType);
        return strdata == null ? null : data;
    }

    @TypeConverter
    public static String toStringData(List<TaxData> data) {
        return data == null ? null : new Gson().toJson(data);
    }
}
