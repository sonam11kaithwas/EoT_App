package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_email_pkg;

public interface Invoice_Email_pi {
    void getInvoiceEmailTempApi(String invId, String isProformaInv);

    void sendInvoiceEmailTempApi(String invId, String compId, String messageInHtml, String emailSubject, String emailTo, String emailCc,
                                 String isProformaInv, String tempId);

    boolean isInputFieldDataValid(String emailTo, String emailCc, String emailSubject, String emailMessage);

    void getQuotationEmailTemplate(String quotId);

    void sendQuotationEmailTemplate(String quotId, String message, String subject, String to, String cc, String bcc, String from, String fromnm, String tempId);

    void getJobDocEmailTemplate(String jobId);

    void sendJObDocEmailTemplate(String jobId, String pdfPath, String message, String subject, String to, String cc, String bcc, String from, String fromnm);

    void getJobCardEmailTemplate(String jobId);

    void sendJobCardEmailTemplate(String jobId, String pdfPath, String message, String subject, String to, String cc, String tempId);

    void getJobInvoicetemplateList();

    void getQuotesInvoicetemplateList();

    void getJobCardetemplateList();
}
