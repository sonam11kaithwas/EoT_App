package com.eot_app.nav_menu.expense.expense_detail.expense_history;

/**
 * Created by Sonam-11 on 8/5/20.
 */
public class ExpenseStatushistoryModel {
    private String status;
    private String changedby;
    private String dateTime;
    private String comment;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChangedby() {
        return changedby;
    }

    public void setChangedby(String changedby) {
        this.changedby = changedby;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
