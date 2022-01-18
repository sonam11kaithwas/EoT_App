package com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.tax_dao.TaxConverter;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.Tax;

import java.util.List;
import java.util.Objects;

/**
 * Created by Sonam-11 on 10/6/20.
 */
@Entity(tableName = "InvoiceItemDataModel")
public class InvoiceItemDataModel implements Parcelable {


    public static final Creator<InvoiceItemDataModel> CREATOR = new Creator<InvoiceItemDataModel>() {
        @Override
        public InvoiceItemDataModel createFromParcel(Parcel in) {
            return new InvoiceItemDataModel(in);
        }

        @Override
        public InvoiceItemDataModel[] newArray(int size) {
            return new InvoiceItemDataModel[size];
        }
    };
    private String tempNm = "";
    private String inm;
    private String ijmmId = "";
    @PrimaryKey
    @NonNull
    private String itemId;
    /**
     * "1":::::"Item" "2":::::::"FW"   "3"::::::"Services"
     **/
    private String dataType;
    /***Inventry ::::: "0"  Non Inventry ::::::: "1"   {BUt services all type Non -inventry} **/
    private String itemType = "";
    private String rate;
    /***me change for equipment**/
    private String qty = "1";
    private String discount;
    private String des;
    private String hsncode;
    private String pno;
    private String unit;
    private String taxamnt;
    private String supplierCost;
    private String isGrouped;
    @TypeConverters(TaxConverter.class)
    private List<Tax> tax;
    private String jtId;
    private String serialNo;
    private String itemConvertCount = "0";
    private String isBillable;
    private String isBillableChange = "";
    private String taxType;

    /***this use for add item***/
    public InvoiceItemDataModel(String tempNm, String inm, String itemId,
                                String dataType, String itemType, String rate,
                                String qty, String discount, String des, String hsncode,
                                String pno, String unit, String taxamnt, String supplierCost,
                                List<Tax> tax, String jtId, String serialNo, String isBillableChange) {//, String isBillable
        this.tempNm = tempNm;
        this.inm = inm;
        this.itemId = itemId;
        this.dataType = dataType;
        this.itemType = itemType;
        this.rate = rate;
        this.qty = qty;
        this.discount = discount;
        this.des = des;
        this.hsncode = hsncode;
        this.pno = pno;
        this.unit = unit;
        this.taxamnt = taxamnt;
        this.supplierCost = supplierCost;
        this.isGrouped = "0";
        this.tax = tax;
        this.jtId = jtId;
        this.serialNo = serialNo;
        //   this.isBillableChange = "";
        this.isBillableChange = isBillableChange;
    }


    /**
     * this use for Update Item
     ***/
    public InvoiceItemDataModel(String tempNm, String inm, String ijmmId, String itemId, String dataType,
                                String itemType, String rate, String qty, String discount, String des,
                                String hsncode, String pno, String unit, String taxamnt, String supplierCost,
                                List<Tax> tax, String jtId, String serialNo, String itemConvertCount
            , String isBillableChange) {//, String isBillable
        this.tempNm = tempNm;
        this.itemConvertCount = itemConvertCount;
        this.inm = inm;
        this.ijmmId = ijmmId;
        this.itemId = itemId;
        this.dataType = dataType;
        this.itemType = itemType;
        this.rate = rate;
        this.qty = qty;
        this.discount = discount;
        this.des = des;
        this.hsncode = hsncode;
        this.pno = pno;
        this.unit = unit;
        this.taxamnt = taxamnt;
        this.supplierCost = supplierCost;
        this.isGrouped = "0";
        this.tax = tax;
        this.jtId = jtId;
        this.serialNo = serialNo;
        // this.isBillable = isBillable;
        this.isBillableChange = isBillableChange;
    }


    public InvoiceItemDataModel() {
    }

    protected InvoiceItemDataModel(Parcel in) {
        tempNm = in.readString();
        inm = in.readString();
        ijmmId = in.readString();
        itemId = in.readString();
        dataType = in.readString();
        itemType = in.readString();
        rate = in.readString();
        qty = in.readString();
        discount = in.readString();
        des = in.readString();
        hsncode = in.readString();
        pno = in.readString();
        unit = in.readString();
        taxamnt = in.readString();
        supplierCost = in.readString();
        isGrouped = in.readString();
        tax = in.createTypedArrayList(Tax.CREATOR);
        jtId = in.readString();
        serialNo = in.readString();
        isBillable = in.readString();
        taxType = in.readString();
        isBillableChange = in.readString();
        itemConvertCount = in.readString();
    }

    public static Creator<InvoiceItemDataModel> getCREATOR() {
        return CREATOR;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public String getTempNm() {
        return tempNm;
    }

    public void setTempNm(String tempNm) {
        this.tempNm = tempNm;
    }

    public String getJtId() {
        return jtId;
    }

    public void setJtId(String jtId) {
        this.jtId = jtId;
    }

    public String getInm() {
        return inm;
    }

    public void setInm(String inm) {
        this.inm = inm;
    }

    public String getIjmmId() {
        return ijmmId;
    }

    public void setIjmmId(String ijmmId) {
        this.ijmmId = ijmmId;
    }

    @NonNull
    public String getItemId() {
        return itemId;
    }

    public void setItemId(@NonNull String itemId) {
        this.itemId = itemId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
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

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
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

    public String getIsGrouped() {
        return isGrouped;
    }

    public void setIsGrouped(String isGrouped) {
        this.isGrouped = isGrouped;
    }

    public List<Tax> getTax() {
        return tax;
    }

    public void setTax(List<Tax> tax) {
        this.tax = tax;
    }

    @androidx.annotation.NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tempNm);
        dest.writeString(inm);
        dest.writeString(ijmmId);
        dest.writeString(itemId);
        dest.writeString(dataType);
        dest.writeString(itemType);
        dest.writeString(rate);
        dest.writeString(qty);
        dest.writeString(discount);
        dest.writeString(des);
        dest.writeString(hsncode);
        dest.writeString(pno);
        dest.writeString(unit);
        dest.writeString(taxamnt);
        dest.writeString(supplierCost);
        dest.writeString(isGrouped);
        dest.writeTypedList(tax);
        dest.writeString(jtId);
        dest.writeString(serialNo);
        dest.writeString(isBillable);
        dest.writeString(taxType);
        dest.writeString(isBillableChange);
        dest.writeString(itemConvertCount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvoiceItemDataModel)) return false;
        InvoiceItemDataModel that = (InvoiceItemDataModel) o;
        return Objects.equals(getTempNm(), that.getTempNm()) &&
                Objects.equals(getInm(), that.getInm()) &&
                Objects.equals(getIjmmId(), that.getIjmmId()) &&
                getItemId().equals(that.getItemId()) &&
                Objects.equals(getDataType(), that.getDataType()) &&
                Objects.equals(getItemType(), that.getItemType()) &&
                Objects.equals(getRate(), that.getRate()) &&
                Objects.equals(getQty(), that.getQty()) &&
                Objects.equals(getDiscount(), that.getDiscount()) &&
                Objects.equals(getDes(), that.getDes()) &&
                Objects.equals(getHsncode(), that.getHsncode()) &&
                Objects.equals(getPno(), that.getPno()) &&
                Objects.equals(getUnit(), that.getUnit()) &&
                Objects.equals(getTaxamnt(), that.getTaxamnt()) &&
                Objects.equals(getSupplierCost(), that.getSupplierCost()) &&
                Objects.equals(getIsGrouped(), that.getIsGrouped()) &&
                Objects.equals(getTax(), that.getTax()) &&
                Objects.equals(getSerialNo(), that.getSerialNo()) &&
                Objects.equals(getJtId(), that.getJtId()) &&
                Objects.equals(getIsBillable(), that.getIsBillable()) &&
                Objects.equals(getIsBillableChange(), that.getIsBillableChange()) &&
                Objects.equals(getItemConvertCount(), that.getItemConvertCount());

    }

    public String getIsBillableChange() {
        return isBillableChange;
    }

    public void setIsBillableChange(String isBillableChange) {
        this.isBillableChange = isBillableChange;
    }

    public String getIsBillable() {
        return isBillable;
    }

    public void setIsBillable(String isBillable) {
        this.isBillable = isBillable;
    }

    public String getItemConvertCount() {
        return itemConvertCount;
    }

    public void setItemConvertCount(String itemConvertCount) {
        this.itemConvertCount = itemConvertCount;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTempNm(), getInm(), getIjmmId(), getItemId(), getDataType(), getItemType(), getRate(), getQty(), getDiscount(), getDes(), getHsncode(), getPno(), getUnit(), getTaxamnt(), getSupplierCost(), getIsGrouped(), getTax(), getJtId(), getIsBillable(), getIsBillableChange());
    }
}
