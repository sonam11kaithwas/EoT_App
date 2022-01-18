package com.eot_app.nav_menu.expense.expense_detail.expense_detail_model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sonam-11 on 11/5/20.
 */
public class Receipt implements Parcelable {
    public static final Creator<Receipt> CREATOR = new Creator<Receipt>() {
        @Override
        public Receipt createFromParcel(Parcel in) {
            return new Receipt(in);
        }

        @Override
        public Receipt[] newArray(int size) {
            return new Receipt[size];
        }
    };
    private String receipt;
    private String erId;

    protected Receipt(Parcel in) {
        receipt = in.readString();
        erId = in.readString();
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getErId() {
        return erId;
    }

    public void setErId(String erId) {
        this.erId = erId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(receipt);
        dest.writeString(erId);
    }
}
