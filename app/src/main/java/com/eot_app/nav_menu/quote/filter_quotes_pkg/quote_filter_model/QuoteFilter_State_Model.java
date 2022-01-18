package com.eot_app.nav_menu.quote.filter_quotes_pkg.quote_filter_model;

import com.eot_app.utility.DropdownListBean;

import java.io.Serializable;

public class QuoteFilter_State_Model implements DropdownListBean, Serializable {
    private String statusId = "0";
    private String statusNm = "Not_Started";

    public QuoteFilter_State_Model(String statusId, String statusNm) {
        this.statusId = statusId;
        this.statusNm = statusNm;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatusNm() {
        return statusNm;
    }

    public void setStatusNm(String statusNm) {
        this.statusNm = statusNm;
    }

    @Override
    public String getKey() {
        return getStatusId();
    }

    @Override
    public String getName() {
        return getStatusNm();
    }
}
