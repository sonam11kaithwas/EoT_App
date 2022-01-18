package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.model_pkg;

import androidx.room.TypeConverter;

import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.ItemData2Model;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Sonam-11 on 3/6/20.
 */
public class ItemData2ModelTypeConverter {
    @TypeConverter
    public static List<ItemData2Model> toTaxData(String strdata) {
        Type listType = new TypeToken<List<ItemData2Model>>() {
        }.getType();
        List<ItemData2Model> data = new Gson().fromJson(strdata, listType);
        return strdata == null ? null : data;
    }

    @TypeConverter
    public static String toStringData(List<ItemData2Model> data) {
        return data == null ? null : new Gson().toJson(data);
    }

}


