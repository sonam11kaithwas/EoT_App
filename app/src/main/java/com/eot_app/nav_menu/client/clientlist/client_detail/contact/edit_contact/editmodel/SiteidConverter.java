package com.eot_app.nav_menu.client.clientlist.client_detail.contact.edit_contact.editmodel;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Sonam-11 on 11/7/20.
 */
public class SiteidConverter {
    @TypeConverter
    public static List<SiteId> toSiteIdData(String strdata) {
        Type listType = new TypeToken<List<SiteId>>() {
        }.getType();
        List<SiteId> data = new Gson().fromJson(strdata, listType);
        return strdata == null ? null : data;
    }

    @TypeConverter
    public static String toStringSiteIdData(List<SiteId> data) {
        return data == null ? null : new Gson().toJson(data);
    }
}
