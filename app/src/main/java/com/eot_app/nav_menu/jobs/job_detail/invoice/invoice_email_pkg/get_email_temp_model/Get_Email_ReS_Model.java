package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_email_pkg.get_email_temp_model;

public class Get_Email_ReS_Model {

    private String message;
    private String subject;
    private String from;
    private String fromnm;
    private String to;

    public Get_Email_ReS_Model(String message, String subject, String from, String fromnm, String to) {
        this.message = message;
        this.subject = subject;
        this.from = from;
        this.fromnm = fromnm;
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromnm() {
        return fromnm;
    }

    public void setFromnm(String fromnm) {
        this.fromnm = fromnm;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
