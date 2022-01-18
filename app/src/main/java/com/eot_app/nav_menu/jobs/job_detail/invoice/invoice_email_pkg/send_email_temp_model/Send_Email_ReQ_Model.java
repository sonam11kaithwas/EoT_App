package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_email_pkg.send_email_temp_model;

public class Send_Email_ReQ_Model {

    private final String isInvPdfSend = "1";
    private String invId;
    private String compId;
    private String message;
    private String subject;
    private String to;
    private String cc;
    private String isProformaInv = "0";
    private String tempId;

    public Send_Email_ReQ_Model(String invId, String compId, String message, String subject, String to, String cc) {
        this.invId = invId;
        this.compId = compId;
        this.message = message;
        this.subject = subject;
        this.to = to;
        this.cc = cc;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public void setIsProformaInv(String isProformaInv) {
        this.isProformaInv = isProformaInv;
    }

    public String getInvId() {
        return invId;
    }

    public void setInvId(String invId) {
        this.invId = invId;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
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

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }
}
