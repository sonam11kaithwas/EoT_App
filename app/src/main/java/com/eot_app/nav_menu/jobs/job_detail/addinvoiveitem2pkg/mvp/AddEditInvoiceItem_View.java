package com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.mvp;

import com.eot_app.nav_menu.jobs.job_detail.invoice.inventry_pkg.Inventry_ReS_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Tax;
import com.eot_app.utility.settings.setting_db.FieldWorker;
import com.eot_app.utility.settings.setting_db.JobTitle;

import java.util.List;

/**
 * Created by Sonam-11 on 10/6/20.
 */
public interface AddEditInvoiceItem_View {

    void setItemdata(List<Inventry_ReS_Model> list);

    void setItemdataFromServer(List<Inventry_ReS_Model> list);

    void setFieldWorKerList(List<FieldWorker> fieldWorkerList);

    void setJobtitleList(List<JobTitle> servicesItemList);

    void setTaxList(List<Tax> taxList);


    // void invoiceItem_view();
}
