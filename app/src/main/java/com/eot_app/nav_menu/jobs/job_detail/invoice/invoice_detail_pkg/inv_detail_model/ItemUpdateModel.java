package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

/**
 * Created by Sonam-11 on 3/6/20.
 */
//@Entity(tableName = "ItemUpdateModel")
public class ItemUpdateModel {
    public String inm;
    @PrimaryKey
    @NonNull
    private String itemId;

    public ItemUpdateModel(String inm, @NonNull String itemId) {
        this.inm = inm;
        this.itemId = itemId;
    }

    public String getInm() {
        return inm;
    }

    public void setInm(String inm) {
        this.inm = inm;
    }

    @NonNull
    public String getItemId() {
        return itemId;
    }

    public void setItemId(@NonNull String itemId) {
        this.itemId = itemId;
    }
}
