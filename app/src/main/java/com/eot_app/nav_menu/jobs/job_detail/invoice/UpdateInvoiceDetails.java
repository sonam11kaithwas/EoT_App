package com.eot_app.nav_menu.jobs.job_detail.invoice;

import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Inv_Res_Model;

public class UpdateInvoiceDetails {
    private static UpdateInvoiceDetails updateInvoiceDetails;
    private Inv_Res_Model invoice_Details;

    public static UpdateInvoiceDetails getInstance() {
        if (updateInvoiceDetails == null) {
            updateInvoiceDetails = new UpdateInvoiceDetails();
        }
        return updateInvoiceDetails;
    }

    public void setInvoiceDetailsitem(Inv_Res_Model invoice_Details) {
        this.invoice_Details = invoice_Details;
    }

    public Inv_Res_Model getInvoice_Details(Inv_Res_Model inv_res_model) {
        return inv_res_model;
    }

}
