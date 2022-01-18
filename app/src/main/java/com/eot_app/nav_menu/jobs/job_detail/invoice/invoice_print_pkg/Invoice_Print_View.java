package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_print_pkg;

import java.io.File;

public interface Invoice_Print_View {
    void onPdfCreated(File filePath, String fileName);
}
