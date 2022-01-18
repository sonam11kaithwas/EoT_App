package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_model_pkg;

import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Tax;

import java.util.List;

public class NewItem {
    private final String hsncode;
    private final String pno;//part number
    private final String unit;
    private final String taxamnt;
    private final String supplierCost;
    private String inm;
    private String ides;
    private String qty;
    private String rate;
    private String discount;
    private int itype;
    private List<Tax> tax;
    private int isItemOrTitle;
    private String des;
    private String amount;
    private String jtId;
    private String itemId;

    /**
     * itype 1 for non inventry item & 2 for shipping cost item
     **/

    /**
     * this for non inventry item
     **/
    public NewItem(String inm, String qty, String rate, String discount, int itype, List<Tax> tax,
                   int isItemOrTitle, String des, String pno, String hsncode, String unit, String taxamnt, String supplierCost, String jtId) {
        this.inm = inm;
        this.ides = des;
        this.qty = qty;
        this.rate = rate;
        this.discount = discount;
        this.itype = itype;
        this.tax = tax;
        this.isItemOrTitle = isItemOrTitle;
        this.des = des;
        this.pno = pno;
        this.hsncode = hsncode;
        this.unit = unit;
        this.taxamnt = taxamnt;
        this.supplierCost = supplierCost;
        this.jtId = jtId;
    }

    /**
     * this for Shipping item
     **/
    public NewItem(String itemId, String inm, String qty, String rate, String discount, int itype, List<Tax> tax,
                   int isItemOrTitle, String des, String pno, String hsncode, String unit, String taxamnt, String supplierCost, String jtId) {
        this.itemId = itemId;
        this.inm = inm;
        this.ides = des;
        this.qty = qty;
        this.rate = rate;
        this.discount = discount;
        this.itype = itype;
        this.tax = tax;
        this.isItemOrTitle = isItemOrTitle;
        this.des = des;
        this.pno = pno;
        this.hsncode = hsncode;
        this.unit = unit;
        this.taxamnt = taxamnt;
        this.supplierCost = supplierCost;
        this.jtId = jtId;
    }

    public String getJtId() {
        return jtId;
    }

    public void setJtId(String jtId) {
        this.jtId = jtId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getInm() {
        return inm;
    }

    public void setInm(String inm) {
        this.inm = inm;
    }

    public String getIdes() {
        return ides;
    }

    public void setIdes(String ides) {
        this.ides = ides;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public int getItype() {
        return itype;
    }

    public void setItype(int itype) {
        this.itype = itype;
    }

    public List<Tax> getTax() {
        return tax;
    }

    public void setTax(List<Tax> tax) {
        this.tax = tax;
    }

    public int getIsItemOrTitle() {
        return isItemOrTitle;
    }

    public void setIsItemOrTitle(int isItemOrTitle) {
        this.isItemOrTitle = isItemOrTitle;
    }
}
