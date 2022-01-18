package com.eot_app.nav_menu.jobs.add_job;

import androidx.room.TypeConverter;

import com.eot_app.nav_menu.jobs.add_job.add_job_recr.RecurReqResModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Mahendra Dabi on 1/4/21.
 */
public class JobRecurTypeConvert {
    @TypeConverter
    public static List<RecurReqResModel> toTagData(String strdata) {
        Type listType = new TypeToken<List<RecurReqResModel>>() {
        }.getType();
        List<RecurReqResModel> data = new Gson().fromJson(strdata, listType);
        return strdata == null ? null : data;
    }

    @TypeConverter
    public static String toStringTagData(List<RecurReqResModel> data) {
        return data == null ? null : new Gson().toJson(data);
    }
}
