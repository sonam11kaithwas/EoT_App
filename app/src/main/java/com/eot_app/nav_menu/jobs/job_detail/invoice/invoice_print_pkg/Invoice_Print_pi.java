package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_print_pkg;

import android.graphics.Bitmap;

import com.eot_app.utility.ZoomLayout;

public interface Invoice_Print_pi {
    Bitmap loadBitmapFromView(ZoomLayout v);

    void createPdf(Bitmap bitmap);
//    double getCalculatedAmount(String str_qty, String str_rate, String str_discount,float total_tax);
}
