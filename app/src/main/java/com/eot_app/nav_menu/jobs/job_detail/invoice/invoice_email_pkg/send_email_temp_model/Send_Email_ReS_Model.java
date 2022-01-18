package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_email_pkg.send_email_temp_model;

public class Send_Email_ReS_Model {


    private Boolean success;

    private String message;

    public Send_Email_ReS_Model(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
