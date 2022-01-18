package com.eot_app.nav_menu.expense.add_expense.add_expense_model;

import com.eot_app.utility.App_preference;

import okhttp3.MultipartBody;

/**
 * Created by Sonam-11 on 11/5/20.
 */
public class UpdateExpenseReq {
    private final MultipartBody.Part[] receipt;
    private final String jobId;
    private final String cltId;
    private final String usrId;
    private final String name;
    private final String amt;
    private final String dateTime;
    private final String category;
    private final String tag;
    private final String status;
    private final String des;
    private final String expId;
    private final String comment;

    public UpdateExpenseReq(String jobId, String cltId, String name, String amt, String dateTime,
                            String category, String tag, String status, String des, MultipartBody.Part[] receipt,
                            String expId, String comment) {
        this.jobId = jobId;
        this.cltId = cltId;
        this.usrId = App_preference.getSharedprefInstance().getLoginRes().getUsrId();
        this.name = name;
        this.amt = amt;
        this.dateTime = dateTime;
        this.category = category;
        this.tag = tag;
        this.status = status;
        this.des = des;
        this.receipt = receipt;
        this.expId = expId;
        this.comment = comment;
    }

    public String getJobId() {
        return jobId;
    }

    public String getCltId() {
        return cltId;
    }

    public String getUsrId() {
        return usrId;
    }

    public String getName() {
        return name;
    }

    public String getAmt() {
        return amt;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getCategory() {
        return category;
    }

    public String getTag() {
        return tag;
    }

    public String getStatus() {
        return status;
    }

    public String getDes() {
        return des;
    }

    public MultipartBody.Part[] getReceipt() {
        return receipt;
    }


    public String getExpId() {
        return expId;
    }

    public String getComment() {
        return comment;
    }
}
