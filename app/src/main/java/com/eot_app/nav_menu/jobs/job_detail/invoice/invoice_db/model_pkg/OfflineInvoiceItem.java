package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_db.model_pkg;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.ItemData2Model;
import com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model.ItemUpdateModel;

import java.util.List;

/**
 * Created by Sonam-11 on 3/6/20.
 */
//@Entity(tableName = "OfflineInvoiceItem")
public class OfflineInvoiceItem implements Parcelable {
    public static final Creator<OfflineInvoiceItem> CREATOR = new Creator<OfflineInvoiceItem>() {
        @Override
        public OfflineInvoiceItem createFromParcel(Parcel in) {
            return new OfflineInvoiceItem(in);
        }

        @Override
        public OfflineInvoiceItem[] newArray(int size) {
            return new OfflineInvoiceItem[size];
        }
    };
    @PrimaryKey
    @NonNull
    private String jobId;
    @TypeConverters(ItemData2ModelTypeConverter.class)
    private List<ItemData2Model> itemData2List;
    @TypeConverters(ItemUpdateModelTypeConverter.class)
    private List<ItemUpdateModel> itemUpdateModelsList;
    @TypeConverters(RmvyItemStringTypeConverter.class)
    private List<String> remobeItem;

    public OfflineInvoiceItem() {
    }

    public OfflineInvoiceItem(@NonNull String jobId, List<ItemData2Model> itemData2List, List<ItemUpdateModel> itemUpdateModelsList, List<String> remobeItem) {
        this.jobId = jobId;
        this.itemData2List = itemData2List;
        this.itemUpdateModelsList = itemUpdateModelsList;
        this.remobeItem = remobeItem;
    }

    protected OfflineInvoiceItem(Parcel in) {
        jobId = in.readString();
        itemData2List = in.createTypedArrayList(ItemData2Model.CREATOR);
        remobeItem = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jobId);
        dest.writeTypedList(itemData2List);
        dest.writeStringList(remobeItem);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public List<ItemUpdateModel> getItemUpdateModelsList() {
        return itemUpdateModelsList;
    }

    public void setItemUpdateModelsList(List<ItemUpdateModel> itemUpdateModelsList) {
        this.itemUpdateModelsList = itemUpdateModelsList;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public List<ItemData2Model> getItemData2List() {
        return itemData2List;
    }

    public void setItemData2List(List<ItemData2Model> itemData2List) {
        this.itemData2List = itemData2List;
    }

    public List<String> getRemobeItem() {
        return remobeItem;
    }

    public void setRemobeItem(List<String> remobeItem) {
        this.remobeItem = remobeItem;
    }
}
