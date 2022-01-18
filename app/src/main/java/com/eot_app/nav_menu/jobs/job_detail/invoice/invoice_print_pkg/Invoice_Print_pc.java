package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_print_pkg;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;

import com.eot_app.utility.ZoomLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class Invoice_Print_pc implements Invoice_Print_pi {
    private final Invoice_Print_View print_view;
    private final String InvoiceFilePath = "Eot_Invoice";

    public Invoice_Print_pc(Invoice_Print_View print_view) {
        this.print_view = print_view;
    }

    @Override
    public Bitmap loadBitmapFromView(ZoomLayout v) {
        Bitmap b = Bitmap.createBitmap(v.getChildAt(0).getWidth(),
                v.getChildAt(0).getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }

    @Override
    public void createPdf(Bitmap bitmap) {
        float height = bitmap.getHeight();
        float width = bitmap.getWidth();

        int convertHeight = (int) height, convertWidth = (int) width;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHeight, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        // write the document content;
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root, InvoiceFilePath);
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        String fname = "Document-" + Calendar.getInstance().getTimeInMillis() + ".pdf";
        File file = new File(myDir, fname);

        try {
            document.writeTo(new FileOutputStream(file));

        } catch (IOException e) {
            e.printStackTrace();
        }

        // close the document
        document.close();
        print_view.onPdfCreated(file, fname);
    }

//    @Override
//    public double getCalculatedAmount(String str_qty, String str_rate, String str_discount, float total_tax) {
//        double amount = 0;
//        try {
//            double qty = 0, rate = 0, dis = 0;
//            qty = Integer.parseInt(str_qty);
//            rate = Double.parseDouble(str_rate);
//            dis = Double.parseDouble(str_discount);
//
//            amount = (qty * rate + qty *((rate * total_tax) / 100)) - qty * ((rate * dis) / 100);
//
//        } catch (Exception ex) {
//            ex.getMessage();
//        }
//        return amount;
//    }
}

