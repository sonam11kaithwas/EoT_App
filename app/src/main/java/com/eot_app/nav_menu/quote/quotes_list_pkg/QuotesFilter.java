package com.eot_app.nav_menu.quote.quotes_list_pkg;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class QuotesFilter implements Parcelable {
    public static final Creator<QuotesFilter> CREATOR = new Creator<QuotesFilter>() {
        @Override
        public QuotesFilter createFromParcel(Parcel in) {
            return new QuotesFilter(in);
        }

        @Override
        public QuotesFilter[] newArray(int size) {
            return new QuotesFilter[size];
        }
    };
    private String search, dtf, dtt;
    private List<String> status;
    private String dateFliterNm;

    QuotesFilter() {
        search = "";
        dtf = "";
        dtt = "";
        status = new ArrayList<>();
        dateFliterNm = "";
    }

    public QuotesFilter(String search, String dtf, String dtt, List<String> status, String dateFliterNm) {
        this.search = search;
        this.dtf = dtf;
        this.dtt = dtt;
        this.status = status;
        this.dateFliterNm = dateFliterNm;
    }

    protected QuotesFilter(Parcel in) {
        search = in.readString();
        dtf = in.readString();
        dtt = in.readString();
        status = in.createStringArrayList();
        dateFliterNm = in.readString();
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getDtf() {
        return dtf;
    }

    public void setDtf(String dtf) {
        this.dtf = dtf;
    }

    public String getDtt() {
        return dtt;
    }

    public void setDtt(String dtt) {
        this.dtt = dtt;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }


    public String getDateFliterNm() {
        return dateFliterNm;
    }

    public void setDateFliterNm(String dateFliterNm) {
        this.dateFliterNm = dateFliterNm;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(search);
        parcel.writeString(dtf);
        parcel.writeString(dtt);
        parcel.writeStringList(status);
        parcel.writeString(dateFliterNm);
    }

    public void clearAllvalues() {
        search = "";
        dtf = "";
        dtt = "";
        status.clear();
        dateFliterNm = "";
    }

    public boolean isAdvansedFilterEmpty() {
        return dtf.isEmpty() && dtt.isEmpty() && status.isEmpty();
    }
}

