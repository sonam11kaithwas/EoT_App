package com.eot_app.nav_menu.jobs.job_detail.invoice2list.itemlist_mvp;

import com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model.InvoiceItemDataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sonam-11 on 11/6/20.
 */
public interface ItemList_PI {
    void getItemListByJobFromDB(String jobId);

    void getItemFromServer(final String jobId);

    void updareRmitemsInDB(String jobId, List<InvoiceItemDataModel> updateItemList, ArrayList<String> ijmmIdList, List<InvoiceItemDataModel> notSyncItemList
            , boolean removeItemOnInvoice);

    void getinvoicedetails(final String jobId);

    void getGenerateInvoicePdf(String invId, String isProformaInv);

    void getloctaxexList();

}
