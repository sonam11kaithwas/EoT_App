package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.Inv_detail_mvp_pkg;

import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Inv_Res_Model;

public interface Inv_Details_View {
    void onSessionExpire(String msg);

    void setItemDataList(Inv_Res_Model invResModel);

    void InvoiceNotFound(String s);

    void setInvoiceDetails(Inv_Res_Model inv_res_model);

    void dismissPullTorefresh();

    void onGetPdfPath(String pdfPath);

}
