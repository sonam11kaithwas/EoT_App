package com.eot_app.utility.settings.equipmentdb;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Equipment")
public class Equipment {
    @PrimaryKey
    @NonNull
    private String equId;
    private String cltId;
    private String parentId;
    private String equnm;
    private String nm;
    private String mno;
    private String sno;
    private String brand;
    private String rate;
    private String supId;
    private String supplier;
    private String notes;
    private String expiryDate;
    private String manufactureDate;
    private String purchaseDate;
    private String barcode;
    private String isusable;
    private String barcodeImg;
    private String adr;
    private String city;
    private String state;
    private String ctry;
    private String zip;
    private String status;
    private String type;
    private String ecId;
    private String egId;
    private String ebId;
    private String isdelete;
    private String image;
    private String isDisable;
    private String lastAuditLabel;
    private String lastAuditDate;
    private String equStatusOnAudit;
    private String lastAuditId;
    private String lastJobLabel;
    private String lastJobDate;
    private String equStatusOnJob;
    private String lastJobId;
    private String groupName;
    private String extraField1;
    private String extraField2;
    private String usrManualDoc;

    public String getExtraField1() {
        return extraField1;
    }

    public void setExtraField1(String extraField1) {
        this.extraField1 = extraField1;
    }

    public String getExtraField2() {
        return extraField2;
    }

    public void setExtraField2(String extraField2) {
        this.extraField2 = extraField2;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getEquId() {
        return equId;
    }

    public void setEquId(String equId) {
        this.equId = equId;
    }

    public String getCltId() {
        return cltId;
    }

    public void setCltId(String cltId) {
        this.cltId = cltId;
    }

    public String getEqunm() {
        return equnm;
    }

    public void setEqunm(String equnm) {
        this.equnm = equnm;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getMno() {
        return mno;
    }

    public void setMno(String mno) {
        this.mno = mno;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getSupId() {
        return supId;
    }

    public void setSupId(String supId) {
        this.supId = supId;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getIsusable() {
        return isusable;
    }

    public void setIsusable(String isusable) {
        this.isusable = isusable;
    }

    public String getBarcodeImg() {
        return barcodeImg;
    }

    public void setBarcodeImg(String barcodeImg) {
        this.barcodeImg = barcodeImg;
    }

    public String getAdr() {
        return adr;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCtry() {
        return ctry;
    }

    public void setCtry(String ctry) {
        this.ctry = ctry;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEcId() {
        return ecId;
    }

    public void setEcId(String ecId) {
        this.ecId = ecId;
    }

    public String getEgId() {
        return egId;
    }

    public void setEgId(String egId) {
        this.egId = egId;
    }

    public String getEbId() {
        return ebId;
    }

    public void setEbId(String ebId) {
        this.ebId = ebId;
    }

    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIsDisable() {
        return isDisable;
    }

    public void setIsDisable(String isDisable) {
        this.isDisable = isDisable;
    }

    public String getLastAuditLabel() {
        return lastAuditLabel;
    }

    public void setLastAuditLabel(String lastAuditLabel) {
        this.lastAuditLabel = lastAuditLabel;
    }

    public String getLastAuditDate() {
        return lastAuditDate;
    }

    public void setLastAuditDate(String lastAuditDate) {
        this.lastAuditDate = lastAuditDate;
    }

    public String getEquStatusOnAudit() {
        return equStatusOnAudit;
    }

    public void setEquStatusOnAudit(String equStatusOnAudit) {
        this.equStatusOnAudit = equStatusOnAudit;
    }

    public String getLastAuditId() {
        return lastAuditId;
    }

    public void setLastAuditId(String lastAuditId) {
        this.lastAuditId = lastAuditId;
    }

    public String getLastJobLabel() {
        return lastJobLabel;
    }

    public void setLastJobLabel(String lastJobLabel) {
        this.lastJobLabel = lastJobLabel;
    }

    public String getLastJobDate() {
        return lastJobDate;
    }

    public void setLastJobDate(String lastJobDate) {
        this.lastJobDate = lastJobDate;
    }

    public String getEquStatusOnJob() {
        return equStatusOnJob;
    }

    public void setEquStatusOnJob(String equStatusOnJob) {
        this.equStatusOnJob = equStatusOnJob;
    }

    public String getLastJobId() {
        return lastJobId;
    }

    public void setLastJobId(String lastJobId) {
        this.lastJobId = lastJobId;
    }

    public String getUsrManualDoc() {
        return usrManualDoc;
    }

    public void setUsrManualDoc(String usrManualDoc) {
        this.usrManualDoc = usrManualDoc;
    }
}
