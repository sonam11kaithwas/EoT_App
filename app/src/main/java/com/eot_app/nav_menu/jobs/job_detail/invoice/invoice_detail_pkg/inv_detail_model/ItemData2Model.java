package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg
        .inv_detail_model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.tax_dao.TaxConverter;

import java.util.List;

/**
 * Created by Sonam-11 on 3/6/20.
 */
//@Entity(tableName = "ItemData2Model")
public class ItemData2Model implements Parcelable {

    public static final Creator<ItemData2Model> CREATOR = new Creator<ItemData2Model>() {
        @Override
        public ItemData2Model createFromParcel(Parcel in) {
            return new ItemData2Model(in);
        }

        @Override
        public ItemData2Model[] newArray(int size) {
            return new ItemData2Model[size];
        }
    };
    public String inm;
    @PrimaryKey
    @NonNull
    private String itemId;
    private String partno;
    private String qty;
    private String rate;
    private String supplierCost;
    @TypeConverters(TaxConverter.class)
    private List<Tax> tax = null;
    private String hsncode;
    private String idesc;
    private String unit;
    private String discount;
    private String type;
    private String image;
    private String taxAmount;
    private String totalAmount;

    public ItemData2Model() {
    }


    public ItemData2Model(@NonNull String itemId, String inm, String partno, String qty, String rate, String supplierCost, List<Tax> tax, String hsncode, String idesc, String unit, String discount, String type, String image, String taxAmount, String totalAmount) {
        this.itemId = itemId;
        this.inm = inm;
        this.partno = partno;
        this.qty = qty;
        this.rate = rate;
        this.supplierCost = supplierCost;
        this.tax = tax;
        this.hsncode = hsncode;
        this.idesc = idesc;
        this.unit = unit;
        this.discount = discount;
        this.type = type;
        this.image = image;
        this.taxAmount = taxAmount;
        this.totalAmount = totalAmount;
    }


    protected ItemData2Model(Parcel in) {
        // jobId = in.readString();
        itemId = in.readString();
        inm = in.readString();
        partno = in.readString();
        qty = in.readString();
        rate = in.readString();
        supplierCost = in.readString();
        tax = in.createTypedArrayList(Tax.CREATOR);
        hsncode = in.readString();
        idesc = in.readString();
        unit = in.readString();
        discount = in.readString();
        type = in.readString();
        image = in.readString();
        taxAmount = in.readString();
        totalAmount = in.readString();
    }

    public static Creator<ItemData2Model> getCREATOR() {
        return CREATOR;
    }

    @NonNull
    public String getItemId() {
        return itemId;
    }

    public void setItemId(@NonNull String itemId) {
        this.itemId = itemId;
    }

    public String getInm() {
        return inm;
    }

    public void setInm(String inm) {
        this.inm = inm;
    }

    public String getPartno() {
        return partno;
    }

    public void setPartno(String partno) {
        this.partno = partno;
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

    public String getSupplierCost() {
        return supplierCost;
    }

    public void setSupplierCost(String supplierCost) {
        this.supplierCost = supplierCost;
    }

    public List<Tax> getTax() {
        return tax;
    }

    public void setTax(List<Tax> tax) {
        this.tax = tax;
    }

    public String getHsncode() {
        return hsncode;
    }

    public void setHsncode(String hsncode) {
        this.hsncode = hsncode;
    }

    public String getIdesc() {
        return idesc;
    }

    public void setIdesc(String idesc) {
        this.idesc = idesc;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // dest.writeString(jobId);
        dest.writeString(itemId);
        dest.writeString(inm);
        dest.writeString(partno);
        dest.writeString(qty);
        dest.writeString(rate);
        dest.writeString(supplierCost);
        dest.writeTypedList(tax);
        dest.writeString(hsncode);
        dest.writeString(idesc);
        dest.writeString(unit);
        dest.writeString(discount);
        dest.writeString(type);
        dest.writeString(image);
        dest.writeString(taxAmount);
        dest.writeString(totalAmount);
    }
}
