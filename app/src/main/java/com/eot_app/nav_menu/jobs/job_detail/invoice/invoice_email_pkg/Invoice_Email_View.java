package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_email_pkg;

import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_email_pkg.get_email_temp_model.Get_Email_ReS_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_email_pkg.get_email_temp_model.InvoiceEmaliTemplate;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_email_pkg.send_email_temp_model.Send_Email_ReS_Model;

import java.util.ArrayList;

public interface Invoice_Email_View {

    void onGetEmailTempData(Get_Email_ReS_Model email_reS_model);

    void onSendInvoiceEmail(Send_Email_ReS_Model email_reS_model);

    void showErrorAlertDialog(String error);

    void setSessionExpire(String msg);

    void setInvoiceTmpList(ArrayList<InvoiceEmaliTemplate> templateList);
}
