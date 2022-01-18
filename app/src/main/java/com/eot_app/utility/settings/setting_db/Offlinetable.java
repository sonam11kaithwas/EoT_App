package com.eot_app.utility.settings.setting_db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by aplite_pc302 on 6/25/18.
 */
@Entity(indices = {@Index(value = "id", unique = true)}) // check user first name is not repeat.
public class Offlinetable implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    int id;
    String service_name;
    String params;
    String timestamp;
    int count;
    private String extra;

    public Offlinetable(String service_name, String params, String timestamp) {
        this.service_name = service_name;
        this.params = params;
        this.timestamp = timestamp;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


}
