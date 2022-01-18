package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.tax_dao;

import androidx.room.TypeConverter;

import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Tax;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


/**
 * Created by Sonam-11 on 30/5/20.
 */
public class TaxConverter {
    @TypeConverter
    public static List<Tax> toTaxData(String strdata) {
        Type listType = new TypeToken<List<Tax>>() {
        }.getType();
        List<Tax> data = new Gson().fromJson(strdata, listType);
        return strdata == null ? null : data;
    }

    @TypeConverter
    public static String toStringData(List<Tax> data) {
        return data == null ? null : new Gson().toJson(data);
    }
}
