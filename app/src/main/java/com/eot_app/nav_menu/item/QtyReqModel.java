package com.eot_app.nav_menu.item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sonam-11 on 16/10/20.
 */
public class QtyReqModel {
    public String jobId;
    public List<ItemDatum> itemData = new ArrayList<>();

    public QtyReqModel(String jobId, List<ItemDatum> itemData) {
        this.jobId = jobId;
        this.itemData = itemData;
    }

    public String getJobId() {
        return jobId;
    }
}
