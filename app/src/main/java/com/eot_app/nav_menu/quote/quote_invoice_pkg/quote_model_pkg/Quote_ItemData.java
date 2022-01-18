package com.eot_app.nav_menu.quote.quote_invoice_pkg.quote_model_pkg;

import android.os.Parcel;
import android.os.Parcelable;

import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Tax;

import java.util.List;

public class Quote_ItemData implements Parcelable {

    public static final Creator<Quote_ItemData> CREATOR = new Creator<Quote_ItemData>() {
        @Override
        public Quote_ItemData createFromParcel(Parcel in) {
            return new Quote_ItemData(in);
        }

        @Override
        public Quote_ItemData[] newArray(int size) {
            return new Quote_ItemData[size];
        }
    };
    private String iqmmId;
    private String itemId;
    private String quotId;
    private String groupId;
    private String type;
    private String rate;
    private String qty;
    private String discount;
    private String inm;
    private String des;
    private String itype;
    private String label;
    private String hsncode;
    private String pno;
    private String unit;
    private String taxamnt;
    private String supplierCost;
    private String jtId;
    private List<Tax> tax = null;

    protected Quote_ItemData(Parcel in) {
        iqmmId = in.readString();
        itemId = in.readString();
        quotId = in.readString();
        groupId = in.readString();
        type = in.readString();
        rate = in.readString();
        qty = in.readString();
        discount = in.readString();
        inm = in.readString();
        des = in.readString();
        itype = in.readString();
        label = in.readString();
        hsncode = in.readString();
        pno = in.readString();
        unit = in.readString();
        taxamnt = in.readString();
        supplierCost = in.readString();
        jtId = in.readString();
        tax = in.createTypedArrayList(Tax.CREATOR);
    }

    public static Creator<Quote_ItemData> getCREATOR() {
        return CREATOR;
    }

    public List<Tax> getTax() {
        return tax;
    }

    public void setTax(List<Tax> tax) {
        this.tax = tax;
    }

    public String getIqmmId() {
        return iqmmId;
    }

    public void setIqmmId(String iqmmId) {
        this.iqmmId = iqmmId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getQuotId() {
        return quotId;
    }

    public void setQuotId(String quotId) {
        this.quotId = quotId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
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

    public String getInm() {
        return inm;
    }

    public void setInm(String inm) {
        this.inm = inm;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getItype() {
        return itype;
    }

    public void setItype(String itype) {
        this.itype = itype;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public String getSupplierCost() {
        return supplierCost;
    }

    public void setSupplierCost(String supplierCost) {
        this.supplierCost = supplierCost;
    }

    public String getJtId() {
        return jtId;
    }

    public void setJtId(String jtId) {
        this.jtId = jtId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(iqmmId);
        dest.writeString(itemId);
        dest.writeString(quotId);
        dest.writeString(groupId);
        dest.writeString(type);
        dest.writeString(rate);
        dest.writeString(qty);
        dest.writeString(discount);
        dest.writeString(inm);
        dest.writeString(des);
        dest.writeString(itype);
        dest.writeString(label);
        dest.writeString(hsncode);
        dest.writeString(pno);
        dest.writeString(unit);
        dest.writeString(taxamnt);
        dest.writeString(supplierCost);
        dest.writeString(jtId);
        dest.writeTypedList(tax);
    }
}
