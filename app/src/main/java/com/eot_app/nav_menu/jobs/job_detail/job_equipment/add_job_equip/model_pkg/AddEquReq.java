package com.eot_app.nav_menu.jobs.job_detail.job_equipment.add_job_equip.model_pkg;

import okhttp3.MultipartBody;

/**
 * Created by Sonam-11 on 6/10/20.
 */
public class AddEquReq {
    /**
     * Owner----1 &&&& Service Provider------2
     **/

    private String type;
    private String egId;
    private String ecId;
    private String zip;
    private String city;
    private String adr;
    private String ctry;
    private String state;
    private String isBarcodeGenerate;
    private String notes;
    private String status;
    private String rate;
    private String purchaseDate;
    private String manufactureDate;
    private String expiryDate;
    private String supplier;
    private String sno;
    private String mno;
    private String brand;
    private String equnm;
    private String cltId;
    private String jobId;
    private String contrId;
    private String itemId;
    /***1 for part 0 for equipment*****/
    private String isPart;
    private String invId = "";
    private String siteId;
    private MultipartBody.Part[] image;
    private String extraField1;
    private String extraField2;


    /**
     * Convert Item TO equipment
     ****/
    public AddEquReq(String type, String egId, String ecId, String zip, String city, String adr,
                     String ctry, String state, String isBarcodeGenerate, String notes, String purchaseDate,
                     String manufactureDate, String expiryDate, String supplier, String sno, String mno, String brand,
                     String equnm, String jobId, String cltId, String contrId
            , String itemId, String rate, String siteId, String isPart, String status, String invId, String extraField1, String extraField2) {
        this.type = type;
        this.egId = egId;
        this.ecId = ecId;
        this.zip = zip;
        this.city = city;
        this.adr = adr;
        this.ctry = ctry;
        this.state = state;
        this.isBarcodeGenerate = isBarcodeGenerate;
        this.notes = notes;
        this.status = status;
        this.purchaseDate = purchaseDate;
        this.manufactureDate = manufactureDate;
        this.expiryDate = expiryDate;
        this.supplier = supplier;
        this.sno = sno;
        this.mno = mno;
        this.brand = brand;
        this.equnm = equnm;
        this.jobId = jobId;
        this.cltId = cltId;
        this.contrId = contrId;
        this.itemId = itemId;
        this.rate = rate;
        this.isPart = isPart;
        this.siteId = siteId;
        this.invId = invId;
        this.extraField1 = extraField1;
        this.extraField2 = extraField2;
    }

    /**
     * Add equipment
     ****/
    public AddEquReq(String type, String egId, String ecId, String zip, String city, String adr,
                     String ctry, String state, String isBarcodeGenerate, String notes, String purchaseDate,
                     String manufactureDate, String expiryDate, String supplier, String sno, String mno, String brand,
                     String equnm, String jobId, String cltId, String contrId, String siteId, String isPart, String status,
                     String extraField1, String extraField2) {
        this.type = type;
        this.egId = egId;
        this.ecId = ecId;
        this.zip = zip;
        this.city = city;
        this.adr = adr;
        this.ctry = ctry;
        this.state = state;
        this.isBarcodeGenerate = isBarcodeGenerate;
        this.notes = notes;
        this.purchaseDate = purchaseDate;
        this.manufactureDate = manufactureDate;
        this.expiryDate = expiryDate;
        this.supplier = supplier;
        this.sno = sno;
        this.mno = mno;
        this.brand = brand;
        this.equnm = equnm;
        this.status = status;
        this.jobId = jobId;
        this.cltId = cltId;
        this.contrId = contrId;
        this.isPart = isPart;
        this.siteId = siteId;
        this.extraField1 = extraField1;
        this.extraField2 = extraField2;
    }


    public AddEquReq() {
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getIsPart() {
        return isPart;
    }

    public void setIsPart(String isPart) {
        this.isPart = isPart;
    }

    public String getContrId() {
        return contrId;
    }

    public String getJobId() {
        return jobId;
    }

    public String getType() {
        return type;
    }

    public String getEgId() {
        return egId;
    }

    public String getEcId() {
        return ecId;
    }

    public String getZip() {
        return zip;
    }

    public String getCity() {
        return city;
    }

    public String getInvId() {
        return invId;
    }

    public String getAdr() {
        return adr;
    }

    public String getCtry() {
        return ctry;
    }

    public String getState() {
        return state;
    }

    public String getIsBarcodeGenerate() {
        return isBarcodeGenerate;
    }

    public String getNotes() {
        return notes;
    }

    public String getStatus() {
        return status;
    }

    public String getRate() {
        return rate;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public String getManufactureDate() {
        return manufactureDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getSno() {
        return sno;
    }

    public String getMno() {
        return mno;
    }

    public String getBrand() {
        return brand;
    }

    public String getEqunm() {
        return equnm;
    }

    public String getCltId() {
        return cltId;
    }

    public MultipartBody.Part[] getImage() {
        return image;
    }

    public String getItemId() {
        return itemId;
    }

    public String getExtraField1() {
        return extraField1;
    }

    public String getExtraField2() {
        return extraField2;
    }
}
