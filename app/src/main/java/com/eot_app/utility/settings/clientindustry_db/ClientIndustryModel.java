package com.eot_app.utility.settings.clientindustry_db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by Sonam-11 on 10/7/20.
 */
@Entity(tableName = "ClientIndustryModel")
public class ClientIndustryModel implements Serializable {
    @PrimaryKey
    @NonNull
    private String industryId;
    private String industryName;
    private String isDefault;

    public ClientIndustryModel() {
    }

    public String getIndustryId() {
        return industryId;
    }

    public void setIndustryId(String industryId) {
        this.industryId = industryId;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }


}
