package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_payment_pkg.payment_mvp;

import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Inv_Res_Model;

public interface PayMent_View {
    void setData(Inv_Res_Model invResModel);

    void onInvoiceSuccessFalse(String errorMessage);

    void showErrorAlertDialog(String errorMessage);

    void onPaymentSuccess(String message);
}
