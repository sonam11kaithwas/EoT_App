package com.eot_app.nav_menu.jobs.job_detail.job_detail_activity_presenter;

import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.InvoiceItemDataModel;

import java.util.List;

public interface Job_Detail_Activity_View {

    void finishActivityWithSetResult();

    void onSessionExpire(String message);

    void setInvoiceDetails();

    void moreInvoiceOption(List<InvoiceItemDataModel> data);

    void onGetPdfPath(String pdfPath);

    void onSignatureUpload(String signaturePath, String msg);

    // void setBooleanForGenerateInvoice(boolean CheckInvoice);
}
