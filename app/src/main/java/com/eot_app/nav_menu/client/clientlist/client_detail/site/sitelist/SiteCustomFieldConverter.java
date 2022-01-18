package com.eot_app.nav_menu.client.clientlist.client_detail.site.sitelist;

import androidx.room.TypeConverter;

import com.eot_app.nav_menu.custom_fileds.custom_model.CustOmFormQuestionsRes;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Sonam-11 on 3/12/20.
 */
public class SiteCustomFieldConverter {
    @TypeConverter
    public static List<CustOmFormQuestionsRes> toequpArray(String strdata) {
        Type listType = new TypeToken<List<CustOmFormQuestionsRes>>() {
        }.getType();
        List<CustOmFormQuestionsRes> data = new Gson().fromJson(strdata, listType);
        return strdata == null ? null : data;
    }

    @TypeConverter
    public static String toStringequpArray(List<CustOmFormQuestionsRes> data) {
        return data == null ? null : new Gson().toJson(data);
    }
}
