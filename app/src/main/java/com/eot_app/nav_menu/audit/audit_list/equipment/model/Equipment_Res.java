package com.eot_app.nav_menu.audit.audit_list.equipment.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.eot_app.nav_menu.audit.audit_list.documents.doc_model.GetFileList_Res;

import java.util.ArrayList;


/**
 * Created by Mahendra Dabi on 11/11/19.
 */
//@Entity(tableName = "Equipment_Res")
public class Equipment_Res implements Parcelable {
    public static final Creator<Equipment_Res> CREATOR = new Creator<Equipment_Res>() {
        @Override
        public Equipment_Res createFromParcel(Parcel in) {
            return new Equipment_Res(in);
        }

        @Override
        public Equipment_Res[] newArray(int size) {
            return new Equipment_Res[size];
        }
    };
    private String equId;
    private String audId;
    private String parentId;
    private String equnm;
    private String mno;
    private String sno;
    private String contactPerson;
    private String location;
    private String remark;
    private String changeBy;
    private String status;
    private String updateData;
    private ArrayList<GetFileList_Res> attachments;
    //new param for equipment details
    private String type;
    private String brand;
    private String rate;
    private String expiryDate;
    private String manufactureDate;
    private String purchaseDate;
    private String barcode;
    private String equipment_group;
    private String image;
    private String ecId;
    private String isPart;
    private String extraField1;
    private String extraField2;
    private String usrManualDoc;
    private String snm;
    private String datetime;

    public Equipment_Res() {

    }

    protected Equipment_Res(Parcel in) {
        equId = in.readString();
        audId = in.readString();
        parentId = in.readString();
        equnm = in.readString();
        mno = in.readString();
        sno = in.readString();
        contactPerson = in.readString();
        location = in.readString();
        remark = in.readString();
        changeBy = in.readString();
        status = in.readString();
        updateData = in.readString();
        attachments = in.createTypedArrayList(GetFileList_Res.CREATOR);
        type = in.readString();
        brand = in.readString();
        rate = in.readString();
        expiryDate = in.readString();
        manufactureDate = in.readString();
        purchaseDate = in.readString();
        barcode = in.readString();
        equipment_group = in.readString();
        image = in.readString();
        ecId = in.readString();
        isPart = in.readString();
        extraField1 = in.readString();
        extraField2 = in.readString();
        usrManualDoc = in.readString();
        snm = in.readString();
        datetime = in.readString();
    }

    public static Creator<Equipment_Res> getCREATOR() {
        return CREATOR;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(equId);
        dest.writeString(audId);
        dest.writeString(parentId);
        dest.writeString(equnm);
        dest.writeString(mno);
        dest.writeString(sno);
        dest.writeString(contactPerson);
        dest.writeString(location);
        dest.writeString(remark);
        dest.writeString(changeBy);
        dest.writeString(status);
        dest.writeString(updateData);
        dest.writeTypedList(attachments);
        dest.writeString(type);
        dest.writeString(brand);
        dest.writeString(rate);
        dest.writeString(expiryDate);
        dest.writeString(manufactureDate);
        dest.writeString(purchaseDate);
        dest.writeString(barcode);
        dest.writeString(equipment_group);
        dest.writeString(image);
        dest.writeString(ecId);
        dest.writeString(isPart);
        dest.writeString(extraField1);
        dest.writeString(extraField2);
        dest.writeString(usrManualDoc);
        dest.writeString(snm);
        dest.writeString(datetime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getEquId() {
        return equId;
    }

    public void setEquId(String equId) {
        this.equId = equId;
    }

    public String getAudId() {
        return audId;
    }

    public void setAudId(String audId) {
        this.audId = audId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getEqunm() {
        return equnm;
    }

    public void setEqunm(String equnm) {
        this.equnm = equnm;
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

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getChangeBy() {
        return changeBy;
    }

    public void setChangeBy(String changeBy) {
        this.changeBy = changeBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdateData() {
        return updateData;
    }

    public void setUpdateData(String updateData) {
        this.updateData = updateData;
    }

    public ArrayList<GetFileList_Res> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<GetFileList_Res> attachments) {
        this.attachments = attachments;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getEquipment_group() {
        return equipment_group;
    }

    public void setEquipment_group(String equipment_group) {
        this.equipment_group = equipment_group;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEcId() {
        return ecId;
    }

    public void setEcId(String ecId) {
        this.ecId = ecId;
    }

    public String getIsPart() {
        return isPart;
    }

    public void setIsPart(String isPart) {
        this.isPart = isPart;
    }

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

    public String getUsrManualDoc() {
        return usrManualDoc;
    }

    public void setUsrManualDoc(String usrManualDoc) {
        this.usrManualDoc = usrManualDoc;
    }

    public String getSnm() {
        return snm;
    }

    public void setSnm(String snm) {
        this.snm = snm;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
