package com.eot_app.utility.settings.contractdb;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.eot_app.utility.DropdownListBean;

@Entity(tableName = "ContractRes")
public class ContractRes implements DropdownListBean, Parcelable {
    public static final Creator<ContractRes> CREATOR = new Creator<ContractRes>() {
        @Override
        public ContractRes createFromParcel(Parcel in) {
            return new ContractRes(in);
        }

        @Override
        public ContractRes[] newArray(int size) {
            return new ContractRes[size];
        }
    };
    @PrimaryKey
    @NonNull
    private String contrId;
    private String label;
    private String amount;
    private String type;
    private String payType;
    private String startDate;
    private String endDate;
    private String status;
    private String nm;
    private String remainingAmt;
    private String cltId;
    private String paidAmt;
    private String invAmount;
    private String ccId;
    private String catgy;
    private String isdelete;

    public ContractRes() {
    }

    protected ContractRes(Parcel in) {
        contrId = in.readString();
        label = in.readString();
        amount = in.readString();
        type = in.readString();
        payType = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        status = in.readString();
        nm = in.readString();
        remainingAmt = in.readString();
        cltId = in.readString();
        paidAmt = in.readString();
        invAmount = in.readString();
        ccId = in.readString();
        catgy = in.readString();
        isdelete = in.readString();
    }

    public static Creator<ContractRes> getCREATOR() {
        return CREATOR;
    }

    public String getContrId() {
        return contrId;
    }

    public void setContrId(String contrId) {
        this.contrId = contrId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getRemainingAmt() {
        return remainingAmt;
    }

    public void setRemainingAmt(String remainingAmt) {
        this.remainingAmt = remainingAmt;
    }

    public String getCltId() {
        return cltId;
    }

    public void setCltId(String cltId) {
        this.cltId = cltId;
    }

    public String getPaidAmt() {
        return paidAmt;
    }

    public void setPaidAmt(String paidAmt) {
        this.paidAmt = paidAmt;
    }

    public String getInvAmount() {
        return invAmount;
    }

    public void setInvAmount(String invAmount) {
        this.invAmount = invAmount;
    }

    public String getCcId() {
        return ccId;
    }

    public void setCcId(String ccId) {
        this.ccId = ccId;
    }

    public String getCatgy() {
        return catgy;
    }

    public void setCatgy(String catgy) {
        this.catgy = catgy;
    }

    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(contrId);
        parcel.writeString(label);
        parcel.writeString(amount);
        parcel.writeString(type);
        parcel.writeString(payType);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeString(status);
        parcel.writeString(nm);
        parcel.writeString(remainingAmt);
        parcel.writeString(cltId);
        parcel.writeString(paidAmt);
        parcel.writeString(invAmount);
        parcel.writeString(ccId);
        parcel.writeString(catgy);
        parcel.writeString(isdelete);
    }

    @Override
    public String getKey() {
        return getContrId();
    }

    @Override
    public String getName() {
        return getLabel();
    }
}
