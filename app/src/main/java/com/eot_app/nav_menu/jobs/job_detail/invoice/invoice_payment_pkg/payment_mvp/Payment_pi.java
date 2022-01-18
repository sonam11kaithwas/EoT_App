package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_payment_pkg.payment_mvp;

import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Inv_Res_Model;

public interface Payment_pi {
    void payment_invoice_apicall(String amount, String notes, String invId, String refName, String payType
            , long paymentDate, Boolean isEmailSendOrNot, String jobId, String isMailSentToClt);

    void getInvoiceDetails(String jobId);

    boolean isInputFieldDataValid(String amount, String payType, Inv_Res_Model inv_res_model);
}
