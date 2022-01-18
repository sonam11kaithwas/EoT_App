package com.eot_app.nav_menu.jobs.job_detail.invoice.add_edit_invoice_pkg.add_edit_inv_mvp;

import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_model_pkg.Invoice_Update_Request_Model;

public interface Add_Edit_Inv_Pi {
    void getFwList();

    void getInventryItemListFromServer();

    void addInvoiceItem(Invoice_Update_Request_Model addItem_model);

    void getTaxList();


    void getInventryItemList();

    void getJobServiceTittle();

    void initialize_FwList_ServiceTittle_inventoryList();

    void updateSearchText(String searchText);
}
