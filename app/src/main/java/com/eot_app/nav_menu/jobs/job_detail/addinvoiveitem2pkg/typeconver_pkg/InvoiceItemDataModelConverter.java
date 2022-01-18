package com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.typeconver_pkg;

import androidx.room.TypeConverter;

import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.InvoiceItemDataModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Sonam-11 on 10/6/20.
 */
public class InvoiceItemDataModelConverter {
    @TypeConverter
    public static List<InvoiceItemDataModel> toInvoiceItemDataModelData(String strdata) {
        Type listType = new TypeToken<List<InvoiceItemDataModel>>() {
        }.getType();
        List<InvoiceItemDataModel> data = new Gson().fromJson(strdata, listType);
        return strdata == null ? null : data;
    }

    @TypeConverter
    public static String toStringData(List<InvoiceItemDataModel> data) {
        return data == null ? null : new Gson().toJson(data);
    }
}
