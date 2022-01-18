package com.eot_app.nav_menu.quote.add_quotes_pkg.model_pkg;

import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Tax;

import java.util.List;

public class Update_Quote_ReQ {//var iqmmId: String?


//    var isInvOrNoninv: String?


    private final String invId;
    private final String type;
    private final String rate;
    private final String qty;
    private final String unit;
    private final String discount;
    private final String des;
    private final String totalAmount;//last
    private final List<Tax> taxData;
    private final String supplierCost;
    // private String isInvOrNoninv;
    private final String pno;
    private final String taxamnt;
    private final String jtId;
    //  private String inm;
    private final String iqmmId;
    private final String itemId;
    private final String inm;
// String isInvOrNoninv,

    public Update_Quote_ReQ(String iqmmId, String invId, String type, String rate, String qty, String unit,
                            String discount, String des, String totalAmount, List<Tax> taxData,
                            String supplierCost, String pno, String taxamnt, String jtId, String itemId, String inm) {
        this.iqmmId = iqmmId;
        this.invId = invId;
        this.type = type;
        this.rate = rate;
        this.qty = qty;
        this.unit = unit;
        this.discount = discount;
        this.des = des;
        this.totalAmount = totalAmount;
        this.taxData = taxData;
        this.supplierCost = supplierCost;
        // this.isInvOrNoninv = isInvOrNoninv;
        this.pno = pno;
        this.taxamnt = taxamnt;
        this.jtId = jtId;
        this.itemId = itemId;
        this.inm = inm;

    }


}
