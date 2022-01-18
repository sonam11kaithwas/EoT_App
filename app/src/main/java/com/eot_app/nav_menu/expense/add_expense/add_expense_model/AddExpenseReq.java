package com.eot_app.nav_menu.expense.add_expense.add_expense_model;

import com.eot_app.utility.App_preference;

import okhttp3.MultipartBody;

/**
 * Created by Sonam-11 on 7/5/20.
 */
public class AddExpenseReq {
    private String jobId;
    private String cltId;
    private String usrId;
    private String name;
    private String amt;
    private String dateTime;
    private String category;
    private String tag;
    private String status;
    private String des;
    private String comment;
    private MultipartBody.Part[] receipt;
    private String expId;
    private String tagName;
    private String categoryName;

    public AddExpenseReq(String jobId, String cltId, String name, String amt, String dateTime, String category,
                         String tag, String des, String status, MultipartBody.Part[] receipt) {
        this.jobId = jobId;
        this.cltId = cltId;
        this.usrId = App_preference.getSharedprefInstance().getLoginRes().getUsrId();
        this.name = name;
        this.amt = amt;
        this.dateTime = dateTime;
        this.category = category;
        this.tag = tag;
        //this.status = "1";
        this.des = des;
        this.comment = "";
        this.status = status;
        this.receipt = receipt;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getExpId() {
        return expId;
    }

    public void setExpId(String expId) {
        this.expId = expId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getCltId() {
        return cltId;
    }

    public void setCltId(String cltId) {
        this.cltId = cltId;
    }

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public MultipartBody.Part[] getReceipt() {
        return receipt;
    }

    public void setReceipt(MultipartBody.Part[] receipt) {
        this.receipt = receipt;
    }
}
