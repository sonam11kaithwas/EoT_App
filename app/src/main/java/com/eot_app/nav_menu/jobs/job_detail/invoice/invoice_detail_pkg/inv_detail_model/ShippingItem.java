package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model;

import android.os.Parcel;
import android.os.Parcelable;

public class ShippingItem implements Parcelable {
    public static final Creator<ShippingItem> CREATOR = new Creator<ShippingItem>() {
        @Override
        public ShippingItem createFromParcel(Parcel in) {
            return new ShippingItem(in);
        }

        @Override
        public ShippingItem[] newArray(int size) {
            return new ShippingItem[size];
        }
    };
    private String ijmmId;
    private String itemId;
    private String rate;
    private String inm;
    private String itype;

    protected ShippingItem(Parcel in) {
        ijmmId = in.readString();
        itemId = in.readString();
        rate = in.readString();
        inm = in.readString();
        itype = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ijmmId);
        dest.writeString(itemId);
        dest.writeString(rate);
        dest.writeString(inm);
        dest.writeString(itype);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getIjmmId() {
        return ijmmId;
    }

    public void setIjmmId(String ijmmId) {
        this.ijmmId = ijmmId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getInm() {
        return inm;
    }

    public void setInm(String inm) {
        this.inm = inm;
    }

    public String getItype() {
        return itype;
    }

    public void setItype(String itype) {
        this.itype = itype;
    }
}
