package com.eot_app.nav_menu.quote.quote_invoice_pkg.quote_model_pkg;

import android.os.Parcel;
import android.os.Parcelable;

public class Quote_Tax implements Parcelable {

    public static final Creator<Quote_Tax> CREATOR = new Creator<Quote_Tax>() {
        @Override
        public Quote_Tax createFromParcel(Parcel in) {
            return new Quote_Tax(in);
        }

        @Override
        public Quote_Tax[] newArray(int size) {
            return new Quote_Tax[size];
        }
    };

    private String taxId;
    private String rate;
    private String label;
    private String isactive;
    private String show_Invoice;
    // private String txRate;

    protected Quote_Tax(Parcel in) {
        // txRate = in.readString();
        taxId = in.readString();
        rate = in.readString();
        label = in.readString();
        isactive = in.readString();
        show_Invoice = in.readString();
    }

    public static Creator<Quote_Tax> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(iqtmmId);
        // parcel.writeString(txRate);
        parcel.writeString(taxId);
        parcel.writeString(rate);
        parcel.writeString(label);
        parcel.writeString(isactive);
        parcel.writeString(show_Invoice);
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public String getShow_Invoice() {
        return show_Invoice;
    }

    public void setShow_Invoice(String show_Invoice) {
        this.show_Invoice = show_Invoice;
    }

}
