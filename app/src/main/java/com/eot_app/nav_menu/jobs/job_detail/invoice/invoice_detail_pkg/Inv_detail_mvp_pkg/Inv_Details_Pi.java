package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.Inv_detail_mvp_pkg;

import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_model_pkg.Invoice_Update_Request_Model;

public interface Inv_Details_Pi {
    void getinvoicedetails(String jobId);

    void rmInvooiceItemApiCall(Invoice_Update_Request_Model itemId);

    void getGenerateInvoicePdf(String invId);

}
