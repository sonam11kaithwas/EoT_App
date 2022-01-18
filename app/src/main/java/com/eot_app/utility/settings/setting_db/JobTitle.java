package com.eot_app.utility.settings.setting_db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.eot_app.utility.DropdownListBean;
import com.eot_app.utility.settings.jobtitle.TaxData;
import com.eot_app.utility.settings.jobtitle.TaxDataConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aplite_pc302 on 6/21/18.
 */
@Entity(indices = {@Index(value = "jtId", unique = true)}) // check user first name is not repeat.

public class JobTitle implements DropdownListBean {
    @PrimaryKey
    @NonNull
    private String jtId;
    private String title;
    private String des;
    private String labour;
    private String extra;
    private String taxType;
    @TypeConverters(TaxDataConverter.class)
    private List<TaxData> taxData;
    @TypeConverters(SuggestionConverter.class)
    private List<Suggestion> suggestionList = new ArrayList<>();
    @Ignore
    private boolean select = false;

    public JobTitle() {
    }

    public List<Suggestion> getSuggestionList() {
        return suggestionList;
    }

    public void setSuggestionList(List<Suggestion> suggestionList) {
        this.suggestionList = suggestionList;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getJtId() {
        return jtId;
    }

    public void setJtId(String jtId) {
        this.jtId = jtId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getLabour() {
        return labour;
    }

    public void setLabour(String labour) {
        this.labour = labour;
    }

    public List<TaxData> getTaxData() {
        return taxData;
    }

    public void setTaxData(List<TaxData> taxData) {
        this.taxData = taxData;
    }

    @Override
    public String getKey() {
        return getJtId();
    }

    @Override
    public String getName() {
        return getTitle();
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
