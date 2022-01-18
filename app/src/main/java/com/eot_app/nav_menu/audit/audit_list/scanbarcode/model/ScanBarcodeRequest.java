package com.eot_app.nav_menu.audit.audit_list.scanbarcode.model;

import java.io.Serializable;

/**
 * Created by Mahendra Dabi on 13/11/19.
 */
public class ScanBarcodeRequest implements Serializable {
    private String audId;
    private String barCode;
    private String search;

    public ScanBarcodeRequest() {
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getAudId() {
        return audId;
    }

    public void setAudId(String audId) {
        this.audId = audId;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
}
