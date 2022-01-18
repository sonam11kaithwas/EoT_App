package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ItemData implements Parcelable {
    public static final Creator<ItemData> CREATOR = new Creator<ItemData>() {
        @Override
        public ItemData createFromParcel(Parcel in) {
            return new ItemData(in);
        }

        @Override
        public ItemData[] newArray(int size) {
            return new ItemData[size];
        }
    };
    private String itemId = "";
    private String jobId;
    private int type;
    private String rate;
    private String qty;
    private String discount;
    private int isGroup = 0;
    private String inm = "";
    private String ides = "";
    private String des = "";
    private String amount;
    private String hsncode;
    private String pno;//part number
    private String unit;
    private String taxamnt;
    private List<Tax> tax;
    private String supplierCost;
    private String jtId;


    public ItemData(String itemId, String jobId, int type, String rate, String qty, String discount, int isGroup,
                    List<Tax> tax, String des, String pno, String hsncode, String unit, String taxamnt, String supplierCost) {//,
        this.itemId = itemId;
        this.jobId = jobId;
        this.type = type;
        this.rate = rate;
        this.qty = qty;
        this.discount = discount;
        this.isGroup = isGroup;
        this.tax = tax;
        this.des = des;
        this.ides = des;
        this.pno = pno;
        this.hsncode = hsncode;
        this.unit = unit;
        this.taxamnt = taxamnt;
        this.supplierCost = supplierCost;
    }

    protected ItemData(Parcel in) {
        itemId = in.readString();
        jobId = in.readString();
        type = in.readInt();
        rate = in.readString();
        supplierCost = in.readString();
        qty = in.readString();
        discount = in.readString();
        isGroup = in.readInt();
        inm = in.readString();
        ides = in.readString();
        des = in.readString();
        amount = in.readString();
        hsncode = in.readString();
        pno = in.readString();
        unit = in.readString();
        taxamnt = in.readString();
        jtId = in.readString();
        tax = in.createTypedArrayList(Tax.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(itemId);
        parcel.writeString(jobId);
        parcel.writeInt(type);
        parcel.writeString(jtId);
        parcel.writeString(rate);
        parcel.writeString(supplierCost);
        parcel.writeString(qty);
        parcel.writeString(discount);
        parcel.writeInt(isGroup);
        parcel.writeString(inm);
        parcel.writeString(ides);
        parcel.writeString(des);
        parcel.writeString(amount);
        parcel.writeString(hsncode);
        parcel.writeString(pno);
        parcel.writeString(unit);
        parcel.writeString(taxamnt);
        parcel.writeTypedList(tax);
    }

    public String getHsncode() {
        return hsncode;
    }

    public void setHsncode(String hsncode) {
        this.hsncode = hsncode;
    }

    public String getPno() {
        return pno;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTaxamnt() {
        return taxamnt;
    }

    public void setTaxamnt(String taxamnt) {
        this.taxamnt = taxamnt;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIdes() {
        return ides;
    }

    public void setIdes(String ides) {
        this.ides = ides;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public List<Tax> getTax() {
        return tax;
    }

    public void setTax(List<Tax> tax) {
        this.tax = tax;
    }

    public int getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(int isGroup) {
        this.isGroup = isGroup;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getInm() {
        return inm;
    }

    public void setInm(String inm) {
        this.inm = inm;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getSupplierCost() {
        return supplierCost;
    }

    public void setSupplierCost(String supplierCost) {
        this.supplierCost = supplierCost;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getJtId() {
        return jtId;
    }

    public void setJtId(String jtId) {
        this.jtId = jtId;
    }
}
