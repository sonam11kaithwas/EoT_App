package com.eot_app.login_next.login_next_model;

/**
 * Created by geet-pc on 28/5/18.
 */

public class Right {
    private int isClientVisible;
    private int isPaymentVisible;
    private int isItemVisible;
    private int isInvoiceVisible;
    private int isQuoteVisible;
    // int isJobVisible;
    private int isJobAddOrNot;
    private int isAuditVisible;
    private int isExpenseVisible;
    private int isAppointmentVisible;
    private int isJobRescheduleOrNot;
    private int isJobRevisitOrNot;
    private int isEditCustomFormVisible;
    private String isRecur;
    private String isTimeSheetEnableMobile;
    private String isCheckInOutDownload;
    private String isLeaveAddEnable;

    public String getIsTimeSheetEnableMobile() {
        return isTimeSheetEnableMobile;
    }

    public String getIsCheckInOutEnableMobile() {
        return isCheckInOutDownload;
    }

    public int getIsAppointmentVisible() {
        return isAppointmentVisible;
    }

    public void setIsAppointmentVisible(int isAppointmentVisible) {
        this.isAppointmentVisible = isAppointmentVisible;
    }

    public int getIsExpenseVisible() {
        return isExpenseVisible;
    }

    public int getIsAuditVisible() {
        return isAuditVisible;
    }

    public int getIsInvoiceVisible() {
        return isInvoiceVisible;
    }

    public int getIsClientVisible() {
        return isClientVisible;
    }


    public int getIsPaymentVisible() {
        return isPaymentVisible;
    }

    public int getIsQuoteVisible() {
        return isQuoteVisible;
    }

    public int getIsItemVisible() {
        return isItemVisible;
    }

    public int getIsJobAddOrNot() {
        return isJobAddOrNot;
    }

    public int getIsJobRescheduleOrNot() {
        return isJobRescheduleOrNot;
    }

    public int getIsJobRevisitOrNot() {
        return isJobRevisitOrNot;
    }

    public int getIsEditCustomFormVisible() {
        return isEditCustomFormVisible;
    }

    public String getIsRecur() {
        return isRecur;
    }

    public String getIsLeaveAddEnable() {
        return isLeaveAddEnable;
    }
}
