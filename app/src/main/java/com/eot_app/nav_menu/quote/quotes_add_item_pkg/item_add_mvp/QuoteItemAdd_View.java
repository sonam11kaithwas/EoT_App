package com.eot_app.nav_menu.quote.quotes_add_item_pkg.item_add_mvp;

import com.eot_app.nav_menu.jobs.job_detail.invoice.inventry_pkg.Inventry_ReS_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Tax;
import com.eot_app.utility.settings.setting_db.FieldWorker;
import com.eot_app.utility.settings.setting_db.JobTitle;

import java.util.List;

public interface QuoteItemAdd_View {
    void setInventryItem(List<Inventry_ReS_Model> inventryItemList);

    void setTaxList(List<Tax> taxList);

    void updateItemServiceListner();

    void setFwList(List<FieldWorker> fwDataList);

    void setJobServices(List<JobTitle> jobServicesDataList);

    void setItemAdded();

    void onSessionExpire(String msg);
}
