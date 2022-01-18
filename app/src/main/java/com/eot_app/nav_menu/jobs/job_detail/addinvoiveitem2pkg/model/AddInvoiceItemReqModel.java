package com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Sonam-11 on 10/6/20.
 */

public class AddInvoiceItemReqModel implements Parcelable {
    public static final Creator<AddInvoiceItemReqModel> CREATOR = new Creator<AddInvoiceItemReqModel>() {
        @Override
        public AddInvoiceItemReqModel createFromParcel(Parcel in) {
            return new AddInvoiceItemReqModel(in);
        }

        @Override
        public AddInvoiceItemReqModel[] newArray(int size) {
            return new AddInvoiceItemReqModel[size];
        }
    };
    private String jobId;
    private String locId;
    private List<InvoiceItemDataModel> itemData = new ArrayList<>();
    private List<InvoiceGroupData> groupData = new ArrayList<>();
    private boolean addItemOnInvoice;

    public AddInvoiceItemReqModel(String jobId, List<InvoiceItemDataModel> itemData, boolean addItemOnInvoice, String locId) {
        this.jobId = jobId;
        this.itemData = itemData;
        groupData = new ArrayList<>();
        this.addItemOnInvoice = addItemOnInvoice;
        this.locId = locId;
    }

    public AddInvoiceItemReqModel() {
    }


    protected AddInvoiceItemReqModel(Parcel in) {
        jobId = in.readString();
        locId = in.readString();
        itemData = in.createTypedArrayList(InvoiceItemDataModel.CREATOR);
        groupData = in.createTypedArrayList(InvoiceGroupData.CREATOR);
        addItemOnInvoice = in.readByte() != 0;
    }

//    public AddInvoiceItemReqModel(String jobId, List<InvoiceItemDataModel> updateItemList, boolean removeItemOnInvoice) {
//        this.jobId = jobId;
//        this.itemData = itemData;
//        this.addItemOnInvoice = addItemOnInvoice;
//    }

    public static Creator<AddInvoiceItemReqModel> getCREATOR() {
        return CREATOR;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public List<InvoiceItemDataModel> getItemData() {
        return itemData;
    }

    public void setItemData(List<InvoiceItemDataModel> itemData) {
        this.itemData = itemData;
    }

    public List<InvoiceGroupData> getGroupData() {
        return groupData;
    }

    public void setGroupData(List<InvoiceGroupData> groupData) {
        this.groupData = groupData;
    }

    public boolean isAddItemOnInvoice() {
        return addItemOnInvoice;
    }

    public void setAddItemOnInvoice(boolean addItemOnInvoice) {
        this.addItemOnInvoice = addItemOnInvoice;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jobId);
        dest.writeString(locId);
        dest.writeTypedList(itemData);
        dest.writeTypedList(groupData);
        dest.writeByte((byte) (addItemOnInvoice ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddInvoiceItemReqModel)) return false;
        AddInvoiceItemReqModel that = (AddInvoiceItemReqModel) o;
        return Objects.equals(getItemData(), that.getItemData());
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItemData());
    }
}
