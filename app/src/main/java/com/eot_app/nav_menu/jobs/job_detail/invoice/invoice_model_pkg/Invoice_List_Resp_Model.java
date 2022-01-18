package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_model_pkg;

import java.io.Serializable;

public class Invoice_List_Resp_Model implements Serializable {
    private String nm;
    private String qty;
    private String rs;
    private String ict;

    public Invoice_List_Resp_Model(String nm, String qty, String rs, String ict) {
        this.nm = nm;
        this.qty = qty;
        this.rs = rs;
        this.ict = ict;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRs() {
        return rs;
    }

    public void setRs(String rs) {
        this.rs = rs;
    }

    public String getIct() {
        return ict;
    }

    public void setIct(String ict) {
        this.ict = ict;
    }
}
