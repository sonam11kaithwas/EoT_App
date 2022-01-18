package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.model_pkg;

import androidx.room.TypeConverter;

import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.ItemUpdateModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Sonam-11 on 3/6/20.
 */
public class ItemUpdateModelTypeConverter {
    @TypeConverter
    public static List<ItemUpdateModel> toTaxData(String strdata) {
        Type listType = new TypeToken<List<ItemUpdateModel>>() {
        }.getType();
        List<ItemUpdateModel> data = new Gson().fromJson(strdata, listType);
        return strdata == null ? null : data;
    }

    @TypeConverter
    public static String toStringData(List<ItemUpdateModel> data) {
        return data == null ? null : new Gson().toJson(data);
    }
}
