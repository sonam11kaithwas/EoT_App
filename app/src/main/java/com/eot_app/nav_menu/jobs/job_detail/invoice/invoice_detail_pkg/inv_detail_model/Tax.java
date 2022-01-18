package com.eot_app.nav_menu.jobs.job_detail.invoice.invoice_detail_pkg.inv_detail_model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "Tax")
public class Tax implements Parcelable {

    public static final Creator<Tax> CREATOR = new Creator<Tax>() {
        @Override
        public Tax createFromParcel(Parcel in) {
            return new Tax(in);
        }

        @Override
        public Tax[] newArray(int size) {
            return new Tax[size];
        }
    };
    @PrimaryKey
    @NonNull
    private String taxId;
    private String label;
    private String isactive;
    private String show_Invoice;
    private String rate = "0";
    private String percentage = "0";
    private String locId = "0";
    @Ignore
    private boolean select = false;
    @Ignore
    private String appliedTax = "";
    @Ignore
    private String oldTax = "";

    public Tax(String taxId, String rate) {
        this.taxId = taxId;
        this.rate = rate;
    }

    public Tax() {
    }

    protected Tax(Parcel in) {
        taxId = in.readString();
        label = in.readString();
        isactive = in.readString();
        show_Invoice = in.readString();
        rate = in.readString();
        percentage = in.readString();
        appliedTax = in.readString();
        oldTax = in.readString();
        locId = in.readString();
        select = in.readByte() != 0;
    }

    public static Creator<Tax> getCREATOR() {
        return CREATOR;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(taxId);
        dest.writeString(label);
        dest.writeString(isactive);
        dest.writeString(show_Invoice);
        dest.writeString(rate);
        dest.writeString(percentage);
        dest.writeString(appliedTax);
        dest.writeString(oldTax);
        dest.writeString(locId);
        dest.writeByte((byte) (select ? 1 : 0));
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @NonNull
    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(@NonNull String taxId) {
        this.taxId = taxId;
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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }


    public String getAppliedTax() {
        return appliedTax;
    }

    public void setAppliedTax(String appliedTax) {
        this.appliedTax = appliedTax;
    }

    public String getOldTax() {
        return oldTax;
    }

    public void setOldTax(String oldTax) {
        this.oldTax = oldTax;
    }

    public String getLocId() {
        return locId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tax)) return false;
        Tax tax = (Tax) o;
        return isSelect() == tax.isSelect() &&
                getTaxId().equals(tax.getTaxId()) &&
                Objects.equals(getLabel(), tax.getLabel()) &&
                Objects.equals(getIsactive(), tax.getIsactive()) &&
                Objects.equals(getShow_Invoice(), tax.getShow_Invoice()) &&
                Objects.equals(getRate(), tax.getRate()) &&
                Objects.equals(getPercentage(), tax.getPercentage()) &&
                Objects.equals(getLocId(), tax.getLocId()) &&
                Objects.equals(getAppliedTax(), tax.getAppliedTax()) &&
                Objects.equals(getOldTax(), tax.getOldTax());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTaxId(), getLabel(), getIsactive(), getShow_Invoice(), getRate(), getPercentage(), getLocId(), isSelect(), getAppliedTax(), getOldTax());
    }
}
