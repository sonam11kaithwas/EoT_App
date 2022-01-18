package com.eot_app.nav_menu.jobs.job_detail.addinvoiveitem2pkg.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * Created by Sonam-11 on 10/6/20.
 */
//@Entity(tableName = "InvoiceGroupData")
public class InvoiceGroupData implements Parcelable {
    public static final Creator<InvoiceGroupData> CREATOR = new Creator<InvoiceGroupData>() {
        @Override
        public InvoiceGroupData createFromParcel(Parcel in) {
            return new InvoiceGroupData(in);
        }

        @Override
        public InvoiceGroupData[] newArray(int size) {
            return new InvoiceGroupData[size];
        }
    };
    //    @PrimaryKey
//    @NonNull
    private String gnm;
    private String rate;
    private String qty;
    private String discount;

    public InvoiceGroupData() {
    }

    public InvoiceGroupData(@NonNull String gnm, String rate, String qty, String discount) {
        this.gnm = gnm;
        this.rate = rate;
        this.qty = qty;
        this.discount = discount;
    }

    protected InvoiceGroupData(Parcel in) {
        gnm = in.readString();
        rate = in.readString();
        qty = in.readString();
        discount = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gnm);
        dest.writeString(rate);
        dest.writeString(qty);
        dest.writeString(discount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @NonNull
    public String getGnm() {
        return gnm;
    }

    public void setGnm(@NonNull String gnm) {
        this.gnm = gnm;
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
}
