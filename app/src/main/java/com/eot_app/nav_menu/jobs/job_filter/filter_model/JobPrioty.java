package com.eot_app.nav_menu.jobs.job_filter.filter_model;

import com.eot_app.utility.DropdownListBean;

import java.io.Serializable;

public class JobPrioty implements DropdownListBean, Serializable {
    String ids;
    String prity;

    public JobPrioty(String ids, String prity) {
        this.ids = ids;
        this.prity = prity;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getPrity() {
        return prity;
    }

    public void setPrity(String prity) {
        this.prity = prity;
    }

    @Override
    public String getKey() {
        return getIds();
    }

    @Override
    public String getName() {
        return getPrity();
    }
}
