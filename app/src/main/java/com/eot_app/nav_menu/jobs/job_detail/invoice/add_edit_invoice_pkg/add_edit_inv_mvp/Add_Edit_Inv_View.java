package com.eot_app.nav_menu.jobs.job_detail.invoice.add_edit_invoice_pkg.add_edit_inv_mvp;

import com.eot_app.nav_menu.jobs.job_detail.invoice.inventry_pkg.Inventry_ReS_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Inv_Res_Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Tax;
import com.eot_app.utility.settings.setting_db.FieldWorker;
import com.eot_app.utility.settings.setting_db.JobTitle;

import java.util.List;

public interface Add_Edit_Inv_View {

    void setInvoiceData(Inv_Res_Model invoiceData);

    void setItemdata(List<Inventry_ReS_Model> list);

    void setFieldWorKerList(List<FieldWorker> fieldWorkerList);

    void errorOccured();

    void setTaxList(List<Tax> taxList);

    //void setServicesList(List<JtId> jobServicesList);

    void setJobtitleList(List<JobTitle> servicesItemList);

    void updateItemServiceListner();

}
