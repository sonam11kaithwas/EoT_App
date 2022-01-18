package com.eot_app.nav_menu.jobs.job_db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Ignore;

import com.eot_app.nav_menu.jobs.job_detail.documents.doc_model.GetFileList_Res;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Sonam-11 on 22/9/20.
 */
public class EquArrayModel implements Parcelable {

    public static final Creator<EquArrayModel> CREATOR = new Creator<EquArrayModel>() {
        @Override
        public EquArrayModel createFromParcel(Parcel in) {
            return new EquArrayModel(in);
        }

        @Override
        public EquArrayModel[] newArray(int size) {
            return new EquArrayModel[size];
        }
    };
    public String equId;
    public String parentId;
    public String equnm;
    public String mno;
    public String sno;
    public String audId;
    public String remark;
    public String changeBy;
    public String status;
    public String updateData;
    public String lat;
    public String lng;
    public String location;
    public String contrid;
    private int linkStatus = 0;
    private int isRemarkAdd = 0;
    private ArrayList<GetFileList_Res> attachments;
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
    private String usrManualDoc;
    private String isPart;
    private String snm;
    @Ignore
    private String adr;
    private String extraField1;
    private String extraField2;
    private String datetime;

    public EquArrayModel() {
    }

    protected EquArrayModel(Parcel in) {
        linkStatus = in.readInt();
        isRemarkAdd = in.readInt();
        equId = in.readString();
        parentId = in.readString();
        equnm = in.readString();
        mno = in.readString();
        sno = in.readString();
        audId = in.readString();
        remark = in.readString();
        changeBy = in.readString();
        status = in.readString();
        updateData = in.readString();
        lat = in.readString();
        lng = in.readString();
        location = in.readString();
        contrid = in.readString();
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
        usrManualDoc = in.readString();
        isPart = in.readString();
        snm = in.readString();
        adr = in.readString();
        extraField1 = in.readString();
        extraField2 = in.readString();
        datetime = in.readString();
    }

    public static Creator<EquArrayModel> getCREATOR() {
        return CREATOR;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EquArrayModel)) return false;
        EquArrayModel that = (EquArrayModel) o;
        return Objects.equals(getEquId(), that.getEquId()) &&
                Objects.equals(getEqunm(), that.getEqunm()) &&
                Objects.equals(getMno(), that.getMno()) &&
                Objects.equals(getSno(), that.getSno()) &&
                Objects.equals(getAudId(), that.getAudId()) &&
                Objects.equals(getRemark(), that.getRemark()) &&
                Objects.equals(getChangeBy(), that.getChangeBy()) &&
                Objects.equals(getStatus(), that.getStatus()) &&
                Objects.equals(getUpdateData(), that.getUpdateData()) &&
                Objects.equals(getLat(), that.getLat()) &&
                Objects.equals(getLng(), that.getLng()) &&
                Objects.equals(getLocation(), that.getLocation()) &&
                Objects.equals(getContrid(), that.getContrid()) &&
                Objects.equals(getAttachments(), that.getAttachments()) &&
                Objects.equals(getType(), that.getType()) &&
                Objects.equals(getBrand(), that.getBrand()) &&
                Objects.equals(getRate(), that.getRate()) &&
                Objects.equals(getExpiryDate(), that.getExpiryDate()) &&
                Objects.equals(getManufactureDate(), that.getManufactureDate()) &&
                Objects.equals(getPurchaseDate(), that.getPurchaseDate()) &&
                Objects.equals(getBarcode(), that.getBarcode()) &&
                Objects.equals(getEquipment_group(), that.getEquipment_group()) &&
                Objects.equals(getImage(), that.getImage()) &&
                Objects.equals(getEcId(), that.getEcId()) &&
                Objects.equals(getSnm(), that.getSnm()) &&
                Objects.equals(getUsrManualDoc(), that.getUsrManualDoc()) &&
                Objects.equals(getIsPart(), that.getIsPart());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(linkStatus);
        dest.writeInt(isRemarkAdd);
        dest.writeString(equId);
        dest.writeString(parentId);
        dest.writeString(equnm);
        dest.writeString(mno);
        dest.writeString(sno);
        dest.writeString(audId);
        dest.writeString(remark);
        dest.writeString(changeBy);
        dest.writeString(status);
        dest.writeString(updateData);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeString(location);
        dest.writeString(contrid);
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
        dest.writeString(usrManualDoc);
        dest.writeString(isPart);
        dest.writeString(snm);
        dest.writeString(adr);
        dest.writeString(extraField1);
        dest.writeString(extraField2);
        dest.writeString(datetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEquId(), getEqunm(), getMno(), getSno(), getAudId(), getRemark(), getChangeBy(), getStatus(), getUpdateData(), getLat(), getLng(), getLocation(), getContrid(), getAttachments(), getType(), getBrand(), getRate(), getExpiryDate(), getManufactureDate(), getPurchaseDate(), getBarcode(), getEquipment_group(), getImage());
    }

    @Override
    public String toString() {
        return "EquArrayModel{" +
                "equId='" + equId + '\'' +
                "parentId='" + parentId + '\'' +
                ", equnm='" + equnm + '\'' +
                ", mno='" + mno + '\'' +
                ", sno='" + sno + '\'' +
                ", audId='" + audId + '\'' +
                ", remark='" + remark + '\'' +
                ", changeBy='" + changeBy + '\'' +
                ", status='" + status + '\'' +
                ", updateData='" + updateData + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", location='" + location + '\'' +
                ", contrid='" + contrid + '\'' +
                ", attachments=" + attachments +
                ", type='" + type + '\'' +
                ", brand='" + brand + '\'' +
                ", rate='" + rate + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", manufactureDate='" + manufactureDate + '\'' +
                ", purchaseDate='" + purchaseDate + '\'' +
                ", barcode='" + barcode + '\'' +
                ", snm='" + snm + '\'' +
                ", equipment_group='" + equipment_group + '\'' +
                ", image='" + image + '\'' +
                ", usrManualDoc='" + usrManualDoc + '\'' +

                '}';
    }

    public String getEquId() {
        return equId;
    }

    public void setEquId(String equId) {
        this.equId = equId;
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

    public String getAudId() {
        return audId;
    }

    public void setAudId(String audId) {
        this.audId = audId;
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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContrid() {
        return contrid;
    }

    public void setContrid(String contrid) {
        this.contrid = contrid;
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

    public String getUsrManualDoc() {
        return usrManualDoc;
    }

    public void setUsrManualDoc(String usrManualDoc) {
        this.usrManualDoc = usrManualDoc;
    }

    public String getIsPart() {
        return isPart;
    }

    public void setIsPart(String isPart) {
        this.isPart = isPart;
    }

    public String getSnm() {
        return snm;
    }

    public void setSnm(String snm) {
        this.snm = snm;
    }

    public String getAdr() {
        return adr;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    public int getLinkStatus() {
        return linkStatus;
    }

    public void setLinkStatus(int linkStatus) {
        this.linkStatus = linkStatus;
    }

    public int getIsRemarkAdd() {
        return isRemarkAdd;
    }

    public void setIsRemarkAdd(int isRemarkAdd) {
        this.isRemarkAdd = isRemarkAdd;
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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
