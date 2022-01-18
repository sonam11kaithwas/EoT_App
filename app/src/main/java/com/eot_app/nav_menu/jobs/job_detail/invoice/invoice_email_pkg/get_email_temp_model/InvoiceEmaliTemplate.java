package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_email_pkg.get_email_temp_model;

/**
 * Created by Sona-11 on 07/12/21.
 */
public class InvoiceEmaliTemplate {
    private String invTempId;
    private String inputValue;
    private String defaultTemp;

    public InvoiceEmaliTemplate(String invTempId, String inputValue, String defaultTemp) {
        this.invTempId = invTempId;
        this.inputValue = inputValue;
        this.defaultTemp = defaultTemp;
    }

    public InvoiceEmaliTemplate() {
    }

    public String getDefaultTemp() {
        return defaultTemp;
    }

    public void setDefaultTemp(String defaultTemp) {
        this.defaultTemp = defaultTemp;
    }

    public String getInvTempId() {
        return invTempId;
    }

    public void setInvTempId(String invTempId) {
        this.invTempId = invTempId;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }
}
