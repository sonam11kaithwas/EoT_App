package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_email_pkg.get_email_temp_model;

public class Get_Email_ReQ_Model {

    private final String invId;
    private final String compId;
    String isProformaInv = "0";

    public Get_Email_ReQ_Model(String invId, String compId) {
        this.invId = invId;
        this.compId = compId;
    }

    public void setIsProformaInv(String isProformaInv) {
        this.isProformaInv = isProformaInv;
    }
}
