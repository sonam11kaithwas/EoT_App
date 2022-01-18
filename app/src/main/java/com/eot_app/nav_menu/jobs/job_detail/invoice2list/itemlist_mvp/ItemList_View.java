package com.eot_app.nav_menu.jobs.job_detail.invoice2list.itemlist_mvp;

import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.InvoiceItemDataModel;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.location_tax_dao.TaxesLocation;
import com.eot_app.nav_menu.jobs.job_detail.invoice2list.itemlist_model.InvoiceItemDetailsModel;

import java.util.List;

/**
 * Created by Sonam-11 on 11/6/20.
 */
public interface ItemList_View {
    void setItemListByJob(List<InvoiceItemDataModel> itemList);

    void dismissPullTorefresh();

    void setInvoiceDetails(InvoiceItemDetailsModel invResModel);

    void onSessionExpire(String msg);

    void InvoiceNotFound(String text);

    void setItemDataList(InvoiceItemDetailsModel invResModel);

    void onGetPdfPath(String pdfPath);

    void finishActivity();

    void errorActivityFinish(String msg);

    void setLocationTaxsList(List<TaxesLocation> taxList);


}
