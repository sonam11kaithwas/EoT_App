package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_payment_pkg.invoice_payment_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payment_ReQ_Model {
    @SerializedName("isEmailSendOrNot")
    @Expose
    private final Boolean isEmailSendOrNot;
    @SerializedName("jobId")
    @Expose
    private final String jobId;
    @SerializedName("amt")
    @Expose
    private float amt;
    @SerializedName("invId")
    @Expose
    private String invId;
    @SerializedName("ref")
    @Expose
    private String ref;
    @SerializedName("paytype")
    @Expose
    private String paytype;
    @SerializedName("paydate")
    @Expose
    private String paydate;
    @SerializedName("note")
    @Expose
    private String note;
    private String isMailSentToClt = "0";

    public Payment_ReQ_Model(float amt, String invId, String ref, String paytype, String paydate, String note, Boolean isEmailSendOrNot
            , String jobId, String isMailSentToClt) {
        this.amt = amt;
        this.invId = invId;
        this.ref = ref;
        this.paytype = paytype;
        this.paydate = paydate;
        this.note = note;
        this.isEmailSendOrNot = isEmailSendOrNot;
        this.jobId = jobId;
        this.isMailSentToClt = isMailSentToClt;
    }

    public float getAmt() {
        return amt;
    }

    public void setAmt(float amt) {
        this.amt = amt;
    }

    public String getInvId() {
        return invId;
    }

    public void setInvId(String invId) {
        this.invId = invId;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getPaydate() {
        return paydate;
    }

    public void setPaydate(String paydate) {
        this.paydate = paydate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
